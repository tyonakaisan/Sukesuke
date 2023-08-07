package github.tyonakaisan.sukesuke.manager;

import github.tyonakaisan.sukesuke.Sukesuke;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class Keys {

    private static final Sukesuke sukesuke = Sukesuke.getPlugin();

    public static final NamespacedKey HelmetKey = new NamespacedKey(sukesuke, "helmet");
    public static final NamespacedKey ChestKey = new NamespacedKey(sukesuke, "chest");
    public static final NamespacedKey LeggingsKey = new NamespacedKey(sukesuke, "leggings");
    public static final NamespacedKey BootsKey = new NamespacedKey(sukesuke, "boots");
    public static final NamespacedKey ToggleKey = new NamespacedKey(sukesuke, "toggle");

    public static void setHideArmorKey(Player player) {
        var pdc = player.getPersistentDataContainer();

        if (!pdc.has(HelmetKey)) {
            pdc.set(HelmetKey, PersistentDataType.STRING, "false");
        }
        if (!pdc.has(ChestKey)) {
            pdc.set(ChestKey, PersistentDataType.STRING, "false");
        }
        if (!pdc.has(LeggingsKey)) {
            pdc.set(LeggingsKey, PersistentDataType.STRING, "false");
        }
        if (!pdc.has(BootsKey)) {
            pdc.set(BootsKey, PersistentDataType.STRING, "false");
        }
        if (!pdc.has(ToggleKey)) {
            pdc.set(ToggleKey, PersistentDataType.STRING, "false");
        }
    }

    public static void setToggleArmorType(Player player, String key) {
        var pdc = player.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(sukesuke, key);

        //trueであれば
        if (Objects.requireNonNull(pdc.get(namespacedKey, PersistentDataType.STRING)).equalsIgnoreCase("true")) {
            //削除->追加
            pdc.remove(namespacedKey);
            pdc.set(namespacedKey, PersistentDataType.STRING, "false");
        } else {
            //削除->追加
            pdc.remove(namespacedKey);
            pdc.set(namespacedKey, PersistentDataType.STRING, "true");
        }

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 2F);
    }
}
