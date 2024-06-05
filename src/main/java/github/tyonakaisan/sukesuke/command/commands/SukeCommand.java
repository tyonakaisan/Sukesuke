package github.tyonakaisan.sukesuke.command.commands;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.command.SukesukeCommand;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.SukesukeKey;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.cloud.paper.PaperCommandManager;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class SukeCommand implements SukesukeCommand {

    private final ArmorPacketManager armorPacketManager;
    private final PaperCommandManager<CommandSourceStack> commandManager;
    private final SukesukeKey sukesukeKey;

    @Inject
    public SukeCommand(
            final ArmorPacketManager armorPacketManager,
            final PaperCommandManager<CommandSourceStack> commandManager,
            final SukesukeKey sukesukeKey
    ) {
        this.armorPacketManager = armorPacketManager;
        this.commandManager = commandManager;
        this.sukesukeKey = sukesukeKey;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("suke")
                .handler(handler -> {
                    if (handler.sender().getSender() instanceof Player player) {
                        //toggleKeyチェック
                        if (!player.getPersistentDataContainer().has(sukesukeKey.toggle())) {
                            sukesukeKey.setHideArmorKeys(player);
                        }
                        sukesukeKey.setToggleArmorType(player, sukesukeKey.toggle());
                        armorPacketManager.sendPacket(player);
                        //actionbar
                        String isToggle = Objects.requireNonNull(player.getPersistentDataContainer().get(sukesukeKey.toggle(), PersistentDataType.STRING));
                        Component toggle;

                        if (Objects.requireNonNull(isToggle).equalsIgnoreCase("true")) {
                            toggle = Component.text()
                                    .append(Component.text("非表示中!"))
                                    .decoration(TextDecoration.BOLD, true)
                                    .decoration(TextDecoration.ITALIC, false)
                                    .build();
                        } else {
                            toggle = Component.text()
                                    .append(Component.text("表示中!"))
                                    .decoration(TextDecoration.BOLD, true)
                                    .decoration(TextDecoration.ITALIC, false)
                                    .build();
                        }

                        Component actionBar = Component.text()
                                .append(Component.text("すけすけモード: ")
                                        .decoration(TextDecoration.BOLD, true)
                                        .decoration(TextDecoration.ITALIC, false)
                                        .color(TextColor.fromCSSHexString("#00fa9a")))
                                .append(toggle)
                                .build();
                        player.sendActionBar(actionBar);
                    }
                })
                .build();

        this.commandManager.command(command);
    }
}
