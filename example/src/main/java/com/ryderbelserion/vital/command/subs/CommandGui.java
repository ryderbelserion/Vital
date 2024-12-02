package com.ryderbelserion.vital.command.subs;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.TestPlugin;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.Gui;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiFiller;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.vital.paper.api.builders.gui.types.PaginatedGui;
import com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder;
import com.ryderbelserion.vital.paper.api.commands.PaperCommand;
import com.ryderbelserion.vital.paper.api.commands.context.PaperCommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class CommandGui extends PaperCommand {

    private final TestPlugin plugin = JavaPlugin.getPlugin(TestPlugin.class);

    private PaginatedGui gui;

    public CommandGui() {}

    @Override
    public void execute(PaperCommandInfo info) {
        this.gui = Gui.paginated().setTitle("Beans").setRows(6).disableInteractions().create();

        if (!info.isPlayer()) return;

        final GuiFiller guiFiller = this.gui.getFiller();

        final ItemType.Typed<ItemMeta> glass = ItemType.BLACK_STAINED_GLASS_PANE;

        guiFiller.fillTop(new GuiItem(glass));
        guiFiller.fillBottom(new GuiItem(glass));

        for (int i = 0; i < 38; i++) {
            this.gui.addItem(new GuiItem(ItemType.APPLE));
        }

        final ItemType.Typed<ItemMeta> arrow = ItemType.ARROW;

        this.gui.open(info.getPlayer(), gui -> {
            final int page = gui.getCurrentPageNumber();

            if (page > 1) {
                setPreviousButton(arrow, gui);
            }

            if (page < gui.getMaxPages()) {
                setNextButton(arrow, gui);
            }
        });
    }

    private void setNextButton(final ItemType itemType, final PaginatedGui gui) {
        gui.setItem(gui.getRows(), 6, new ItemBuilder<>().withType(itemType).setDisplayName("<red>Next Page #{page}").addNamePlaceholder("{page}", String.valueOf(gui.getNextPageNumber())).asGuiItem(event -> {
            event.setCancelled(true);

            gui.next();

            final int page = gui.getCurrentPageNumber();

            if (page < gui.getMaxPages()) {
                setNextButton(itemType, gui);
            } else {
                gui.setItem(gui.getRows(), 6, new GuiItem(ItemType.BLACK_STAINED_GLASS_PANE));
            }

            if (page > 1) {
                setPreviousButton(itemType, gui);
            } else {
                gui.setItem(gui.getRows(), 4, new GuiItem(ItemType.BLACK_STAINED_GLASS_PANE));
            }
        }));
    }

    private void setPreviousButton(final ItemType itemType, final PaginatedGui gui) {
        gui.setItem(gui.getRows(), 4, new ItemBuilder<>().withType(itemType).setDisplayName("<red>Previous Page #{page}").addNamePlaceholder("{page}", String.valueOf(gui.getPreviousPageNumber())).asGuiItem(event -> {
            event.setCancelled(true);

            gui.previous();

            final int page = gui.getCurrentPageNumber();

            if (page > 1) {
                setPreviousButton(itemType, gui);
            } else {
                gui.setItem(gui.getRows(), 4, new GuiItem(ItemType.BLACK_STAINED_GLASS_PANE));
            }

            if (page < gui.getMaxPages()) {
                setNextButton(itemType, gui);
            }
        }));
    }

    @Override
    public @NotNull final String getPermission() {
        return "vital.gui";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        return Commands.literal("gui")
                .requires(source -> source.getSender().hasPermission(getPermission()))
                .executes(context -> {
                    execute(new PaperCommandInfo(context));

                    return com.mojang.brigadier.Command.SINGLE_SUCCESS;
                }).build();
    }

    @Override
    public @NotNull final PaperCommand registerPermission() {
        final PluginManager server = this.plugin.getServer().getPluginManager();

        final Permission permission = server.getPermission(getPermission());

        if (permission == null) {
            server.addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}