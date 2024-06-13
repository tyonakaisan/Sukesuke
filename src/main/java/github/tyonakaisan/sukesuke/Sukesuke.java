package github.tyonakaisan.sukesuke;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import github.tyonakaisan.sukesuke.command.CommandFactory;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.interfaces.paper.PaperInterfaceListeners;

import java.nio.file.Path;
import java.util.Set;

@DefaultQualifier(NonNull.class)
public final class Sukesuke extends JavaPlugin {

    private final Injector injector;

    public Sukesuke(
            final Path dataDirectory,
            final ComponentLogger logger
    ) {
        this.injector = Guice.createInjector(new SuskesukeModule(this, dataDirectory, logger));
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        PaperInterfaceListeners.install(this);

        // Listeners
        final Set<Listener> listeners = this.injector.getInstance(Key.get(new TypeLiteral<>() {}));
        listeners.forEach(listener -> this.getServer().getPluginManager().registerEvents(listener, this));

        // Commands
        this.injector.getInstance(CommandFactory.class).registerViaEnable(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //あ
    }
}
