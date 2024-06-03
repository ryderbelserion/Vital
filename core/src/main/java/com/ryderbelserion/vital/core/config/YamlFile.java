package com.ryderbelserion.vital.core.config;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentEncoder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.File;

/**
 * An extension of YamlConfiguration utilizing methods from PaperMC with some basic overrides for our own FileManager
 *
 * @version 1.6
 * @since 1.0
 */
public class YamlFile extends org.simpleyaml.configuration.file.YamlFile {

    /**
     * Creates a {@link YamlFile} from a {@link File}
     *
     * @param file {@link File}
     */
    public YamlFile(@NotNull final File file) {
        super(file);
    }

    /**
     * Creates a yaml file object from a string.
     * @param file the name of the file
     */
    public YamlFile(@NotNull final String file) {
        super(file);
    }

    /**
     * Gets the requested {@link net.kyori.adventure.text.minimessage.MiniMessage} formatted {@link String} as {@link Component} by path.
     * <p>
     * If the {@link Component} does not exist but a default value has been specified,
     * this will return the default value. If the {@link Component} does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Converts a {@link String} to a {@link Component}
     * @return {@link Component}
     */
    public @Nullable Component getRichMessage(@NotNull final String path) { // Paper's way of adding component support to configurations.
        return this.getRichMessage(path, null);
    }

    /**
     * Gets the requested {@link net.kyori.adventure.text.minimessage.MiniMessage} formatted {@link String} as {@link Component} by path.
     * <p>
     * If the {@link Component} does not exist but a default value has been specified,
     * this will return the default value. If the {@link Component} does not exist and no
     * default value was specified, this will return null.
     *
     * @param path path of the {@link Component} to get
     * @param fallback {@link Component} that will be used as fallback
     * @return {@link Component}
     */
    @Contract("_, !null -> !null")
    public @Nullable Component getRichMessage(@NotNull final String path, final @Nullable Component fallback) { // Paper's way of adding component support to configurations.
        return this.getComponent(path, net.kyori.adventure.text.minimessage.MiniMessage.miniMessage(), fallback);
    }

    /**
     * Sets the specified path to the given value.
     * <p>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless of what the new value is.
     *
     * @param path path of the object to set
     * @param value new value to set the path to
     */
    public void setRichMessage(@NotNull final String path, final @Nullable Component value) { // Paper's way of adding component support to configurations.
        this.setComponent(path, net.kyori.adventure.text.minimessage.MiniMessage.miniMessage(), value);
    }

    /**
     * Gets the requested formatted String as {@link Component} by path deserialized by the {@link net.kyori.adventure.text.serializer.ComponentDecoder}.
     * <p>
     * If the {@link Component} does not exist but a default value has been specified,
     * this will return the default value. If the {@link Component} does not exist and no
     * default value was specified, this will return null.
     *
     * @param path Converts a {@link String} to a {@link Component}
     * @param decoder {@link net.kyori.adventure.text.serializer.ComponentDecoder} instance used for deserialization
     * @param <C> a generic value
     * @return {@link Component}
     */
    public <C extends Component> @Nullable C getComponent(@NotNull final String path, final net.kyori.adventure.text.serializer.@NotNull ComponentDecoder<? super String, C> decoder) { // Paper's way of adding component support to configurations.
        return this.getComponent(path, decoder, null);
    }

    /**
     * Gets the requested formatted {@link String} as {@link Component} by path deserialized by the {@link net.kyori.adventure.text.serializer.ComponentDecoder}.
     * <p>
     * If the {@link Component} does not exist but a default value has been specified,
     * this will return the default value. If the {@link Component} does not exist and no
     * default value was specified, this will return null.
     *
     * @param path path of the {@link Component} to get
     * @param decoder {@link net.kyori.adventure.text.serializer.ComponentDecoder} instance used for deserialization
     * @param fallback {@link Component} that will be used as fallback
     * @param <C> a generic value
     * @return {@link Component}
     */
    public <C extends Component> @Nullable C getComponent(@NotNull final String path, final net.kyori.adventure.text.serializer.@NotNull ComponentDecoder<? super String, C> decoder, @Nullable final C fallback) { // Paper's way of adding component support to configurations.
        java.util.Objects.requireNonNull(decoder, "decoder");
        final String value = this.getString(path);
        return decoder.deserializeOr(value, fallback);
    }

    /**
     * Sets the specified path to the given value.
     * <p>
     * If value is null, the entry will be removed. Any existing entry will be
     * replaced, regardless of what the new value is.
     *
     * @param path path of the object to set
     * @param encoder the {@link ComponentEncoder} used to transform the value
     * @param value new value to set the path to
     * @param <C> a generic value
     */
    public <C extends Component> void setComponent(@NotNull final String path, final @NotNull ComponentEncoder<C, String> encoder, @Nullable final C value) { // Paper's way of adding component support to configurations.
        java.util.Objects.requireNonNull(encoder, "encoder");
        this.set(path, encoder.serializeOrNull(value));
    }
}