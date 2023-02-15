package github.tyonakaisan.sukesuke.commands;

import com.comphenix.protocol.ProtocolManager;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import github.tyonakaisan.sukesuke.manager.gui.SettingsMenu;
import github.tyonakaisan.sukesuke.player.PlayerSetKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.incendo.interfaces.paper.PlayerViewer;
import org.jetbrains.annotations.NotNull;

public class PlayerCommands implements CommandExecutor {

    private final Sukesuke plugin;
    private final ArmorManager armorManager;
    private final ProtocolManager protocolManager;

    public PlayerCommands(Sukesuke pl,ProtocolManager pm, ArmorManager am) {
        this.plugin = pl;
        this.protocolManager = pm;
        this.armorManager = am;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player;
            player = (Player) sender;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("test")) {
                    new PlayerSetKey(plugin).setToggleArmorType(player, "self_toggle");
                    armorManager.sendPacket(player);
                    return true;
                }
            }

            new PlayerSetKey(plugin).setHideArmorType(player);
            new SettingsMenu(plugin, protocolManager, armorManager).buildInterface().open(PlayerViewer.of(player));
            return true;
        }
        return false;
    }
}
