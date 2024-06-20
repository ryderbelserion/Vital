package com.ryderbelserion.vital.paper.commands;

import com.ryderbelserion.vital.core.commands.CommandEngine;
import com.ryderbelserion.vital.core.commands.args.Argument;
import com.ryderbelserion.vital.paper.VitalPaper;
import com.ryderbelserion.vital.paper.builders.ComponentBuilder;
import com.ryderbelserion.vital.paper.commands.reqs.PaperRequirements;
import com.ryderbelserion.vital.paper.commands.reqs.PaperRequirementsBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public abstract class PaperCommandEngine extends Command implements CommandEngine {

    private final @NotNull VitalPaper paper = (VitalPaper) VitalPaper.api();
    private final @NotNull JavaPlugin plugin = this.paper.getPlugin();

    //private final @NotNull CommandHelpProvider locale = this.plugin.getHelpProvider();

    public PaperRequirements requirements;

    private final LinkedList<PaperCommandEngine> subCommands = new LinkedList<>();

    private final LinkedList<Argument> requiredArgs = new LinkedList<>();
    private final LinkedList<Argument> optionalArgs = new LinkedList<>();

    public PaperCommandEngine(@NotNull String name, @NotNull String description, @NotNull String usageMessage, @NotNull List<String> aliases) {
        super(name, description, usageMessage, aliases);
    }

    public abstract void perform(PaperCommandContext context, String[] args);

    public void execute(PaperCommandContext context, String[] args) {
        StringBuilder label = new StringBuilder(context.getLabel());

        if (!context.getArgs().isEmpty()) {
            for (PaperCommandEngine command : this.subCommands) {
                boolean isPresent = context.getArgs().stream().findFirst().isPresent();

                String arg = context.getArgs().stream().findFirst().get().toLowerCase();

                if (isPresent && arg.equalsIgnoreCase(command.getLabel())) {
                    label.append(" ").append(context.getArgs().getFirst());

                    context.getArgs().removeFirst();
                    context.setLabel(label.toString());

                    command.execute(context, args);

                    return;
                }
            }
        }

        if (!this.requirements.checkRequirements(context, true)) return;

        if (!validate(context)) return;

        perform(context, args);
    }

    private boolean validate(PaperCommandContext context) {
        if (context.getArgs().size() < this.requiredArgs.size()) {
            //context.reply(this.locale.notEnoughArgs());

            format(context);

            return false;
        }

        if (context.getArgs().size() > this.requiredArgs.size() + this.optionalArgs.size() || context.getArgs().size() > this.requiredArgs.size()) {
            //context.reply(this.locale.tooManyArgs());

            format(context);

            return false;
        }

        return true;
    }

    private void format(PaperCommandContext context) {
        ArrayList<Argument> arguments = new ArrayList<>();

        arguments.addAll(this.requiredArgs);
        arguments.addAll(this.optionalArgs);

        this.requiredArgs.sort(Comparator.comparing(Argument::order));

        if (context.isPlayer()) {
            StringBuilder baseFormat = new StringBuilder("/" + getUsage());

            //String format = this.locale.invalidFormat().replaceAll("\\{command}", baseFormat.toString());

            StringBuilder types = new StringBuilder();

            ComponentBuilder builder = new ComponentBuilder(context.getPlayer());

            for (Argument arg : arguments) {
                boolean isPresent = arg.argumentType().getPossibleValues().stream().findFirst().isPresent();

                if (isPresent) types.append(arg.argumentType().getPossibleValues().stream().findFirst().get());
            }

            //builder.setMessage(format.replaceAll("\\{args}", String.valueOf(types)));

            String hoverShit = baseFormat.append(" ").append(types).toString();

            //String hoverFormat = this.locale.hoverMessage();

            //builder.hover(hoverFormat.replaceAll("\\{command}", hoverShit)).click(ClickEvent.Action.SUGGEST_COMMAND, hoverShit);

            //context.reply(builder.build());

            return;
        }

        StringBuilder baseFormat = new StringBuilder("/" + getUsage());

        StringBuilder types = new StringBuilder();

        for (Argument arg : arguments) {
            boolean isPresent = arg.argumentType().getPossibleValues().stream().findFirst().isPresent();

            if (isPresent) types.append(arg.argumentType().getPossibleValues().stream().findFirst().get());
        }

        context.reply(baseFormat.append(" ").append(types).toString());
    }

    @Override
    public boolean execute(final @NotNull CommandSender sender, final @NotNull String label, final @NotNull String[] args) {
        PaperCommandContext context = new PaperCommandContext(
                sender,
                label,
                List.of(args)
        );

        execute(context, args);

        return true;
    }

    private boolean value = true;

    @Override
    public void setVisible(final boolean value) {
        this.value = value;
    }

    @Override
    public final boolean isVisible() {
        return this.value;
    }

    public List<String> handleTabComplete(final CommandSender sender, final List<String> args) {
        if (args.size() == 1) {
            ArrayList<String> completions = new ArrayList<>();

            if (args.getFirst().isEmpty()) {
                this.subCommands.forEach(sub -> {
                    // If command is visible return.
                    if (!sub.isVisible()) return;

                    // Check if requirements are null and if not, check if they meet the requirements then add completions if they do.
                    if (sub.requirements != null) {
                        PaperRequirementsBuilder builder = sub.requirements.getRequirementsBuilder();

                        if (builder.isPlayer()) {
                            Player player = (Player) sender;

                            if (builder.getPermission() != null && player.hasPermission(builder.getPermission()) || builder.getRawPermission() != null && player.hasPermission(builder.getRawPermission())) {
                                completions.add(sub.getName());
                            }

                            return;
                        }

                        completions.add(sub.getName());
                    }
                });
            } else {
                for (PaperCommandEngine subCommand : this.subCommands) {
                    for (String alias : subCommand.getAliases()) {
                        if (alias.toLowerCase().startsWith(args.getFirst())) completions.add(alias);
                    }
                }
            }

            return completions;
        }

        if (args.size() >= 2) {
            int relativeIndex = 2;
            int subCommandIndex = 0;

            PaperCommandEngine commandTab = this;

            while (!commandTab.subCommands.isEmpty()) {
                PaperCommandEngine findCommand = null;

                for (PaperCommandEngine sub : this.subCommands) {
                    if (sub.getName().equals(args.get(subCommandIndex).toLowerCase())) {
                        findCommand = sub;
                        break;
                    }
                }

                subCommandIndex++;
                if (findCommand != null) commandTab = findCommand; else break;
                relativeIndex++;
            }

            int argToComplete = args.size() + 1 - relativeIndex;

            if (commandTab.requiredArgs.size() >= argToComplete) {
                ArrayList<Argument> arguments = new ArrayList<>();

                arguments.addAll(commandTab.requiredArgs);
                arguments.addAll(commandTab.optionalArgs);

                ArrayList<String> possibleValues = new ArrayList<>();

                for (Argument argument : arguments) {
                    if (argument.order() == argToComplete) {
                        if (!argument.argumentType().getPossibleValues().isEmpty()) {
                            List<String> possibleArgs = argument.argumentType().getPossibleValues();

                            possibleValues = new ArrayList<>(possibleArgs);

                            break;
                        }
                    }
                }

                return Collections.unmodifiableList(possibleValues);
            }
        }

        return Collections.emptyList();
    }

    public void addCommand(final PaperCommandEngine command, final boolean first) {
        if (hasCommand(command)) return;

        this.paper.getCommandManager().addCommand(command, first);

        this.subCommands.add(command);
    }

    public void removeCommand(final PaperCommandEngine command, final boolean first) {
        if (!hasCommand(command)) return;

        this.paper.getCommandManager().removeCommand(command, first);

        this.subCommands.remove(command);
    }

    public boolean hasCommand(final PaperCommandEngine command) {
        return this.subCommands.contains(command);
    }

    public List<PaperCommandEngine> getCommands(final PaperCommandEngine command) {
        return Collections.unmodifiableList(command.subCommands);
    }

    public List<Argument> getOptionalArgs(final PaperCommandEngine command) {
        return command.requiredArgs;
    }

    public List<Argument> getRequiredArgs(final PaperCommandEngine command) {
        return command.optionalArgs;
    }

    public void addRequiredArgument(final PaperCommandEngine command, final Argument argument) {
        command.requiredArgs.add(argument);
    }

    public void addOptionalArgument(final PaperCommandEngine command, final Argument argument) {
        command.optionalArgs.add(argument);
    }

    public final List<Argument> getOptionalArgs() {
        return this.requiredArgs;
    }

    public final List<Argument> getRequiredArgs() {
        return this.optionalArgs;
    }
}