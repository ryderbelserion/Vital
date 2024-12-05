package com.ryderbelserion.vital.paper.api.builders.items;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.nexomc.nexo.api.NexoItems;
import com.ryderbelserion.vital.VitalProvider;
import com.ryderbelserion.vital.api.Vital;
import com.ryderbelserion.vital.api.exceptions.GenericException;
import com.ryderbelserion.vital.paper.api.builders.PlayerBuilder;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiAction;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.vital.paper.api.enums.Support;
import com.ryderbelserion.vital.paper.util.PaperMethods;
import com.ryderbelserion.vital.utils.Methods;
import io.th0rgal.oraxen.api.OraxenItems;
import me.arcaniax.hdb.api.HeadDatabaseAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Tag;
import org.bukkit.block.Banner;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.damage.DamageType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.tag.DamageTypeTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The itembuilder to end all itembuilders.
 *
 * @param <T> the class extending {@link ItemBuilder}
 *
 * @author Ryder Belserion
 * @author SvenjaReissaus
 * @version 0.1.0
 * @since 0.1.0
 */
public class ItemBuilder<T extends ItemBuilder<T>> {

    private final Vital api = VitalProvider.get();

    private final NbtBuilder nbt = new NbtBuilder();

    private ItemStack itemStack;

    /**
     * Constructs a new {@link ItemStack} with a dummy {@link ItemType}.
     *
     * @since 0.1.0
     */
    public ItemBuilder() {
        this(ItemType.STONE, 1);
    }

    /**
     * Constructs a new {@link ItemStack} with the specified {@link ItemType}.
     *
     * @param itemType the {@link ItemType} to use
     * @since 0.1.0
     */
    public ItemBuilder(@NotNull final ItemType itemType) {
        this(itemType, 1);
    }

    /**
     * Constructs a new {@link ItemStack} with the specified {@link ItemType} and amount.
     *
     * @param itemType the {@link ItemType} to use
     * @param amount the amount to set
     * @since 0.1.0
     */
    public ItemBuilder(@NotNull final ItemType itemType, final int amount) {
        this(itemType.createItemStack(amount), true);
    }

    /**
     * Constructs a new {@link ItemStack} with the specified {@link ItemType} and amount.
     *
     * @param itemStack the {@link ItemStack}
     * @param createNewStack create a new {@link ItemStack} or reuse the passed object
     * @since 0.1.0
     */
    public ItemBuilder(@NotNull final ItemStack itemStack, final boolean createNewStack) {
        this.itemStack = createNewStack ? itemStack.clone() : itemStack;
    }

    /**
     * Deconstructs an existing {@link ItemBuilder} without creating a new {@link ItemStack}.
     *
     * @param itemBuilder the {@link ItemBuilder}
     * @since 0.1.0
     */
    public ItemBuilder(@NotNull final ItemBuilder<T> itemBuilder) {
        this(itemBuilder, false);
    }

    /**
     * Deconstructs an existing {@link ItemBuilder} while creating a new {@link ItemStack}.
     *
     * @param itemBuilder the {@link ItemBuilder}
     * @param createNewStack create a new {@link ItemStack} or reuse the passed object
     * @since 0.1.0
     */
    public ItemBuilder(@NotNull final ItemBuilder<T> itemBuilder, final boolean createNewStack) {
        this.itemStack = createNewStack ? itemBuilder.itemStack.clone() : itemBuilder.itemStack;
        this.isCustom = itemBuilder.isCustom;

        this.customModelData = itemBuilder.customModelData;
        this.damage = itemBuilder.damage;
        this.color = itemBuilder.color;

        this.displayLorePlaceholders = itemBuilder.displayLorePlaceholders;
        this.displayNamePlaceholders = itemBuilder.displayNamePlaceholders;

        this.displayComponentLore = itemBuilder.displayComponentLore;
        this.displayComponent = itemBuilder.displayComponent;
        this.displayLore = itemBuilder.displayLore;
        this.displayName = itemBuilder.displayName;

        this.potionEffects = itemBuilder.potionEffects;
        this.fireworkEffects = itemBuilder.fireworkEffects;

        this.entityType = itemBuilder.entityType;

        this.nutritionalValue = itemBuilder.nutritionalValue;
        this.canAlwaysEat = itemBuilder.canAlwaysEat;
        this.saturation = itemBuilder.saturation;
        this.eatSeconds = itemBuilder.eatSeconds;

        this.fireworkPower = itemBuilder.fireworkPower;

        this.itemFlags = itemBuilder.itemFlags;
        this.patterns = itemBuilder.patterns;

        this.player = itemBuilder.player;
        this.url = itemBuilder.url;
        this.uuid = itemBuilder.uuid;

        this.potionType = itemBuilder.potionType;

        this.isHidingItemFlags = itemBuilder.isHidingItemFlags;
        this.isHidingToolTips = itemBuilder.isHidingToolTips;

        this.isUnbreakable = itemBuilder.isUnbreakable;
        this.isGlowing = itemBuilder.isGlowing;

        this.trimMaterial = itemBuilder.trimMaterial;
        this.trimPattern = itemBuilder.trimPattern;
    }

    private static final EnumSet<Material> LEATHER_ARMOR = EnumSet.of(
            Material.LEATHER_HELMET,
            Material.LEATHER_CHESTPLATE,
            Material.LEATHER_LEGGINGS,
            Material.LEATHER_BOOTS,
            Material.LEATHER_HORSE_ARMOR
    );

    private static final EnumSet<Material> POTIONS = EnumSet.of(
            Material.POTION, Material.SPLASH_POTION, Material.LINGERING_POTION
    );

    private static final EnumSet<Material> BANNERS = EnumSet.of(
            Material.WHITE_BANNER, Material.ORANGE_BANNER, Material.MAGENTA_BANNER, Material.LIGHT_BLUE_BANNER, Material.YELLOW_BANNER,
            Material.LIME_BANNER, Material.PINK_BANNER, Material.GRAY_BANNER, Material.LIGHT_GRAY_BANNER, Material.CYAN_BANNER,
            Material.PURPLE_BANNER, Material.BLUE_BANNER, Material.BROWN_BANNER, Material.GREEN_BANNER, Material.RED_BANNER,
            Material.BLACK_BANNER,
            Material.WHITE_WALL_BANNER, Material.ORANGE_WALL_BANNER, Material.MAGENTA_WALL_BANNER, Material.LIGHT_BLUE_WALL_BANNER, Material.YELLOW_WALL_BANNER,
            Material.LIME_WALL_BANNER, Material.PINK_WALL_BANNER, Material.GRAY_WALL_BANNER, Material.LIGHT_GRAY_WALL_BANNER, Material.CYAN_WALL_BANNER,
            Material.PURPLE_WALL_BANNER, Material.BLUE_WALL_BANNER, Material.BROWN_WALL_BANNER, Material.GREEN_WALL_BANNER, Material.RED_WALL_BANNER,
            Material.BLACK_WALL_BANNER
    );

