package github.tyonakaisan.sukesuke.listener;

import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import github.tyonakaisan.sukesuke.player.PlayerSetKey;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.N;

public class GameModeChangeListener implements Listener {
    Sukesuke plugin;
    ArmorManager armorManager;

    public GameModeChangeListener(Sukesuke pl, ArmorManager am){
        this.plugin = pl;
        this.armorManager = am;
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
            //trueに強制戻し
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "self_toggle"), PersistentDataType.STRING, "true");
            new PlayerSetKey(plugin).setToggleArmorType(player, "self_toggle");
            armorManager.sendPacket(player);
        } else {
            new BukkitRunnable(){
                @Override
                public void run() {
                    new PlayerSetKey(plugin).setToggleArmorType(player, "self_toggle");
                    armorManager.sendPacket(player);
                }
            }.runTaskLater(plugin, 1L);
        }
    }
}
