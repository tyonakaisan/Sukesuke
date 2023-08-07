package github.tyonakaisan.sukesuke.command.commands;

import cloud.commandframework.CommandManager;
import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.command.SukesukeCommand;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class SukeCommand implements SukesukeCommand {

    private final ArmorPacketManager armorPacketManager;
    private final CommandManager<CommandSender> commandManager;

    @Inject
    SukeCommand(
            ArmorPacketManager armorPacketManager,
            CommandManager<CommandSender> commandManager
    ) {
        this.armorPacketManager = armorPacketManager;
        this.commandManager = commandManager;
    }

    @Override
    public void init() {
        final var command = this.commandManager.commandBuilder("suke")
                .senderType(CommandSender.class)
                .handler(handler -> {
                    final var sender = (Player) handler.getSender();

                    //toggleKeyチェック
                    if (!sender.getPersistentDataContainer().has(Keys.ToggleKey)) {
                        Keys.setHideArmorKey(sender);
                    }
                    Keys.setToggleArmorType(sender, "toggle");
                    armorPacketManager.sendPacket(sender);
                    //actionbar
                    String isToggle = Objects.requireNonNull(sender.getPersistentDataContainer().get(Keys.ToggleKey, PersistentDataType.STRING));
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
                    sender.sendActionBar(actionBar);
                })
                .build();

        this.commandManager.command(command);
    }
}
