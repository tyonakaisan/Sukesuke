package github.tyonakaisan.sukesuke.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class LegacyUpdater {

    private static final NamespacedKey LEGACY_DISPLAY_KEY = new NamespacedKey(NamespacedKeyUtils.namespace(), "toggle");

    private LegacyUpdater() {
    }

    public static void playerPDCUpdateIfNeeded(final Player player, final NamespacedKey namespacedKey) {
        final var pdc = player.getPersistentDataContainer();

        if (pdc.has(LEGACY_DISPLAY_KEY)) {
            final var boolString = Objects.requireNonNull(pdc.get(LEGACY_DISPLAY_KEY, PersistentDataType.STRING));
            final var bool = Boolean.parseBoolean(boolString);

            pdc.remove(LEGACY_DISPLAY_KEY);
            pdc.set(NamespacedKeyUtils.display(), PersistentDataType.BOOLEAN, bool);
        }

        if (pdc.has(namespacedKey, PersistentDataType.STRING)) {
            final var boolString = Objects.requireNonNull(pdc.get(namespacedKey, PersistentDataType.STRING));
            final var bool = Boolean.parseBoolean(boolString);

            pdc.set(namespacedKey, PersistentDataType.BOOLEAN, bool);
        }
    }
}
