package github.tyonakaisan.sukesuke.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;

public class SelfPacketListener {

    public SelfPacketListener(Sukesuke sukesuke, ProtocolManager protocolManager, ArmorManager armorManager) {
        PacketAdapter.AdapterParameteters params = PacketAdapter.params().plugin(sukesuke)
                .listenerPriority(ListenerPriority.HIGH)
                .types(PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS);

        protocolManager.addPacketListener(new PacketAdapter(params) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                var pdc = player.getPersistentDataContainer();
                //もりぱぱっち
                if (!pdc.has(Keys.ToggleKey)) {
                    Keys.setHideArmorKey(player);
                }

                //パーミッションチェック
                if (player.hasPermission("sukesuke.suke")) {
                    //self_toggle = falseであれば or Creativeモード であれば返す
                    if (Objects.requireNonNull(pdc.get(Keys.ToggleKey, PersistentDataType.STRING)).equalsIgnoreCase("false")
                            || player.getGameMode().equals(GameMode.CREATIVE)) return;

                    //SET_SLOT
                    if (packet.getType().equals(PacketType.Play.Server.SET_SLOT) && packet.getIntegers().read(0).equals(0) && packet.getIntegers().read(2) > 4 && packet.getIntegers().read(2) < 9) {
                        switch (packet.getIntegers().read(2)) {
                            case 5 -> {
                                ItemStack itemStack = packet.getItemModifier().read(0);
                                System.out.println(itemStack);

                                //false(表示)だったら
                                if (Objects.requireNonNull(pdc.get(Keys.HelmetKey, PersistentDataType.STRING)).equalsIgnoreCase("false")) {
                                    packet.getItemModifier().write(0, itemStack);
                                }

                                //true(非表示)だったら
                                else {
                                    packet.getItemModifier().write(0, armorManager.HideArmor(itemStack));
                                }
                            }
                            case 6 -> {
                                ItemStack itemStack = packet.getItemModifier().read(0);

                                //false(表示)だったら
                                if (Objects.requireNonNull(pdc.get(Keys.ChestKey, PersistentDataType.STRING)).equalsIgnoreCase("false")) {
                                    packet.getItemModifier().write(0, itemStack);
                                }

                                //true(非表示)だったら
                                else {
                                    packet.getItemModifier().write(0, armorManager.HideArmor(itemStack));
                                }
                            }
                            case 7 -> {
                                ItemStack itemStack = packet.getItemModifier().read(0);

                                //false(表示)だったら
                                if (Objects.requireNonNull(pdc.get(Keys.LeggingsKey, PersistentDataType.STRING)).equalsIgnoreCase("false")) {
                                    packet.getItemModifier().write(0, itemStack);
                                }

                                //true(非表示)だったら
                                else {
                                    packet.getItemModifier().write(0, armorManager.HideArmor(itemStack));
                                }
                            }
                            case 8 -> {
                                ItemStack itemStack = packet.getItemModifier().read(0);

                                //false(表示)だったら
                                if (Objects.requireNonNull(pdc.get(Keys.BootsKey, PersistentDataType.STRING)).equalsIgnoreCase("false")) {
                                    packet.getItemModifier().write(0, itemStack);
                                }

                                //true(非表示)だったら
                                else {
                                    packet.getItemModifier().write(0, armorManager.HideArmor(itemStack));
                                }
                            }
                        }
                    }
                    //WINDOW_ITEMS
                    if (packet.getType().equals(PacketType.Play.Server.WINDOW_ITEMS) && packet.getIntegers().read(0).equals(0)) {
                        List<ItemStack> itemStacks = packet.getItemListModifier().read(0);

                        if (Objects.requireNonNull(pdc.get(Keys.HelmetKey, PersistentDataType.STRING)).equalsIgnoreCase("true")) {
                            ItemStack armor = itemStacks.get(5);
                            armor.setItemMeta(armorManager.HideArmor(armor).getItemMeta());
                        }
                        if (Objects.requireNonNull(pdc.get(Keys.ChestKey, PersistentDataType.STRING)).equalsIgnoreCase("true")) {
                            ItemStack armor = itemStacks.get(6);
                            armor.setItemMeta(armorManager.HideArmor(armor).getItemMeta());
                        }
                        if (Objects.requireNonNull(pdc.get(Keys.LeggingsKey, PersistentDataType.STRING)).equalsIgnoreCase("true")) {
                            ItemStack armor = itemStacks.get(7);
                            armor.setItemMeta(armorManager.HideArmor(armor).getItemMeta());
                        }
                        if (Objects.requireNonNull(pdc.get(Keys.BootsKey, PersistentDataType.STRING)).equalsIgnoreCase("true")) {
                            ItemStack armor = itemStacks.get(8);
                            armor.setItemMeta(armorManager.HideArmor(armor).getItemMeta());
                        }
                    }

                }
            }
        });

    }
}
