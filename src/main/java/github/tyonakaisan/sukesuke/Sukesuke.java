package github.tyonakaisan.sukesuke;

import com.google.inject.Guice;
import com.google.inject.Injector;
import github.tyonakaisan.sukesuke.command.SukesukeCommand;
import github.tyonakaisan.sukesuke.command.commands.GuiCommand;
import github.tyonakaisan.sukesuke.command.commands.SukeCommand;
import github.tyonakaisan.sukesuke.listener.*;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import github.tyonakaisan.sukesuke.manager.SukesukeKey;
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

    private static final Set<Class<? extends Listener>> LISTENER_CLASSES = Set.of(
            ArmorChangeListener.class,
            GameModeChangeListener.class,
            PlayerArmSwingListener.class,
            PlayerJoinListener.class,
            InventoryCloseListener.class
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
        PaperInterfaceListeners.install(this);

        //manager
        ArmorManager armorManager = new ArmorManager();
        SukesukeKey sukesukeKey = new SukesukeKey(this);

        // Commands
        for (final Class<? extends SukesukeCommand> commandClass : COMMAND_CLASS) {
            var command = this.injector.getInstance(commandClass);
            command.init();
        }

        //packet
        new SelfPacketListener(this, sukesukeKey, armorManager);
        new OthersPacketListener(this, sukesukeKey, armorManager);

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
}
