package com.ryderbelserion.vital.paper.commands;

import com.ryderbelserion.vital.core.commands.args.Argument;
import com.ryderbelserion.vital.paper.VitalPaper;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Comparator;

public class PaperCommandHelpEntry {

    private final @NotNull VitalPaper paper = (VitalPaper) VitalPaper.api();

    private final @NotNull PaperCommandManager manager = this.paper.getCommandManager();

    //private final @NotNull CommandHelpProvider locale = this.plugin.getHelpProvider();

    private int page = 1;
    private int perPage;
    private int totalPages;
    private int totalResults;
    private boolean lastPage;

    public PaperCommandHelpEntry() {
        this.perPage = 10;
    }

    public void showHelp(PaperCommandContext context) {
        int min = this.perPage * (this.page - 1);
        int max = min + this.perPage;

        this.totalResults = this.manager.getClasses().size();

        this.totalPages = this.totalResults / this.perPage;

        if (min >= this.totalResults) {
            //context.reply(this.locale.invalidPage().replaceAll("\\{page}", String.valueOf(page)));

            return;
        }

        //context.reply(this.locale.pageHeader().replaceAll("\\{page}", String.valueOf(page)));

        for (int value = min; value < max; value++) {
            if (this.totalResults - 1 < value) continue;

            PaperCommandEngine command = this.manager.getClasses().get(value);

            boolean isVisible = command.isVisible();

            if (!isVisible) continue;

            if (!command.requirements.checkRequirements(context, false)) continue;

            StringBuilder baseFormat = new StringBuilder("/" + command.getUsage());

            //String format = this.locale.pageFormat()
            //        .replaceAll("\\{command}", baseFormat.toString())
            //        .replaceAll("\\{description}", command.getDescription());

            // Append aliases.
            if (!command.getAliases().isEmpty()) baseFormat.append(" ").append(command.getAliases().get(0));

            // Sort arguments.
            ArrayList<Argument> arguments = new ArrayList<>();

            arguments.addAll(command.getOptionalArgs());
            arguments.addAll(command.getRequiredArgs());

            arguments.sort(Comparator.comparingInt(Argument::order));

            //String footer = this.locale.pageFooter();

            if (context.isPlayer()) {
                /*StringBuilder types = new StringBuilder();

                ComponentBuilder builder = new ComponentBuilder();

                for (Argument arg : arguments) {
                    String argValue = command.getOptionalArgs().contains(arg) ? " (" + arg.name() + ") " : " <" + arg.name() + ">";

                    types.append(argValue);
                }

                builder.setMessage(format.replaceAll("\\{args}", String.valueOf(types)));

                String hoverShit = baseFormat.append(types).toString();

                String hoverFormat = this.locale.hoverMessage();

                builder.hover(hoverFormat.replaceAll("\\{command}", hoverShit)).click(ClickEvent.Action.valueOf(this.locale.hoverAction().toUpperCase()), hoverShit);

                context.reply(builder.build());

                String text = this.locale.pageNavigation();

                ComponentBuilder builder = new ComponentBuilder();

                if (page > 1) {
                    int number = page-1;

                    String fullUsage = "/" + command.getUsage() + " " + number;
                    String newPage = String.valueOf(page);
                    String newNumber = String.valueOf(number);

                    builder.setMessage(footer.replaceAll("\\{page}", newPage));

                    builder.getFancyComponentBuilder().hover(this.locale.pageBackButton(), text.replaceAll("\\{page}", newNumber));

                    builder.getFancyComponentBuilder().click(ClickEvent.Action.RUN_COMMAND, fullUsage);

                    context.reply(builder.build());
                } else if (page < this.manager.getClasses().size()) {
                    int number = page+1;

                    String fullUsage = "/" + command.getUsage() + " " + number;
                    String newPage = String.valueOf(page);
                    String newNumber = String.valueOf(number);

                    builder.setMessage(footer.replaceAll("\\{page}", newPage));

                    builder.getFancyComponentBuilder().hover(this.locale.pageNextButton(), text.replaceAll("\\{page}", newNumber));

                    builder.getFancyComponentBuilder().click(ClickEvent.Action.RUN_COMMAND, fullUsage);

                    context.reply(builder.build());
                }*/
            } else {
                //context.reply(footer.replaceAll("\\{page}", String.valueOf(page)));
            }
        }

        this.lastPage = max >= this.totalResults;
    }

    public void setPage(final int page) {
        this.page = page;
    }

    public void setPerPage(final int perPage) {
        this.perPage = perPage;
    }

    public void setPage(final int page, final int perPage) {
        this.setPerPage(perPage);
        this.setPage(page);
    }

    public final int getPage() {
        return this.page;
    }

    public final int getPerPage() {
        return this.perPage;
    }

    public final int getTotalResults() {
        return this.totalResults;
    }

    public final int getTotalPages() {
        return this.totalPages;
    }

    public final boolean isLastPage() {
        return this.lastPage;
    }
}