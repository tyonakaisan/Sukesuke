package github.tyonakaisan.sukesuke.listener;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class ArmorChangeListener implements Listener {
    private final ArmorPacketManager armorPacketManager;

    @Inject
    public ArmorChangeListener(
            ArmorPacketManager armorPacketManager
    ) {
        this.armorPacketManager = armorPacketManager;
    }

    @EventHandler
    public void onPlayerArmorChange(PlayerArmorChangeEvent event) {
        Player player = event.getPlayer();
        ItemStack newArmor = event.getNewItem();
        var pdc = player.getPersistentDataContainer();
        //もりぱぱっち
        //toggleKeyチェック
        if (!player.getPersistentDataContainer().has(Keys.ToggleKey)) {
            Keys.setHideArmorKey(player);
        }
        if (Objects.requireNonNull(pdc.get(Keys.ToggleKey, PersistentDataType.STRING)).equalsIgnoreCase("false")) return;
        if (newArmor == null) return;

        armorPacketManager.sendPacket(player);
    }

    //エリトラ飛行中
    @EventHandler
    public void onPlayerToggleGlide(EntityToggleGlideEvent event){
        if(!(event.getEntity() instanceof Player player)) return;
        var pdc = player.getPersistentDataContainer();
        //もりぱぱっち
        if (!pdc.has(Keys.ToggleKey)) return;
        if (Objects.requireNonNull(player.getPersistentDataContainer().get(Keys.ToggleKey, PersistentDataType.STRING)).equalsIgnoreCase("false")) return;

        armorPacketManager.sendPacket(player);
    }
}
