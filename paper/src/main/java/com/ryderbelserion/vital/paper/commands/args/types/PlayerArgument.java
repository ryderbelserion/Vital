package com.ryderbelserion.vital.paper.commands.args.types;

import com.ryderbelserion.vital.core.commands.args.ArgumentType;
import com.ryderbelserion.vital.paper.VitalPaper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class PlayerArgument extends ArgumentType {

    private final @NotNull VitalPaper paper = (VitalPaper) VitalPaper.api();

    private final @NotNull JavaPlugin plugin = this.paper.getPlugin();

    @Override
    public List<String> getPossibleValues() {
        return this.plugin.getServer().getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
    }
}