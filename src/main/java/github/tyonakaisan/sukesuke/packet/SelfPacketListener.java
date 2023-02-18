package github.tyonakaisan.sukesuke.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class SelfPacketListener {

    public SelfPacketListener(Sukesuke plugin, ProtocolManager protocolManager, ArmorPacketManager armorPacketManager) {
        PacketAdapter.AdapterParameteters params = PacketAdapter.params().plugin(plugin)
                .listenerPriority(ListenerPriority.HIGH)
                .types(PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS);

        protocolManager.addPacketListener(new PacketAdapter(params) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                //self_toggle = trueでなければ or Creativeモード であれば返す
                if (player.getPersistentDataContainer().get(new NamespacedKey(plugin, "self_toggle"), PersistentDataType.STRING).equalsIgnoreCase("false")
                        || player.getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }

                //SET_SLOT
                //アイテムを選択した時？
                //ダメージを受けて耐久が減った時も？

                if (packet.getType().equals(PacketType.Play.Server.SET_SLOT) && packet.getIntegers().read(0).equals(0)) {
                    switch (packet.getIntegers().read(2)) {
                        case 5 -> {
                            ItemStack itemStack = packet.getItemModifier().read(0);
                            //false(表示)だったら
                            if (player.getPersistentDataContainer().get(new NamespacedKey(plugin, "helmet"), PersistentDataType.STRING).equalsIgnoreCase("false")) {
                                packet.getItemModifier().write(0, itemStack);
                            }
                            //true(非表示)だったら
                            else {
                                packet.getItemModifier().write(0, armorPacketManager.HideArmor(itemStack, player));
                            }
                        }
                        case 6 -> {
                            ItemStack itemStack = packet.getItemModifier().read(0);
                            //false(表示)だったら
                            if (player.getPersistentDataContainer().get(new NamespacedKey(plugin, "chest"), PersistentDataType.STRING).equalsIgnoreCase("false")) {
                                packet.getItemModifier().write(0, itemStack);
                            }
                            //true(非表示)だったら
                            else {
                                packet.getItemModifier().write(0, armorPacketManager.HideArmor(itemStack, player));
                            }
                        }
                        case 7 -> {
                            ItemStack itemStack = packet.getItemModifier().read(0);
                            //false(表示)だったら
                            if (player.getPersistentDataContainer().get(new NamespacedKey(plugin, "leggings"), PersistentDataType.STRING).equalsIgnoreCase("false")) {
                                packet.getItemModifier().write(0, itemStack);
                            }
                            //true(非表示)だったら
                            else {
                                packet.getItemModifier().write(0, armorPacketManager.HideArmor(itemStack, player));
                            }
                        }
                        case 8 -> {
                            ItemStack itemStack = packet.getItemModifier().read(0);
                            //false(表示)だったら
                            if (player.getPersistentDataContainer().get(new NamespacedKey(plugin, "boots"), PersistentDataType.STRING).equalsIgnoreCase("false")) {
                                packet.getItemModifier().write(0, itemStack);
                            }
                            //true(非表示)だったら
                            else {
                                packet.getItemModifier().write(0, armorPacketManager.HideArmor(itemStack, player));
                            }
                        }
                    }
                }

                /*
                if (packet.getType().equals(PacketType.Play.Server.SET_SLOT) && packet.getIntegers().read(0).equals(0)) {
                    if (packet.getIntegers().read(0) == 5) {
                        if (player.getPersistentDataContainer().get(new NamespacedKey(plugin, "helmet"), PersistentDataType.STRING).equalsIgnoreCase("false")) {
                            ItemStack itemStack = packet.getItemModifier().read(0);
                            packet.getItemModifier().write(0, itemStack);
                        }
                    }
                    if ()
                }

                 */

                //WINDOW_ITEMS
                //アイテムが変わった時？

                if (packet.getType().equals(PacketType.Play.Server.WINDOW_ITEMS) && packet.getIntegers().read(0).equals(0)) {
                    List<ItemStack> itemStacks = packet.getItemListModifier().read(0);

                    if (!player.getPersistentDataContainer().get(new NamespacedKey(plugin, "helmet"), PersistentDataType.STRING).equalsIgnoreCase("false")) {
                        ItemStack armor = itemStacks.get(5);
                        armor.setItemMeta(armorPacketManager.HideArmor(armor, player).getItemMeta());
                    }
                    else if (!player.getPersistentDataContainer().get(new NamespacedKey(plugin, "chest"), PersistentDataType.STRING).equalsIgnoreCase("false")) {
                        ItemStack armor = itemStacks.get(6);
                        armor.setItemMeta(armorPacketManager.HideArmor(armor, player).getItemMeta());
                    }
                    else if (!player.getPersistentDataContainer().get(new NamespacedKey(plugin, "leggings"), PersistentDataType.STRING).equalsIgnoreCase("false")) {
                        ItemStack armor = itemStacks.get(7);
                        armor.setItemMeta(armorPacketManager.HideArmor(armor, player).getItemMeta());
                    }
                    else if (!player.getPersistentDataContainer().get(new NamespacedKey(plugin, "boots"), PersistentDataType.STRING).equalsIgnoreCase("false")) {
                        ItemStack armor = itemStacks.get(8);
                        armor.setItemMeta(armorPacketManager.HideArmor(armor, player).getItemMeta());
                    }
                    //    itemStacks.stream().skip(5).limit(4).forEach(item -> {
                    //    if (item != null) {
                    //        item.setItemMeta(armorManager.HideArmor(item, player).getItemMeta());
                    //    }
                    //});
                }
            }
        });

    }
}
