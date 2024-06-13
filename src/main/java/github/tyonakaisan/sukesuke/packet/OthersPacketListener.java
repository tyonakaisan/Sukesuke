package github.tyonakaisan.sukesuke.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.Pair;
import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.utils.NamespacedKeyUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;
import java.util.Map;

@DefaultQualifier(NonNull.class)
public final class OthersPacketListener implements Listener {

    private final Sukesuke sukesuke;
    private static final Map<EnumWrappers.ItemSlot, NamespacedKey> SLOT_KEYS = Map.of(
            EnumWrappers.ItemSlot.HEAD, NamespacedKeyUtils.helmet(),
            EnumWrappers.ItemSlot.CHEST, NamespacedKeyUtils.chest(),
            EnumWrappers.ItemSlot.LEGS, NamespacedKeyUtils.leggings(),
            EnumWrappers.ItemSlot.FEET, NamespacedKeyUtils.boots()
    );

    @Inject
    public OthersPacketListener(
            final Sukesuke sukesuke
    ) {
        this.sukesuke = sukesuke;

        this.othersPacketListener();
    }

    @Inject
    public void othersPacketListener() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this.sukesuke, PacketType.Play.Server.ENTITY_EQUIPMENT) {
            @Override
            public void onPacketSending(final PacketEvent event) {
                final var packet = event.getPacket();
                if (!packet.getType().equals(PacketType.Play.Server.ENTITY_EQUIPMENT)) {
                    return;
                }

                final var entity = ProtocolLibrary.getProtocolManager().getEntityFromID(event.getPlayer().getWorld(), packet.getIntegers().read(0));
                if (entity instanceof final Player otherPlayer && otherPlayer.hasPermission("sukesuke.suke")) {

                    if (!NamespacedKeyUtils.isValueTrue(otherPlayer, NamespacedKeyUtils.display()) || otherPlayer.getGameMode().equals(GameMode.CREATIVE)) {
                        return;
                    }

                    final List<Pair<EnumWrappers.ItemSlot, ItemStack>> pairs = packet.getSlotStackPairLists().read(0);

                    pairs.stream()
                            .filter(OthersPacketListener::isArmorSlot)
                            .filter(pair -> !pair.getSecond().isEmpty())
                            .forEach(slotPair -> {
                                final var slot = slotPair.getFirst();
                                final var itemStack = slotPair.getSecond();

                                // Armor
                                if (isElytra(itemStack, otherPlayer) || (SLOT_KEYS.containsKey(slot) && !NamespacedKeyUtils.isValueTrue(otherPlayer, SLOT_KEYS.get(slot)))) {
                                    slotPair.setSecond(itemStack.clone());
                                } else {
                                    slotPair.setSecond(ItemStack.empty());
                                }
                            });

                    packet.getSlotStackPairLists().write(0, pairs);
                }
            }
        });
    }

    private static boolean isElytra(final ItemStack itemStack, final Player player) {
        return itemStack.getType().equals(Material.ELYTRA) && player.isGliding();
    }

    private static boolean isArmorSlot(final Pair<EnumWrappers.ItemSlot, ItemStack> pair) {
        final var slot = pair.getFirst();
        return slot.equals(EnumWrappers.ItemSlot.FEET) ||
                slot.equals(EnumWrappers.ItemSlot.LEGS) ||
                slot.equals(EnumWrappers.ItemSlot.CHEST) ||
                slot.equals(EnumWrappers.ItemSlot.HEAD);
    }
}