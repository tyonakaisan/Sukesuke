package github.tyonakaisan.sukesuke.manager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.List;


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

        //エリトラの場合はそのまま返す
        if (itemStack.getType().equals(Material.ELYTRA)) {
            return itemStack;
        }
        //それ以外はボタンに
        else {
            itemStack.setType(Material.CONDUIT);
        }
        //おまけのカスタムモデルデータ
        itemMeta.setCustomModelData(1);

        itemStack.setItemMeta(itemMeta);

        System.out.println(itemMeta);
        System.out.println(itemStack.setItemMeta(itemMeta));
        System.out.println(itemStack);
        return itemStack;
    }

    public Component getItemDurability(ItemStack itemStack) {
        Damageable damageMeta = (Damageable) itemStack.getItemMeta();

        double MaxDurability = itemStack.getType().getMaxDurability();
        double currentDurability = MaxDurability - damageMeta.getDamage();
        double percentage = (100 - (damageMeta.getDamage() * 100 / MaxDurability)) / 100;

        if (Double.isNaN(percentage)) {
            return Component.text().build();
        }

        return Component.text()
                .append(Component.text("耐久値 : "))
                .append(Component.text()
                        .content(String.valueOf(currentDurability))
                        .color(TextColor.fromCSSHexString(getGradientColor(percentage, "#56ab2f", "#dd3e54")))
                        .build())
                .append(Component.text("/"))
                .append(Component.text(MaxDurability))
                .decoration(TextDecoration.ITALIC, false)
                .color(TextColor.color(NamedTextColor.WHITE))
                .build();
    }

    public static String getGradientColor(double percentage, String startColor, String endColor) {
        Color start = Color.decode(startColor);
        Color end = Color.decode(endColor);

        int red = (int) (end.getRed() + percentage * (start.getRed() - end.getRed()));
        int green = (int) (end.getGreen() + percentage * (start.getGreen() - end.getGreen()));
        int blue = (int) (end.getBlue() + percentage * (start.getBlue() - end.getBlue()));

        return String.format("#%02x%02x%02x", red, green, blue);
    }

    public Material ButtonMaterial(ItemStack itemStack) {
        Damageable damageMeta = (Damageable) itemStack.getItemMeta();
        int MaxDurability = itemStack.getType().getMaxDurability();
        if (MaxDurability == 0) return Material.BIRCH_BUTTON;

        int percentage = 100 - (damageMeta.getDamage() * 100 / MaxDurability);

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
        }
        return new ItemStack(Material.AIR);
    }
}
