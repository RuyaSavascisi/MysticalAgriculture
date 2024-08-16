package com.blakebr0.mysticalagriculture.item.tool;

import com.blakebr0.cucumber.item.BaseItem;
import com.blakebr0.cucumber.lib.Tooltips;
import com.blakebr0.mysticalagriculture.api.tinkering.AugmentType;
import com.blakebr0.mysticalagriculture.api.tinkering.IElementalItem;
import com.blakebr0.mysticalagriculture.api.tinkering.ITinkerable;
import com.blakebr0.mysticalagriculture.init.ModDataComponentTypes;
import com.blakebr0.mysticalagriculture.lib.ModTooltips;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class EssenceStaffItem extends BaseItem implements ITinkerable, IElementalItem {
    private static final EnumSet<AugmentType> TYPES = EnumSet.of(AugmentType.STAFF);
    private final int tinkerableTier;
    private final int slots;

    public EssenceStaffItem(int tinkerableTier, int slots) {
        super(p -> p.component(ModDataComponentTypes.EQUIPPED_AUGMENTS, new ArrayList<>(slots)));
        this.tinkerableTier = tinkerableTier;
        this.slots = slots;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Tooltips.NOT_YET_IMPLEMENTED.build());
        tooltip.add(ModTooltips.getTooltipForTier(this.tinkerableTier));
        ModTooltips.addAugmentListToTooltip(tooltip, stack, this.slots);
    }

    @Override
    public int getAugmentSlots() {
        return this.slots;
    }

    @Override
    public EnumSet<AugmentType> getAugmentTypes() {
        return TYPES;
    }

    @Override
    public int getTinkerableTier() {
        return this.tinkerableTier;
    }
}
