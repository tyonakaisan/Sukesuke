package github.tyonakaisan.sukesuke.command.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.ArgumentBuilder;
import github.tyonakaisan.sukesuke.command.SukesukeCommand;
import github.tyonakaisan.sukesuke.utils.NamespacedKeyUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
@DefaultQualifier(NonNull.class)
public final class DebugCommand implements SukesukeCommand {

    @Inject
    public DebugCommand(
    ) {
    }

    @Override
    public ArgumentBuilder<CommandSourceStack, ?> init() {
        return literal("debug")
                .requires(source -> source.getSender().hasPermission("sukesuke.command.debug"))
                .then(literal("removePDC")
                        .executes(context -> {
                            if (context.getSource().getSender() instanceof Player player) {
                                this.pdcRemove(player);
                            }
                    return 1;
                }))
                .then(literal("legacy")
                        .executes(context -> {
                            if (context.getSource().getSender() instanceof Player player) {
                                final var pdc = player.getPersistentDataContainer();
                                this.pdcRemove(player);

                                pdc.set(new NamespacedKey(NamespacedKeyUtils.namespace(), "toggle"), PersistentDataType.STRING, "true");
                                pdc.set(NamespacedKeyUtils.helmet(), PersistentDataType.STRING, "true");
                                pdc.set(NamespacedKeyUtils.chest(), PersistentDataType.STRING, "true");
                                pdc.set(NamespacedKeyUtils.leggings(), PersistentDataType.STRING, "true");
                                pdc.set(NamespacedKeyUtils.boots(), PersistentDataType.STRING, "true");
                            }
                            return 1;
                        }));
    }

    private void pdcRemove(final Player player) {
        final var pdc = player.getPersistentDataContainer();

        pdc.remove(NamespacedKeyUtils.display());
        pdc.remove(NamespacedKeyUtils.helmet());
        pdc.remove(NamespacedKeyUtils.chest());
        pdc.remove(NamespacedKeyUtils.leggings());
        pdc.remove(NamespacedKeyUtils.boots());
    }
}
