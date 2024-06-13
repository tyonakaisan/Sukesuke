package github.tyonakaisan.sukesuke;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import github.tyonakaisan.sukesuke.listener.GameModeChangeListener;
import github.tyonakaisan.sukesuke.listener.PlayerGlideListener;
import github.tyonakaisan.sukesuke.packet.OthersPacketListener;
import github.tyonakaisan.sukesuke.packet.SelfPacketListener;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

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

    @Override
    public void configure() {
        this.bind(ComponentLogger.class).toInstance(this.logger);
        this.bind(Sukesuke.class).toInstance(this.sukesuke);
        this.bind(Server.class).toInstance(this.sukesuke.getServer());
        this.bind(Path.class).toInstance(this.dataDirectory);

        this.configureListeners();
    }

    private void configureListeners() {
        final Multibinder<Listener> listeners = Multibinder.newSetBinder(this.binder(), Listener.class);
        listeners.addBinding().to(GameModeChangeListener.class).in(Scopes.SINGLETON);
        listeners.addBinding().to(PlayerGlideListener.class).in(Scopes.SINGLETON);
        listeners.addBinding().to(OthersPacketListener.class).in(Scopes.SINGLETON);
        listeners.addBinding().to(SelfPacketListener.class).in(Scopes.SINGLETON);
    }
}
