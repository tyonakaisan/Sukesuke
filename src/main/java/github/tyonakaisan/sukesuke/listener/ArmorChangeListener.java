package github.tyonakaisan.sukesuke.listener;

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent;
import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class ArmorChangeListener implements Listener {
    private final Sukesuke sukesuke;
    private final ArmorPacketManager armorPacketManager;

    @Inject
    public ArmorChangeListener(
            Sukesuke sukesuke,
            ArmorPacketManager armorPacketManager
    ) {
        this.sukesuke = sukesuke;
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

        new BukkitRunnable() {
            @Override
            public void run() {
                armorPacketManager.sendPacket(player);
            }
        }.runTaskLater(sukesuke, 1L);
    }
}
