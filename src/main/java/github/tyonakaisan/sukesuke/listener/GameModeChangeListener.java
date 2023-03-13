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
    Sukesuke plugin;
    ArmorPacketManager armorPacketManager;

    public GameModeChangeListener(Sukesuke pl, ArmorPacketManager am){
        this.plugin = pl;
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
            new BukkitRunnable(){
                @Override
                public void run() {
                    //new SetKey(plugin).setToggleArmorType(player, "toggle");
                    Keys.setToggleArmorType(player, "toggle");
                    armorPacketManager.sendPacket(player);
                }
            }.runTaskLater(plugin, 1L);
        }
    }
}
