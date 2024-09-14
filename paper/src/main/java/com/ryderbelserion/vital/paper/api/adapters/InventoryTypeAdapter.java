package com.ryderbelserion.vital.paper.api.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Converts inventories to/from json
 *
 * @author ryderbelserion
 * @version 0.0.1
 * @since 0.0.1
 */
public class InventoryTypeAdapter implements JsonSerializer<Inventory>, JsonDeserializer<Inventory> {

    /**
     * Empty inventory type adapter constructor
     *
     * @since 0.0.1
     */
    public InventoryTypeAdapter() {}

    /**
     * Serialize content
     *
     * @param inventory the object that needs to be converted to Json.
     * @param type the actual type (fully genericized version) of the source object.
     * @param jsonSerializationContext {@link JsonSerializationContext}
     * @return {@link JsonElement}
     * @since 0.0.1
     */
    @Override
    public JsonElement serialize(final Inventory inventory, final Type type, final JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();

        object.add("contents", new JsonPrimitive(toBase64(inventory)));

        return object;
    }

    /**
     * Deserialize content
     *
     * @param jsonElement The Json data being deserialized
     * @param type The type of the Object to deserialize to
     * @param jsonDeserializationContext {@link JsonSerializationContext}
     * @return {@link Inventory}
     * @since 0.0.1
     */
    @Override
    public Inventory deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) {
        JsonObject object = jsonElement.getAsJsonObject();

        return fromBase64(object.get("contents").getAsString());
    }

    /**
     * Converts an array of items to a string.
     *
     * @param items {@link ItemStack} array
     * @return the string
     * @throws IllegalStateException throws if failed
     * @since 0.0.1
     */
    public static String inventoryToString(final ItemStack[] items) throws IllegalStateException {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeInt(items.length);

            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception exception) {
            throw new IllegalStateException("Unable to save item stacks.", exception);
        }
    }

    /**
     * Converts a string to an itemstack array
     *
     * @param data the string
     * @return {@link ItemStack} array
     * @throws IOException throws if failed
     * @since 0.0.1
     */
    public static ItemStack[] stringToInventory(final String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException exception) {
            throw new IOException("Unable to decode class type.", exception);
        }
    }

    /**
     * Converts a {@link Inventory} to base64
     *
     * @param inventory {@link Inventory}
     * @return {@link String}
     * @since 0.0.1
     */
    public static String toBase64(final Inventory inventory) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(inventory.getSize());

            // Save every element in the list
            for (int i = 0; i < inventory.getSize(); i++) {
                dataOutput.writeObject(inventory.getItem(i));
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception exception) {
            throw new IllegalStateException("Cannot convert itemstacks into base64!", exception);
        }
    }

    /**
     * Converts an array of {@link ItemStack} to base64
     *
     * @param is array of {@link ItemStack}
     * @param size the size
     * @return {@link String}
     * @since 0.0.1
     */
    public static String toBase64(final ItemStack[] is, final int size) {
        Inventory inventory = Bukkit.getServer().createInventory(null, size);

        inventory.setContents(is);

        return toBase64(inventory);
    }

    /**
     * Converts base64 string to {@link Inventory}
     *
     * @param data {@link String}
     * @return {@link Inventory}
     * @since 0.0.1
     */
    public static Inventory fromBase64(final String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            Inventory inventory = Bukkit.getServer().createInventory(null, dataInput.readInt());

            // Read the serialized inventory
            for (int i = 0; i < inventory.getSize(); i++) {
                inventory.setItem(i, (ItemStack) dataInput.readObject());
            }

            dataInput.close();

            return inventory;
        } catch (Exception ignored) {}

        return null;
    }
}