package github.tyonakaisan.sukesuke.manager;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.message.Messages;
import github.tyonakaisan.sukesuke.utils.NamespacedKeyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;

import static github.tyonakaisan.sukesuke.utils.ColorUtils.getGradientColor;

@DefaultQualifier(NonNull.class)
public final class ArmorManager {

    private final Messages messages;

    @Inject
    public ArmorManager(
            final Messages messages
    ) {
        this.messages = messages;
    }

    public ItemStack fakeArmorStack(final ItemStack itemStack, final Player player) {
        if (itemStack.isEmpty() || itemStack.getType().equals(Material.ELYTRA) || itemStack.getItemMeta().getPersistentDataContainer().has(NamespacedKeyUtils.fake())) {
            return itemStack;
        }

        final var fakeStack = new ItemStack(buttonMaterial(itemStack));
        fakeStack.editMeta(itemMeta -> {
            itemMeta.setCustomModelData(1);

            final var displayName = this.getDisplayName(itemStack);
            if (!displayName.equals(Component.empty())) {
                itemMeta.displayName(this.messages.translatable(Messages.Style.INFO, player, "armor.hidden_icon").appendSpace().append(displayName));
            }

            itemMeta.lore(
                    List.of(Component.empty(),
                            this.getItemDurability(itemStack, player))
            );

            itemMeta.getPersistentDataContainer().set(NamespacedKeyUtils.fake(), PersistentDataType.BOOLEAN, true);
        });
        return fakeStack.clone();
    }

    private Component getItemDurability(final ItemStack itemStack, final Player player) {
        if (itemStack.getItemMeta() instanceof final Damageable damageMeta) {
            final var max = itemStack.getType().getMaxDurability();
            final var remaining = max - damageMeta.getDamage();
            final double percentage = (double) remaining / max;

            if (Double.isNaN(percentage)) {
                return Component.empty();
            }

            return this.messages.translatable(
                    Messages.Style.INFO,
                    player,
                    "armor.durability",
                    TagResolver.builder()
                            .tag("durability_color", Tag.styling(getGradientColor(percentage, "#00ff00", "#ff0000")))
                            .tag("remaining", Tag.selfClosingInserting(Component.text(remaining)))
                            .tag("max", Tag.selfClosingInserting(Component.text(max)))
                            .build());
        } else {
            return Component.empty();
        }
    }

    private Component getDisplayName(final ItemStack itemStack) {
        final var itemMeta = itemStack.getItemMeta();
        final @Nullable Component displayName = itemMeta.displayName();
        return displayName != null
                ? displayName
                : MiniMessage.miniMessage().deserialize("<!italic><lang:%s>".formatted(itemStack.getType().getItemTranslationKey()));
    }

    private Material buttonMaterial(final ItemStack itemStack) {
        if (itemStack.getItemMeta() instanceof Damageable damageMeta) {
            final var max = itemStack.getType().getMaxDurability();

            if (max == 0) {
                return Material.BIRCH_BUTTON;
            }

            final var percentage = (double) (max - damageMeta.getDamage()) / max * 100;

            if (percentage >= 67) {
                return Material.POLISHED_BLACKSTONE_BUTTON;
            } else if (percentage >= 33) {
                return Material.WARPED_BUTTON;
            } else {
                return Material.MANGROVE_BUTTON;
            }
        } else {
            return Material.BIRCH_BUTTON;
        }
    }

    public ItemStack getArmorOrAir(final int slotId, final PlayerInventory inventory) {
        final @Nullable ItemStack armor = switch (slotId) {
            case 5 -> inventory.getHelmet();
            case 6 -> inventory.getChestplate();
            case 7 -> inventory.getLeggings();
            case 8 -> inventory.getBoots();
            default -> null;
        };

        return armor != null
                ? armor.clone()
                : ItemStack.empty();
    }
}
