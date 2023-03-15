package github.tyonakaisan.sukesuke.listener;

import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerJoinListener implements Listener {
    private final ArmorPacketManager armorPacketManager;

    public PlayerJoinListener(ArmorPacketManager am) {
        this.armorPacketManager = am;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //toggleKeyチェック
        if (!player.getPersistentDataContainer().has(Keys.ToggleKey)) {
            Keys.setHideArmorKey(player);
        }

        if (player.hasPermission("sukesuke.suke") && player.getPersistentDataContainer().get(Keys.ToggleKey, PersistentDataType.STRING).equalsIgnoreCase("true")) {
            armorPacketManager.sendPacket(player);
        }
    }
}
