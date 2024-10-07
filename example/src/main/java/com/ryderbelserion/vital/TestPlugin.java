package com.ryderbelserion.vital;

import com.ryderbelserion.vital.common.api.managers.FileManager;
import com.ryderbelserion.vital.common.api.managers.files.CustomFile;
import com.ryderbelserion.vital.common.api.managers.files.enums.FileType;
import com.ryderbelserion.vital.common.api.managers.files.types.YamlCustomFile;
import com.ryderbelserion.vital.paper.Vital;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;

public class TestPlugin extends Vital {

    /**
     * Gets the plugin
     *
     * @return {@link TestPlugin}
     */
    public static TestPlugin getPlugin() {
        return JavaPlugin.getPlugin(TestPlugin.class);
    }

    @Override
    public void onEnable() {
        final FileManager fileManager = new FileManager();

        fileManager.addFile("config.yml", FileType.YAML);

        /*getFileManager().addFile("config.yml").addFile("data.yml").addFile("locations.yml").addFile("example.log", "logs").addFolder("crates").init();

        FileUtil.getFiles(new File(getDataFolder(), "crates"), ".yml", false);

        Config config = new Config(getDataFolder());
        config.load();

        Config.itemBurnTypes.forEach(item -> getLogger().warning("Item: " + item));

        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            LiteralArgumentBuilder<CommandSourceStack> root = new BaseCommand().registerPermission().literal().createBuilder();

            List.of(
                    new CommandItem(),
                    new CommandFile(),
                    new CommandGui()
            ).forEach(command -> root.then(command.registerPermission().literal()));

            event.registrar().register(root.build(), "the base command for Vital");
        });*/
    }
}