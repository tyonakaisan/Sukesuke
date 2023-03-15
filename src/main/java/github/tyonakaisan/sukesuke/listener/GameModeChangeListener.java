package github.tyonakaisan.sukesuke.listener;

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

public class GameModeChangeListener implements Listener {
    private final Sukesuke sukesuke;
    private final ArmorPacketManager armorPacketManager;

    public GameModeChangeListener(Sukesuke sk, ArmorPacketManager am){
        this.sukesuke = sk;
        this.armorPacketManager = am;
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
            //trueに強制戻し
            player.getPersistentDataContainer().set(Keys.ToggleKey, PersistentDataType.STRING, "true");
            Keys.setToggleArmorType(player, "toggle");
            armorPacketManager.sendPacket(player);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    Keys.setToggleArmorType(player, "toggle");
                    armorPacketManager.sendPacket(player);
                }
            }.runTaskLater(sukesuke, 1L);
        }
    }
}