    private static final EnumSet<Material> ARMOR = EnumSet.of(
            Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS,
            Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS,
            Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS,
            Material.GOLDEN_HELMET, Material.GOLDEN_CHESTPLATE, Material.GOLDEN_LEGGINGS, Material.GOLDEN_BOOTS,
            Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS,
            Material.NETHERITE_HELMET, Material.NETHERITE_CHESTPLATE, Material.NETHERITE_LEGGINGS, Material.NETHERITE_BOOTS
    );

    private static final EnumSet<Material> SHULKERS = EnumSet.of(
            Material.SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BLACK_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.GRAY_SHULKER_BOX,
            Material.BROWN_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.LIME_SHULKER_BOX,
            Material.MAGENTA_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.ORANGE_SHULKER_BOX,
            Material.RED_SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX
    );

    /**
     * Deconstructs an existing {@link ItemStack}.
     *
     * @param itemStack the {@link ItemStack}
     * @since 0.1.0
     */
    public ItemBuilder(@NotNull final ItemStack itemStack) {
        this.itemStack = itemStack;

        if (hasItemMeta()) {
            final ItemMeta itemMeta = this.itemStack.getItemMeta();

            if (itemMeta.hasDisplayName()) {
                this.displayComponent = itemMeta.displayName();
            }

            if (itemMeta.hasLore()) {
                this.displayComponentLore = itemMeta.lore();
            }

            // Populates the custom model data
            if (itemMeta.hasCustomModelData()) this.customModelData = Optional.of(itemMeta.getCustomModelData());

            // Populates the damage
            this.damage = itemMeta instanceof final Damageable damageable ? damageable.getDamage() : 1;

            // Populates the power field
            if (isFirework() || isFireworkStar()) {
                if (itemMeta instanceof final FireworkMeta firework) {
                    this.fireworkEffects = firework.getEffects();
                    this.fireworkPower = firework.getPower();
                }
            } else if (isSpawner()) { // populates the entity type field
                if (itemMeta instanceof final BlockStateMeta blockState) {
                    @NotNull final CreatureSpawner creatureSpawner = (CreatureSpawner) blockState.getBlockState();
                    @Nullable final EntityType type = creatureSpawner.getSpawnedType();

                    if (type != null) this.entityType = type;
                }
            } else if (isBanner()) {
                if (itemMeta instanceof final BannerMeta banner) {
                    this.patterns.addAll(banner.getPatterns());
                }
            } else if (isShield()) {
                if (itemMeta instanceof final BlockStateMeta shield) {
                    @NotNull final Banner banner = (Banner) shield.getBlockState();

                    this.patterns.addAll(banner.getPatterns());
                }
            } else if (isPlayerHead()) {
                if (itemMeta instanceof final SkullMeta skull) {
                    if (skull.hasOwner()) {
                        @Nullable final OfflinePlayer target = skull.getOwningPlayer();

                        if (target != null) this.uuid = target.getUniqueId();
                    }
                }
            } else if (isArrow() || isPotion()) {
                if (itemMeta instanceof final PotionMeta potionMeta) {
                    this.color = potionMeta.getColor();
                    this.potionEffects = potionMeta.getCustomEffects();
                    this.potionType = potionMeta.getBasePotionType();
                }
            } else if (isLeather()) {
                if (itemMeta instanceof final LeatherArmorMeta armor) this.color = armor.getColor();
            } else if (isMap()) {
                if (itemMeta instanceof final MapMeta map) this.color = map.getColor();
            }

            setHidingToolTips(itemMeta.isHideTooltip())
                    .setHidingItemFlags(itemMeta.getItemFlags().contains(ItemFlag.HIDE_ATTRIBUTES)) //todo() itemflags are dead
                    .setUnbreakable(itemMeta.isUnbreakable());

            if (itemMeta.hasDamageResistant()) {
                final Tag<DamageType> tag = itemMeta.getDamageResistant();

                if (tag != null) {
                    this.damageTags = new ArrayList<>() {{
                        add(tag);
                    }};
                }
            }

            if (itemMeta.hasEnchantmentGlintOverride()) setGlowing(itemMeta.getEnchantmentGlintOverride());
        }
    }

    /**
     * Declares if this {@link ItemStack} was created from a custom source.
     */
    private boolean isCustom = false;

    /**
     * Holds the {@link Color} of the {@link ItemStack}.
     */
    private @Nullable Color color = null;

    /**
     * Holds the custom model data for the {@link ItemStack}.
     */
    private @NotNull Optional<Number> customModelData = Optional.empty();

    /**
     * Holds the amount of damage for the {@link ItemStack}.
     */
    private int damage = 0;

    /**
     * Holds the {@link List<String>} of the {@link ItemStack}.
     */
    private List<String> displayLore = new ArrayList<>();

    /**
     * Holds the {@link List<Component>} of the {@link ItemStack}.
     */
    private List<Component> displayComponentLore = new ArrayList<>();

    /**
     * Holds the placeholders to be used with the lore.
     */
    private Map<String, String> displayLorePlaceholders = new HashMap<>();

    /**
     * Holds the {@link String} of the {@link ItemStack}.
     */
    private String displayName = "";

    /**
     * Holds the {@link Component} of the {@link ItemStack}.
     */
    private Component displayComponent = Component.empty();

    /**
     * Holds the placeholders to be used with the display name.
     */
    private Map<String, String> displayNamePlaceholders = new HashMap<>();

    /**
     * Holds a {@link List<ConfigurationSerializable>} that may be used in fireworks or potions.
     */
    private List<PotionEffect> potionEffects = new ArrayList<>();

    private List<FireworkEffect> fireworkEffects = new ArrayList<>();

    /**
     * Holds the {@link EntityType} of the {@link ItemStack}.
     */
    private @NotNull EntityType entityType = EntityType.PIG;

