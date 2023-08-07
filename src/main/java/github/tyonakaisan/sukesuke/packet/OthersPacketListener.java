package github.tyonakaisan.sukesuke.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import github.tyonakaisan.sukesuke.manager.SukesukeKey;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;
import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class OthersPacketListener{
    private final Sukesuke sukesuke;
    private final SukesukeKey sukesukeKey;
    private final ArmorManager armorManager;

    public OthersPacketListener(
            Sukesuke sukesuke,
            SukesukeKey sukesukeKey,
            ArmorManager armorManager
    ) {
        this.sukesuke = sukesuke;
        this.sukesukeKey = sukesukeKey;
        this.armorManager = armorManager;

        othersPacketListener();
    }

    public void othersPacketListener() {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(sukesuke, PacketType.Play.Server.ENTITY_EQUIPMENT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                var pdc = player.getPersistentDataContainer();
                //もりぱぱっち
                if (!pdc.has(sukesukeKey.toggle())) {
                    sukesukeKey.setHideArmorKeys(player);
                }

                LivingEntity livingEntity = (LivingEntity) protocolManager.getEntityFromID(player.getWorld(), packet.getIntegers().read(0));
                if (!(livingEntity instanceof Player livPlayer)) return;
                var ldc = livPlayer.getPersistentDataContainer();
                //パーミッションチェック
                if (livPlayer.hasPermission("sukesuke.suke")) {
                    //toggle = falseであれば or Creativeモード であれば返す
                    if (Objects.requireNonNull(ldc.get(sukesukeKey.toggle(), PersistentDataType.STRING)).equalsIgnoreCase("false")
                            || livPlayer.getGameMode().equals(GameMode.CREATIVE)) {
                        return;
                    }

                    List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = packet.getSlotStackPairLists().read(0);

                    pairList.stream().filter(OthersPacketListener::isArmorSlot).forEach(slotPair -> {
                        //ヘルメット
                        if (slotPair.getFirst().equals(EnumWrappers.ItemSlot.HEAD) && Objects.requireNonNull(ldc.get(sukesukeKey.helmet(), PersistentDataType.STRING)).equalsIgnoreCase("false")) {
                            slotPair.setSecond(slotPair.getSecond().clone());
                        }
                        //チェスト
                        else if (slotPair.getFirst().equals(EnumWrappers.ItemSlot.CHEST) && Objects.requireNonNull(ldc.get(sukesukeKey.chest(), PersistentDataType.STRING)).equalsIgnoreCase("false")) {
                            slotPair.setSecond(slotPair.getSecond().clone());
                        }
                        //レギンス
                        else if (slotPair.getFirst().equals(EnumWrappers.ItemSlot.LEGS) && Objects.requireNonNull(ldc.get(sukesukeKey.leggings(), PersistentDataType.STRING)).equalsIgnoreCase("false")) {
                            slotPair.setSecond(slotPair.getSecond().clone());
                        }
                        //ブーツ
                        else if (slotPair.getFirst().equals(EnumWrappers.ItemSlot.FEET) && Objects.requireNonNull(ldc.get(sukesukeKey.boots(), PersistentDataType.STRING)).equalsIgnoreCase("false")) {
                            slotPair.setSecond(slotPair.getSecond().clone());
                        }
                        //エリトラ
                        else if (slotPair.getSecond().getType().equals(Material.ELYTRA) && livPlayer.isGliding()) {
                            slotPair.setSecond(armorManager.hideArmor(slotPair.getSecond().clone()));
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