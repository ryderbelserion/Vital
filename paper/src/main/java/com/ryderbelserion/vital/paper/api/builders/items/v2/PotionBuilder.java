package com.ryderbelserion.vital.paper.api.builders.items.v2;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.PotionContents;
import org.bukkit.Color;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * {@link PotionBuilder} is an experimental class that extends {@link BaseItemBuilder} for creating potion item builders.
 *
 * @author Ryder Belserion
 * @version 0.2.0
 * @since 0.2.0
 */
@ApiStatus.Experimental
public class PotionBuilder extends BaseItemBuilder<PotionBuilder> {

    private final PotionContents.Builder builder;

    /**
     * Creates a new instance with {@link ItemStack}.
     *
     * @param itemStack {@link ItemStack}, must not be null
     * @since 0.2.0
     */
    PotionBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = PotionContents.potionContents();
    }

    /**
     * Adds a potion effect to the potion with specific parameters.
     *
     * @param potionEffectType the type of the potion effect, must not be null
     * @param duration the duration of the potion effect in ticks
     * @param amplifier the amplifier of the potion effect
     * @param isAmbient whether the effect is ambient
     * @param isParticles whether the effect shows particles
     * @param hasIcon whether the effect has an icon
     * @return the {@link PotionBuilder} instance
     * @since 0.2.0
     */
    public PotionBuilder withPotionEffect(final PotionEffectType potionEffectType, final int duration, final int amplifier, final boolean isAmbient, final boolean isParticles, final boolean hasIcon) {
        this.builder.addCustomEffect(new PotionEffect(potionEffectType, duration, amplifier).withAmbient(isAmbient).withParticles(isParticles).withIcon(hasIcon));

        return this;
    }

    /**
     * Adds a potion effect to the potion with default parameters for ambient, particles, and icon.
     *
     * @param potionEffectType the type of the potion effect, must not be null
     * @param duration the duration of the potion effect in ticks
     * @param amplifier the amplifier of the potion effect
     * @return the {@link PotionBuilder} instance
     * @since 0.2.0
     */
    public PotionBuilder withPotionEffect(final PotionEffectType potionEffectType, final int duration, final int amplifier) {
        return withPotionEffect(potionEffectType, duration, amplifier, true, true, true);
    }

    /**
     * Sets the type of the potion.
     *
     * @param potionType the type of the potion, must not be null
     * @return the {@link PotionBuilder} instance
     * @since 0.2.0
     */
    public PotionBuilder withPotionType(final PotionType potionType) {
        this.builder.potion(potionType);

        return this;
    }

    /**
     * Sets the custom name for the potion.
     *
     * @param customName the custom name for the potion
     * @return the {@link PotionBuilder} instance
     * @since 0.2.0
     */
    public PotionBuilder withCustomName(final String customName) {
        this.builder.customName(customName);

        return this;
    }

    /**
     * Sets the color of the potion.
     *
     * @param color the color of the potion, must not be null
     * @return the {@link PotionBuilder} instance
     * @since 0.2.0
     */
    public PotionBuilder withColor(final Color color) {
        this.builder.customColor(color);

        return this;
    }

    /**
     * Completes the building process and applies the potion contents to the item.
     *
     * @return the {@link PotionBuilder} instance
     * @since 0.2.0
     */
    public PotionBuilder complete() {
        getItemStack().setData(DataComponentTypes.POTION_CONTENTS, this.builder.build());

        return this;
    }
}