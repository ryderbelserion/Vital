package com.ryderbelserion.vital.paper.commands;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A command object class to provide useful utilities and enforcement on methods.
 *
 * @author Ryder Belserion
 * @version 2.4.3
 * @since 2.4
 */
public abstract class Command {

    /**
     * Empty constructor
     */
    public Command() {}

    /**
     * Executes the command while passing through {@link CommandData}
     *
     * @param data {@link CommandData}
     */
    public abstract void execute(CommandData data);

    /**
     * Get the permission required to execute the command.
     *
     * @return the permission node
     */
    public abstract @NotNull String getPermission();

    /**
     * Get the command literal which is effectively what builds the command.
     *
     * @return {@link LiteralCommandNode}
     */
    public abstract @NotNull LiteralCommandNode<CommandSourceStack> literal();

    /**
     * Registers the permission to the server.
     *
     * @return {@link Command}
     */
    public abstract @NotNull Command registerPermission();

    /**
     * Return a list of uuids for the suggestion provider.
     *
     * @param builder {@link SuggestionsBuilder}
     * @return a list of suggestions
     */
    public @NotNull final CompletableFuture<Suggestions> suggestNames(final SuggestionsBuilder builder) {
        for (int count = 1; count <= 7; count++) builder.suggest(UUID.randomUUID().toString().replace("-", "").substring(0, 8));

        return builder.buildFuture();
    }

    /**
     * Return a list of Integers for the suggestion provider.
     *
     * @param builder {@link SuggestionsBuilder}
     * @return a list of suggestions
     */
    public @NotNull final CompletableFuture<Suggestions> suggestIntegers(final SuggestionsBuilder builder) {
        for (int count = 1; count <= 100; count++) builder.suggest(count);

        return builder.buildFuture();
    }

    /**
     * Return a list of Doubles for the suggestion provider.
     *
     * @param builder {@link SuggestionsBuilder}
     * @return a list of suggestions
     */
    public @NotNull final CompletableFuture<Suggestions> suggestDoubles(final SuggestionsBuilder builder) {
        int count = 0;

        while (count <= 1000) {
            double x = count / 10.0;

            builder.suggest(String.valueOf(x));

            count++;
        }

        return builder.buildFuture();
    }
}