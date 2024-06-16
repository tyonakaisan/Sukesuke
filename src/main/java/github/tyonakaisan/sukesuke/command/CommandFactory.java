package github.tyonakaisan.sukesuke.command;

import com.google.inject.Inject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import github.tyonakaisan.sukesuke.command.commands.DebugCommand;
import github.tyonakaisan.sukesuke.command.commands.ReloadCommand;
import github.tyonakaisan.sukesuke.command.commands.SettingsCommand;
import github.tyonakaisan.sukesuke.command.commands.ToggleCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@DefaultQualifier(NonNull.class)
public final class CommandFactory {

    private final DebugCommand debugCommand;
    private final ReloadCommand reloadCommand;
    private final SettingsCommand settingsCommand;
    private final ToggleCommand toggleCommand;

    private static final LiteralArgumentBuilder<CommandSourceStack> FIRST_LITERAL_ARGUMENT = Commands.literal("sukesuke");
    private static final List<String> ALIASES = List.of("suke", "sk");

    @Inject
    public CommandFactory(
            final DebugCommand debugCommand,
            final ReloadCommand reloadCommand,
            final SettingsCommand settingsCommand,
            final ToggleCommand toggleCommand
    ) {
        this.debugCommand = debugCommand;
        this.reloadCommand = reloadCommand;
        this.settingsCommand = settingsCommand;
        this.toggleCommand = toggleCommand;
    }

    public void registerViaBootstrap(final BootstrapContext context) {
        final LifecycleEventManager<BootstrapContext> lifecycleManager = context.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event ->
                event.registrar().register(this.bootstrapCommands(), null, ALIASES)
        );
    }

    public void registerViaEnable(final JavaPlugin plugin) {
        final LifecycleEventManager<Plugin> lifecycleManager = plugin.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event ->
                event.registrar().register(this.pluginCommands(), null, ALIASES)
        );
    }

    private LiteralCommandNode<CommandSourceStack> bootstrapCommands() {
        return this.literal()
                .build();
    }

    private LiteralCommandNode<CommandSourceStack> pluginCommands() {
        return this.literal()
                .then(this.debugCommand.init())
                .then(this.reloadCommand.init())
                .then(this.settingsCommand.init())
                .then(this.toggleCommand.init())
                .build();

    }

    private LiteralArgumentBuilder<CommandSourceStack> literal() {
        return FIRST_LITERAL_ARGUMENT
                .requires(source -> source.getSender().hasPermission("sukesuke.command"));
    }
}