package com.ryderbelserion.vital;

import com.ryderbelserion.vital.paper.VitalPaper;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new VitalPaper(this);
    }
}