    /**
     * Declares whether the {@link ItemStack} can be eaten.
     */
    private boolean canAlwaysEat = false;

    /**
     * Holds the nutritional value of the edible {@link ItemStack}.
     */
    private int nutritionalValue = 0;

    /**
     * Holds the saturation value of the edible {@link ItemStack}.
     */
    private float saturation = 0f;

    /**
     * Holds the time to eat in seconds of the edible {@link ItemStack}.
     */
    private float eatSeconds = 0;

    /**
     * Holds the power of the firework of the {@link ItemStack}.
     */
    private int fireworkPower = 1;

    /**
     * Holds the {@link List<ItemFlag>} of {@link ItemFlag} of the {@link ItemStack}.
     */
    private @NotNull List<ItemFlag> itemFlags = new ArrayList<>(); //todo() itemflags are dead

    /**
     * Holds the {@link List<Pattern>} used by the {@link ItemStack}.
     */
    private @NotNull List<Pattern> patterns = new ArrayList<>();

    /**
     * Holds the {@link UUID} of the {@link Player} relevant to the {@link ItemStack}.
     */
    private @Nullable UUID player = null;

    /**
     * Holds the {@link PotionType} used by the {@link ItemStack}.
     */
    private @Nullable PotionType potionType = null;

    /**
     * Holds the {@link UUID} of the skull of which this {@link ItemStack} belongs to.
     */
    private @Nullable UUID uuid = null;

    /**
     * Holds the url for the skull.
     */
    private String url = "";

    /**
     * Declares if this {@link ItemStack} is hiding item flags.
     */
    private boolean isHidingItemFlags = false; //todo() itemflags are dead

    /**
     * Declares if the {@link ItemStack} hides all tooltips.
     */
    private boolean isHidingToolTips = false;

    /**
     * List of {@link Tag<DamageType>} to apply to the {@link ItemStack}.
     */
    private List<Tag<DamageType>> damageTags = new ArrayList<>();

    /**
     * Declares if this {@link ItemStack} is unbreakable.
     */
    private boolean isUnbreakable = false;

    /**
     * Declares if this {@link ItemStack} is glowing.
     */
    private Boolean isGlowing = null;

    /**
     * Holds the {@link TrimMaterial} for the armor.
     */
    private TrimMaterial trimMaterial;

    /**
     * Holds the {@link TrimPattern} for the armor.
     */
    private TrimPattern trimPattern;
    /**
     * Converts {@link ItemStack} to {@link GuiItem}
     *
     * @param action runs a click event
     * @return {@link GuiItem}
     * @since 0.1.0
     */
    public @NotNull GuiItem asGuiItem(@Nullable final GuiAction<@NotNull InventoryClickEvent> action) {
        return new GuiItem(asItemStack(), action);
    }

    /**
     * Converts {@link ItemStack} to {@link GuiItem}
     *
     * @return {@link GuiItem}
     * @since 0.1.0
     */
    public @NotNull GuiItem asGuiItem() {
        return new GuiItem(asItemStack(), null);
    }

    /**
     * Gets the {@link ItemStack} without a consumer
     *
     * @return {@link ItemStack}
     */
    public @NotNull ItemStack asItemStack() {
        return asItemStack(null);
    }

    /**
     * Turns the builder into {@link ItemStack}.
     *
     * @param consumer {@link Consumer<ItemMeta>}
     * @return the fully built {@link ItemStack}
     * @since 0.1.0
     */
    public @NotNull ItemStack asItemStack(@Nullable final Consumer<ItemMeta> consumer) {
        if (this.isCustom) return this.itemStack;

        // Daisy chain it all!
        applyColor().applyEffects().applyEntityType().applyPattern().applySkull().applyTexture().applyDamage();

        if (this.trimPattern != null && this.trimMaterial != null) {
            applyTrim(this.trimPattern, this.trimMaterial);
        }

        this.itemStack.editMeta(itemMeta -> {
            if (consumer != null) consumer.accept(itemMeta);

            String displayName = this.displayName;

            if (!displayName.isEmpty()) {
                if (!this.displayNamePlaceholders.isEmpty()) {
                    for (final Map.Entry<String, String> entry : this.displayNamePlaceholders.entrySet()) {
                        final String key = entry.getKey().toLowerCase(); // Make the placeholder lowercase
                        final String value = entry.getValue();

                        displayName = displayName.replace(key, value);
                    }
                }

                itemMeta.displayName(this.displayComponent = Methods.parse(displayName));
            }

            if (!this.displayLore.isEmpty()) {
                final boolean isEmpty = this.displayLorePlaceholders.isEmpty();

                final List<Component> components = new ArrayList<>();

                for (String line : this.displayLore) {
                    if (!isEmpty) {
                        for (final Map.Entry<String, String> entry : this.displayLorePlaceholders.entrySet()) {
                            final String key = entry.getKey().toLowerCase();
                            final String value = entry.getValue();

                            line = line.replace(key, value);
                        }
                    }

                    components.add(Methods.parse(line));
                }

                itemMeta.lore(this.displayComponentLore = components);
            }

            this.customModelData.ifPresent(number -> {
                if (number.intValue() == -1) return;

                itemMeta.setCustomModelData(number.intValue());
            });

            if (this.isHidingItemFlags) { //todo() itemflags are dead
                itemMeta.addItemFlags(ItemFlag.values());
            } else {
                this.itemFlags.forEach(itemMeta::addItemFlags);
            }

            itemMeta.setEnchantmentGlintOverride(this.isGlowing);

            this.damageTags.forEach(itemMeta::setDamageResistant);

            itemMeta.setHideTooltip(this.isHidingToolTips);
            itemMeta.setUnbreakable(this.isUnbreakable);
        });

        return this.itemStack;
    }

    /**
     * Encodes and serializes an {@link ItemStack} into a {@link String}.
     *
     * @return {@link String}
     * @since 0.1.0
     */
    public @NotNull final String toBase64() {
        return PaperMethods.toBase64(asItemStack());
    }

