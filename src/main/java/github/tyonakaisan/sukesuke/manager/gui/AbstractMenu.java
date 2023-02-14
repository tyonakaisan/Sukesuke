package github.tyonakaisan.sukesuke.manager.gui;

import broccolai.corn.paper.item.PaperItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.core.transform.Transform;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.element.ItemStackElement;
import org.incendo.interfaces.paper.pane.ChestPane;
import org.incendo.interfaces.paper.transform.PaperTransform;
import org.incendo.interfaces.paper.type.ChestInterface;

import java.util.function.Supplier;

abstract class AbstractMenu {
    protected final Transform<ChestPane, PlayerViewer> emptySlot = PaperTransform.chestFill(
            ItemStackElement.of(PaperItemBuilder.ofType(Material.BLACK_STAINED_GLASS_PANE)
                    .name(Component.text(""))
                    .customModelData(1)
                    .build()));

    protected abstract ChestInterface buildInterface();

    protected static Transform<ChestPane, PlayerViewer> chestItem(
            final ItemStackElement<ChestPane> element,
            final int x,
            final int y
    ) {
        return PaperTransform.chestItem(() -> element, x, y);
    }

    public static Transform<ChestPane, PlayerViewer> chestItem(
            final Supplier<ItemStackElement<ChestPane>> element,
            final int x,
            final int y
    ) {
        return PaperTransform.chestItem(element, x, y);
    }

    public final void replaceActiveScreen(
            final ClickContext<?, ?, PlayerViewer> context
    ) {
        this.buildInterface().open(context.viewer());
    }
}
