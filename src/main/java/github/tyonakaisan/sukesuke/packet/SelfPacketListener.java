package github.tyonakaisan.sukesuke.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.Converters;
import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import github.tyonakaisan.sukesuke.utils.NamespacedKeyUtils;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.Map;

@DefaultQualifier(NonNull.class)
public final class SelfPacketListener implements Listener {

    private final Sukesuke sukesuke;
    private final ArmorManager armorManager;

    private static final Map<Integer, NamespacedKey> ARMOR_SLOTS = Map.of(
            5, NamespacedKeyUtils.helmet(),
            6, NamespacedKeyUtils.chest(),
            7, NamespacedKeyUtils.leggings(),
            8, NamespacedKeyUtils.boots()
    );

    @Inject
    public SelfPacketListener(
            final Sukesuke sukesuke,
            final ArmorManager armorManager
    ) {
        this.sukesuke = sukesuke;
        this.armorManager = armorManager;

        this.selfPacketListener();
    }

    public void selfPacketListener() {
        final var params = PacketAdapter.params()
                .plugin(this.sukesuke)
                .listenerPriority(ListenerPriority.HIGH)
                .types(PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS, PacketType.Play.Client.WINDOW_CLICK);

        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(params) {
            @Override
            public void onPacketSending(final PacketEvent event) {
                final var packet = event.getPacket();
                final var player = event.getPlayer();

                if (!player.hasPermission("sukesuke.suke")) {
                    return;
                }

                if (!NamespacedKeyUtils.isValueTrue(player, NamespacedKeyUtils.display()) || player.getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }

                setSlotPacket(packet, player);
                windowItemsPacket(packet, player);
            }

            @Override
            public void onPacketReceiving(final PacketEvent event) {
                final var packet = event.getPacket();

                containerClickPacket(packet, event.getPlayer());
            }
        });
    }

    private void setSlotPacket(final PacketContainer packet, final Player player) {
        if (!packet.getType().equals(PacketType.Play.Server.SET_SLOT) || !this.isPlayerWindow(packet)) {
            return;
        }

        final var slotId = packet.getIntegers().read(2);
        if (this.isArmorSlot(slotId)) {
            final var namespacedKey = ARMOR_SLOTS.get(slotId);
            this.writeItemModifier(packet, player, NamespacedKeyUtils.isValueTrue(player, namespacedKey));
        }
    }

    private void windowItemsPacket(final PacketContainer packet, final Player player) {
        if (packet.getType() != PacketType.Play.Server.WINDOW_ITEMS || !this.isPlayerWindow(packet)) {
            return;
        }

        final var itemStacks = packet.getItemListModifier().read(0);
        ARMOR_SLOTS.forEach((slot, key) -> {
            if (NamespacedKeyUtils.isValueTrue(player, key)) {
                itemStacks.set(slot, this.armorManager.fakeArmorStack(itemStacks.get(slot), player));
            }
        });

        packet.getItemListModifier().write(0, itemStacks);
    }

    private void containerClickPacket(final PacketContainer packet, final Player player) {
        if (!packet.getType().equals(PacketType.Play.Client.WINDOW_CLICK) || !this.isPlayerWindow(packet)) {
            return;
        }

        final var clickType = packet.getEnumModifier(InventoryClickType.class, 4).read(0);
        if (!(clickType.equals(InventoryClickType.QUICK_MOVE) || clickType.equals(InventoryClickType.SWAP))) {
            return;
        }

        final var slotDataModifier = packet.getStructures()
                .read(2)
                .getMaps(Converters.passthrough(int.class), BukkitConverters.getItemStackConverter());

        final var slotDataMap = slotDataModifier.read(0);

        slotDataMap.entrySet()
                .stream()
                .filter(entry -> this.isArmorSlot(entry.getKey()) && !entry.getValue().isEmpty())
                .filter(entry -> NamespacedKeyUtils.isValueTrue(player, ARMOR_SLOTS.get(entry.getKey())))
                .forEach(entry -> slotDataMap.put(entry.getKey(), this.armorManager.fakeArmorStack(entry.getValue(), player)));

        slotDataModifier.write(0, slotDataMap);
    }

    private void writeItemModifier(final PacketContainer packet, final Player player, final boolean hide) {
        final var itemModifier = packet.getItemModifier();
        final var itemStack = itemModifier.read(0);
        final var fakeArmor = hide
                ? this.armorManager.fakeArmorStack(itemStack, player)
                : itemStack;

        itemModifier.write(0, fakeArmor);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isPlayerWindow(final PacketContainer packet) {
        final var windowId = packet.getIntegers().read(0);
        return windowId == 0;
    }

    private boolean isArmorSlot(final int slotId) {
        return slotId > 4 && slotId < 9;
    }

    private enum InventoryClickType {
        PICKUP,
        QUICK_MOVE,
        SWAP,
        CLONE,
        THROW,
        QUICK_CRAFT,
        PICKUP_ALL
    }
}
