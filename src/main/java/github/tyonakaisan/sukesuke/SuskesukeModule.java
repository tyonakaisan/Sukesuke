package github.tyonakaisan.sukesuke;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Server;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.incendo.cloud.paper.PaperCommandManager;

import java.nio.file.Path;

@DefaultQualifier(NonNull.class)
public final class SuskesukeModule extends AbstractModule {
    private final ComponentLogger logger;
    private final Sukesuke sukesuke;
    private final Path dataDirectory;

    SuskesukeModule(
            final Sukesuke sukesuke,
            final Path dataDirectory,
            final ComponentLogger logger
    ) {
        this.sukesuke = sukesuke;
        this.dataDirectory = dataDirectory;
        this.logger = logger;
    }

    @Provides
    @Singleton
    public PaperCommandManager<CommandSourceStack> commandManager() {
        return PaperCommandManager.builder()
                .executionCoordinator(ExecutionCoordinator.simpleCoordinator())
                .buildOnEnable(this.sukesuke);
    }

    @Override
    public void configure() {
        this.bind(ComponentLogger.class).toInstance(this.logger);
        this.bind(Sukesuke.class).toInstance(this.sukesuke);
        this.bind(Server.class).toInstance(this.sukesuke.getServer());
        this.bind(Path.class).toInstance(this.dataDirectory);
    }
}
