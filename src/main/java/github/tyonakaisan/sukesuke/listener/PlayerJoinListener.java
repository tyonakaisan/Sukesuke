package github.tyonakaisan.sukesuke.listener;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class PlayerJoinListener implements Listener {
    private final ArmorPacketManager armorPacketManager;

    @Inject
    public PlayerJoinListener(
            ArmorPacketManager armorPacketManager
    ) {
        this.armorPacketManager = armorPacketManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Keys.setHideArmorKey(player);

        if (player.hasPermission("sukesuke.suke")
                && Objects.requireNonNull(player.getPersistentDataContainer().get(Keys.ToggleKey, PersistentDataType.STRING)).equalsIgnoreCase("true")) {
            armorPacketManager.sendPacket(player);
        }
    }
}
