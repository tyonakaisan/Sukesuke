package github.tyonakaisan.sukesuke.listener;

import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final Sukesuke plugin;
    ArmorPacketManager armorPacketManager;

    public PlayerJoinListener(Sukesuke pl, ArmorPacketManager am) {
        this.plugin = pl;
        this.armorPacketManager = am;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        //toggleKeyチェック
        if (!player.getPersistentDataContainer().has(Keys.ToggleKey)) {
            Keys.setHideArmorKey(player);
        }

        if (player.hasPermission("sukesuke.suke")) {
            armorPacketManager.sendPacket(player);
        }
    }
}
