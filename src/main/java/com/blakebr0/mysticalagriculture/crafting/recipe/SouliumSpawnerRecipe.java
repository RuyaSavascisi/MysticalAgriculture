package com.blakebr0.mysticalagriculture.crafting.recipe;

import com.blakebr0.cucumber.crafting.ingredient.IngredientWithCount;
import com.blakebr0.mysticalagriculture.api.crafting.ISouliumSpawnerRecipe;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SouliumSpawnerRecipe implements ISouliumSpawnerRecipe {
    private final NonNullList<IngredientWithCount> inputs;
    private final WeightedRandomList<WeightedEntry.Wrapper<EntityType<?>>> entityTypes;

    public SouliumSpawnerRecipe(IngredientWithCount input, WeightedRandomList<WeightedEntry.Wrapper<EntityType<?>>> entityTypes) {
        this.inputs = NonNullList.of(IngredientWithCount.EMPTY, input);
        this.entityTypes = entityTypes;
    }

    @Override
    public boolean matches(CraftingInput inventory, Level level) {
        var stack = inventory.getItem(0);
        return this.inputs.getFirst().test(stack);
    }

    @Override
    public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider lookup) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs.stream()
                .map(ICustomIngredient::toVanilla)
                .collect(Collectors.toCollection(NonNullList::create));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SOULIUM_SPAWNER.get();
    }

    @Override
    public RecipeType<? extends ISouliumSpawnerRecipe> getType() {
        return ModRecipeTypes.SOULIUM_SPAWNER.get();
    }

    @Override
    public WeightedRandomList<WeightedEntry.Wrapper<EntityType<?>>> getEntityTypes() {
        return this.entityTypes;
    }

    @Override
    public EntityType<?> getFirstEntityType() {
        return this.entityTypes.unwrap().getFirst().data();
    }

    @Override
    public Optional<WeightedEntry.Wrapper<EntityType<?>>> getRandomEntityType(RandomSource random) {
        return this.entityTypes.getRandom(random);
    }

    @Override
    public int getCount(int index) {
        if (index < 0 || index >= this.inputs.size())
            return -1;

        return this.inputs.get(index).getCount();
    }

    public static class Serializer implements RecipeSerializer<SouliumSpawnerRecipe> {
        private static final StreamCodec<RegistryFriendlyByteBuf, EntityType<?>> ENTITY_TYPE_STREAM_CODEC = ByteBufCodecs.registry(Registries.ENTITY_TYPE);
        public static final MapCodec<SouliumSpawnerRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        IngredientWithCount.MAP_CODEC.fieldOf("input").forGetter(recipe -> recipe.inputs.getFirst()),
                        WeightedRandomList.codec(
                                RecordCodecBuilder.<WeightedEntry.Wrapper<EntityType<?>>>create(wrapper ->
                                        wrapper.group(
                                                BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity").forGetter(WeightedEntry.Wrapper::data),
                                                Weight.CODEC.fieldOf("weight").forGetter(WeightedEntry.Wrapper::weight)
                                        ).apply(wrapper, WeightedEntry.Wrapper::new)
                                )
                        ).fieldOf("entities").forGetter(recipe -> recipe.entityTypes)
                ).apply(builder, SouliumSpawnerRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, SouliumSpawnerRecipe> STREAM_CODEC = StreamCodec.of(
                SouliumSpawnerRecipe.Serializer::toNetwork, SouliumSpawnerRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<SouliumSpawnerRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SouliumSpawnerRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SouliumSpawnerRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var input = IngredientWithCount.STREAM_CODEC.decode(buffer);
            var entities = buffer.readVarInt();

            List<WeightedEntry.Wrapper<EntityType<?>>> entityTypes = new ArrayList<>();

            for (int i = 0; i < entities; i++) {
                var entityType = ENTITY_TYPE_STREAM_CODEC.decode(buffer);
                var entityTypeWeight = buffer.readVarInt();

                entityTypes.add(WeightedEntry.wrap(entityType, entityTypeWeight));
            }

            return new SouliumSpawnerRecipe(input, WeightedRandomList.create(entityTypes));
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SouliumSpawnerRecipe recipe) {
            IngredientWithCount.STREAM_CODEC.encode(buffer, recipe.inputs.getFirst());

            buffer.writeVarInt(recipe.entityTypes.unwrap().size());

            for (var entityType : recipe.entityTypes.unwrap()) {
                ENTITY_TYPE_STREAM_CODEC.encode(buffer, entityType.data());
                buffer.writeVarInt(entityType.weight().asInt());
            }
        }
    }
}
