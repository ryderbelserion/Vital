package com.ryderbelserion.vital.core.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentEncoder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Map;

/**
 * A class handling components or minimessage related functions
 *
 * @version 1.5.1
 * @since 1.0
 */
public class AdvUtil {

    private AdvUtil() {
        throw new AssertionError();
    }

    /**
     * Gets the requested {@link MiniMessage} formatted {@link String} as {@link Component} by path.
     * <p>
     * If the {@link Component} does not exist but a default value has been specified,
     * this will return the default value. If the {@link Component} does not exist and no
     * default value was specified, this will return null.
     *
     * @param value Converts a {@link String} to a {@link Component}
     * @return {@link Component}
     * @since 1.0
     */
    public static @Nullable Component getRichMessage(@NotNull final String value) { // Paper's way of adding component support to configurations.
        return getRichMessage(value, null);
    }

    /**
     * Gets the requested {@link MiniMessage} formatted {@link String} as {@link Component} by path.
     * <p>
     * If the {@link Component} does not exist but a default value has been specified,
     * this will return the default value. If the {@link Component} does not exist and no
     * default value was specified, this will return null.
     *
     * @param fallback {@link Component} that will be used as fallback
     * @param value Converts a {@link String} to a {@link Component}
     * @return {@link Component}
     * @since 1.0
     */
    @Contract("_, !null -> !null")
    public static @Nullable Component getRichMessage(@NotNull final String value, final @Nullable Component fallback) { // Paper's way of adding component support to configurations.
        return getComponent(value, MiniMessage.miniMessage(), fallback);
    }

    /**
     * Gets the requested formatted String as {@link Component} by path deserialized by the {@link net.kyori.adventure.text.serializer.ComponentDecoder}.
     * <p>
     * If the {@link Component} does not exist but a default value has been specified,
     * this will return the default value. If the {@link Component} does not exist and no
     * default value was specified, this will return null.
     *
     * @param value Converts a {@link String} to a {@link Component}
     * @param decoder {@link net.kyori.adventure.text.serializer.ComponentDecoder} instance used for deserialization
     * @param <C> a generic value
     * @return {@link Component}
     * @since 1.0
     */
    public static <C extends Component> @Nullable C getComponent(@NotNull final String value, final net.kyori.adventure.text.serializer.@NotNull ComponentDecoder<? super String, C> decoder) { // Paper's way of adding component support to configurations.
        return getComponent(value, decoder, null);
    }

    /**
     * Gets the requested formatted {@link String} as {@link Component} by path deserialized by the {@link net.kyori.adventure.text.serializer.ComponentDecoder}.
     * <p>
     * If the {@link Component} does not exist but a default value has been specified,
     * this will return the default value. If the {@link Component} does not exist and no
     * default value was specified, this will return null.
     *
     * @param value path of the {@link Component} to get
     * @param decoder {@link net.kyori.adventure.text.serializer.ComponentDecoder} instance used for deserialization
     * @param fallback {@link Component} that will be used as fallback
     * @param <C> a generic value
     * @return {@link Component}
     * @since 1.0
     */
    public static <C extends Component> @Nullable C getComponent(@NotNull final String value, final net.kyori.adventure.text.serializer.@NotNull ComponentDecoder<? super String, C> decoder, @Nullable final C fallback) {
        java.util.Objects.requireNonNull(decoder, "decoder");
        return decoder.deserializeOr(value, fallback);
    }

    /**
     * Sets the specified path to the given value.
     * <p>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless of what the new value is.
     *
     * @param value new value to set the path to
     * @return the serialized {@link String}
     * @since 1.0
     */
    public static @Nullable String getRichMessage(final @Nullable Component value) { // Paper's way of adding component support to configurations.
        return getComponent(MiniMessage.miniMessage(), value);
    }

    /**
     * Sets the specified path to the given value.
     * <p>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless of what the new value is.
     *
     * @param encoder the {@link ComponentEncoder} used to transform the value
     * @param value new value to set the path to
     * @param <C> a generic value
     * @return the serialized {@link String}
     * @since 1.0
     */
    public static @Nullable <C extends Component> String getComponent(@NotNull final ComponentEncoder<C, String> encoder, @Nullable final C value) {
        java.util.Objects.requireNonNull(encoder, "encoder");
        return encoder.serializeOrNull(value);
    }

    /**
     * Parse a {@link String} than returns a {@link Component}.
     *
     * @param message the {@link String} to alter
     * @return {@link Component}
     * @since 1.0
     */
    public static @NotNull Component parse(@NotNull final String message) {
        return parse(message, null);
    }

    /**
     * Parse placeholders in a {@link String} than returns a {@link Component}.
     *
     * @param message the {@link String} to alter
     * @param placeholders the {@link Map} to use
     * @return {@link Component}
     * @since 1.0
     */
    public static @NotNull Component parse(@NotNull final String message, @Nullable Map<String, String> placeholders) {
        if (message.isEmpty()) return Component.empty();

        String clonedMessage = message;

        if (placeholders != null && !placeholders.isEmpty()) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                String key = entry.getKey().toLowerCase(); // Make the placeholder lowercase
                String value = entry.getValue();

                clonedMessage = clonedMessage.replace(key, value);
            }
        }
        
        return MiniMessage.miniMessage().deserialize(clonedMessage).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }
}