package github.tyonakaisan.sukesuke.commands;

import com.comphenix.protocol.ProtocolManager;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.gui.SettingsMenu;
import github.tyonakaisan.sukesuke.player.PlayerSetKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.incendo.interfaces.paper.PlayerViewer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlayerCommands implements CommandExecutor, TabCompleter {

    private final Sukesuke plugin;
    private final ArmorPacketManager armorPacketManager;
    private final ProtocolManager protocolManager;

    public PlayerCommands(Sukesuke pl,ProtocolManager pm, ArmorPacketManager am) {
        this.plugin = pl;
        this.protocolManager = pm;
        this.armorPacketManager = am;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player;
            player = (Player) sender;
            if (args.length == 1) {
                switch (args[0]) {
                    case "toggle" -> {
                        new PlayerSetKey(plugin).setToggleArmorType(player, "self_toggle");
                        armorPacketManager.sendPacket(player);
                        return true;
                    }
                    case "suke" -> {
                        armorPacketManager.sendPacket(player);
                        return true;
                    }
                    case "gui" -> {
                        new PlayerSetKey(plugin).setHideArmorKey(player);
                        new SettingsMenu(plugin, protocolManager, armorPacketManager).buildInterface().open(PlayerViewer.of(player));
                        return true;
                    }
                }
            }

            new PlayerSetKey(plugin).setHideArmorKey(player);
            new SettingsMenu(plugin, protocolManager, armorPacketManager).buildInterface().open(PlayerViewer.of(player));
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
                commands.add("toggle");
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
