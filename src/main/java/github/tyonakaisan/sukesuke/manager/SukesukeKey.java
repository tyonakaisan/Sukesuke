package github.tyonakaisan.sukesuke.manager;

import com.google.inject.Inject;
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
public final class SukesukeKey {

    private final Sukesuke sukesuke;
    @Inject
    public SukesukeKey(
            final Sukesuke sukesuke
    ) {
        this.sukesuke = sukesuke;
    }

    public NamespacedKey helmet() {
        return new NamespacedKey(sukesuke, "helmet");
    }

    public NamespacedKey chest() {
        return new NamespacedKey(sukesuke, "chest");
    }

    public NamespacedKey leggings() {
        return new NamespacedKey(sukesuke, "leggings");
    }

    public NamespacedKey boots() {
        return new NamespacedKey(sukesuke, "boots");
    }

    public NamespacedKey toggle() {
        return new NamespacedKey(sukesuke, "toggle");
    }

    public void setHideArmorKeys(Player player) {
        var pdc = player.getPersistentDataContainer();

        if (!pdc.has(helmet())) {
            pdc.set(helmet(), PersistentDataType.STRING, "false");
        }
        if (!pdc.has(chest())) {
            pdc.set(chest(), PersistentDataType.STRING, "false");
        }
        if (!pdc.has(leggings())) {
            pdc.set(leggings(), PersistentDataType.STRING, "false");
        }
        if (!pdc.has(boots())) {
            pdc.set(boots(), PersistentDataType.STRING, "false");
        }
        if (!pdc.has(toggle())) {
            pdc.set(toggle(), PersistentDataType.STRING, "false");
        }
    }

    public void setToggleArmorType(Player player, NamespacedKey key) {
        var pdc = player.getPersistentDataContainer();

        //trueであれば
        if (Objects.requireNonNull(pdc.get(key, PersistentDataType.STRING)).equalsIgnoreCase("true")) {
            //削除->追加
            pdc.remove(key);
            pdc.set(key, PersistentDataType.STRING, "false");
        } else {
            //削除->追加
            pdc.remove(key);
            pdc.set(key, PersistentDataType.STRING, "true");
        }

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 2F);
    }
}
