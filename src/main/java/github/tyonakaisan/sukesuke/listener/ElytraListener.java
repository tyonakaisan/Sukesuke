package github.tyonakaisan.sukesuke.listener;

import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class ElytraListener implements Listener {
    Sukesuke plugin;
    ArmorManager armorManager;

    public ElytraListener(Sukesuke pl, ArmorManager am){
        this.plugin = pl;
        this.armorManager = am;
    }

    @EventHandler
    public void onPlayerToggleGlide(EntityToggleGlideEvent e){
        if(!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();
        if(!player.getPersistentDataContainer().get(new NamespacedKey(plugin, "self_toggle"), PersistentDataType.STRING).equalsIgnoreCase("true")) {
            return;
        }

        new BukkitRunnable(){
            @Override
            public void run() {
                armorManager.sendPacket(player);
            }
        }.runTaskLater(plugin, 1L);
    }
}
