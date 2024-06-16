package github.tyonakaisan.sukesuke.command.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import github.tyonakaisan.sukesuke.command.SukesukeCommand;
import github.tyonakaisan.sukesuke.config.ConfigFactory;
import github.tyonakaisan.sukesuke.message.Messages;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.jetbrains.annotations.NotNull;

import static io.papermc.paper.command.brigadier.Commands.literal;

@SuppressWarnings("UnstableApiUsage")
public final class ReloadCommand implements SukesukeCommand {

    private final ConfigFactory configFactory;
    private final Messages messages;

    public ReloadCommand (
            final ConfigFactory configFactory,
            final Messages messages
    ) {
        this.configFactory = configFactory;
        this.messages = messages;
    }

    @Override
    public @NotNull ArgumentBuilder<CommandSourceStack, ?> init() {
        return literal("reload")
                .requires(source -> source.getSender().hasPermission("sukesuke.command.reload"))
                .executes(context -> {
                    final var sender = context.getSource().getSender();

                    this.configFactory.reloadPrimaryConfig();
                    this.messages.reloadMessage();

                    sender.sendMessage(this.messages.translatable(Messages.Style.SUCCESS, sender, "command.reload.success"));
                    return 1;
                });
    }
}
