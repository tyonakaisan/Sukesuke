package github.tyonakaisan.sukesuke.manager;

import github.tyonakaisan.sukesuke.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.Formatter;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;

import java.util.List;

import static github.tyonakaisan.sukesuke.utils.ColorUtils.getGradientColor;


public class ArmorManager {
    public ItemStack hideArmor(ItemStack itemStack) {
        if (itemStack.getType().equals(Material.AIR)) return itemStack;

        var durability = getItemDurability(itemStack);

        var displayName = MiniMessage.miniMessage().deserialize("<item_name>",
                Placeholder.component("item_name", getDisplayName(itemStack))
        );

        //エリトラの場合はそのまま返す
        if (itemStack.getType().equals(Material.ELYTRA)) {
            return itemStack;
        }
        //それ以外はボタンに
        else {
            itemStack.setType(buttonMaterial(itemStack));
        }

        return ItemBuilder.of(itemStack)
                .customModelData(1)
                .displayName(displayName)
                .lore(List.of(Component.empty(),
                        durability,
                        Component.text()
                                .append(Component.text("非表示中!"))
                                .decoration(TextDecoration.BOLD, true)
                                .decoration(TextDecoration.ITALIC, false)
                                .color(TextColor.color(NamedTextColor.GRAY))
                                .build()))
                .build();
    }

    public Component getItemDurability(ItemStack itemStack) {
        Damageable damageMeta = (Damageable) itemStack.getItemMeta();

        double maxDurability = itemStack.getType().getMaxDurability();
        int currentDurability = (int) (maxDurability - damageMeta.getDamage());
        double percentage = (100 - (damageMeta.getDamage() * 100 / maxDurability)) / 100;

        if (Double.isNaN(percentage)) {
            return Component.empty();
        }

        return MiniMessage.miniMessage().deserialize("<color:white>耐久値 : <durability_color><current></durability_color>/<max></color>",
                TagResolver.resolver("durability_color", Tag.styling(getGradientColor(percentage, "#00ff00", "#ff0000"))),
                Formatter.number("current", currentDurability),
                Formatter.number("max", (int) maxDurability)
                );
    }

    private Component getDisplayName(ItemStack itemStack) {
        if (itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().displayName();
        } else {
            return Component.translatable(itemStack.translationKey(), Component.empty());
        }
    }

    private Material buttonMaterial(ItemStack itemStack) {
        Damageable damageMeta = (Damageable) itemStack.getItemMeta();
        int maxDurability = itemStack.getType().getMaxDurability();
        if (maxDurability == 0) return Material.BIRCH_BUTTON;

        int percentage = 100 - (damageMeta.getDamage() * 100 / maxDurability);

        if (percentage >= 67) {
            return Material.POLISHED_BLACKSTONE_BUTTON;
        }
        else if (percentage >= 33) {
            return Material.WARPED_BUTTON;
        }
        else {
            return Material.MANGROVE_BUTTON;
        }
    }

    public ItemStack getArmor(int i, PlayerInventory inv) {
        switch (i) {
            case 5 -> {
                if(inv.getHelmet() != null) {
                    return inv.getHelmet().clone();
                }
            }
            case 6 -> {
                if(inv.getChestplate() != null) {
                    return inv.getChestplate().clone();
                }
            }
            case 7 -> {
                if(inv.getLeggings() != null) {
                    return inv.getLeggings().clone();
                }
            }
            case 8 -> {
                if(inv.getBoots() != null) {
                    return inv.getBoots().clone();
                }
            }
            default -> {
                return new ItemStack(Material.AIR);
            }
        }
        return new ItemStack(Material.AIR);
    }
}
