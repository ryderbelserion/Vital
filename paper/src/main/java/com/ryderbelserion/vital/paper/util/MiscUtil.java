package com.ryderbelserion.vital.paper.util;

import com.ryderbelserion.vital.common.util.AdvUtil;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Any random utility
 *
 * @author ryderbelserion
 * @version 1.0
 * @since 1.0
 */
public class MiscUtil {

    /**
     * Any random utility
     *
     * @since 1.0
     */
    public MiscUtil() {
        throw new AssertionError();
    }

    /**
     * Updates the inventory title, without re-building the inventory.
     *
     * @param player {@link Player}
     * @param title {@link String}
     * @since 1.0
     */
    public static void updateTitle(@NotNull final Player player, @NotNull final String title) {
        /*this.isUpdating = true;

        final int size = this.inventory.getSize();

        this.inventory = Bukkit.createInventory(this, size, AdvUtil.parse(this.title));

        open(player);

        this.isUpdating = false;*/

        final ServerPlayer entityPlayer = (ServerPlayer) ((CraftHumanEntity) player).getHandle();
        final int containerId = entityPlayer.containerMenu.containerId;
        final MenuType<?> windowType = CraftContainer.getNotchInventoryType(player.getOpenInventory().getTopInventory());
        entityPlayer.connection.send(new ClientboundOpenScreenPacket(containerId, windowType, CraftChatMessage.fromJSON(JSONComponentSerializer.json().serialize(AdvUtil.parse(title)))));

        player.updateInventory();
    }
}