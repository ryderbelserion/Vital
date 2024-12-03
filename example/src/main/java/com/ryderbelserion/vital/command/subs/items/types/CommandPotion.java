package com.ryderbelserion.vital.command.subs.items.types;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.TestPlugin;
import com.ryderbelserion.vital.paper.api.builders.items.v2.ItemBuilder;
import com.ryderbelserion.vital.paper.api.builders.items.v2.PotionBuilder;
import com.ryderbelserion.vital.paper.api.commands.PaperCommand;
import com.ryderbelserion.vital.paper.api.commands.context.PaperCommandInfo;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemType;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static io.papermc.paper.command.brigadier.Commands.argument;

public class CommandPotion extends PaperCommand {

    private final TestPlugin plugin = JavaPlugin.getPlugin(TestPlugin.class);

    @Override
    public void execute(PaperCommandInfo info) {
        if (!info.isPlayer()) return;

        final CommandContext<CommandSourceStack> context = info.getContext();

        final ItemType itemType = context.getArgument("material", ItemType.class);

        final int amount = context.getArgument("amount", Integer.class);

        final PotionBuilder itemBuilder = ItemBuilder.from(itemType).withDisplayName("<red>Super Potion").withAmount(amount).asPotionBuilder();

        final PotionType potionType = context.getArgument("type", PotionType.class);

        final PotionEffectType potionEffectType = context.getArgument("effect", PotionEffectType.class);

        final int duration = context.getArgument("duration", Integer.class);

        final int amplifier = context.getArgument("amplifier", Integer.class);

        itemBuilder.withPotionEffect(potionEffectType, duration, amplifier).withPotionType(potionType).withColor(Color.GREEN).complete();

        final Player player = info.getPlayer();

        itemBuilder.addItemToInventory(player.getInventory());
    }

    @Override
    public @NotNull final String getPermission() {
        return "vital.item";
    }

    @Override
    public @NotNull final LiteralCommandNode<CommandSourceStack> literal() {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("potion").requires(source -> source.getSender().hasPermission(getPermission()));

        final RequiredArgumentBuilder<CommandSourceStack, ItemType> arg1 = argument("material", ArgumentTypes.resource(RegistryKey.ITEM)).suggests((context, builder) -> {
            List.of(
                    "minecraft:potion",
                    "minecraft:lingering_potion",
                    "minecraft:splash_potion"
            ).forEach(builder::suggest);

            return builder.buildFuture();
        });

        final RequiredArgumentBuilder<CommandSourceStack, Integer> arg2 = argument("amount", IntegerArgumentType.integer(1, 64)).suggests((context, builder) -> suggestIntegerArgument(builder, 1, 64, "<green>The amount of items to give!")).executes(context -> {
            execute(new PaperCommandInfo(context));

            return Command.SINGLE_SUCCESS;
        });

        final RequiredArgumentBuilder<CommandSourceStack, PotionType> arg3 = argument("type", ArgumentTypes.resource(RegistryKey.POTION)); // base potion type

        final RequiredArgumentBuilder<CommandSourceStack, PotionEffectType> arg4 = argument("effect", ArgumentTypes.resource(RegistryKey.MOB_EFFECT)); // extra effect

        final RequiredArgumentBuilder<CommandSourceStack, Integer> arg5 = argument("duration", IntegerArgumentType.integer(1, 6000)).suggests((context, builder) -> suggestIntegerArgument(builder, 1, 6000, "<red>The duration of the potion!"));

        final RequiredArgumentBuilder<CommandSourceStack, Integer> arg6 = argument("amplifier", IntegerArgumentType.integer(1, 12)).suggests((context, builder) -> suggestIntegerArgument(builder, 1, 12, "<red>The amplification of the potion!")).executes(context -> {
            execute(new PaperCommandInfo(context));

            return Command.SINGLE_SUCCESS;
        });

        return root.then(arg1.then(arg2.then(arg3.then(arg4.then(arg5.then(arg6)))))).build();
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