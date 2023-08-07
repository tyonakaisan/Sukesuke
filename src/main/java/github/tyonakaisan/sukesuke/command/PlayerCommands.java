package github.tyonakaisan.sukesuke.command;

import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import github.tyonakaisan.sukesuke.manager.gui.SettingsMenu;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.StringUtil;
import org.incendo.interfaces.paper.PlayerViewer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PlayerCommands implements CommandExecutor, TabCompleter {

    private final Sukesuke sukesuke;
    private final ArmorPacketManager armorPacketManager;

    public PlayerCommands(Sukesuke sk, ArmorPacketManager am) {
        this.sukesuke = sk;
        this.armorPacketManager = am;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("sukesuke.suke")) {
                if (args.length == 1) {
                    switch (args[0]) {
                        case "suke" -> {
                            //toggleKeyチェック
                            if (!player.getPersistentDataContainer().has(Keys.ToggleKey)) {
                                Keys.setHideArmorKey(player);
                            }
                            armorPacketManager.sendPacket(player);
                            return true;
                        }
                        case "gui" -> {
                            //toggleKeyチェック
                            if (!player.getPersistentDataContainer().has(Keys.ToggleKey)) {
                                Keys.setHideArmorKey(player);
                            }
                            new SettingsMenu(sukesuke).buildInterface().open(PlayerViewer.of(player));
                            return true;
                        }
                    }
                }
                //toggleKeyチェック
                if (!player.getPersistentDataContainer().has(Keys.ToggleKey)) {
                    Keys.setHideArmorKey(player);
                }
                Keys.setToggleArmorType(player, "toggle");
                armorPacketManager.sendPacket(player);
                //actionbar
                String isToggle = player.getPersistentDataContainer().get(Keys.ToggleKey, PersistentDataType.STRING);
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
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> commands = new ArrayList<>();
        if (sender instanceof Player) {
            if (args.length == 0) {
                commands.add("suke");
            }
            else if (args.length == 1) {
                commands.add("suke");
                commands.add("gui");
                return SortedCommands(args[0], commands);
            }
        }
        return commands;
    }

    private List<String> SortedCommands(String arg, List<String> commands) {
        final List<String> matches = new ArrayList<>();
        StringUtil.copyPartialMatches(arg, commands, matches);
        Collections.sort(matches);
        commands.clear();
        commands.addAll(matches);
        return commands;
    }
}
