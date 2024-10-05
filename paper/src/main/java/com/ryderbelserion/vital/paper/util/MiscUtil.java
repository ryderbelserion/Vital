package com.ryderbelserion.vital.paper.util;

import com.ryderbelserion.vital.common.util.AdvUtil;
import com.ryderbelserion.vital.paper.api.bStats;
import net.kyori.adventure.text.serializer.json.JSONComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.MenuType;
import org.bukkit.craftbukkit.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.inventory.CraftContainer;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.concurrent.Callable;

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

    /**
     * Gets the chart data
     *
     * @param callable map of a string/integer
     * @return json object
     * @throws Exception if fetching the object fails
     * @since 0.0.1
     */
    public static bStats.JsonObjectBuilder.JsonObject getChartData(Callable<Map<String, Integer>> callable) throws Exception {
        bStats.JsonObjectBuilder valuesBuilder = new bStats.JsonObjectBuilder();

        Map<String, Integer> map = callable.call();

        if (map == null || map.isEmpty()) {
            // Null = skip the chart
            return null;
        }

        boolean allSkipped = true;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 0) {
                // Skip this invalid
                continue;
            }

            allSkipped = false;
            valuesBuilder.appendField(entry.getKey(), entry.getValue());
        }

        if (allSkipped) {
            // Null = skip the chart
            return null;
        }

        return new bStats.JsonObjectBuilder().appendField("values", valuesBuilder.build()).build();
    }
}