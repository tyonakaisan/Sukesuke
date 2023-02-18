package github.tyonakaisan.sukesuke;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import github.tyonakaisan.sukesuke.commands.PlayerCommands;
import github.tyonakaisan.sukesuke.listener.ArmorChangeListener;
import github.tyonakaisan.sukesuke.listener.ElytraListener;
import github.tyonakaisan.sukesuke.listener.GameModeChangeListener;
import github.tyonakaisan.sukesuke.listener.PlayerArmSwingListener;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.packet.OthersPacketListener;
import github.tyonakaisan.sukesuke.packet.SelfPacketListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.incendo.interfaces.paper.PaperInterfaceListeners;

public final class Sukesuke extends JavaPlugin {
private static Sukesuke plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        PaperInterfaceListeners.install(this);

        //manager
        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
        ArmorPacketManager armorPacketManager = new ArmorPacketManager(this, protocolManager);

        //command
        getCommand("suke").setExecutor(new PlayerCommands(this, protocolManager, armorPacketManager));

        //packet
        new SelfPacketListener(this, protocolManager, armorPacketManager);
        new OthersPacketListener(this, protocolManager, armorPacketManager);

        //event
        getServer().getPluginManager().registerEvents(new ElytraListener(this, armorPacketManager), this);
        getServer().getPluginManager().registerEvents(new ArmorChangeListener(this, armorPacketManager), this);
        getServer().getPluginManager().registerEvents(new GameModeChangeListener(this, armorPacketManager), this);
        getServer().getPluginManager().registerEvents(new PlayerArmSwingListener(this, armorPacketManager), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //あ
    }

    public static Sukesuke getPlugin() {
        return plugin;
    }
}
