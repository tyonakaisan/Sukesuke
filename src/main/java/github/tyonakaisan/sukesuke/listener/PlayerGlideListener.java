package github.tyonakaisan.sukesuke.listener;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.utils.NamespacedKeyUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class PlayerGlideListener implements Listener {
    private final Sukesuke sukesuke;
    private final ArmorPacketManager armorPacketManager;

    @Inject
    public PlayerGlideListener(
            final Sukesuke sukesuke,
            final ArmorPacketManager armorPacketManager
    ) {
        this.sukesuke = sukesuke;
        this.armorPacketManager = armorPacketManager;
    }

    //エリトラ飛行中
    @EventHandler
    public void onPlayerToggleGlide(EntityToggleGlideEvent event){
        if (event.getEntity() instanceof Player player) {
            if (!NamespacedKeyUtils.isValueTrue(player, NamespacedKeyUtils.display())) {
                return;
            }

            Bukkit.getServer().getScheduler().runTaskLater(this.sukesuke, () -> this.armorPacketManager.sendPacket(player), 1L);
        }
    }
}
