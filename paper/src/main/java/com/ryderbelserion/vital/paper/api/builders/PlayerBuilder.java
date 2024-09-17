package com.ryderbelserion.vital.paper.api.builders;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Builds a player.
 *
 * @param name the name of the player
 *
 * @author ryderbelserion
 * @version 0.0.2
 * @since 0.0.1
 */
public record PlayerBuilder(String name) {

    /**
     * Gets the {@link OfflinePlayer}.
     *
     * @return {@link OfflinePlayer}
     * @since 0.0.1
     */
    public @Nullable OfflinePlayer getOfflinePlayer() {
        if (this.name.isEmpty()) return null;

        CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> Bukkit.getServer().getOfflinePlayer(this.name)).thenApply(OfflinePlayer::getUniqueId);

        return Bukkit.getServer().getOfflinePlayer(future.join());
    }

    /**
     * Gets the {@link Player}.
     *
     * @return {@link Player}
     * @since 0.0.1
     */
    public @Nullable Player getPlayer() {
        if (this.name.isEmpty()) return null;

        return Bukkit.getServer().getPlayer(this.name);
    }
}