package github.tyonakaisan.sukesuke.manager.gui;

import net.kyori.adventure.text.minimessage.MiniMessage;
import org.incendo.interfaces.core.click.ClickContext;
import org.incendo.interfaces.paper.PlayerViewer;
import org.incendo.interfaces.paper.type.BookInterface;

abstract class AbstractBook {
    static final MiniMessage miniMessage = MiniMessage.miniMessage();

    protected abstract BookInterface buildInterface();

    public final void replaceActiveScreen(
            final ClickContext<?, ?, PlayerViewer> context
    ) {
        this.buildInterface().open(context.viewer());
    }
}
