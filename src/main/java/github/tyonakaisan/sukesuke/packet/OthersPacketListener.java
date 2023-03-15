package github.tyonakaisan.sukesuke.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class OthersPacketListener {

    public OthersPacketListener(Sukesuke sukesuke, ProtocolManager protocolManager, ArmorManager armorManager) {
        protocolManager.addPacketListener(new PacketAdapter(sukesuke, PacketType.Play.Server.ENTITY_EQUIPMENT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();

                LivingEntity livingEntity = (LivingEntity) protocolManager.getEntityFromID(player.getWorld(), packet.getIntegers().read(0));
                if (!(livingEntity instanceof Player)) return;
                Player livPlayer = (Player) livingEntity;

                //パーミッションチェック
                if (livPlayer.hasPermission("sukesuke.suke")) {
                    var pdc = livPlayer.getPersistentDataContainer();
                    //self_toggle = falseであれば or Creativeモード であれば返す
                    if (pdc.get(Keys.ToggleKey, PersistentDataType.STRING).equalsIgnoreCase("false")
                            || livPlayer.getGameMode().equals(GameMode.CREATIVE)) {
                        return;
                    }

                    List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = packet.getSlotStackPairLists().read(0);

                    pairList.stream().filter(OthersPacketListener::isArmorSlot).forEach(slotPair -> {
                        //ヘルメット
                        if (slotPair.getFirst().equals(EnumWrappers.ItemSlot.HEAD) && pdc.get(Keys.HelmetKey, PersistentDataType.STRING).equalsIgnoreCase("false")) {
                            slotPair.setSecond(slotPair.getSecond().clone());
                        }
                        //チェスト
                        else if (slotPair.getFirst().equals(EnumWrappers.ItemSlot.CHEST) && pdc.get(Keys.ChestKey, PersistentDataType.STRING).equalsIgnoreCase("false")) {
                            slotPair.setSecond(slotPair.getSecond().clone());
                        }
                        //レギンス
                        else if (slotPair.getFirst().equals(EnumWrappers.ItemSlot.LEGS) && pdc.get(Keys.LeggingsKey, PersistentDataType.STRING).equalsIgnoreCase("false")) {
                            slotPair.setSecond(slotPair.getSecond().clone());
                        }
                        //ブーツ
                        else if (slotPair.getFirst().equals(EnumWrappers.ItemSlot.FEET) && pdc.get(Keys.BootsKey, PersistentDataType.STRING).equalsIgnoreCase("false")) {
                            slotPair.setSecond(slotPair.getSecond().clone());
                        }
                        //エリトラ
                        else if (slotPair.getSecond().getType().equals(Material.ELYTRA) && livPlayer.isGliding()) {
                            slotPair.setSecond(armorManager.HideArmor(slotPair.getSecond().clone()));
                        }
                        //透明
                        else {
                            slotPair.setSecond(new ItemStack(Material.AIR));
                        }

                    });
                    packet.getSlotStackPairLists().write(0, pairList);
                }
            }
        });
    }

    private static boolean isArmorSlot(Pair<EnumWrappers.ItemSlot, ItemStack> pair){
        return pair.getFirst().equals(EnumWrappers.ItemSlot.FEET) ||
                pair.getFirst().equals(EnumWrappers.ItemSlot.LEGS) ||
                pair.getFirst().equals(EnumWrappers.ItemSlot.CHEST) ||
                pair.getFirst().equals(EnumWrappers.ItemSlot.HEAD);
    }
}