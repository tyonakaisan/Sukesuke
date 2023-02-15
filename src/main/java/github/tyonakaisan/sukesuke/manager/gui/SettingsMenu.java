package github.tyonakaisan.sukesuke.manager.gui;

import broccolai.corn.paper.item.PaperItemBuilder;
import com.comphenix.protocol.ProtocolManager;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorManager;
import github.tyonakaisan.sukesuke.player.PlayerSetKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;

import java.util.List;
import java.util.Objects;

public class SettingsMenu extends AbstractMenu {
    private final Sukesuke plugin;
    private final ProtocolManager protocolManager;
    private final ArmorManager armorManager;

    public SettingsMenu(Sukesuke pl, ProtocolManager pm, ArmorManager am) {
        this.plugin = pl;
        this.protocolManager = pm;
        this.armorManager = am;
    }

    private static final ItemStack helmet = PaperItemBuilder.ofType(Material.BARRIER)
            .name(Component.text()
                    .append(Component.text("ヘルメット"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#00fa9a"))
                    .build())
            .lore(List.of(Component.text()
                    .append(Component.text("何も着ていないようだ..."))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY)
                    .build()))
            .build();

    private static final ItemStack chestplate = PaperItemBuilder.ofType(Material.BARRIER)
            .name(Component.text()
                    .append(Component.text("チェストプレート"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#00fa9a"))
                    .build())
            .lore(List.of(Component.text()
                    .append(Component.text("何も着ていないようだ..."))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY)
                    .build()))
            .build();

    private static final ItemStack leggings = PaperItemBuilder.ofType(Material.BARRIER)
            .name(Component.text()
                    .append(Component.text("レギンス"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#00fa9a"))
                    .build())
            .lore(List.of(Component.text()
                    .append(Component.text("何も着ていないようだ..."))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY)
                    .build()))
            .build();

    private static final ItemStack boots = PaperItemBuilder.ofType(Material.BARRIER)
            .name(Component.text()
                    .append(Component.text("ブーツ"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#00fa9a"))
                    .build())
            .lore(List.of(Component.text()
                    .append(Component.text("何も着ていないようだ..."))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY)
                    .build()))
            .build();

    private static final ItemStack self_toggle = PaperItemBuilder.ofType(Material.ENDER_EYE)
            .name(Component.text()
                    .append(Component.text("自分の装備の表示/非表示"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#00fa9a"))
                    .build())
            .lore(List.of(
                    Component.text()
                        .append(Component.text("装備を表示/非表示にします!"))
                        .decoration(TextDecoration.ITALIC, false)
                        .color(NamedTextColor.WHITE)
                        .build()))
            .build();

    private static final ItemStack others_toggle = PaperItemBuilder.ofType(Material.PLAYER_HEAD)
            .name(Component.text()
                    .append(Component.text("他プレイヤーの装備の表示/非表示"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#00fa9a"))
                    .build())
            .lore(List.of(
                    Component.text()
                            .append(Component.text("装備を表示/非表示にします!"))
                            .decoration(TextDecoration.ITALIC, false)
                            .color(NamedTextColor.WHITE)
                            .build()))
            .build();

    private static final ItemStack toggleInVisible = PaperItemBuilder.ofType(Material.GRAY_DYE)
            .name(Component.text()
                    .append(Component.text("非表示中"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                .build())
            .lore(List.of(Component.text()
                    .append(Component.text("クリックして表示!"))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#ffd700"))
                    .build()))
            .build();

    private static final ItemStack toggleVisible = PaperItemBuilder.ofType(Material.LIME_DYE)
            .name(Component.text()
                    .append(Component.text("表示中"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .build())
            .lore(List.of(Component.text()
                    .append(Component.text("クリックして非表示!"))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#ffd700"))
                    .build()))
            .build();

    private static final ItemStack close = PaperItemBuilder.ofType(Material.BARRIER)
            .name(Component.text()
                    .append(Component.text("とじる"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#00fa9a"))
                    .build())
            .build();

    @Override
    public ChestInterface buildInterface() {
        return ChestInterface.builder()
                .rows(2)
                .updates(true, 5)
                .clickHandler(ClickHandler.cancel())
                .title(Component.text()
                        .append(Component.text("せってい"))
                        .decoration(TextDecoration.BOLD, true)
                        .build())
                .addTransform(this.emptySlot)
                //1段目
                .addTransform(helmet())
                .addTransform(chest())
                .addTransform(leggings())
                .addTransform(boots())
                .addTransform(chestItem(ItemStackElement.of(self_toggle), 6, 0))
                .addTransform(chestItem(ItemStackElement.of(others_toggle), 7, 0))
                //2段目
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(toggleItem("helmet", view.viewer().player()), context -> new PlayerSetKey(plugin).setToggleArmorType(view.viewer().player(), "helmet")), 1, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(toggleItem("chest", view.viewer().player()), context -> new PlayerSetKey(plugin).setToggleArmorType(view.viewer().player(), "chest")), 2, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(toggleItem("leggings", view.viewer().player()), context -> new PlayerSetKey(plugin).setToggleArmorType(view.viewer().player(), "leggings")), 3, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(toggleItem("boots", view.viewer().player()), context -> new PlayerSetKey(plugin).setToggleArmorType(view.viewer().player(), "boots")), 4, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(toggleItem("self_toggle", view.viewer().player()), context -> {
                    new PlayerSetKey(plugin).setToggleArmorType(view.viewer().player(), "self_toggle");
                    //なぜか自分視点の装備だけ切り替えることができない
                    //他プレイヤー視点だと変わっている
                    //gamemodeを変えた時はしっかり変わってる
                    armorManager.sendPacket(view.viewer().player());
                }), 6, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(toggleItem("others_toggle", view.viewer().player()), context -> new PlayerSetKey(plugin).setToggleArmorType(view.viewer().player(), "others_toggle")), 7, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(close, context -> context.viewer().close()), 8, 1))
        .build();
    }

    public Transform<ChestPane, PlayerViewer> helmet() {
        return (pane, view) -> {
            ItemStack viewerHelmet;
            viewerHelmet = view.viewer().player().getInventory().getHelmet();
            if (viewerHelmet == null) {
                viewerHelmet = helmet.clone();
            }
            return pane.element(ItemStackElement.of(viewerHelmet), 1, 0);
        };
    }

    public Transform<ChestPane, PlayerViewer> chest() {
        return (pane, view) -> {
            ItemStack viewerChest;
            viewerChest = view.viewer().player().getInventory().getChestplate();
            if (viewerChest == null) {
                viewerChest = chestplate.clone();
            }
            return pane.element(ItemStackElement.of(viewerChest), 2, 0);
        };
    }

    public Transform<ChestPane, PlayerViewer> leggings() {
        return (pane, view) -> {
            ItemStack viewerLeggings;
            viewerLeggings = view.viewer().player().getInventory().getLeggings();
            if (viewerLeggings == null) {
                viewerLeggings = leggings.clone();
            }
            return pane.element(ItemStackElement.of(viewerLeggings), 3, 0);
        };
    }

    public Transform<ChestPane, PlayerViewer> boots() {
        return (pane, view) -> {
            ItemStack viewerBoots;
            viewerBoots = view.viewer().player().getInventory().getBoots();
            if (viewerBoots == null) {
                viewerBoots = boots.clone();
            }
            return pane.element(ItemStackElement.of(viewerBoots), 4, 0);
        };
    }

    public ItemStack toggleItem(String Key, Player player) {
        PaperItemBuilder.ofType(Material.BARRIER).build();
        ItemStack item;

        if (Objects.requireNonNull(player.getPersistentDataContainer().get(new NamespacedKey(plugin, Key), PersistentDataType.STRING)).equalsIgnoreCase("true")) {
            item = toggleInVisible;
        } else {
            item = toggleVisible;
        }
        return item;
    }
}
