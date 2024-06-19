package com.ryderbelserion.vital.paper.util.structures;

import com.google.common.collect.Lists;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.structure.Structure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;


/**
 * A structure manager extending {@link IStructureManager} which builds {@link Structure}.
 *
 * @author Ryder Belserion
 * @version 1.6
 * @since 1.0
 */
public class StructureManager implements IStructureManager {

    private final Set<Location> postStructurePasteBlocks = new HashSet<>();
    private final Set<Location> preStructurePasteBlocks = new HashSet<>();

    private final JavaPlugin plugin;

    /**
     * Builds a structure manager instance.
     *
     * @param plugin {@link JavaPlugin}
     * @since 1.0
     */
    public StructureManager(@NotNull final JavaPlugin plugin) {
        this.plugin = plugin;
    }

    private File file = null;

    private Structure structure = null;

    private boolean doNotApply = false;

    /**
     {@inheritDoc}
     */
    @Override
    public void applyStructure(@Nullable final File file) {
        if (file == null) {
            this.doNotApply = true;

            return;
        }

        this.file = file;

        this.structure = CompletableFuture.supplyAsync(() -> {
            try {
                return this.plugin.getServer().getStructureManager().loadStructure(this.file);
            } catch (IOException exception) {
                this.plugin.getLogger().log(Level.SEVERE, "Failed to load structure: " + this.file.getName() + "!", exception);

                return null;
            }
        }).join();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull final org.bukkit.structure.StructureManager getStructureManager() {
        return this.plugin.getServer().getStructureManager();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void saveStructure(@Nullable final File file, @Nullable final Location one, @Nullable final Location two, boolean includeEntities) {
        if (this.doNotApply) return;

        if (file == null || one == null || two == null) return;

        // Fill the structure with blocks between 2 corners.
        this.structure.fill(one, two, includeEntities);

        // Save structure to file.
        try {
            getStructureManager().saveStructure(file, this.structure);
        } catch (IOException exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to save structure to " + file.getName() + "!", exception);
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void pasteStructure(@Nullable final Location location, final boolean storeBlocks) {
        if (this.doNotApply) return;

        if (location == null) return;

        try {
            // Get the blocks from the hashset and set them.
            if (storeBlocks) getBlocks(location);

            // Place the structure.
            this.structure.place(location, false, StructureRotation.NONE, Mirror.NONE, 0, 1F, ThreadLocalRandom.current());

            // Get the structure blocks.
            if (storeBlocks) getStructureBlocks(location);
        } catch (Exception exception) {
            this.plugin.getLogger().log(Level.SEVERE, "Could not paste structure", exception);
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void removeStructure() {
        if (this.doNotApply) return;

        this.postStructurePasteBlocks.forEach(block -> {
            if (block.getBlock().getType() != Material.AIR) {
                Location location = block.toBlockLocation();

                location.getBlock().setType(Material.AIR, true);
            }
        });
    }

    /**
     {@inheritDoc}
     */
    private void getStructureBlocks(@NotNull final Location location) {
        for (int x = 0; x < getStructureX(); x++) {
            for (int y = 0; y < getStructureY(); y++) {
                for (int z = 0; z < getStructureZ(); z++) {
                    final Block relativeLocation = location.getBlock().getRelative(x, y, z);

                    this.postStructurePasteBlocks.add(relativeLocation.getLocation());

                    this.postStructurePasteBlocks.forEach(block -> {
                        final Location blockLoc = block.toBlockLocation();

                        blockLoc.getBlock().getState().update();
                    });
                }
            }
        }
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull final Set<Location> getBlocks(@Nullable final Location location) {
        if (this.doNotApply) return Collections.emptySet();

        if (location == null) return getNearbyBlocks();

        for (int x = 0; x < getStructureX(); x++) {
            for (int y = 0; y < getStructureY(); y++) {
                for (int z = 0; z < getStructureZ(); z++) {
                    Block relativeLocation = location.getBlock().getRelative(x, y, z).getLocation().subtract(2, 0.0, 2).getBlock();

                    this.preStructurePasteBlocks.add(relativeLocation.getLocation());
                }
            }
        }

        return getNearbyBlocks();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final double getStructureX() {
        if (this.doNotApply) return 0.0;

        return this.structure.getSize().getX();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final double getStructureY() {
        if (this.doNotApply) return 0.0;

        return this.structure.getSize().getY();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public final double getStructureZ() {
        if (this.doNotApply) return 0.0;

        return this.structure.getSize().getZ();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull final Set<Location> getNearbyBlocks() {
        return Collections.unmodifiableSet(this.preStructurePasteBlocks);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull final List<Material> getBlockBlacklist() {
        return Lists.newArrayList(
                Material.OAK_SIGN, Material.SPRUCE_SIGN, Material.BIRCH_SIGN, Material.JUNGLE_SIGN, Material.ACACIA_SIGN, Material.CHERRY_SIGN, Material.DARK_OAK_SIGN,
                Material.MANGROVE_SIGN, Material.BAMBOO_SIGN, Material.CRIMSON_SIGN, Material.WARPED_SIGN, Material.OAK_HANGING_SIGN, Material.SPRUCE_HANGING_SIGN,
                Material.BIRCH_HANGING_SIGN, Material.JUNGLE_HANGING_SIGN, Material.ACACIA_HANGING_SIGN, Material.CHERRY_HANGING_SIGN, Material.DARK_OAK_HANGING_SIGN,
                Material.MANGROVE_HANGING_SIGN, Material.BAMBOO_HANGING_SIGN, Material.CRIMSON_HANGING_SIGN, Material.WARPED_HANGING_SIGN,

                Material.STONE_BUTTON, Material.POLISHED_BLACKSTONE_BUTTON, Material.OAK_BUTTON, Material.SPRUCE_BUTTON, Material.BIRCH_BUTTON,
                Material.JUNGLE_BUTTON, Material.ACACIA_BUTTON, Material.CHERRY_BUTTON, Material.DARK_OAK_BUTTON, Material.MANGROVE_BUTTON, Material.BAMBOO_BUTTON,
                Material.CRIMSON_BUTTON, Material.WARPED_BUTTON);
    }

    /**
     {@inheritDoc}
     */
    @Override
    public void createStructure() {
        this.structure = getStructureManager().createStructure();
    }

    /**
     {@inheritDoc}
     */
    @Override
    public @NotNull final File getStructureFile() {
        return this.file;
    }
}