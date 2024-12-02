package com.ryderbelserion.vital.command.subs.items;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.TestPlugin;
import com.ryderbelserion.vital.paper.api.builders.items.v2.ItemBuilder;
import com.ryderbelserion.vital.paper.api.commands.PaperCommand;
import com.ryderbelserion.vital.paper.api.commands.context.PaperCommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandItem extends PaperCommand {

    private final TestPlugin plugin = JavaPlugin.getPlugin(TestPlugin.class);

    @Override
    public void execute(PaperCommandInfo info) {
        if (!info.isPlayer()) return;

        final CommandContext<CommandSourceStack> context = info.getContext();

        final ItemType itemType = context.getArgument("material", ItemType.class);

        if (itemType == null) return;

        final int amount = context.getArgument("amount", Integer.class);

        final ItemBuilder itemBuilder = ItemBuilder.from(itemType).withDisplayName("<red>").withAmount(amount);

        final Player player = info.getPlayer();

        itemBuilder.addItemToInventory(player.getInventory());
    }

    @Override
    public @NotNull final String getPermission() {
        return "vital.item";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("item").requires(source -> source.getSender().hasPermission(getPermission()));

        final RequiredArgumentBuilder<CommandSourceStack, ItemType> arg1 = argument("material", ArgumentTypes.resource(RegistryKey.ITEM));

        final RequiredArgumentBuilder<CommandSourceStack, Integer> arg2 = argument("amount", IntegerArgumentType.integer(1, 10)).suggests((context, builder) -> suggestIntegerArgument(builder, 1, 10, "<green>The amount of items to give!")).executes(context -> {
            execute(new PaperCommandInfo(context));

            return Command.SINGLE_SUCCESS;
        });

        return root.then(arg1.then(arg2)).build();
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