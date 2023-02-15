package github.tyonakaisan.sukesuke;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import github.tyonakaisan.sukesuke.commands.PlayerCommands;
import github.tyonakaisan.sukesuke.listener.ElytraListener;
import github.tyonakaisan.sukesuke.listener.GameModeChangeListener;
import github.tyonakaisan.sukesuke.listener.InventoryClickListener;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
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
        ArmorManager armorManager = new ArmorManager(this, protocolManager);

        //command
        getCommand("suke").setExecutor(new PlayerCommands(this, protocolManager, armorManager));

        //packet
        new SelfPacketListener(this, protocolManager, armorManager);
        new OthersPacketListener(this, protocolManager, armorManager);

        //event
        getServer().getPluginManager().registerEvents(new ElytraListener(this, armorManager), this);
        getServer().getPluginManager().registerEvents(new InventoryClickListener(this, armorManager), this);
        getServer().getPluginManager().registerEvents(new GameModeChangeListener(this, armorManager), this);
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
