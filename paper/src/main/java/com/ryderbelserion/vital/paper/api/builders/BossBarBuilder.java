package com.ryderbelserion.vital.paper.api.builders;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Builds a bossbar to send to a target.
 *
 * @author ryderbelserion
 * @version 0.0.5
 * @since 0.0.1
 */
public class BossBarBuilder {

    private final Player target;

    private BossBar bossBar;

    /**
     * The constructor to build a {@link BossBar}.
     *
     * @param target the {@link Player} to send to
     * @param bossBar the {@link BossBar} to send
     * @since 0.0.1
     */
    public BossBarBuilder(@NotNull final Player target, @NotNull final BossBar bossBar) {
        this.target = target;

        this.bossBar = bossBar;
    }

    /**
     * Gets the target recipient.
     *
     * @return the {@link Player}
     * @since 0.0.1
     */
    public @NotNull final Player getTarget() {
        return this.target;
    }

    /**
     * Returns a {@link BossBar} with all our goodies attached!
     *
     * @return the {@link BossBar}
     * @since 0.0.1
     */
    public @NotNull final BossBar getBossBar() {
        return this.bossBar;
    }

    /**
     * Sets the {@link BossBar}.
     *
     * @param bossBar the new {@link BossBar}
     * @since 0.0.1
     */
    public void applyBossBar(@Nullable final BossBar bossBar) {
        this.bossBar = bossBar;
    }

    /**
     * Hides the {@link BossBar} then sets the variable to null.
     *
     * @since 0.0.1
     */
    public void hideBossBar() {
        getTarget().hideBossBar(getBossBar());

        applyBossBar(null);
    }
}