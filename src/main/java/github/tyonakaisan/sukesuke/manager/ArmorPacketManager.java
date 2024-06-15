package github.tyonakaisan.sukesuke.manager;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.utils.NamespacedKeyUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@DefaultQualifier(NonNull.class)
public final class ArmorPacketManager {

    private final ArmorManager armorManager;
    private final Server server;

    @Inject
    public ArmorPacketManager(
            final ArmorManager armorManager,
            final Server server
    ) {
        this.armorManager = armorManager;
        this.server = server;
    }

    public void sendPacket(final Player player) {
        this.selfPacket(player);
        this.othersPacket(player);
    }

    public void selfPacket(final Player player) {
        final Map<Integer, NamespacedKey> armorSlots = Map.of(
                5, NamespacedKeyUtils.helmet(),
                6, NamespacedKeyUtils.chest(),
                7, NamespacedKeyUtils.leggings(),
                8, NamespacedKeyUtils.boots()
        );
        final List<PacketContainer> packets = new ArrayList<>();

        for (int i = 5; i <= 8; i++) {
            final var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SET_SLOT);
            packet.getIntegers().write(0, 0);
            packet.getIntegers().write(2, i);

            final var armor = this.armorManager.getArmorOrAir(i, player.getInventory());
            final var test = NamespacedKeyUtils.isValueTrue(player, NamespacedKeyUtils.display()) && NamespacedKeyUtils.isValueTrue(player, armorSlots.get(i))
                    ? this.armorManager.fakeArmorStack(armor, player)
                    : armor;

            packet.getItemModifier().write(0, test);

            packets.add(packet);
        }

        packets.forEach(packet -> ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet));
    }

    public void othersPacket(final Player player) {
        final var inventory = player.getInventory();
        final var packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_EQUIPMENT);

        packet.getIntegers().write(0, player.getEntityId());

        final List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairs = new ArrayList<>(); // packet.getSlotStackPairLists().read(0);

        pairs.add(new Pair<>(EnumWrappers.ItemSlot.HEAD, this.armorManager.getArmorOrAir(5, inventory)));
        pairs.add(new Pair<>(EnumWrappers.ItemSlot.CHEST, this.armorManager.getArmorOrAir(6, inventory)));
        pairs.add(new Pair<>(EnumWrappers.ItemSlot.LEGS, this.armorManager.getArmorOrAir(7, inventory)));
        pairs.add(new Pair<>(EnumWrappers.ItemSlot.FEET, this.armorManager.getArmorOrAir(8, inventory)));
        pairs.add(new Pair<>(EnumWrappers.ItemSlot.MAINHAND, inventory.getItemInMainHand().clone()));
        pairs.add(new Pair<>(EnumWrappers.ItemSlot.OFFHAND, inventory.getItemInOffHand().clone()));

        packet.getSlotStackPairLists().write(0, pairs);
        this.broadcastPacketOtherPlayers(player, packet);
    }

    // Send to all players in the same world except yourself.
    private void broadcastPacketOtherPlayers(final Player player, final PacketContainer packet) {
        this.server.getOnlinePlayers().stream()
                .filter(other -> other.getWorld().equals(player.getWorld()) && !other.equals(player))
                .forEach(other -> ProtocolLibrary.getProtocolManager().sendServerPacket(other, packet));
    }
}
