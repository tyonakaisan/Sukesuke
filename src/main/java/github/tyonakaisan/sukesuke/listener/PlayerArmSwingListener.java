package github.tyonakaisan.sukesuke.listener;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import io.papermc.paper.event.player.PlayerArmSwingEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EquipmentSlot;

//無敵のmob等に攻撃した際に防具の透明化が解除されてしまう対策
//パケットでやろうとしたけど上手く動作しなかったから代用

public class PlayerArmSwingListener implements Listener {
    private final ArmorPacketManager armorPacketManager;

    @Inject
    public PlayerArmSwingListener(
            ArmorPacketManager armorPacketManager
    ) {
        this.armorPacketManager = armorPacketManager;
    }

    @EventHandler
    public void onArmSwing(PlayerArmSwingEvent event) {
        Player player = event.getPlayer();

        if (event.getHand() == EquipmentSlot.HAND && player.getTargetEntity(4, false) != null) {
            armorPacketManager.sendPacket(player);
        }
    }
}