    /**
     * Deserialize a base64 {@link String} back into an {@link ItemStack}.
     *
     * @param base64 the base64 {@link String}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T fromBase64(@NotNull final String base64) {
        if (base64.isEmpty()) return (T) new ItemBuilder<T>();

        return (T) new ItemBuilder<T>(PaperMethods.fromBase64(base64));
    }

    /**
     * A consumer for itembuilder
     *
     * @param builder {@link ItemBuilder}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T apply(final Consumer<ItemBuilder<T>> builder) {
        if (builder != null) {
            builder.accept(this);
        }

        return (T) this;
    }

    /**
     * Sets the {@link ItemType} type.
     *
     * @param itemType the {@link ItemType} to set
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T withType(@Nullable final ItemType itemType) {
        if (itemType == null) return (T) this;

        this.itemStack = itemType.createItemStack();

        return (T) this;
    }

    /**
     * Sets the {@link ItemType} type.
     *
     * @param itemType the {@link ItemType} to set
     * @param amount the amount of {@link ItemType}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T withType(@Nullable final ItemType itemType, final int amount) {
        if (itemType == null) return (T) this;

        this.itemStack = itemType.createItemStack(amount);

        return (T) this;
    }

    /**
     * Sets the {@link ItemType} type.
     *
     * @param key the {@link ItemType} to set
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T withType(@NotNull final String key) {
        if (key.isEmpty()) return (T) this;

        if (Support.nexo.isEnabled()) {
            if (NexoItems.exists(key)) {
                com.nexomc.nexo.items.ItemBuilder item = NexoItems.itemFromId(key);

                if (item != null) {
                    this.itemStack = item.build();
                } else {
                    throw new GenericException("The id " + key + " is not a valid Nexo item!");
                }
            }
        } else if (Support.oraxen.isEnabled()) {
            if (OraxenItems.exists(key)) {
                io.th0rgal.oraxen.items.ItemBuilder item = OraxenItems.getItemById(key);

                if (item != null) {
                    this.itemStack = item.build();
                } else {
                    throw new GenericException("The id " + key + " is not a valid Oraxen item!");
                }
            }
        }

        // Don't override the provided material but copy it instead.
        String type = key;

        if (key.contains(":")) {
            final String[] sections = key.split(":");

            type = sections[0];
            String data = sections[1];

            if (data.contains("#")) {
                final String model = data.split("#")[1];

                this.customModelData = Methods.tryParseInt(model);

                if (this.customModelData.isPresent()) data = data.replace("#" + this.customModelData.get(), "");
            }

            final Optional<Number> damage = Methods.tryParseInt(data);

            if (damage.isEmpty()) {
                @Nullable final PotionEffectType potionEffect = PaperMethods.getPotionEffect(data);

                if (potionEffect != null) {
                    this.potionEffects.add(new PotionEffect(potionEffect, 1, 1));
                }

                this.potionType = PaperMethods.getPotionType(data);

                this.color = data.contains(",") ? PaperMethods.getColor(data) : PaperMethods.getDefaultColor(data);
            } else {
                this.damage = damage.get().intValue();
            }
        } else if (key.contains("#")) {
            final String[] sections = key.split("#");
            type = sections[0];
            final String model = sections[1];

            this.customModelData = Methods.tryParseInt(model);
        }

        final @Nullable ItemType itemType = PaperMethods.getItemType(type);

        if (itemType == null) return (T) this;

        return withType(itemType);
    }

    /**
     * Override the display lore.
     *
     * @param displayLore the {@link List<String>}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setDisplayLore(@NotNull final List<String> displayLore) {
        if (displayLore.isEmpty()) return (T) this;

        this.displayLore = displayLore;

        return (T) this;
    }

    /**
     * Add to the display lore.
     *
     * @param displayLore the {@link String}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addDisplayLore(@NotNull final String displayLore) {
        if (displayLore.isEmpty()) return (T) this;

        this.displayLore.add(displayLore);

        return (T) this;
    }

    /**
     * Sets the display name.
     *
     * @param displayName the {@link String} to use
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setDisplayName(@NotNull final String displayName) {
        if (displayName.isEmpty()) return (T) this;

        this.displayName = displayName;

        return (T) this;
    }

    /**
     * Changes the custom model data of the {@link ItemStack}.
     *
     * @param model the {@link Integer}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setCustomModelData(final int model) {
        if (model == -1) return (T) this;

        this.customModelData = Optional.of(model);

        return (T) this;
    }

    /**
     * Hides all item flags on an item.
     *
     * @param isHidingItemFlags true or false
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setHidingItemFlags(final boolean isHidingItemFlags) { //todo() itemflags are dead
        this.isHidingItemFlags = isHidingItemFlags;

        return (T) this;
    }

    /**
     * Hides all tooltips on items.
     *
     * @param isHidingToolTips true or false
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setHidingToolTips(final boolean isHidingToolTips) {
        this.isHidingToolTips = isHidingToolTips;

        return (T) this;
    }

    /**
     * Makes the item fire-resistant.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     *
     * @deprecated
     */
    @Deprecated(forRemoval = true)
    public @NotNull T setFireResistant() {
        return addDamageTag(DamageTypeTags.IS_FIRE);
    }

    /**
     * Adds a damage type tag to the list.
     *
     * @param tag {@link Tag<DamageType>}
     * @return {@link ItemBuilder}
     * @since 2.1.3
     */
    public @NotNull T addDamageTag(final Tag<DamageType> tag) {
        this.damageTags.add(tag);

        return (T) this;
    }

    /**
     * Removes a damage type tag from the list.
     *
     * @param tag {@link Tag<DamageType>}
     * @return {@link ItemBuilder}
     * @since 2.1.3
     */
    public @NotNull T removeDamageTag(final Tag<DamageType> tag) {
        this.damageTags.remove(tag);

        return (T) this;
    }

