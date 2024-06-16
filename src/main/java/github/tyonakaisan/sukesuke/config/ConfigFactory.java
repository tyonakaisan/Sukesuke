package github.tyonakaisan.sukesuke.config;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import github.tyonakaisan.sukesuke.config.primary.PrimaryConfig;
import net.kyori.adventure.serializer.configurate4.ConfigurateComponentSerializer;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Singleton
@DefaultQualifier(NonNull.class)
public final class ConfigFactory {

    private static final String PRIMARY_CONFIG_FILE_NAME = "config.conf";

    private final Path dataDirectory;
    private final ComponentLogger logger;

    private @MonotonicNonNull PrimaryConfig primaryConfig;

    @Inject
    public ConfigFactory(
            final Path dataDirectory,
            final ComponentLogger logger
    ) {
        this.dataDirectory = dataDirectory;
        this.logger = logger;

        this.reloadPrimaryConfig();
    }

    public PrimaryConfig reloadPrimaryConfig() {
        this.logger.info("Reloading configuration...");
        final @Nullable PrimaryConfig load = this.load(PrimaryConfig.class, PRIMARY_CONFIG_FILE_NAME);
        if (load != null) {
            this.primaryConfig = load;
        } else {
            this.logger.error("Failed to reload primary config, see above for further details");
        }

        return this.primaryConfig;
    }

    public PrimaryConfig primaryConfig() {
        return this.primaryConfig != null ? this.primaryConfig : this.reloadPrimaryConfig();
    }

    public ConfigurationLoader<?> configurationLoader(final Path file) {
        return HoconConfigurationLoader.builder()
                .prettyPrinting(true)
                .defaultOptions(opts -> {
                    final var miniMessageSerializer =
                            ConfigurateComponentSerializer.builder()
                                    .scalarSerializer(MiniMessage.miniMessage())
                                    .outputStringComponents(true)
                                    .build();
                    final var componentSerializer =
                            ConfigurateComponentSerializer.configurate();

                    return opts.shouldCopyDefaults(true).serializers(serializerBuilder ->
                            serializerBuilder
                                    .registerAll(miniMessageSerializer.serializers())
                                    .registerAll(componentSerializer.serializers())
                    );
                })
                .path(file)
                .build();
    }

    public <T> @Nullable T load(final Class<T> clazz, final String fileName) {
        final Path file = this.dataDirectory.resolve(fileName);

        if (!Files.exists(this.dataDirectory)) {
            try {
                Files.createDirectories(this.dataDirectory);
            } catch (final IOException e) {
                this.logger.error(String.format("Failed to create parent directories for '%s'", file), e);
                return null;
            }
        }

        final var loader = this.configurationLoader(file);

        try {
            final var root = loader.load();
            final @Nullable T config = root.get(clazz);

            if (!Files.exists(file)) {
                root.set(clazz, config);
                loader.save(root);
            }

            this.logger.info("Successfully configuration file loaded!");
            return config;
        } catch (final ConfigurateException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
