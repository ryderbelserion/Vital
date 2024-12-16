package com.ryderbelserion.vital.paper.api.builders.items.v2;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerTextures;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.UUID;

/**
 * {@link SkullBuilder} is an experimental class that extends {@link BaseItemBuilder} for creating skull item builders.
 *
 * @author Ryder Belserion
 * @version 0.2.0
 * @since 0.2.0
 */
@ApiStatus.Experimental
public class SkullBuilder extends BaseItemBuilder<SkullBuilder> {

    private final ResolvableProfile.Builder builder;

    /**
     * Creates a new instance with {@link ItemStack}.
     *
     * @param itemStack {@link ItemStack}, must not be null
     * @since 0.2.0
     */
    SkullBuilder(@NotNull final ItemStack itemStack) {
        super(itemStack);

        this.builder = ResolvableProfile.resolvableProfile();
    }

    /**
     * Set a texture to the {@link ItemStack} if it is a player head.
     *
     * @param audience the {@link Audience}
     * @return {@link SkullBuilder}
     * @since 0.2.0
     */
    public SkullBuilder withAudience(@NotNull final Audience audience) {
        final UUID uuid = audience.getOrDefault(Identity.UUID, null);

        if (uuid == null) return this;

        this.builder.uuid(uuid);

        return this;
    }

    /**
     * Set a texture to the {@link ItemStack} if it is a player head.
     *
     * @param url the url
     * @return {@link SkullBuilder}
     * @since 0.2.0
     */
    public SkullBuilder withUrl(@NotNull final String url) {
        if (url.isEmpty()) return this;

        final String newUrl = "https://textures.minecraft.net/texture/" + url;

        final ItemStack itemStack = getItemStack();

        itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof SkullMeta skullMeta) {
                final PlayerProfile profile = this.plugin.getServer().createProfile(null, "");

                profile.setProperty(new ProfileProperty("", ""));

                final PlayerTextures textures = profile.getTextures();

                try {
                    textures.setSkin(URI.create(newUrl).toURL(), PlayerTextures.SkinModel.CLASSIC);
                } catch (MalformedURLException exception) {
                    if (this.api.isVerbose()) this.api.getLogger().error("Failed to set the texture url", exception);
                }

                profile.setTextures(textures);

                skullMeta.setPlayerProfile(profile);
            }
        });

        setItemStack(itemStack);

        return this;
    }

    /**
     * Set a texture to the {@link ItemStack} if it is a player head.
     *
     * @param base64 the base64 string
     * @return {@link SkullBuilder}
     * @since 0.2.0
     */
    public SkullBuilder withBase64(@NotNull final String base64) {
        if (base64.isEmpty()) return this;

        this.builder.addProperty(new ProfileProperty("textures", base64));

        return this;
    }

    /**
     * Set a texture to the {@link ItemStack} if it is a player head.
     *
     * @param playerName the player name
     * @return {@link SkullBuilder}
     * @since 0.2.0
     */
    public SkullBuilder withName(@Subst("player") @NotNull final String playerName) {
        if (playerName.isEmpty()) return this;

        this.builder.name(playerName);

        return this;
    }

    /**
     * Completes the building process and applies the skull data to the item.
     *
     * @return {@link SkullBuilder}
     * @since 0.2.0
     */
    @Override
    public SkullBuilder complete() {
        getItemStack().setData(DataComponentTypes.PROFILE, this.builder.build());

        return this;
    }
}