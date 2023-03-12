package github.tyonakaisan.sukesuke;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import github.tyonakaisan.sukesuke.commands.PlayerCommands;
import github.tyonakaisan.sukesuke.listener.*;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
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
        ArmorManager armorManager = new ArmorManager();
        ArmorPacketManager armorPacketManager = new ArmorPacketManager(this, armorManager, protocolManager);

        //command
        getCommand("suke").setExecutor(new PlayerCommands(this, armorPacketManager));

        //packet
        new SelfPacketListener(this, protocolManager, armorManager);
        new OthersPacketListener(this, protocolManager, armorManager);

        //event
        getServer().getPluginManager().registerEvents(new ArmorChangeListener(this, armorPacketManager), this);
        getServer().getPluginManager().registerEvents(new GameModeChangeListener(this, armorPacketManager), this);
        getServer().getPluginManager().registerEvents(new PlayerArmSwingListener(this, armorPacketManager), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this, armorPacketManager), this);
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
