package github.tyonakaisan.sukesuke.listener;

import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerArmSwingListener implements Listener {
    Sukesuke plugin;
    ArmorPacketManager armorPacketManager;

    public PlayerArmSwingListener(Sukesuke pl, ArmorPacketManager am){
        this.plugin = pl;
        this.armorPacketManager = am;
    }

    @EventHandler
    public void onArmSwing(PlayerArmSwingEvent event) {
        Player player = event.getPlayer();

        if (event.getHand() == EquipmentSlot.HAND && player.getTargetEntity(4, false) != null) {
            armorPacketManager.sendPacket(player);
        }
    }
}
