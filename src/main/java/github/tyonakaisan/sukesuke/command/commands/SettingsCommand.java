package github.tyonakaisan.sukesuke.command.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.ArgumentBuilder;
import github.tyonakaisan.sukesuke.command.SukesukeCommand;
import github.tyonakaisan.sukesuke.manager.gui.SettingsMenu;
import github.tyonakaisan.sukesuke.utils.NamespacedKeyUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
@DefaultQualifier(NonNull.class)
public final class SettingsCommand implements SukesukeCommand {

    private final SettingsMenu settingsMenu;

    @Inject
     public SettingsCommand(
            final SettingsMenu settingsMenu
    ) {
        this.settingsMenu = settingsMenu;
    }

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> init() {
        return literal("settings")
                .requires(source -> source.getSender().hasPermission("sukesuke.command.settings"))
                .executes(context -> {
                    if (context.getSource().getSender() instanceof final Player player) {
                        NamespacedKeyUtils.initKeys(player);
                        this.settingsMenu.open(player);
                        return 1;
                    }
                    return 0;
                });
    }
}
