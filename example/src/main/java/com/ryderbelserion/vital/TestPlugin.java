package com.ryderbelserion.vital;

import com.ryderbelserion.vital.paper.builders.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        ItemBuilder itemBuilder = new ItemBuilder().withType(Material.PLAYER_HEAD);

        itemBuilder.setPlayer("Rukkhadevata");

        event.getPlayer().getInventory().addItem(itemBuilder.getStack());
    }
}
