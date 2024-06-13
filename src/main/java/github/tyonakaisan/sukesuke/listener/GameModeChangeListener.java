package github.tyonakaisan.sukesuke.listener;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.utils.NamespacedKeyUtils;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class GameModeChangeListener implements Listener {

    private final Sukesuke sukesuke;
    private final ArmorPacketManager armorPacketManager;
    private final Server server;

    @Inject
    public GameModeChangeListener(
            final Sukesuke sukesuke,
            final ArmorPacketManager armorPacketManager,
            final Server server
    ) {
        this.sukesuke = sukesuke;
        this.armorPacketManager = armorPacketManager;
        this.server = server;
    }

    // クリエイティブの場合フェイクアーマーが実体となる(？)
    // クリエイティブの場合はオフにするか、フェイクアーマーにしない処理が必要
    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        final var player = event.getPlayer();

        if (event.getNewGameMode().equals(GameMode.CREATIVE)) {
            // 強制false
            this.setDisplayKeyValue(player, false);
        } else {
            // 強制true
            this.server.getScheduler().runTaskLater(this.sukesuke, () -> this.setDisplayKeyValue(player, true), 1L);
        }
    }

    private void setDisplayKeyValue(final Player player, final boolean display) {
        final var pdc = player.getPersistentDataContainer();
        pdc.set(NamespacedKeyUtils.display(), PersistentDataType.BOOLEAN, display);
        this.armorPacketManager.sendPacket(player);
    }
}
