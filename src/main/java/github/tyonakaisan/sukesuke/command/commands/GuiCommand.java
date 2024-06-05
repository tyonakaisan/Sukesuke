package github.tyonakaisan.sukesuke.command.commands;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.command.SukesukeCommand;
import github.tyonakaisan.sukesuke.manager.SukesukeKey;
import github.tyonakaisan.sukesuke.manager.gui.SettingsMenu;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.paper.PaperCommandManager;
import org.incendo.interfaces.paper.PlayerViewer;

@DefaultQualifier(NonNull.class)
public final class GuiCommand implements SukesukeCommand {

    private final SettingsMenu settingsMenu;
    private final SukesukeKey sukesukeKey;
    private final PaperCommandManager<CommandSourceStack> commandManager;

    @Inject
     public GuiCommand(
            final SettingsMenu settingsMenu,
            final SukesukeKey sukesukeKey,
            final PaperCommandManager<CommandSourceStack> commandManager
    ) {
        this.settingsMenu = settingsMenu;
        this.sukesukeKey = sukesukeKey;
        this.commandManager = commandManager;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("suke")
                .literal("gui")
                .handler(handler -> {
                    if (handler.sender().getSender() instanceof Player player) {
                        if (!player.getPersistentDataContainer().has(sukesukeKey.toggle())) {
                            sukesukeKey.setHideArmorKeys(player);
                        }
                        settingsMenu.buildInterface().open(PlayerViewer.of(player));
                    }
                })
                .build();

        this.commandManager.command(command);
    }
}
