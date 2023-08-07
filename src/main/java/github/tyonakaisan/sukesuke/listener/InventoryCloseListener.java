package github.tyonakaisan.sukesuke.listener;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class InventoryCloseListener implements Listener {

    private final ArmorPacketManager armorPacketManager;

    @Inject
    public InventoryCloseListener(
            ArmorPacketManager armorPacketManager
    ) {
        this.armorPacketManager = armorPacketManager;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        var player = (Player) event.getPlayer();

        if (player.hasPermission("sukesuke.suke")
                && Objects.requireNonNull(player.getPersistentDataContainer().get(Keys.ToggleKey, PersistentDataType.STRING)).equalsIgnoreCase("true")) {
            armorPacketManager.sendPacket(player);
        }
    }
}
