package github.tyonakaisan.sukesuke.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class NamespacedKeyUtils {

    private static final String NAMESPACE = "sukesuke";
    private static final NamespacedKey HELMET = new NamespacedKey(NAMESPACE, "helmet");
    private static final NamespacedKey CHEST = new NamespacedKey(NAMESPACE, "chest");
    private static final NamespacedKey LEGGINGS = new NamespacedKey(NAMESPACE, "leggings");
    private static final NamespacedKey BOOTS = new NamespacedKey(NAMESPACE, "boots");
    private static final NamespacedKey DISPLAY = new NamespacedKey(NAMESPACE, "display");
    private static final NamespacedKey FAKE = new NamespacedKey(NAMESPACE, "fake");

    public static String namespace() {
        return NAMESPACE;
    }

    public static NamespacedKey helmet() {
        return HELMET;
    }

    public static NamespacedKey chest() {
        return CHEST;
    }

    public static NamespacedKey leggings() {
        return LEGGINGS;
    }

    public static NamespacedKey boots() {
        return BOOTS;
    }

    public static NamespacedKey display() {
        return DISPLAY;
    }

    public static NamespacedKey fake() {
        return FAKE;
    }

    public static boolean isValueTrue(final Player player, final NamespacedKey namespacedKey) {
        LegacyUpdater.playerPDCUpdateIfNeeded(player, namespacedKey);
        final var pdc = player.getPersistentDataContainer();

        // if pdc is not present return false
        if (!pdc.has(namespacedKey, PersistentDataType.BOOLEAN)) {
            return false;
        }

        return Boolean.TRUE.equals(Objects.requireNonNull(pdc.get(namespacedKey, PersistentDataType.BOOLEAN)));
    }

    public static void initKeys(final Player player) {
        var pdc = player.getPersistentDataContainer();

        if (!pdc.has(helmet())) {
            pdc.set(helmet(), PersistentDataType.BOOLEAN, true);
        }
        if (!pdc.has(chest())) {
            pdc.set(chest(), PersistentDataType.BOOLEAN, true);
        }
        if (!pdc.has(leggings())) {
            pdc.set(leggings(), PersistentDataType.BOOLEAN, true);
        }
        if (!pdc.has(boots())) {
            pdc.set(boots(), PersistentDataType.BOOLEAN, true);
        }
        if (!pdc.has(display())) {
            pdc.set(display(), PersistentDataType.BOOLEAN, true);
        }
    }

    public static void toggleKeyValue(final Player player, final NamespacedKey namespacedKey) {
        LegacyUpdater.playerPDCUpdateIfNeeded(player, namespacedKey);
        final var pdc = player.getPersistentDataContainer();

        if (!pdc.has(namespacedKey)) {
            return;
        }

        pdc.set(namespacedKey, PersistentDataType.BOOLEAN, !Objects.requireNonNull(pdc.get(namespacedKey, PersistentDataType.BOOLEAN)));
    }
}
