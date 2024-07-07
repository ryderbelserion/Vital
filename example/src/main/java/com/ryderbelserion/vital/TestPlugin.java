package com.ryderbelserion.vital;

import com.ryderbelserion.vital.commands.CommandManager;
import com.ryderbelserion.vital.paper.VitalPaper;
import com.ryderbelserion.vital.paper.files.config.FileManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    private FileManager fileManager;

    @Override
    public void onEnable() {
        new VitalPaper(this);

        this.fileManager = new FileManager();
        this.fileManager.addFile("data.yml").addFile("locations.yml").addFile("config.yml").init();

        CommandManager.load();
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }
}