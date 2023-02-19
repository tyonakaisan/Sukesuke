package github.tyonakaisan.sukesuke.commands;

import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.gui.SettingsMenu;
import github.tyonakaisan.sukesuke.player.PlayerSetKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
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

public class PlayerCommands implements CommandExecutor, TabCompleter {

    private final Sukesuke plugin;
    private final ArmorPacketManager armorPacketManager;

    public PlayerCommands(Sukesuke pl, ArmorPacketManager am) {
        this.plugin = pl;
        this.armorPacketManager = am;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player;
            player = (Player) sender;
            if (args.length == 1) {
                switch (args[0]) {
                    case "suke" -> {
                        armorPacketManager.sendPacket(player);
                        return true;
                    }
                    case "gui" -> {
                        new SettingsMenu(plugin).buildInterface().open(PlayerViewer.of(player));
                        return true;
                    }
                }
            }
            new PlayerSetKey(plugin).setToggleArmorType(player, "self_toggle");
            armorPacketManager.sendPacket(player);
            //actionbar
            String toggle = player.getPersistentDataContainer().get(new NamespacedKey(plugin, "self_toggle"), PersistentDataType.STRING);

            Component actionBar = Component.text()
                    .append(Component.text("すけすけモード: ")
                            .decoration(TextDecoration.BOLD, true)
                            .decoration(TextDecoration.ITALIC, false)
                            .color(TextColor.fromCSSHexString("#00fa9a")))
                    .append(Component.text(toggle))
                    .build();
            player.sendActionBar(actionBar);
            return true;
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
