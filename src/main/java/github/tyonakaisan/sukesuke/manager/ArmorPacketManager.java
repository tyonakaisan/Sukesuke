package github.tyonakaisan.sukesuke.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;

public class ArmorPacketManager {
    private final ArmorManager armorManager;

    @Inject
    public ArmorPacketManager(
            ArmorManager armorManager
    ) {
        this.armorManager = armorManager;
    }

    public void sendPacket(Player player) {
        SelfPacket(player);
        OthersPacket(player);
    }

    public void SelfPacket(Player player) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        PlayerInventory inventory = player.getInventory();

        for (int i = 5; i <= 8; i++) {
            PacketContainer packetSelf = protocolManager.createPacket(PacketType.Play.Server.SET_SLOT);
            packetSelf.getIntegers().write(0, 0);
            packetSelf.getIntegers().write(2, i);

            ItemStack armor = armorManager.getArmor(i, inventory);
            packetSelf.getItemModifier().write(0, armor);

            protocolManager.sendServerPacket(player, packetSelf);
        }
    }

    public void OthersPacket(Player player) {
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        PlayerInventory inv = player.getInventory();

        PacketContainer packetOthers = protocolManager.createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);
        packetOthers.getIntegers().write(0, player.getEntityId());

        List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairList = packetOthers.getSlotStackPairLists().read(0);
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, armorManager.getArmor(5, inv)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, armorManager.getArmor(6, inv)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, armorManager.getArmor(7, inv)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.FEET, armorManager.getArmor(8, inv)));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, player.getInventory().getItemInMainHand().clone()));
        pairList.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, player.getInventory().getItemInOffHand().clone()));

        packetOthers.getSlotStackPairLists().write(0, pairList);
        broadcastPlayerPacket(protocolManager, packetOthers, player);
    }

    private static void broadcastPlayerPacket(ProtocolManager manager, PacketContainer packet, Player player) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getWorld().equals(player.getWorld()) && !p.equals(player)) {
                manager.sendServerPacket(p, packet);
            }
        }
    }
}
