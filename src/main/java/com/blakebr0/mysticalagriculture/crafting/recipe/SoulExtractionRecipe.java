package com.blakebr0.mysticalagriculture.crafting.recipe;

import com.blakebr0.cucumber.crafting.ISpecialRecipe;
import com.blakebr0.cucumber.helper.StackHelper;
import com.blakebr0.mysticalagriculture.api.crafting.ISoulExtractionRecipe;
import com.blakebr0.mysticalagriculture.api.soul.MobSoulType;
import com.blakebr0.mysticalagriculture.api.util.MobSoulUtils;
import com.blakebr0.mysticalagriculture.init.ModItems;
import com.blakebr0.mysticalagriculture.init.ModRecipeSerializers;
import com.blakebr0.mysticalagriculture.init.ModRecipeTypes;
import com.blakebr0.mysticalagriculture.registry.MobSoulTypeRegistry;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class SoulExtractionRecipe implements ISpecialRecipe, ISoulExtractionRecipe {
    private final NonNullList<Ingredient> inputs;
    private final MobSoulType type;
    private final double souls;
    private final Result result;
    private final ItemStack resultStack;

    public SoulExtractionRecipe(Ingredient input, Result result) {
        this.inputs = NonNullList.of(Ingredient.EMPTY, input);
        this.type = MobSoulTypeRegistry.getInstance().getMobSoulTypeById(result.type);
        this.souls = result.amount;
        this.result = result;
        this.resultStack = MobSoulUtils.getSoulJar(type, souls, ModItems.SOUL_JAR.get());
    }

    @Override
    public boolean matches(RecipeInput inventory, Level level) {
        var input = inventory.getItem(0);

        if (!this.inputs.getFirst().test(input))
            return false;

        var output = inventory.getItem(2);

        if (!output.is(this.resultStack.getItem()))
            return false;

        return MobSoulUtils.canAddTypeToJar(output, this.type) && !MobSoulUtils.isJarFull(output);
    }

    @Override
    public ItemStack assemble(RecipeInput inventory, HolderLookup.Provider provider) {
        var stack = inventory.getItem(2);
        var jar = StackHelper.withSize(stack, 1, false);

        MobSoulUtils.addSoulsToJar(jar, this.type, this.souls);

        return jar;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return this.resultStack;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return this.inputs;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipeSerializers.SOUL_EXTRACTION.get();
    }

    @Override
    public RecipeType<? extends ISoulExtractionRecipe> getType() {
        return ModRecipeTypes.SOUL_EXTRACTION.get();
    }

    @Override
    public MobSoulType getMobSoulType() {
        return this.type;
    }

    @Override
    public double getSouls() {
        return this.souls;
    }

    public static class Serializer implements RecipeSerializer<SoulExtractionRecipe> {
        public static final MapCodec<SoulExtractionRecipe> CODEC = RecordCodecBuilder.mapCodec(builder ->
                builder.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(recipe -> recipe.inputs.getFirst()),
                        Result.CODEC.fieldOf("output").forGetter(recipe -> recipe.result)
                ).apply(builder, SoulExtractionRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, SoulExtractionRecipe> STREAM_CODEC = StreamCodec.of(
                SoulExtractionRecipe.Serializer::toNetwork, SoulExtractionRecipe.Serializer::fromNetwork
        );

        @Override
        public MapCodec<SoulExtractionRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SoulExtractionRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SoulExtractionRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var input = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            var result = Result.STREAM_CODEC.decode(buffer);

            return new SoulExtractionRecipe(input, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, SoulExtractionRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.inputs.getFirst());
            Result.STREAM_CODEC.encode(buffer, recipe.result);
        }
    }

    public record Result(ResourceLocation type, double amount) {
        public static final MapCodec<Result> CODEC = RecordCodecBuilder.mapCodec(builder ->
               builder.group(
                       ResourceLocation.CODEC.fieldOf("type").forGetter(Result::type),
                       Codec.DOUBLE.fieldOf("amount").forGetter(Result::amount)
               ).apply(builder, Result::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, Result> STREAM_CODEC = StreamCodec.composite(
                ResourceLocation.STREAM_CODEC,
                Result::type,
                ByteBufCodecs.DOUBLE,
                Result::amount,
                Result::new
        );
    }
}
