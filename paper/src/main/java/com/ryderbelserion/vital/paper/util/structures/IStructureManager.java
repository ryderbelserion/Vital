package com.ryderbelserion.vital.paper.util.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;
import org.jetbrains.annotations.Nullable;
import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * A structure manager extending {@link IStructureManager} which builds {@link Structure}.
 *
 * @author ryderbelserion
 * @version 0.0.8
 * @since 0.0.1
 */
public interface IStructureManager {

    /**
     * Loads the {@link File} as a structure.
     *
     * @param file {@link File}
     * @since 0.0.1
     */
    void applyStructure(@Nullable final File file);

    /**
     * Gets the {@link StructureManager} from the {@link org.bukkit.plugin.java.JavaPlugin}.
     *
     * @return {@link StructureManager}
     * @since 0.0.1
     */
    StructureManager getStructureManager();

    /**
     * Save a {@link org.bukkit.structure.Structure} from multiple {@link Location}.
     *
     * @param file the name of the {@link File}
     * @param one the first {@link Location}
     * @param two the second {@link Location}
     * @param includeEntities include entities or not
     * @since 0.0.1
     */
    void saveStructure(@Nullable final File file, @Nullable final Location one, @Nullable final Location two, final boolean includeEntities);

    /**
     * Paste the {@link org.bukkit.structure.Structure}.
     *
     * @param location the {@link Location} to paste
     * @param storeBlocks if we should store old blocks to restore
     * @since 0.0.1
     */
    void pasteStructure(@Nullable final Location location, final boolean storeBlocks);

    /**
     * Remove the {@link org.bukkit.structure.Structure}.
     *
     * @since 0.0.1
     */
    void removeStructure();

    /**
     * Get blocks of a {@link org.bukkit.structure.Structure}.
     *
     * @param location the {@link Location} to check
     * @return {@link Set<Location>}
     * @since 0.0.1
     */
    Set<Location> getBlocks(@Nullable final Location location);

    /**
     * Gets the structure's x coordinate.
     *
     * @return {@link Double}
     * @since 0.0.1
     */
    double getStructureX();

    /**
     * Gets the structure's y coordinate.
     *
     * @return {@link Double}
     * @since 0.0.1
     */
    double getStructureY();

    /**
     * Gets the structure's z coordinate.
     *
     * @return {@link Double}
     * @since 0.0.1
     */
    double getStructureZ();

    /**
     * Gets a set of nearby blocks in order to store and restore them after the {@link org.bukkit.structure.Structure} is removed.
     *
     * @return a set of nearby blocks
     * @since 0.0.1
     */
    Set<Location> getNearbyBlocks();

    /**
     * A list of blocks that cannot be overridden when pasting a {@link org.bukkit.structure.Structure}.
     *
     * @return {@link List<Material>}
     * @since 0.0.1
     */
    List<Material> getBlockBlacklist();

    /**
     * Creates the {@link org.bukkit.structure.Structure}.
     */
    void createStructure();

    /**
     * Loads the {@link org.bukkit.structure.Structure} {@link File} if not null from the server files.
     *
     * @return the {@link org.bukkit.structure.Structure} {@link File}
     * @since 0.0.1
     */
    File getStructureFile();

}