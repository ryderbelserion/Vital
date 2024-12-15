package com.ryderbelserion.vital.paper.api.builders.items.v2;

import com.nexomc.nexo.api.NexoItems;
import com.nexomc.nexo.items.ItemBuilder;
import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.api.exceptions.GenericException;
import com.ryderbelserion.vital.paper.VitalPaper;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.vital.paper.api.enums.Support;
import com.ryderbelserion.vital.paper.util.PaperMethods;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.ItemArmorTrim;
import io.papermc.paper.datacomponent.item.ItemEnchantments;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.th0rgal.oraxen.api.OraxenItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * BaseItemBuilder is an abstract class for creating item builders.
 *
 * @param <B> the type of the builder extending this abstract class
 *
 * @author Ryder Belserion
 * @version 0.2.0
 * @since 0.2.0
 */
@ApiStatus.Experimental
public abstract class BaseItemBuilder<B extends BaseItemBuilder<B>> {

    private static final EnumSet<Material> POTIONS = EnumSet.of(
            Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION
    );

    /**
     * Gets the java plugin.
     */
    protected final JavaPlugin plugin = VitalPaper.getPlugin();

    /**
     * Gets vital api.
     */
    protected final Vital api = VitalProvider.get();

    private ItemStack itemStack;

    /**
     * Constructs a {@link BaseItemBuilder} with the provided {@link ItemStack}.
     *
     * @param itemStack the ItemStack to be wrapped by this builder, must not be null
     * @since 0.2.0
     */
    protected BaseItemBuilder(@NotNull final ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Constructs a {@link BaseItemBuilder} with the provided value.
     *
     * @param value the value representing the item, must not be null
     * @throws GenericException if the id is not a valid Nexo or Oraxen item
     * @since 0.2.0
     */
    protected BaseItemBuilder(@NotNull final String value) {
        if (Support.nexo.isEnabled() && NexoItems.exists(value)) {
            final ItemBuilder item = NexoItems.itemFromId(value);

            if (item != null) {
                this.itemStack = item.build();
            } else {
                throw new GenericException("The id " + value + " is not a valid Nexo item!");
            }

            return;
        }

        if (Support.oraxen.isEnabled() && OraxenItems.exists(value)) {
            final io.th0rgal.oraxen.items.ItemBuilder item = OraxenItems.getItemById(value);

            if (item != null) {
                this.itemStack = item.build();
            } else {
                throw new GenericException("The id " + value + " is not a valid Oraxen item!");
            }

            return;
        }

        this.itemStack = PaperMethods.fromBase64(value);
    }

    /**
     * Turns the builder into {@link ItemStack}.
     *
     * @return the fully built {@link ItemStack}
     * @since 0.2.0
     */
    public ItemStack asItemStack() {
        if (!this.displayName.isBlank()) {
            this.itemStack.setData(this.isFixed ? DataComponentTypes.ITEM_NAME : DataComponentTypes.CUSTOM_NAME, this.api.color(this.displayName));
        }

        if (!this.displayLore.isEmpty()) {
            final List<Component> components = new ArrayList<>();

            this.displayLore.forEach(line -> components.add(this.api.color(line)));

            final ItemLore lore = ItemLore.lore(components);

            this.itemStack.setData(DataComponentTypes.LORE, lore);
        }

        return this.itemStack;
    }

    /**
     * Sets the {@link ItemType} type.
     *
     * @param itemType the {@link ItemType} to set
     * @param amount the amount of {@link ItemType}
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withType(@Nullable final ItemType itemType, final int amount) {
        if (itemType == null) return (B) this;

        this.itemStack = itemType.createItemStack(amount);

        return (B) this;
    }

    /**
     * Sets the {@link ItemType} type.
     *
     * @param itemType the {@link ItemType} to set
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withType(@Nullable final ItemType itemType) {
        return withType(itemType, 1);
    }

    /**
     * Sets the amount an item should be.
     *
     * @param amount the amount
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withAmount(int amount) {
        if (amount <= 0) amount = 1;

        this.itemStack.setAmount(amount);

        return (B) this;
    }

    /**
     * Adds an {@link Enchantment} to the {@link ItemStack}.
     *
     * @param enchant the enchant name
     * @param level the enchant level
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B addEnchantment(@NotNull final String enchant, final int level) {
        if (enchant.isEmpty()) return (B) this;

        final Enchantment enchantment = PaperMethods.getEnchantment(enchant);

        if (enchantment == null) return (B) this;

        final ItemEnchantments.Builder builder = ItemEnchantments.itemEnchantments();

        final boolean isEnchantedBook = getType().equals(Material.ENCHANTED_BOOK);

        if (isEnchantedBook && this.itemStack.hasData(DataComponentTypes.STORED_ENCHANTMENTS)) {
            @Nullable final ItemEnchantments enchantments = this.itemStack.getData(DataComponentTypes.STORED_ENCHANTMENTS);

            if (enchantments != null) {
                builder.addAll(enchantments.enchantments());
            }
        } else if (this.itemStack.hasData(DataComponentTypes.ENCHANTMENTS)) {
            @Nullable final ItemEnchantments enchantments = this.itemStack.getData(DataComponentTypes.ENCHANTMENTS);

            if (enchantments != null) {
                builder.addAll(enchantments.enchantments());
            }
        }

        builder.add(enchantment, level);

        this.itemStack.setData(isEnchantedBook ? DataComponentTypes.STORED_ENCHANTMENTS : DataComponentTypes.ENCHANTMENTS, builder.build());

        return (B) this;
    }

    /**
     * Removes an enchantment from {@link ItemStack}.
     *
     * @param enchant the enchant name
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B removeEnchantment(@NotNull final String enchant) {
        if (enchant.isEmpty()) return (B) this;

        final Enchantment enchantment = PaperMethods.getEnchantment(enchant);

        if (enchantment == null) return (B) this;

        this.itemStack.removeEnchantment(enchantment);

        return (B) this;
    }

    /**
     * Removes all enchantments from {@link ItemStack}
     *
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B removeAllEnchantments() {
        this.itemStack.removeEnchantments();

        return (B) this;
    }

    private String displayName = "";
    private boolean isFixed = false;

    /**
     * Sets the display name. It includes a toggle which will prevent removing the name in anvils if set to true.
     *
     * @param displayName the {@link String} to use
     * @param isFixed true or false, if true. it will use ITEM_NAME DataComponentType, and if false CUSTOM_NAME DataComponentType
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withDisplayName(@NotNull final String displayName, final boolean isFixed) {
        this.displayName = displayName;

        this.isFixed = isFixed;

        return (B) this;
    }

    /**
     * Sets the display name.
     *
     * @param displayName the {@link String} to use
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withDisplayName(@NotNull final String displayName) {
        return withDisplayName(displayName, false);
    }

    /**
     * This method must be called after running {@link BaseItemBuilder#asItemStack()} or {@link BaseItemBuilder#asGuiItem()}.
     * It will return a string stripped of all styling.
     *
     * @return string without any styling
     * @since 0.2.0
     */
    public String getPlainName() {
        Component component = Component.empty();

        if (this.itemStack.hasData(DataComponentTypes.ITEM_NAME)) {
            final Component item_name = this.itemStack.getData(DataComponentTypes.ITEM_NAME);

            if (item_name != null) {
                component = item_name;
            }
        } else if (this.itemStack.hasData(DataComponentTypes.CUSTOM_NAME)) {
            final Component custom_name = this.itemStack.getData(DataComponentTypes.CUSTOM_NAME);

            if (custom_name != null) {
                component = custom_name;
            }
        }

        return PlainTextComponentSerializer.plainText().serializeOr(component, "");
    }

    private List<String> displayLore = new ArrayList<>();

    /**
     * Add to the display lore.
     *
     * @param displayLore the {@link String}
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B addDisplayLore(@NotNull final String displayLore) {
        if (displayLore.isEmpty()) return (B) this;

        this.displayLore.add(displayLore);

        return (B) this;
    }

    /**
     * Override the display lore.
     *
     * @param displayLore the {@link List<String>}
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B withDisplayLore(@NotNull final List<String> displayLore) {
        this.displayLore = displayLore;

        return (B) this;
    }

    /**
     * This method must be called after running {@link BaseItemBuilder#asItemStack()} or {@link BaseItemBuilder#asGuiItem()}.
     * It will return a list of strings stripped of all styling.
     *
     * @return lore without any styling
     * @since 0.2.0
     */
    public List<String> getPlainLore() {
        final List<String> plainLore = new ArrayList<>();

        if (this.itemStack.hasData(DataComponentTypes.LORE)) {
            final ItemLore lore = this.itemStack.getData(DataComponentTypes.LORE);

            if (lore != null) {
                lore.lines().forEach(line -> plainLore.add(PlainTextComponentSerializer.plainText().serialize(line)));
            }
        }

        return plainLore;
    }

    /**
     * Adds enchant glint to the item.
     *
     * @param enchantGlintOverride true or false
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B setEnchantGlint(final boolean enchantGlintOverride) {
        this.itemStack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, enchantGlintOverride);

        return (B) this;
    }

    /**
     * Removes enchant glint from the item.
     *
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B removeEnchantGlint() {
        this.itemStack.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);

        return (B) this;
    }

    /**
     * Hides the tooltip for this item by setting the HIDE_TOOLTIP data.
     *
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B hideToolTip() {
        this.itemStack.setData(DataComponentTypes.HIDE_TOOLTIP);

        return (B) this;
    }

    /**
     * Shows the tooltip for this item by unsetting the HIDE_TOOLTIP data.
     *
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B showToolTip() {
        if (!this.itemStack.hasData(DataComponentTypes.HIDE_TOOLTIP)) {
            return (B) this;
        }

        this.itemStack.unsetData(DataComponentTypes.HIDE_TOOLTIP);

        return (B) this;
    }

    /**
     * Hides the additional tooltip for this item by setting the HIDE_ADDITIONAL_TOOLTIP data.
     *
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B hideAdditionalToolTip() {
        this.itemStack.setData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP);

        return (B) this;
    }

    /**
     * Shows the additional tooltip for this item by unsetting the HIDE_ADDITIONAL_TOOLTIP data.
     *
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B showAdditionalToolTip() {
        if (!this.itemStack.hasData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)) {
            return (B) this;
        }

        this.itemStack.unsetData(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP);

        return (B) this;
    }

    /**
     * Changes the item model of the {@link ItemStack}.
     *
     * @param itemModel the item model found in the resource pack/data pack
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B setItemModel(@NotNull final String itemModel) {
        if (itemModel.isEmpty()) return (B) this;

        this.itemStack.setData(DataComponentTypes.ITEM_MODEL, NamespacedKey.minecraft(itemModel));

        return (B) this;
    }

    /**
     * Adds a {@link ArmorTrim} to a piece of armor.
     *
     * @param pattern the pattern to add
     * @param material the material to use
     * @param hideToolTip true or false
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B setTrim(@NotNull final String pattern, @NotNull final String material, final boolean hideToolTip) {
        if (pattern.isEmpty() || material.isEmpty()) return (B) this;

        final TrimMaterial trimMaterial = PaperMethods.getTrimMaterial(material);

        if (trimMaterial == null) {
            return (B) this;
        }

        final TrimPattern trimPattern = PaperMethods.getTrimPattern(pattern);

        if (trimPattern == null) {
            return (B) this;
        }

        final ItemArmorTrim.Builder builder = ItemArmorTrim
                .itemArmorTrim(new ArmorTrim(trimMaterial, trimPattern))
                .showInTooltip(hideToolTip);

        this.itemStack.setData(DataComponentTypes.TRIM, builder.build());

        return (B) this;
    }

    /**
     * Sets the item damage to the {@link ItemStack}.
     *
     * @param damage the item damage
     * @return {@link BaseItemBuilder}
     * @since 0.2.0
     */
    public B setItemDamage(final int damage) {
        this.itemStack.setData(DataComponentTypes.DAMAGE, Math.min(damage, getType().getMaxDurability()));

        return (B) this;
    }

    /**
     * Changes the custom model data of the {@link ItemStack}.
     *
     * @param modelData the model data found in the resource pack/data pack
     * @return 0.2.0
     */
    public B setModelData(final int modelData) {
        if (modelData == -1) return (B) this;

        this.itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData().addFloat(modelData).build());

        return (B) this;
    }

