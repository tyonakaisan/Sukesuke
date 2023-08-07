package github.tyonakaisan.sukesuke.command.commands;

import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.command.SukesukeCommand;
import github.tyonakaisan.sukesuke.manager.Keys;
import github.tyonakaisan.sukesuke.manager.gui.SettingsMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.interfaces.paper.PlayerViewer;

@DefaultQualifier(NonNull.class)
public final class GuiCommand implements SukesukeCommand {

    private final SettingsMenu settingsMenu;
    private final CommandManager<CommandSender> commandManager;

    @Inject
    GuiCommand(
            SettingsMenu settingsMenu,
            CommandManager<CommandSender> commandManager
    ) {
        this.settingsMenu = settingsMenu;
        this.commandManager = commandManager;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("suke")
                .literal("gui")
                .senderType(CommandSender.class)
                .handler(handler -> {
                    final var sender = (Player) handler.getSender();

                    if (!sender.getPersistentDataContainer().has(Keys.ToggleKey)) {
                        Keys.setHideArmorKey(sender);
                    }

                    settingsMenu.buildInterface().open(PlayerViewer.of(sender));
                })
                .build();

        this.commandManager.command(command);
    }
}
