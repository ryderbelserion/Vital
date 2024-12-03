package com.ryderbelserion.vital.paper.api.builders.items.v2;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;

public class PotionBuilder extends BaseItemBuilder<PotionBuilder> {

    private final PotionContents.Builder contents;

    /**
     * Creates a new instance with {@link ItemStack}
     *
     * @param itemStack {@link ItemStack}
     * @since 0.2.0
     */
    PotionBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);

        this.contents = PotionContents.potionContents();
    }

    public PotionBuilder withPotionEffect(final PotionEffectType potionEffectType, final int duration, final int amplifier, final boolean isAmbient, final boolean isParticles, final boolean hasIcon) {
        this.contents.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier).withAmbient(isAmbient).withParticles(isParticles).withIcon(hasIcon));

        return this;
    }

    public PotionBuilder withPotionEffect(final PotionEffectType potionEffectType, final int duration, final int amplifier) {
        return withPotionEffect(potionEffectType, duration, amplifier, true, true, true);
    }

    public PotionBuilder withPotionType(final PotionType potionType) {
        this.contents.potion(potionType);

        return this;
    }

    public PotionBuilder withCustomName(final String customName) {
        this.contents.customName(customName);

        return this;
    }

    public PotionBuilder withColor(final Color color) {
        this.contents.customColor(color);

        return this;
    }

    public PotionBuilder complete() {
        getItemStack().setData(DataComponentTypes.POTION_CONTENTS, this.contents.build());

        return this;
    }
}