    /**
     * Adds a new Firework Effect to the {@link ItemStack} if the {@link ItemType} allows it.
     *
     * @param effect the {@link FireworkEffect.Builder}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addFireworkEffect(@NotNull final FireworkEffect.Builder effect) {
        if (!isFirework() && !isFireworkStar()) return (T) this;

        this.fireworkEffects.add(effect.build());

        return (T) this;
    }

    /**
     * Sets the Firework Power of the {@link ItemStack}.
     *
     * @param fireworkPower the power of the firework i.e. how far up it shoots
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setFireworkPower(final int fireworkPower) {
        if (!isFirework() && !isFireworkStar()) return (T) this;

        this.fireworkPower = fireworkPower;

        return (T) this;
    }

    /**
     * Makes the item unbreakable.
     *
     * @param isUnbreakable true or false
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setUnbreakable(final boolean isUnbreakable) {
        this.isUnbreakable = isUnbreakable;

        return (T) this;
    }

    /**
     * Makes the item glow.
     *
     * @param isGlowing true or false or null
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setGlowing(@Nullable final Boolean isGlowing) {
        this.isGlowing = isGlowing;

        return (T) this;
    }

    /**
     * Sets the amount an item should be.
     *
     * @param amount the amount
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setAmount(final int amount) {
        this.itemStack.setAmount(amount);

        return (T) this;
    }

    /**
     * Adds a placeholder to the hashmap for display names.
     *
     * @param placeholder the placeholder to add
     * @param value the value to replace the placeholder with
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addNamePlaceholder(@NotNull final String placeholder, @NotNull final String value) {
        return addPlaceholder(placeholder, value, false);
    }


    /**
     * Adds a map of placeholders to the name
     *
     * @param placeholders the map of placeholders
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setNamePlaceholders(Map<String, String> placeholders) {
        placeholders.forEach(this::addNamePlaceholder);
        
        return (T) this;
    }

    /**
     * Adds a placeholder to the hashmap for display lore.
     *
     * @param placeholder the placeholder to add
     * @param value the value to replace the placeholder with
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addLorePlaceholder(@NotNull final String placeholder, @NotNull final String value) {
        return addPlaceholder(placeholder, value, true);
    }

    /**
     * Adds a map of placeholders to the lore
     *
     * @param placeholders the map of placeholders
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setLorePlaceholders(Map<String, String> placeholders) {
        placeholders.forEach(this::addLorePlaceholder);

        return (T) this;
    }

    /**
     * Adds a {@link Pattern} to the {@link ItemStack} from a {@link PatternType} and {@link DyeColor}.
     *
     * @param type the {@link PatternType}
     * @param color the {@link DyeColor}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addPattern(@NotNull final PatternType type, @NotNull final DyeColor color) {
        if (!isBanner() && !isShield()) return (T) this;

        this.patterns.add(new Pattern(color, type));

        return (T) this;
    }

    /**
     * Adds a {@link Pattern} to the {@link ItemStack} from a {@link String} representation.
     *
     * @param pattern the {@link String} to convert
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addPattern(@NotNull final String pattern) {
        if (!isBanner() && !isShield()) return (T) this;

        if (!pattern.contains(":")) return (T) this;

        final String[] sections = pattern.split(":");
        final PatternType type = PaperMethods.getPatternType(sections[0].toLowerCase());
        final DyeColor color = PaperMethods.getDyeColor(sections[1]);

        if (type == null) return (T) this;

        return addPattern(type, color);
    }

    /**
     * Adds a {@link List<String>} to the {@link ItemStack}.
     *
     * @param patterns {@link List<String>}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addPatterns(@NotNull final List<String> patterns) {
        if (!isBanner() && !isShield()) return (T) this;

        patterns.forEach(this::addPattern);

        return (T) this;
    }

    /**
     * Adds a {@link PotionEffect} to the {@link ItemStack} if the {@link ItemType} allows it.
     *
     * @param type the type of {@link PotionEffectType}
     * @param duration the duration of the {@link PotionType}
     * @param amplifier the potion level
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addPotionEffect(@NotNull final PotionEffectType type, final int duration, final int amplifier) {
        if (!isArrow() && !isPotion()) return (T) this;

        this.potionEffects.add(new PotionEffect(type, duration, amplifier));

        return (T) this;
    }

    /**
     * Changes the {@link ItemStack} {@link PotionType}.
     *
     * @param potionType the {@link PotionType}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setPotionType(@NotNull final PotionType potionType) {
        if (!isPotion()) return (T) this;

        this.potionType = potionType;

        return (T) this;
    }

    /**
     * Changes the color of the {@link ItemStack} if it is a {@link ItemType} that supports it.
     *
     * @param color the {@link Color}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setColor(@NotNull final Color color) {
        this.color = color;

        return (T) this;
    }

    /**
     * Specifies that the current instance of {@link ItemStack} was created using a custom source.
     *
     * @param isCustom true or false
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setCustom(final boolean isCustom) {
        this.isCustom = isCustom;

        return (T) this;
    }

    /**
     * Adds an {@link ItemFlag} to the {@link ItemStack}.
     *
     * @param itemFlag the {@link ItemFlag} to add
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addItemFlag(@NotNull final ItemFlag itemFlag) {
        this.itemFlags.add(itemFlag);

        return (T) this;
    }

    /**
     * Sets a {@link List<ItemFlag>} to the {@link ItemStack}.
     *
     * @param itemFlags the {@link List<String>} to set
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setItemFlags(@NotNull final List<String> itemFlags) { //todo() itemflags are dead
        if (itemFlags.isEmpty()) return (T) this;

        itemFlags.forEach(flag -> addItemFlag(ItemFlag.valueOf(flag)));

        return (T) this;
    }

    /**
     * Removes an {@link ItemFlag} from the {@link ItemStack}.
     *
     * @param itemFlag the {@link ItemFlag} to remove
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T removeItemFlag(@NotNull final ItemFlag itemFlag) { //todo() itemflags are dead
        this.itemFlags.remove(itemFlag);

        return (T) this;
    }

    /**
     * Sets an {@link EntityType}.
     *
     * @param entityType the {@link EntityType}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setEntityType(@NotNull final EntityType entityType) {
        this.entityType = entityType;

        return (T) this;
    }

    /**
     * Sets the amount of a damage an item can take.
     *
     * @param damage the damage
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setDamage(final int damage) {
        this.damage = damage;

        return (T) this;
    }

    /**
     * Adds a {@link Double} to the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link Double} to set
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setPersistentDouble(@NotNull final NamespacedKey key, final double value) {
        this.nbt.setItemStack(this.itemStack).setPersistentDouble(key, value);

        return (T) this;
    }

    /**
     * Adds a {@link Integer} to the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link Integer} to set
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setPersistentInteger(@NotNull final NamespacedKey key, final int value) {
        this.nbt.setItemStack(this.itemStack).setPersistentInteger(key, value);

        return (T) this;
    }

    /**
     * Adds a {@link Boolean} to the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link Boolean} to set
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setPersistentBoolean(@NotNull final NamespacedKey key, final boolean value) {
        this.nbt.setItemStack(this.itemStack).setPersistentBoolean(key, value);

        return (T) this;
    }

    /**
     * Adds a {@link String} to the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param value the {@link String} to set
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setPersistentString(@NotNull final NamespacedKey key, @NotNull final String value) {
        this.nbt.setItemStack(this.itemStack).setPersistentString(key, value);

        return (T) this;
    }

    /**
     * Adds a {@link List<String>} to the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @param values the {@link List<String>} to set
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setPersistentList(@NotNull final NamespacedKey key, @NotNull final List<String> values) {
        this.nbt.setItemStack(this.itemStack).setPersistentList(key, values);

        return (T) this;
    }

    /**
     * Gets a {@link Boolean} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public final boolean getBoolean(@NotNull final NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).getBoolean(key);
    }

    /**
     * Gets a {@link Double} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public final double getDouble(@NotNull final NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).getDouble(key);
    }

    /**
     * Gets a {@link Integer} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public final int getInteger(@NotNull final NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).getInteger(key);
    }

    /**
     * Gets a {@link List<String>} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull final List<String> getList(@NotNull final NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).getList(key);
    }

    /**
     * Gets a {@link String} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull final String getString(@NotNull final NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).getString(key);
    }

    /**
     * Removes {@link NamespacedKey} from the {@link ItemStack} {@link org.bukkit.persistence.PersistentDataContainer}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T removePersistentKey(@Nullable final NamespacedKey key) {
        this.nbt.setItemStack(this.itemStack).removePersistentKey(key);

        return (T) this;
    }

    /**
     * Checks if the {@link ItemStack} has a specific {@link NamespacedKey}.
     *
     * @param key the {@link NamespacedKey}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public final boolean hasKey(@NotNull final NamespacedKey key) {
        return this.nbt.setItemStack(this.itemStack).hasKey(key);
    }

    /**
     * Gets the {@link UUID} from {@link Player}.
     *
     * @return the {@link Player} {@link UUID}
     * @since 0.1.0
     */
    public @Nullable final UUID getPlayer() {
        return this.player;
    }

