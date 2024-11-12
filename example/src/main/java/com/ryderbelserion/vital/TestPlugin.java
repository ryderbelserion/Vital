package com.ryderbelserion.vital;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.ryderbelserion.vital.api.files.FileManager;
import com.ryderbelserion.vital.api.files.enums.FileType;
import com.ryderbelserion.vital.command.BaseCommand;
import com.ryderbelserion.vital.command.persist.Config;
import com.ryderbelserion.vital.command.subs.CommandFile;
import com.ryderbelserion.vital.command.subs.CommandGui;
import com.ryderbelserion.vital.command.subs.CommandItem;
import com.ryderbelserion.vital.paper.VitalPaper;
import com.ryderbelserion.vital.utils.Methods;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.util.List;

public class TestPlugin extends JavaPlugin {

    /**
     * Gets the plugin
     *
     * @return {@link TestPlugin}
     */
    public static TestPlugin getPlugin() {
        return JavaPlugin.getPlugin(TestPlugin.class);
    }

    private final VitalPaper paper;

    public TestPlugin() {
        this.paper = new VitalPaper(this);
    }

    @Override
    public void onEnable() {
        getFileManager().addFile("config.yml", FileType.YAML).addFile("data.yml", FileType.YAML)
                .addFile("locations.yml", FileType.YAML).addFile("logs/example.log")
                .addFolder("crates", FileType.YAML);

        //Methods.getFiles(new File(getDataFolder(), "crates"), ".yml", false);

        //Config config = new Config(getDataFolder());
        //config.load();

        //Config.itemBurnTypes.forEach(item -> getLogger().warning("Item: " + item));

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new BaseCommand().registerPermission().literal().createBuilder();

            List.of(
                    new CommandItem(),
                    new CommandFile(),
                    new CommandGui()
            ).forEach(command -> root.then(command.registerPermission().literal()));

            event.registrar().register(root.build(), "the base command for Vital");
        });
    }

    public final VitalPaper getPaper() {
        return this.paper;
    }

    public final FileManager getFileManager() {
        return getPaper().getFileManager();
    }
}