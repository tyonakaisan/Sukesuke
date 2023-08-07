package github.tyonakaisan.sukesuke.listener;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class GameModeChangeListener implements Listener {
    private final Sukesuke sukesuke;
    private final ArmorPacketManager armorPacketManager;

    @Inject
    public GameModeChangeListener(
            Sukesuke sukesuke,
            ArmorPacketManager armorPacketManager
    ) {
        this.sukesuke = sukesuke;
        this.armorPacketManager = armorPacketManager;
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();

        if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
            //強制false
            player.getPersistentDataContainer().set(Keys.ToggleKey, PersistentDataType.STRING, "false");
            armorPacketManager.sendPacket(player);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    //強制true
                    player.getPersistentDataContainer().set(Keys.ToggleKey, PersistentDataType.STRING, "true");
                    armorPacketManager.sendPacket(player);
                }
            }.runTaskLater(sukesuke, 1L);
        }
    }
}
