package github.tyonakaisan.sukesuke.listener;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

public class ArmorChangeListener implements Listener {
    Sukesuke plugin;
    ArmorPacketManager armorPacketManager;

    public ArmorChangeListener(Sukesuke pl, ArmorPacketManager am){
        this.plugin = pl;
        this.armorPacketManager = am;
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        PlayerInventory inventory = player.getInventory();
        if (!player.getPersistentDataContainer().get(new NamespacedKey(plugin, "self_toggle"), PersistentDataType.STRING).equalsIgnoreCase("true")) {
            return;
        }
        ItemStack new_armor = event.getNewItem();

        if (new_armor == null) {
            return;
        }
        armorPacketManager.sendPacket(player);
    }
}
