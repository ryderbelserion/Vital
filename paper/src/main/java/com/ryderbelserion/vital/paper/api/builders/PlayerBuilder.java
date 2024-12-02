package com.ryderbelserion.vital.paper.api.builders;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * Builds a player using the provided name.
 *
 * @param name the name of the player
 *
 * @author Ryder Belserion
 * @version 0.1.0
 * @since 0.1.0
 */
public record PlayerBuilder(String name) {

    /**
     * Retrieves the {@link OfflinePlayer} corresponding to the provided player name.
     * This method fetches the player's UUID asynchronously and then retrieves the {@link OfflinePlayer} instance.
     *
     * @return the {@link OfflinePlayer}, or null if the player name is empty
     * @since 0.1.0
     */
    public @Nullable OfflinePlayer getOfflinePlayer() {
        if (this.name.isEmpty()) return null;

        CompletableFuture<UUID> future = CompletableFuture.supplyAsync(() -> Bukkit.getServer().getOfflinePlayer(this.name)).thenApply(OfflinePlayer::getUniqueId);

        return Bukkit.getServer().getOfflinePlayer(future.join());
    }

    /**
     * Retrieves the {@link Player} corresponding to the provided player name.
     * This method fetches the online {@link Player} instance if the player is currently online.
     *
     * @return the {@link Player}, or null if the player name is empty or the player is not online
     * @since 0.1.0
     */
    public @Nullable Player getPlayer() {
        if (this.name.isEmpty()) return null;

        return Bukkit.getServer().getPlayerExact(this.name);
    }
}