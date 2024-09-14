package com.ryderbelserion.vital.common.api.commands;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.ryderbelserion.vital.common.api.commands.context.CommandInfo;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A class to extend per platform, which allows you to handle the command execution.
 *
 * @author ryderbelserion
 * @param <S> the command source
 * @param <I> the command info
 * @version 0.0.1
 * @since 0.0.1
 */
public abstract class Command<S, I extends CommandInfo<S>> {

    /**
     * Empty constructor
     */
    public Command() {}

    /**
     * Executes the command while passing through {@link CommandInfo}
     *
     * @param info {@link I}
     */
    public abstract void execute(I info);

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
    public abstract @NotNull LiteralCommandNode<S> literal();

    /**
     * Registers the permission to the server.
     *
     * @return {@link Command}
     */
    public abstract @NotNull Command<S, I> registerPermission();

    /**
     * Return a list of uuids for the suggestion provider.
     *
     * @param builder {@link SuggestionsBuilder}
     * @param min min amount
     * @param max max amount
     * @return a list of suggestions
     */
    public @NotNull final CompletableFuture<Suggestions> suggestNames(final SuggestionsBuilder builder, final int min, final int max) {
        for (int count = min; count <= max; count++) builder.suggest(UUID.randomUUID().toString().replace("-", "").substring(0, 8));

        return builder.buildFuture();
    }

    /**
     * Return a list of uuids for the suggestion provider.
     *
     * @param builder {@link SuggestionsBuilder}
     * @return a list of suggestions
     */
    public @NotNull final CompletableFuture<Suggestions> suggestNames(final SuggestionsBuilder builder) {
        return suggestNames(builder, 1, 8);
    }

    /**
     * Return a list of Integers for the suggestion provider.
     *
     * @param builder {@link SuggestionsBuilder}
     * @param min min amount
     * @param max max amount
     * @return a list of suggestions
     */
    public @NotNull final CompletableFuture<Suggestions> suggestIntegers(final SuggestionsBuilder builder, final int min, final int max) {
        for (int count = min; count <= max; count++) builder.suggest(count);

        return builder.buildFuture();
    }

    /**
     * Return a list of Integers for the suggestion provider.
     *
     * @param builder {@link SuggestionsBuilder}
     * @return a list of suggestions
     */
    public @NotNull final CompletableFuture<Suggestions> suggestIntegers(final SuggestionsBuilder builder) {
        return suggestIntegers(builder, 1, 60);
    }

    /**
     * Return a list of Doubles for the suggestion provider.
     *
     * @param builder {@link SuggestionsBuilder}
     * @param min min amount
     * @param max max amount
     * @return a list of suggestions
     */
    public @NotNull final CompletableFuture<Suggestions> suggestDoubles(final SuggestionsBuilder builder, final int min, final int max) {
        int count = min;

        while (count <= max) {
            double x = count / 10.0;

            builder.suggest(String.valueOf(x));

            count++;
        }

        return builder.buildFuture();
    }

    /**
     * Return a list of Doubles for the suggestion provider.
     *
     * @param builder {@link SuggestionsBuilder}
     * @return a list of suggestions
     */
    public @NotNull final CompletableFuture<Suggestions> suggestDoubles(final SuggestionsBuilder builder) {
        return suggestDoubles(builder, 0, 1000);
    }
}