package github.tyonakaisan.sukesuke.listener;

import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryClickListener implements Listener {
    Sukesuke plugin;
    ArmorManager armorManager;

    public InventoryClickListener(Sukesuke pl, ArmorManager am){
        this.plugin = pl;
        this.armorManager = am;
    }

    @EventHandler
    public void onShiftClickArmor(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (!player.getPersistentDataContainer().get(new NamespacedKey(plugin, "self_toggle"), PersistentDataType.STRING).equalsIgnoreCase("true")) {
            return;
        }
        if (!(event.getClickedInventory() instanceof PlayerInventory)) {
            return;
        }
        if (!event.isShiftClick()) {
            return;
        }

        PlayerInventory inv = player.getInventory();
        ItemStack armor = event.getCurrentItem();

        if (armor == null) return;

        if ((armor.getType().toString().endsWith("_HELMET") && inv.getHelmet() == null) ||
                ((armor.getType().toString().endsWith("_CHESTPLATE") || armor.getType().equals(Material.ELYTRA)) && inv.getChestplate() == null) ||
                (armor.getType().toString().endsWith("_LEGGINGS") && inv.getLeggings() == null) ||
                (armor.getType().toString().endsWith("_BOOTS") && inv.getBoots() == null)){
            new BukkitRunnable(){
                @Override
                public void run() {
                    armorManager.sendPacket(player);
                }
            }.runTaskLater(plugin, 1L);
        }
    }
}
