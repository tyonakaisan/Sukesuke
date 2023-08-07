package github.tyonakaisan.sukesuke;

import cloud.commandframework.CommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.nio.file.Path;
import java.util.function.Function;

@DefaultQualifier(NonNull.class)
public class SuskesukeModule extends AbstractModule {
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
    public CommandManager<CommandSender> commandManager() {
        final PaperCommandManager<CommandSender> commandManager;
        try {
            commandManager = new PaperCommandManager<>(
                    this.sukesuke,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );
        } catch (final Exception exception) {
            throw new RuntimeException("Failed to initialize command manager.", exception);
        }
        commandManager.registerAsynchronousCompletions();
        return commandManager;
    }


    @Override
    public void configure() {
        this.bind(ComponentLogger.class).toInstance(this.logger);
        this.bind(Sukesuke.class).toInstance(this.sukesuke);
        this.bind(Server.class).toInstance(this.sukesuke.getServer());
        this.bind(Server.class).toInstance(this.sukesuke.getServer());
        this.bind(Path.class).toInstance(this.dataDirectory);
    }
}
