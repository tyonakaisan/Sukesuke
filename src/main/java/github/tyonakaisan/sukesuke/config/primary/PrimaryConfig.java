package github.tyonakaisan.sukesuke.config.primary;

import org.bukkit.Material;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
@DefaultQualifier(NonNull.class)
@SuppressWarnings("FieldMayBeFinal")
public final class PrimaryConfig {

    private final List<Material> notAllowedArmors = List.of();

    public List<Material> notAllowedArmors() {
        return this.notAllowedArmors;
    }
}
