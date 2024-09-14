package com.ryderbelserion.vital.command.subs;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.TestPlugin;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.Gui;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiFiller;
import com.ryderbelserion.vital.paper.api.builders.gui.interfaces.GuiItem;
import com.ryderbelserion.vital.paper.api.builders.gui.types.PaginatedGui;
import com.ryderbelserion.vital.paper.api.builders.items.ItemBuilder;
import com.ryderbelserion.vital.paper.commands.PaperCommand;
import com.ryderbelserion.vital.paper.commands.context.PaperCommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Material;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class CommandGui extends PaperCommand {

    private final TestPlugin plugin = JavaPlugin.getPlugin(TestPlugin.class);

    private final PaginatedGui gui;

    public CommandGui() {
        this.gui = Gui.paginated().setTitle("Beans").setRows(6).disableInteractions().create();
    }

    @Override
    public void execute(PaperCommandInfo info) {
        if (!info.isPlayer()) return;

        final GuiFiller guiFiller = this.gui.getFiller();

        final Material glass = Material.BLACK_STAINED_GLASS_PANE;

        List.of(
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE,
                Material.APPLE
        ).forEach(itemStack -> this.gui.addItem(new GuiItem(itemStack)));

        guiFiller.fillTop(new GuiItem(glass));
        guiFiller.fillBottom(new GuiItem(glass));

        final Material arrow = Material.ARROW;

        final int page = this.gui.getCurrentPageNumber();

        if (page > 1) {
            setPreviousButton(this.gui, arrow);
        }

        if (page < this.gui.getMaxPages()) {
            setNextButton(this.gui, arrow);
        }

        this.gui.open(info.getPlayer());
    }

    private void setNextButton(final PaginatedGui gui, final Material material) {
        gui.setItem(6, 6, new ItemBuilder<>().withType(material).setDisplayName("<red>Next Page #{page}").addNamePlaceholder("{page}", String.valueOf(gui.getNextPageNumber())).asGuiItem(event -> {
            event.setCancelled(true);

            this.gui.next();

            final int page = this.gui.getCurrentPageNumber();

            if (page < this.gui.getMaxPages()) {
                setNextButton(this.gui, material);
            } else {
                this.gui.setItem(6, 6, new GuiItem(Material.BLACK_STAINED_GLASS_PANE));
            }

            if (page > 1) {
                setPreviousButton(this.gui, material);
            } else {
                this.gui.setItem(6, 4, new GuiItem(Material.BLACK_STAINED_GLASS_PANE));
            }
        }));
    }

    private void setPreviousButton(final PaginatedGui gui, final Material material) {
        gui.setItem(6, 4, new ItemBuilder<>().withType(material).setDisplayName("<red>Previous Page #{page}").addNamePlaceholder("{page}", String.valueOf(gui.getPreviousPageNumber())).asGuiItem(event -> {
            event.setCancelled(true);

            this.gui.previous();

            final int page = this.gui.getCurrentPageNumber();

            if (page > 1) {
                setPreviousButton(this.gui, material);
            } else {
                this.gui.setItem(6, 4, new GuiItem(Material.BLACK_STAINED_GLASS_PANE));
            }

            if (page < this.gui.getMaxPages()) {
                setNextButton(this.gui, material);
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
        final Permission permission = this.plugin.getServer().getPluginManager().getPermission(getPermission());

        if (permission == null) {
            this.plugin.getServer().getPluginManager().addPermission(new Permission(getPermission(), PermissionDefault.OP));
        }

        return this;
    }
}