    /**
     * Gets a {@link Player} for {@link me.clip.placeholderapi.PlaceholderAPI} support.
     *
     * @param player the player name
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setPlayer(@NotNull final Player player) {
        if (player.isEmpty()) return (T) this;

        this.player = player.getUniqueId();

        return (T) this;
    }

    /**
     * Gets a {@link Player} by name with offline support for custom heads.
     *
     * @param player the player name
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setPlayer(@NotNull final String player) {
        if (player.isEmpty()) return (T) this;

        // This is temporary until HDB is updated, The dev of the plugin has a house now
        // and his plugin doesn't work on 1.20.6
        if (player.length() > 16) {
            this.url = "https://textures.minecraft.net/texture/" + player;

            return (T) this;
        }

        @NotNull final PlayerBuilder builder = new PlayerBuilder(player);
        // More extensive but we only call methods once, and we avoid NPE.
        @Nullable final Player target = builder.getPlayer();

        if (target != null) {
            this.uuid = target.getUniqueId();
        } else {
            @Nullable final OfflinePlayer offlineTarget = builder.getOfflinePlayer();

            if (offlineTarget != null) this.uuid = offlineTarget.getUniqueId();
        }

        return (T) this;
    }

    /**
     * Sets the {@link UUID} of the Player Head {@link ItemStack}.
     *
     * @param uuid the {@link UUID} of the {@link Player}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setSkull(@NotNull final UUID uuid) {
        if (!isPlayerHead()) return (T) this;

        this.uuid = uuid;

        return (T) this;
    }

    /**
     * Sets a custom skull.
     *
     * @param skull the id of the skull
     * @param hdb the {@link HeadDatabaseAPI}
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T setSkull(@NotNull final String skull, @Nullable final HeadDatabaseAPI hdb) {
        if (skull.isEmpty() || hdb == null) return (T) this;

        this.itemStack = hdb.isHead(skull) ? hdb.getItemHead(skull) : this.itemStack.withType(Material.PLAYER_HEAD);

        return (T) this;
    }

    /**
     * Adds an {@link Enchantment} to the {@link ItemMeta}.
     *
     * @param enchant the enchant name
     * @param level the enchant level
     * @param ignoreLevelCap true or false, allows unsafe enchantments
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addEnchantment(@NotNull final String enchant, final int level, final boolean ignoreLevelCap) {
        if (this.isCustom || enchant.isEmpty() || level < 0) return (T) this;

        final Enchantment enchantment = PaperMethods.getEnchantment(enchant);

        if (enchantment == null) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (isEnchantedBook() && itemMeta instanceof EnchantmentStorageMeta storageMeta) {
                storageMeta.addStoredEnchant(enchantment, level, ignoreLevelCap);

                return;
            }

            itemMeta.addEnchant(enchantment, level, ignoreLevelCap);
        });

        return (T) this;
    }

    /**
     * Removes an {@link Enchantment} from {@link ItemMeta}.
     *
     * @param enchant the enchant name
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T removeEnchantment(@NotNull final String enchant) {
        if (this.isCustom) return (T) this;
        if (enchant.isEmpty()) return (T) this;

        @Nullable final Enchantment enchantment = PaperMethods.getEnchantment(enchant);
        if (enchantment == null) return (T) this;

        if (hasItemMeta()) {
            this.itemStack.editMeta(itemMeta -> {
                if (isEnchantedBook() && itemMeta instanceof EnchantmentStorageMeta storageMeta && storageMeta.hasStoredEnchant(enchantment)) {
                    storageMeta.removeStoredEnchant(enchantment);

                    return;
                }

                if (itemMeta.hasEnchant(enchantment)) {
                    itemMeta.removeEnchant(enchantment);
                }
            });
        }

        return (T) this;
    }

    /**
     * Adds multiple {@link Enchantment}'s to the {@link ItemMeta}.
     *
     * @param enchantments the map of {@link Enchantment}'s to add
     * @param ignoreLevelCap true or false, allows unsafe enchantments
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T addEnchantments(@NotNull final Map<String, Integer> enchantments, final boolean ignoreLevelCap) {
        if (this.isCustom) return (T) this;
        if (enchantments.isEmpty()) return (T) this;

        enchantments.forEach((enchantment, level) -> addEnchantment(enchantment, level, ignoreLevelCap));

        return (T) this;
    }

    /**
     * Removes multiple {@link Enchantment}'s from the {@link ItemMeta}.
     *
     * @param enchantments the map of {@link Enchantment}'s to remove
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T removeEnchantments(@NotNull final Set<String> enchantments) {
        if (this.isCustom) return (T) this;
        if (enchantments.isEmpty()) return (T) this;

        enchantments.forEach(this::removeEnchantment);

        return (T) this;
    }

    /**
     * Shows or hides the {@link List<ItemFlag>} of the {@link ItemStack} applying changes.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyHiddenItemFlags() { //todo() itemflags are dead
        if (this.isCustom) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (this.isHidingItemFlags) itemMeta.addItemFlags(ItemFlag.values()); else this.itemFlags.forEach(itemMeta::addItemFlags);
        });

        return (T) this;
    }

    /**
     * Changes the custom model data of the {@link ItemStack} applying changes.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyCustomModelData() {
        if (this.isCustom) return (T) this;
        if (this.customModelData.isEmpty()) return (T) this;

        this.itemStack.editMeta(itemMeta -> itemMeta.setCustomModelData(this.customModelData.get().intValue()));

        return (T) this;
    }

    /**
     * Changes the {@link ItemStack} to be unbreakable applying changes.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyUnbreakable() {
        if (this.isCustom) return (T) this;

        this.itemStack.editMeta(itemMeta -> itemMeta.setUnbreakable(this.isUnbreakable));

        return (T) this;
    }

    /**
     * Changes the {@link ItemStack} {@link EntityType} if it is a {@link CreatureSpawner} applying changes.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyEntityType() {
        if (this.isCustom) return (T) this;
        if (!isSpawner()) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final BlockStateMeta blockState) {
                final CreatureSpawner creatureSpawner = (CreatureSpawner) blockState.getBlockState();

                creatureSpawner.setSpawnedType(this.entityType);
                blockState.setBlockState(creatureSpawner);
            }
        });

        return (T) this;
    }

    /**
     * Adds a {@link ArmorTrim} to a piece of armor using {@link String}.
     *
     * @param pattern the pattern to add
     * @param material the material to use
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyTrim(@NotNull final String pattern, @NotNull final String material) {
        if (this.isCustom) return (T) this;
        if (pattern.isEmpty() || material.isEmpty()) return (T) this;

        final TrimMaterial trimMaterial = PaperMethods.getTrimMaterial(material);

        final TrimPattern trimPattern = PaperMethods.getTrimPattern(pattern);

        if (trimPattern == null || trimMaterial == null) return (T) this;

        return applyTrim(trimPattern, trimMaterial);
    }

    /**
     * Populates a {@link TrimPattern} variable.
     *
     * @param pattern the pattern to add
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyTrimPattern(@NotNull final String pattern) {
        if (this.isCustom) return (T) this;
        if (pattern.isEmpty()) return (T) this;

        final TrimPattern trimPattern = PaperMethods.getTrimPattern(pattern);

        if (trimPattern == null) return (T) this;

        this.trimPattern = trimPattern;

        return (T) this;
    }

    /**
     * Populates a {@link TrimMaterial} variable.
     *
     * @param material the material to add
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyTrimMaterial(@NotNull final String material) {
        if (this.isCustom) return (T) this;
        if (material.isEmpty()) return (T) this;

        final TrimMaterial trimMaterial = PaperMethods.getTrimMaterial(material);

        if (trimMaterial == null) return (T) this;

        this.trimMaterial = trimMaterial;

        return (T) this;
    }

    /**
     * Changes the {@link List<Pattern>} of the {@link ItemStack} if it is a {@link ItemType} that supports it.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyPattern() {
        if (this.isCustom) return (T) this;
        if (this.patterns.isEmpty()) return (T) this;

        if (isBanner()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final BannerMeta banner) banner.setPatterns(this.patterns);
            });
        } else if (isShield()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final BlockStateMeta shield) {
                    Banner banner = (Banner) shield.getBlockState();
                    banner.setPatterns(this.patterns);
                    banner.update();

                    shield.setBlockState(banner);
                }
            });
        }

        return (T) this;
    }

    /**
     * Changes the {@link ItemStack} durability to that specified or the maximum if it exceeds it.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyDamage() {
        if (this.isCustom) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final Damageable damageable) {
                if (this.damage <= 0) return;

                if (this.damage >= getType().getMaxDurability()) {
                    damageable.setDamage(getType().getMaxDurability());
                } else {
                    damageable.setDamage(this.damage);
                }
            }
        });

        return (T) this;
    }

    /**
     * Changes the {@link ItemStack} {@link PotionType} if the {@link ItemType} supports it, applying changes.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyEffects() {
        if (this.isCustom) return (T) this;

        if (isFirework() || isFireworkStar() && !this.fireworkEffects.isEmpty()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final FireworkMeta firework) {
                    this.fireworkEffects.forEach(firework::addEffect);

                    firework.setPower(this.fireworkPower);
                }
            });
        } else if (isPotion() || isArrow() && !this.potionEffects.isEmpty()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final PotionMeta potion) {
                    this.potionEffects.forEach(effect -> potion.addCustomEffect(effect, true));

                    if (this.potionType != null) potion.setBasePotionType(this.potionType);
                }
            });
        }

        return (T) this;
    }

    /**
     * Changes the {@link ItemStack} to be glowing if it is not already enchanted applying changes.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyGlowing() {
        if (this.isCustom) return (T) this;

        this.itemStack.editMeta(itemMeta -> itemMeta.setEnchantmentGlintOverride(!itemMeta.hasEnchants() && this.isGlowing));

        return (T) this;
    }

    /**
     * Applies a custom texture using player profiles.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyTexture() {
        if (this.isCustom) return (T) this;
        if (this.url.isEmpty()) return (T) this;
        if (!isPlayerHead()) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final SkullMeta skullMeta) {
                final PlayerProfile profile = Bukkit.getServer().createProfile(null, "");

                profile.setProperty(new ProfileProperty("", ""));

                PlayerTextures textures = profile.getTextures();

                try {
                    textures.setSkin(URI.create(this.url).toURL(), PlayerTextures.SkinModel.CLASSIC);
                } catch (MalformedURLException exception) {
                    if (this.api.isVerbose()) this.api.getLogger().error("Failed to set the texture url", exception);
                }

                profile.setTextures(textures);
                skullMeta.setPlayerProfile(profile);
            }
        });

        return (T) this;
    }

    /**
     * Changes the Owning {@link Player} of the {@link ItemStack} if it is a player head.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applySkull() {
        if (this.isCustom) return (T) this;
        if (this.uuid == null) return (T) this;
        if (!isPlayerHead()) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final SkullMeta skull) {
                if (!skull.hasOwner()) {
                    skull.setOwningPlayer(getOfflinePlayer(this.uuid));
                }
            }
        });

        return (T) this;
    }

    /**
     * Adds a {@link ArmorTrim} to a piece of armor.
     *
     * @param trimPattern the pattern to add
     * @param trimMaterial the material to use
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyTrim(@NotNull final TrimPattern trimPattern, @NotNull final TrimMaterial trimMaterial) {
        if (this.isCustom) return (T) this;
        if (!isArmor()) return (T) this;

        this.itemStack.editMeta(itemMeta -> {
            if (itemMeta instanceof final ArmorMeta armorMeta) armorMeta.setTrim(new ArmorTrim(trimMaterial, trimPattern));
        });

        return (T) this;
    }

    /**
     * Changes the {@link Color} of the {@link ItemStack} applying changes.
     *
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    public @NotNull T applyColor() {
        if (this.isCustom) return (T) this;
        if (this.color == null) return (T) this;

        if (isArrow() || isPotion()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final PotionMeta potion) potion.setColor(this.color);
            });
        }

        else if (isLeather()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final LeatherArmorMeta armor) armor.setColor(this.color);
            });
        }

        else if (isMap()) {
            this.itemStack.editMeta(itemMeta -> {
                if (itemMeta instanceof final MapMeta map) {
                    map.setScaling(true);
                    map.setColor(this.color);
                }
            });
        }

        return (T) this;
    }

    /**
     * Strips a {@link Component} of all colors and returns it blank.
     *
     * @return a {@link Component} stripped and serialized to a {@link String}
     * @since 0.1.0
     */
    public @NotNull final String getStrippedName() {
        return PlainTextComponentSerializer.plainText().serialize(this.itemStack.displayName());
    }

