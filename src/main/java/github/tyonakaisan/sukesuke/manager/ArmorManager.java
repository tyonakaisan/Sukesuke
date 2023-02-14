package github.tyonakaisan.sukesuke.manager;

import broccolai.corn.paper.item.PaperItemBuilder;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.google.common.collect.Multimap;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.util.ProtocolUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.units.qual.C;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArmorManager {
    private final Sukesuke plugin;
    private final ProtocolManager protocolManager;

    public ArmorManager(Sukesuke pl, ProtocolManager pm) {
        this.plugin = pl;
        this.protocolManager = pm;
    }

    public void sendPacket(Player player) {
        SelfPacket(player);
        OthersPacket(player);
    }

    public void SelfPacket(Player player) {
        PlayerInventory inv = player.getInventory();

        for (int i = 5; i <= 8; i++) {
            PacketContainer packetSelf = protocolManager.createPacket(PacketType.Play.Server.SET_SLOT);
            packetSelf.getIntegers().write(0, 0);
            packetSelf.getIntegers().write(2, i);

            ItemStack armor = ProtocolUtils.getArmor(ProtocolUtils.ArmorType.getType(i), inv);
            packetSelf.getItemModifier().write(0, armor);

            try {
                protocolManager.sendServerPacket(player, packetSelf);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void OthersPacket(Player player) {
        PlayerInventory inv = player.getInventory();

        PacketContainer packetOthers = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packetOthers.getIntegers().write(0, player.getEntityId());

        List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = packetOthers.getSlotStackPairLists().read(0);
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, ProtocolUtils.getArmor(ProtocolUtils.ArmorType.HELMET, inv)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, ProtocolUtils.getArmor(ProtocolUtils.ArmorType.CHEST, inv)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, ProtocolUtils.getArmor(ProtocolUtils.ArmorType.LEGGS, inv)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.FEET, ProtocolUtils.getArmor(ProtocolUtils.ArmorType.BOOTS, inv)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, player.getInventory().getItemInMainHand().clone()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, player.getInventory().getItemInOffHand().clone()));

        packetOthers.getSlotStackPairLists().write(0, pairList);
        ProtocolUtils.broadcastPlayerPacket(protocolManager, packetOthers, player);
    }

    public ItemStack HideArmor(ItemStack itemStack) {
        if (itemStack.getType().equals(Material.AIR)) return itemStack;

        // Getting item meta and lore
        ItemMeta itemMeta = itemStack.getItemMeta().clone();

        // Adding item durability percentage to lore, if it has it
        itemMeta.lore(List.of(getItemDurability(itemStack)));

        // ArmoredElytra mod compatibility
        if (itemStack.getType().equals(Material.ELYTRA)) {
            itemMeta = hideElytra(itemStack);
        }

        // Changing armor material and name to its placeholder's, if it has one
        itemStack.setType(Material.STONE_BUTTON);

        // Applying item meta and lore
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

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
        for(Enchantment key : encs.keySet()){
            itemMeta.addEnchant(key, encs.get(key), true);
        }

        // Applying stored attributes to the new elytra
        itemMeta.setAttributeModifiers(attrs);

        // Applying stored damage to the new elytra
        ((org.bukkit.inventory.meta.Damageable) itemMeta).setDamage(damage);

        return itemMeta;
    }

    private Component getItemDurability(ItemStack itemStack){
        //int percentage = getDurabilityPer(itemStack);
        Damageable dmeta = (Damageable) itemStack.getItemMeta();
        int Damage = itemStack.getType().getMaxDurability();
        int mDamage = Damage - dmeta.getDamage();
        return Component.text()
                .append(Component.text("耐久値 : "))
                .append(Component.text(mDamage))
                .append(Component.text("/"))
                .append(Component.text(Damage))
                .decoration(TextDecoration.ITALIC, false)
                .color(TextColor.color(NamedTextColor.WHITE))
                .build();
    }
}