    /**
     * Adds the item to the inventory at a specific slot.
     *
     * @param inventory {@link Inventory}
     * @param slot the slot
     * @since 0.2.0
     */
    public void setItemToInventory(final Inventory inventory, final int slot) {
        inventory.setItem(slot, this.itemStack);
    }

    /**
     * Adds the item to the inventory.
     *
     * @param inventory {@link Inventory}
     * @since 0.2.0
     */
    public void addItemToInventory(final Inventory inventory) {
        inventory.addItem(this.itemStack);
    }

    /**
     * Creates an instance of {@link SkullBuilder}.
     *
     * @return {@link SkullBuilder}
     * @since 0.2.0
     */
    public SkullBuilder asSkullBuilder() {
        if (!isPlayerHead()) {
            throw new GenericException("This item type is not a skull");
        }

        return new SkullBuilder(this.itemStack);
    }

    /**
     * Creates an instance of {@link PotionBuilder}.
     *
     * @return {@link PotionBuilder}
     * @since 0.2.0
     */
    public PotionBuilder asPotionBuilder() {
        if (!isPotion()) {
            throw new GenericException("This item type is not a potion");
        }

        return new PotionBuilder(this.itemStack);
    }

    /**
     * Converts {@link ItemStack} to {@link GuiItem}.
     *
     * @param action runs a click event
     * @return {@link GuiItem}
     * @since 0.2.0
     */
    public GuiItem asGuiItem(@Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        return new GuiItem(asItemStack(), action);
    }

