package github.tyonakaisan.sukesuke.command.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.ArgumentBuilder;
import github.tyonakaisan.sukesuke.command.SukesukeCommand;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.message.Messages;
import github.tyonakaisan.sukesuke.utils.NamespacedKeyUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
@DefaultQualifier(NonNull.class)
public final class ToggleCommand implements SukesukeCommand {

    private final ArmorPacketManager armorPacketManager;
    private final Messages messages;

    @Inject
    public ToggleCommand(
            final ArmorPacketManager armorPacketManager,
            final Messages messages
    ) {
        this.armorPacketManager = armorPacketManager;
        this.messages = messages;
    }

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> init() {
        return literal("toggle")
                .requires(source -> source.getSender().hasPermission("sukesuke.command.toggle"))
                .executes(context -> {
                    if (context.getSource().getSender() instanceof final Player player) {
                        if (player.getGameMode().equals(GameMode.CREATIVE)) {
                            player.sendMessage(this.messages.translatable(Messages.Style.ERROR, player, "command.toggle.error.creative_not_allowed"));
                            return 0;
                        }

                        NamespacedKeyUtils.toggleKeyValue(player, NamespacedKeyUtils.display());
                        NamespacedKeyUtils.initKeys(player);
                        this.armorPacketManager.sendPacket(player);

                        if (NamespacedKeyUtils.isValueTrue(player, NamespacedKeyUtils.display())) {
                            player.sendMessage(this.messages.translatable(Messages.Style.SUCCESS, player, "command.toggle.success.display_enabled"));
                        } else {
                            player.sendMessage(this.messages.translatable(Messages.Style.SUCCESS, player, "command.toggle.success.display_disabled"));
                        }
                        return 1;
                    }
                    return 0;
                });
    }
}
