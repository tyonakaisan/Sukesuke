package github.tyonakaisan.sukesuke;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.google.inject.Guice;
import com.google.inject.Injector;
import github.tyonakaisan.sukesuke.command.SukesukeCommand;
import github.tyonakaisan.sukesuke.command.commands.GuiCommand;
import github.tyonakaisan.sukesuke.command.commands.SukeCommand;
import github.tyonakaisan.sukesuke.listener.ArmorChangeListener;
import github.tyonakaisan.sukesuke.listener.GameModeChangeListener;
import github.tyonakaisan.sukesuke.listener.PlayerArmSwingListener;
import github.tyonakaisan.sukesuke.listener.PlayerJoinListener;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import github.tyonakaisan.sukesuke.packet.OthersPacketListener;
import github.tyonakaisan.sukesuke.packet.SelfPacketListener;
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
private static Sukesuke sukesuke;

    private static final Set<Class<? extends Listener>> LISTENER_CLASSES = Set.of(
            ArmorChangeListener.class,
            GameModeChangeListener.class,
            PlayerArmSwingListener.class,
            PlayerJoinListener.class
    );

    private static final Set<Class<? extends SukesukeCommand>> COMMAND_CLASS = Set.of(
            SukeCommand.class,
            GuiCommand.class
    );

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
        sukesuke = this;
        PaperInterfaceListeners.install(this);

        //manager
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        ArmorManager armorManager = new ArmorManager();

        // Commands
        for (final Class<? extends SukesukeCommand> commandClass : COMMAND_CLASS) {
            var command = this.injector.getInstance(commandClass);
            command.init();
        }

        //packet
        new SelfPacketListener(this, protocolManager, armorManager);
        new OthersPacketListener(this, protocolManager, armorManager);

        // Listeners
        for (final Class<? extends Listener> listenerClass : LISTENER_CLASSES) {
            var listener = this.injector.getInstance(listenerClass);
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //あ
    }

    public static Sukesuke getPlugin() {
        return sukesuke;
    }
}