    /**
     * Converts {@link ItemStack} to {@link GuiItem}.
     *
     * @return {@link GuiItem}
     * @since 0.2.0
     */
    public GuiItem asGuiItem() {
        return new GuiItem(asItemStack(), null);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a player head.
     *
     * @return true or false
     * @since 0.2.0
     */
    protected final boolean isPlayerHead() {
        return getType().equals(Material.PLAYER_HEAD);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a firework star.
     *
     * @return true or false
     * @since 0.2.0
     */
    protected final boolean isFireworkStar() {
        return getType().equals(Material.FIREWORK_STAR);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a firework.
     *
     * @return true or false
     * @since 0.2.0
     */
    protected final boolean isFirework() {
        return getType().equals(Material.FIREWORK_ROCKET);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a spawner.
     *
     * @return true or false
     * @since 0.2.0
     */
    protected final boolean isSpawner() {
        return getType().equals(Material.SPAWNER);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a shield.
     *
     * @return true or false
     * @since 0.2.0
     */
    protected final boolean isShield() {
        return getType().equals(Material.SHIELD);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a potion.
     *
     * @return true or false
     * @since 0.2.0
     */
    protected final boolean isPotion() {
        return POTIONS.contains(getType());
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is an arrow.
     *
     * @return true or false
     * @since 0.2.0
     */
    protected final boolean isArrow() {
        return getType().equals(Material.ARROW);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a map.
     *
     * @return true or false
     * @since 0.2.0
     */
    protected final boolean isMap() {
        return getType().equals(Material.MAP);
    }

    /**
     * Gets the {@link ItemType} type.
     *
     * @return the {@link ItemType} type
     * @since 0.2.0
     */
    protected final @NotNull Material getType() {
        return this.itemStack.getType();
    }

    /**
     * Gets the current ItemStack instance.
     *
     * @return {@link ItemStack}
     * @since 0.2.0
     */
    protected @NotNull ItemStack getItemStack() {
        return this.itemStack;
    }
}