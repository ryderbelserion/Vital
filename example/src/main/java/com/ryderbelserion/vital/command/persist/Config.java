package com.ryderbelserion.vital.command.persist;

import com.google.gson.annotations.Expose;
import com.ryderbelserion.vital.api.storage.Serializer;
import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

public class Config {

    @Expose
    public static boolean bloodSprayToggle = false;

    @Expose
    public static boolean fastIceBreak = false;

    @Expose
    public static boolean denyPearlGlitch = false;

    @Expose
    public static boolean denyFireSpread = false;

    @Expose
    public static boolean denyIceMelt = false;

    @Expose
    public static boolean denyEndermanGrief = false;

    @Expose
    public static boolean denyEndermanTeleport = false;

    @Expose
    public static boolean denyEndermanTarget = false;

    @Expose
    public static boolean denyBlazeDrowning = false;

    @Expose
    public static boolean denyPoppyDrops = false;

    @Expose
    public static boolean denyWildernessSpawners = false;

    @Expose
    public static boolean denyBabyMobs = false;

    @Expose
    public static boolean denySpawnerStorage = false;

    @Expose
    public static boolean denySpawnerProtection = false;

    @Expose
    public static boolean denyMobItemPickup = false;

    @Expose
    public static boolean denyRespawnScreen = false;

    @Expose
    public static boolean denyItemBurn = false;

    @Expose
    public static boolean enableTrackX = true;

    @Expose
    public static boolean cleanScoreboardDatOnStart = false;

    @Expose
    public static Set<String> itemBurnTypes = new LinkedHashSet<>() {{
        add("fire_tick");
        add("fire");
        add("lava");
        add("block_explosion");
        add("entity_explosion");
    }};

    @Expose
    public static boolean denyWaterItemBreak = false;

    @Expose
    public static Set<String> itemWaterDeny = new LinkedHashSet<>() {{
        add("torch");
        add("redstone_torch");
        add("redstone");
    }};

    @Expose
    public static boolean denyWeather = false;

    @Expose
    public static Set<String> denyWeatherInWorlds = new LinkedHashSet<>() {{
        add("world");
    }};

    private final Serializer<Config> serializer;

    public Config(final File dataFolder) {
        this.serializer = new Serializer<>(new File(dataFolder, "config.json"), this)
                .withoutExposeAnnotation()
                .setPrettyPrinting();
    }

    public void save() {
        this.serializer.write();
    }

    public void load() {
        this.serializer.load();
    }
}