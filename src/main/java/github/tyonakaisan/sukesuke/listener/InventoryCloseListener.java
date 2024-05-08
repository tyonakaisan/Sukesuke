package github.tyonakaisan.sukesuke.listener;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.SukesukeKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class InventoryCloseListener implements Listener {

    private final ArmorPacketManager armorPacketManager;
    private final SukesukeKey sukesukeKey;

    @Inject
    public InventoryCloseListener(
            final ArmorPacketManager armorPacketManager,
            final SukesukeKey sukesukeKey
    ) {
        this.armorPacketManager = armorPacketManager;
        this.sukesukeKey = sukesukeKey;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {

        var player = (Player) event.getPlayer();

        if (player.hasPermission("sukesuke.suke")
                && Objects.requireNonNull(player.getPersistentDataContainer().get(sukesukeKey.toggle(), PersistentDataType.STRING)).equalsIgnoreCase("true")) {
            armorPacketManager.sendPacket(player);
        }
    }
}
