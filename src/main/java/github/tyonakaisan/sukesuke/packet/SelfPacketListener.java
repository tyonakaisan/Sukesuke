package github.tyonakaisan.sukesuke.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

public class SelfPacketListener {

    public SelfPacketListener(Sukesuke plugin, ProtocolManager protocolManager, ArmorManager armorManager){
        PacketAdapter.AdapterParameteters params = PacketAdapter.params().plugin(plugin)
                .listenerPriority(ListenerPriority.HIGH)
                .types(PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS);

        protocolManager.addPacketListener(new PacketAdapter(params) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                //self_toggle = trueでなければ or Creativeモード であれば返す
                if (!player.getPersistentDataContainer().get(new NamespacedKey(plugin, "self_toggle"), PersistentDataType.STRING).equalsIgnoreCase("true")
                        || player.getGameMode().equals(GameMode.CREATIVE)) {
                    return;
                }

                //SET_SLOT
                //アイテムを選択した時？
                //ダメージを受けて耐久が減った時も
                if (packet.getType().equals(PacketType.Play.Server.SET_SLOT) && packet.getIntegers().read(0).equals(0) && packet.getIntegers().read(2) > 4 && packet.getIntegers().read(2) < 9) {
                    ItemStack itemStack = packet.getItemModifier().read(0);
                    if (itemStack != null) {
                        packet.getItemModifier().write(0, armorManager.HideArmor(itemStack, player));
                    }
                }

                //WINDOW_ITEMS
                //アイテムが変わった時？
                if (packet.getType().equals(PacketType.Play.Server.WINDOW_ITEMS) && packet.getIntegers().read(0).equals(0)) {
                    List<ItemStack> itemStacks = packet.getItemListModifier().read(0);
                    itemStacks.stream().skip(5).limit(4).forEach(item -> {
                        System.out.println(item);
                        if (item != null) {
                            item.setItemMeta(armorManager.HideArmor(item, player).getItemMeta());
                        }
                    });
                }
            }
        });

    }
}
