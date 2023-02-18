package github.tyonakaisan.sukesuke.player;

import github.tyonakaisan.sukesuke.Sukesuke;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class PlayerSetKey {
    private final Sukesuke plugin;

    public PlayerSetKey(Sukesuke pl) {
        this.plugin = pl;
    }

    public void setHideArmorKey(Player player) {
        if (!player.getPersistentDataContainer().has(new NamespacedKey(plugin, "helmet"))) {
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "helmet"), PersistentDataType.STRING, "false");
        }
        if (!player.getPersistentDataContainer().has(new NamespacedKey(plugin, "chest"))) {
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "chest"), PersistentDataType.STRING, "false");
        }
        if (!player.getPersistentDataContainer().has(new NamespacedKey(plugin, "leggings"))) {
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "leggings"), PersistentDataType.STRING, "false");
        }
        if (!player.getPersistentDataContainer().has(new NamespacedKey(plugin, "boots"))) {
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "boots"), PersistentDataType.STRING, "false");
        }
        if (!player.getPersistentDataContainer().has(new NamespacedKey(plugin, "self_toggle"))) {
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "self_toggle"), PersistentDataType.STRING, "false");
        }
        if (!player.getPersistentDataContainer().has(new NamespacedKey(plugin, "others_toggle"))) {
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, "others_toggle"), PersistentDataType.STRING, "false");
        }
    }

    public void setToggleArmorType(Player player, String key) {
        //trueであれば
        if (player.getPersistentDataContainer().get(new NamespacedKey(plugin, key), PersistentDataType.STRING).equalsIgnoreCase("true")) {
            //削除->追加
            player.getPersistentDataContainer().remove(new NamespacedKey(plugin, key));
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, "false");
        } else {
            //削除->追加
            player.getPersistentDataContainer().remove(new NamespacedKey(plugin, key));
            player.getPersistentDataContainer().set(new NamespacedKey(plugin, key), PersistentDataType.STRING, "true");
        }
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 2F);
    }
}
