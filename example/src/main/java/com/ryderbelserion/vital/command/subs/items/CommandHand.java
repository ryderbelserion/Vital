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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandHand extends PaperCommand {

    private final TestPlugin plugin = JavaPlugin.getPlugin(TestPlugin.class);

    @Override
    public void execute(PaperCommandInfo info) {
        if (!info.isPlayer()) return;

        final Player player = info.getPlayer();

        final ItemStack itemStack = player.getInventory().getItemInMainHand();

        if (itemStack.getType().equals(Material.AIR)) return;

        final CommandContext<CommandSourceStack> context = info.getContext();

        final int amount = context.getArgument("amount", Integer.class);

        final ItemBuilder itemBuilder = ItemBuilder.from(itemStack.clone()).withDisplayName("<red>Super Sword").withAmount(amount);

        itemBuilder.addEnchantment("sharpness", 10);
        itemBuilder.addEnchantment("fire_aspect", 5);

        final Inventory inventory = player.getInventory();

        itemBuilder.setItemToInventory(inventory, inventory.firstEmpty());
    }

    @Override
    public @NotNull final String getPermission() {
        return "vital.hand";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("hand").requires(source -> source.getSender().hasPermission(getPermission()));

        final RequiredArgumentBuilder<CommandSourceStack, Integer> arg1 = argument("amount", IntegerArgumentType.integer(1, 10)).suggests((context, builder) -> suggestIntegerArgument(builder, 1, 10, "<green>The amount of items to give!")).executes(context -> {
            execute(new PaperCommandInfo(context));

            return Command.SINGLE_SUCCESS;
        });

        return root.then(arg1).build();
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