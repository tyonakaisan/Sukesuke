package github.tyonakaisan.sukesuke.manager.gui;

import com.google.inject.Inject;
import github.tyonakaisan.sukesuke.manager.ArmorPacketManager;
import github.tyonakaisan.sukesuke.message.Messages;
import github.tyonakaisan.sukesuke.utils.NamespacedKeyUtils;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.incendo.interfaces.core.click.ClickHandler;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.transform.PaperTransform;
import org.incendo.interfaces.paper.type.ChestInterface;

import java.util.List;

@DefaultQualifier(NonNull.class)
public final class SettingsMenu extends AbstractMenu {

    private final ArmorPacketManager armorPacketManager;
    private final Messages messages;

    @Inject
    public SettingsMenu(
            final ArmorPacketManager armorPacketManager,
            final Messages messages
    ) {
        this.armorPacketManager = armorPacketManager;
        this.messages = messages;
    }

    @Override
    public ChestInterface.Builder buildInterface() {
        return ChestInterface.builder()
                .rows(2)
                .updates(true, 5)
                .clickHandler(ClickHandler.cancel())
                .addTransform(PaperTransform.chestFill(ItemStackElement.of(filler())))
                //1段目
                .addTransform(this.getPreview(EquipmentSlot.HEAD, 1, 0))
                .addTransform(this.getPreview(EquipmentSlot.CHEST, 2, 0))
                .addTransform(this.getPreview(EquipmentSlot.LEGS, 3, 0))
                .addTransform(this.getPreview(EquipmentSlot.FEET, 4, 0))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(this.armorDisplayItemStack(view.viewer().player())), 6, 0))
                //2段目
                .addTransform(this.toggleState(NamespacedKeyUtils.helmet(), 1, 1))
                .addTransform(this.toggleState(NamespacedKeyUtils.chest(), 2, 1))
                .addTransform(this.toggleState(NamespacedKeyUtils.leggings(), 3, 1))
                .addTransform(this.toggleState(NamespacedKeyUtils.boots(), 4, 1))
                .addTransform(this.toggleState(NamespacedKeyUtils.display(), 6, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(this.tipsItemStack(view.viewer().player())), 7, 1))
                .addTransform((pane, view) -> pane.element(ItemStackElement.of(this.closeItemStack(view.viewer().player()), context -> {
                    context.viewer().close();
                    this.armorPacketManager.sendPacket(view.viewer().player());
                }), 8, 1))
                .addCloseHandler((pane, view) -> this.armorPacketManager.sendPacket(view.viewer().player()));
    }

    @Override
    public void open(final Player player) {
        this.buildInterface()
                .title(this.messages.translatable(Messages.Style.INFO, player, "gui.title"))
                .build()
                .open(PlayerViewer.of(player));
    }

    private ItemStack armorDisplayItemStack(final Player player) {
        return this.displayItem(
                Material.ENDER_EYE,
                this.messages.translatable(Messages.Style.INFO, player, "gui.item.display.display_name"),
                List.of()
        );
    }

    private ItemStack tipsItemStack(final Player player) {
        return this.displayItem(
                Material.LIGHT,
                this.messages.translatable(Messages.Style.INFO, player, "gui.item.tips.display_name"),
                List.of(this.messages.translatable(Messages.Style.INFO, player, "gui.item.tips.lore"))
        );
    }

    private ItemStack closeItemStack(final Player player) {
        return this.displayItem(
                Material.BARRIER,
                this.messages.translatable(Messages.Style.INFO, player, "gui.item.close.display_name"),
                List.of()
        );
    }

    private Transform<ChestPane, PlayerViewer> getPreview(final EquipmentSlot equipmentSlot, final int x, final int y) {
        return (pane, view) -> {
            final var preview = view.viewer().player().getInventory().getItem(equipmentSlot);
            if (preview.isEmpty()) {
                return pane.element(ItemStackElement.of(filler().withType(Material.LIGHT_GRAY_STAINED_GLASS_PANE)), x, y);
            }
            return pane.element(ItemStackElement.of(preview), x, y);
        };
    }

    private Transform<ChestPane, PlayerViewer> toggleState(final NamespacedKey namespacedKey, final int x, final int y) {
        return (pane, view) -> {
            final var player = view.viewer().player();
            final var material = NamespacedKeyUtils.isValueTrue(player, namespacedKey)
                    ? Material.LIME_DYE
                    : Material.GRAY_DYE;

            return pane.element(
                    ItemStackElement.of(
                            this.displayItem(
                                    material,
                                    this.displayName(player, namespacedKey),
                                    List.of(this.messages.translatable(Messages.Style.INFO, player, "gui.item.toggle.lore"))),
                            context -> {
                                NamespacedKeyUtils.toggleKeyValue(player, namespacedKey);
                                player.playSound(Sound.sound()
                                        .type(Key.key("minecraft:ui.button.click"))
                                        .volume(0.1f)
                                        .build());
                            }),
                    x,
                    y
            );
        };
    }

    private ItemStack displayItem(final Material material, final Component displayName, final List<Component> lore) {
        final var itemStack = new ItemStack(material);
        itemStack.editMeta(itemMeta -> {
            if (!displayName.equals(Component.empty())) {
                itemMeta.displayName(displayName);
            }
            if (!lore.isEmpty()) {
                itemMeta.lore(lore);
            }
        });
        return itemStack;
    }

    private Component displayName(final Player player, final NamespacedKey namespacedKey) {
        return NamespacedKeyUtils.isValueTrue(player, namespacedKey)
                ? this.messages.translatable(Messages.Style.INFO, player, "gui.item.toggle.enabled.display_name")
                : this.messages.translatable(Messages.Style.INFO, player, "gui.item.toggle.disabled.display_name");
    }
}
