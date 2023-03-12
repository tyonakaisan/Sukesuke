package github.tyonakaisan.sukesuke.manager;

import com.google.common.collect.Multimap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class ArmorManager {
    public ItemStack HideArmor(ItemStack itemStack) {
        if (itemStack.getType().equals(Material.AIR)) return itemStack;

        //meta
        ItemMeta itemMeta = itemStack.getItemMeta().clone();

        //アイテム名
        itemMeta.displayName(Component.text()
                .append(Component.text("すけすけ"))
                .decoration(TextDecoration.BOLD, true)
                .decoration(TextDecoration.ITALIC, false)
                .color(TextColor.fromCSSHexString("#00fa9a"))
                .build());

        //説明ぶん
        itemMeta.lore(List.of(Component.text().build(),
                getItemDurability(itemStack),
                Component.text().build(),
                Component.text()
                        .append(Component.text("非表示中!"))
                        .decoration(TextDecoration.BOLD, true)
                        .decoration(TextDecoration.ITALIC, false)
                        .color(TextColor.color(NamedTextColor.GRAY))
                        .build()));

        //エリトラの場合
        if (itemStack.getType().equals(Material.ELYTRA)) {
            itemMeta = hideElytra(itemStack);
        }
        else {
            itemStack.setType(ButtonMaterial(itemStack));
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    //そのまま
    public ItemMeta hideElytra(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();

        // Storing the elytra current enchantments, attributes and damage
        Map<Enchantment, Integer> encs = itemMeta.getEnchants();
        Multimap<Attribute, AttributeModifier> attrs = itemMeta.getAttributeModifiers();
        int damage = ((org.bukkit.inventory.meta.Damageable) itemMeta).getDamage();

        itemStack = new ItemStack(Material.ELYTRA);

        // Getting item meta from the new elytra
        itemMeta = itemStack.getItemMeta();

        // Applying stored enchantments to the new elytra
        for (Enchantment key : encs.keySet()) {
            itemMeta.addEnchant(key, encs.get(key), true);
        }

        // Applying stored attributes to the new elytra
        itemMeta.setAttributeModifiers(attrs);

        // Applying stored damage to the new elytra
        ((org.bukkit.inventory.meta.Damageable) itemMeta).setDamage(damage);

        return itemMeta;
    }

    public Component getItemDurability(ItemStack itemStack) {
        Damageable damageMeta = (Damageable) itemStack.getItemMeta();
        int MaxDurability = itemStack.getType().getMaxDurability();
        int currentDurability = MaxDurability - damageMeta.getDamage();

        return Component.text()
                .append(Component.text("耐久値 : "))
                .append(Component.text(currentDurability))
                .append(Component.text("/"))
                .append(Component.text(MaxDurability))
                .decoration(TextDecoration.ITALIC, false)
                .color(TextColor.color(NamedTextColor.WHITE))
                .build();
    }

    public Material ButtonMaterial(ItemStack itemStack) {
        Damageable damageMeta = (Damageable) itemStack.getItemMeta();
        int MaxDurability = itemStack.getType().getMaxDurability();
        if (MaxDurability == 0) return Material.BIRCH_BUTTON;

        int percentage = 100 - (damageMeta.getDamage() * 100 / MaxDurability);

        if (percentage > 67) {
            return Material.POLISHED_BLACKSTONE_BUTTON;
        }
        if (percentage <= 66 && percentage >= 33) {
            return Material.WARPED_BUTTON;
        }
        if (percentage <= 32) {
            return Material.MANGROVE_BUTTON;
        }
        return null;
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
        }
        return new ItemStack(Material.AIR);
    }
}
