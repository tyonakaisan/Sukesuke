package github.tyonakaisan.sukesuke.manager.gui;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.Sukesuke;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.manager.Keys;
import github.tyonakaisan.sukesuke.utils.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.type.ChestInterface;

import java.util.List;
import java.util.Objects;

@DefaultQualifier(NonNull.class)
public final class SettingsMenu extends AbstractMenu {
    private final Sukesuke sukesuke;
    private final ArmorPacketManager armorPacketManager;

    @Inject
    public SettingsMenu(
            Sukesuke sukesuke,
            ArmorPacketManager armorPacketManager
    ) {
        this.sukesuke = sukesuke;
        this.armorPacketManager = armorPacketManager;
    }

    private static final ItemStack help = ItemBuilder.of(Material.LIGHT)
            .displayName(Component.text()
                    .append(Component.text("使い方"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#00fa9a"))
                    .build())
            .lore(List.of(Component.text()
                    .append(Component.text("装備ごとに表示か非表示を選べます"))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GRAY)
                    .build(),
                    Component.text().build(),
                    Component.text()
                            .append(Component.text("/suke で表示/非表示の切り替えができます"))
                            .decoration(TextDecoration.ITALIC, false)
                            .color(NamedTextColor.GRAY)
                            .build(),
                    Component.text()
                            .append(Component.text("表示がおかしくなった場合は"))
                            .decoration(TextDecoration.ITALIC, false)
                            .color(NamedTextColor.GRAY)
                            .build(),
                    Component.text()
                            .append(Component.text("もう一度装備を着直したら治ります"))
                            .decoration(TextDecoration.ITALIC, false)
                            .color(NamedTextColor.GRAY)
                            .build(),
                    Component.text()
                            .append(Component.text("また、エリトラは強制的に表示されます"))
                            .decoration(TextDecoration.ITALIC, false)
                            .color(NamedTextColor.GOLD)
                            .build()))
            .build();

    private static final ItemStack bug = ItemBuilder.of(Material.PUFFERFISH_BUCKET)
            .displayName(Component.text()
                    .append(Component.text("既知のバグ等"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#00fa9a"))
                    .build())
            .lore(List.of(
                    Component.text()
                            .append(Component.text("・皮のブーツを着ても粉雪の上を歩けない"))
                            .decoration(TextDecoration.ITALIC, false)
                            .color(TextColor.fromCSSHexString("#dc143c"))
                            .build(),
                    Component.text()
                            .append(Component.text("  ↳ 足の部位を表示する設定にしたら治ります"))
                            .decoration(TextDecoration.ITALIC, false)
                            .color(NamedTextColor.GRAY)
                            .build()))
            .build();

    private static final ItemStack helmet = ItemBuilder.of(Material.BARRIER)
            .displayName(Component.text()
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

    private static final ItemStack chestplate = ItemBuilder.of(Material.BARRIER)
            .displayName(Component.text()
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

    private static final ItemStack leggings = ItemBuilder.of(Material.BARRIER)
            .displayName(Component.text()
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

    private static final ItemStack boots = ItemBuilder.of(Material.BARRIER)
            .displayName(Component.text()
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

    private static final ItemStack self_toggle = ItemBuilder.of(Material.ENDER_EYE)
            .displayName(Component.text()
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

    private static final ItemStack toggleInVisible = ItemBuilder.of(Material.GRAY_DYE)
            .displayName(Component.text()
                    .append(Component.text("非表示中"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                .build())
            .lore(List.of(Component.text()
                    .append(Component.text("クリックして表示に切り替える!"))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#ffd700"))
                    .build()))
            .build();

    private static final ItemStack toggleVisible = ItemBuilder.of(Material.LIME_DYE)
            .displayName(Component.text()
                    .append(Component.text("表示中"))
                    .decoration(TextDecoration.BOLD, true)
                    .decoration(TextDecoration.ITALIC, false)
                    .build())
            .lore(List.of(Component.text()
                    .append(Component.text("クリックして非表示に切り替える!"))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(TextColor.fromCSSHexString("#ffd700"))
                    .build()))
            .build();

    private static final ItemStack close = ItemBuilder.of(Material.BARRIER)
            .displayName(Component.text()
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
                //2段目
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(toggleItem("helmet", view.viewer().player()), context -> {
                    Keys.setToggleArmorType(view.viewer().player(), "helmet");
                    armorPacketManager.sendPacket(view.viewer().player());
                }), 1, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(toggleItem("chest", view.viewer().player()), context -> {
                    Keys.setToggleArmorType(view.viewer().player(), "chest");
                    armorPacketManager.sendPacket(view.viewer().player());
                }), 2, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(toggleItem("leggings", view.viewer().player()), context -> {
                    Keys.setToggleArmorType(view.viewer().player(), "leggings");
                    armorPacketManager.sendPacket(view.viewer().player());
                }), 3, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(toggleItem("boots", view.viewer().player()), context -> {
                    Keys.setToggleArmorType(view.viewer().player(), "boots");
                    armorPacketManager.sendPacket(view.viewer().player());
                }), 4, 1))
                .addTransform(chestItem(ItemStackElement.of(help), 6, 1))
                .addTransform(chestItem(ItemStackElement.of(bug), 7, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(close, context -> {
                    armorPacketManager.sendPacket(view.viewer().player());
                    context.viewer().close();
                }), 8, 1))
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

    public ItemStack toggleItem(String key, Player player) {
        ItemBuilder.of(Material.BARRIER).build();
        ItemStack item;

        if (Objects.requireNonNull(player.getPersistentDataContainer().get(new NamespacedKey(sukesuke, key), PersistentDataType.STRING)).equalsIgnoreCase("true")) {
            item = toggleInVisible;
        } else {
            item = toggleVisible;
        }
        return item;
    }
}
