package github.tyonakaisan.sukesuke.packet;

import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class AbstractPacket {
    private final ProtocolManager protocolManager;

    public AbstractPacket(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }

    public void sendOtherPlayerPacket(Player player, PacketContainer packet) {
        //サーバー上のプレイヤーの取得
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            //送信
            try {
                protocolManager.sendServerPacket(onlinePlayer, packet);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
