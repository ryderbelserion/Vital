package com.ryderbelserion.vital.paper.api.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import java.io.IOException;

/**
 * Converts locations to/from json
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public class LocationTypeAdapter extends TypeAdapter<Location> {

    /**
     * Empty inventory type adapter constructor
     *
     * @since 0.0.1
     */
    public LocationTypeAdapter() {}

    /**
     * Write to json object
     *
     * @param out {@link JsonWriter}
     * @param location the Java object to write. May be null.
     * @throws IOException if failed
     * @since 0.0.1
     */
    @Override
    public void write(JsonWriter out, Location location) throws IOException {
        out.beginObject();

        out.name("world").value(location.getWorld().getName());
        out.name("x").value(location.getX());
        out.name("y").value(location.getY());
        out.name("z").value(location.getZ());
        out.name("yaw").value(location.getYaw());
        out.name("pitch").value(location.getPitch());

        out.endObject();
    }

    /**
     * Read from json object
     *
     * @param reader {@link JsonReader}
     * @throws IOException if failed
     * @since 0.0.1
     */
    @Override
    public Location read(JsonReader reader) throws IOException {
        reader.beginObject();

        String worldName = null;
        double x = 0, y = 0, z = 0;
        float yaw = 0, pitch = 0;

        while (reader.hasNext()) {
            String name = reader.nextName();
            
            switch (name) {
                case "world" -> worldName = reader.nextString();
                case "x" -> x = reader.nextDouble();
                case "y" -> y = reader.nextDouble();
                case "z" -> z = reader.nextDouble();
                case "yaw" -> yaw = (float) reader.nextDouble();
                case "pitch" -> pitch = (float) reader.nextDouble();
                default -> reader.skipValue();
            }
        }

        reader.endObject();

        assert worldName != null;

        return new Location(Bukkit.getServer().getWorld(worldName), x, y, z, yaw, pitch);
    }
}