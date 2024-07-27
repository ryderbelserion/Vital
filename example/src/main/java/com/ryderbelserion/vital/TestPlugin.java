package com.ryderbelserion.vital;

import com.ryderbelserion.vital.commands.CommandManager;
import com.ryderbelserion.vital.core.util.FileUtil;
import com.ryderbelserion.vital.paper.VitalPaper;
import com.ryderbelserion.vital.paper.files.config.FileManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    private FileManager fileManager;

    @Override
    public void onEnable() {
        new VitalPaper(this, false).setLogging(true);

        FileUtil.extract("config.yml", "examples", true);

        this.fileManager = new FileManager();
        this.fileManager.addFile("data.yml").addFile("locations.yml").addFile("config.yml").addFolder("codes").addFolder("vouchers").init();

        getLogger().warning("Size: " + this.fileManager.getCustomFiles().size());
        getLogger().warning("Other: " + this.fileManager.getFiles().size());

        CommandManager.load();
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }
}