    /**
     * Strips a list of {@link Component} of all colors and returns it blank.
     *
     * @return a list of {@link Component} stripped and serialized to a list of {@link String}
     * @since 0.1.0
     */
    public @NotNull final List<String> getStrippedLore() {
        final List<String> lore = new ArrayList<>();

        this.displayComponentLore.forEach(line -> lore.add(PlainTextComponentSerializer.plainText().serialize(line)));

        return lore;
    }

    /**
     * A list of {@link ItemFlag}.
     * 
     * @return a list of {@link ItemFlag}
     * @since 0.1.0
     */
    public @NotNull final List<ItemFlag> getItemFlags() { //todo() itemflags are dead
        return this.itemFlags;
    }

    /**
     * Gets the display lore of the {@link ItemStack}.
     *
     * @return the display lore without being turned into a {@link Component}
     * @since 0.1.0
     */
    public @NotNull final List<String> getDisplayLore() {
        return this.displayLore;
    }

    /**
     * Gets the display name of the {@link ItemStack}.
     *
     * @return the display name without being turned into a {@link Component}
     * @since 0.1.0
     */
    public @NotNull final String getDisplayName() {
        return this.displayName;
    }

    /**
     * Checks if the {@link ItemStack} has {@link ItemMeta}.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean hasItemMeta() {
        return !this.itemStack.hasItemMeta();
    }

    /**
     * Gets the {@link ItemType} type.
     *
     * @return the {@link ItemType} type
     * @since 0.1.0
     */
    public @NotNull final Material getType() {
        return this.itemStack.getType();
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a {@link Banner}.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isBanner() {
        return BANNERS.contains(getType());
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is an armor piece.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isArmor() {
        return ARMOR.contains(getType());
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a shulker box.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isShulker() {
        return SHULKERS.contains(getType());
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a leather variant.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isLeather() {
        return LEATHER_ARMOR.contains(getType());
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a potion.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isPotion() {
        return POTIONS.contains(getType());
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is an enchanted book.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isEnchantedBook() {
        return getType().equals(Material.ENCHANTED_BOOK);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a player head.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isPlayerHead() {
        return getType().equals(Material.PLAYER_HEAD);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a firework star.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isFireworkStar() {
        return getType().equals(Material.FIREWORK_STAR);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a {@link org.bukkit.entity.Firework}.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isFirework() {
        return getType().equals(Material.FIREWORK_ROCKET);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a {@link CreatureSpawner}.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isSpawner() {
        return getType().equals(Material.SPAWNER);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a shield.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isShield() {
        return getType().equals(Material.SHIELD);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is an {@link org.bukkit.entity.Arrow}.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isArrow() {
        return getType().equals(Material.ARROW);
    }

    /**
     * Reactively checks if the {@link ItemStack} {@link ItemType} is a map.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isMap() {
        return getType().equals(Material.MAP);
    }

    /**
     * Checks if an item is hiding item flags.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isHidingItemFlags() { //todo() itemflags are dead
        return this.isHidingItemFlags;
    }

    /**
     * Checks if an item is hiding tool tips.
     *
     * @return true or false
     * @since 0.1.0
     */
    public final boolean isHidingToolTips() {
        return this.isHidingToolTips;
    }

    /**
     * Adds a placeholder to the hashmap.
     *
     * @param placeholder the placeholder to add
     * @param value the value to replace the placeholder with
     * @param isLore true or false
     * @return {@link ItemBuilder}
     * @since 0.1.0
     */
    private @NotNull T addPlaceholder(@NotNull final String placeholder, @NotNull final String value, boolean isLore) {
        if (isLore) {
            this.displayLorePlaceholders.put(placeholder, value);

            return (T) this;
        }

        this.displayNamePlaceholders.put(placeholder, value);

        return (T) this;
    }

    /**
     * Get an {@link OfflinePlayer}.
     *
     * @param uuid the {@link UUID} of the {@link OfflinePlayer}
     * @return the {@link OfflinePlayer}
     * @since 0.1.0
     */
    private @NotNull OfflinePlayer getOfflinePlayer(@NotNull final UUID uuid) {
        return Bukkit.getServer().getOfflinePlayer(uuid);
    }

    /**
     * Get an {@link Player} by {@link UUID}.
     *
     * @param uuid the uuid of the {@link Player}
     * @return the {@link Player}
     * @since 0.1.0
     */
    private @Nullable Player getPlayer(@NotNull final UUID uuid) {
        return Bukkit.getServer().getPlayer(uuid);
    }
}