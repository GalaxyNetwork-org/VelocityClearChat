package xyz.lncvrt.velocityClearChat.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import xyz.lncvrt.velocityClearChat.ConfigManager;
import xyz.lncvrt.velocityClearChat.VelocityClearChat;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ClearChat implements SimpleCommand {
    private final MiniMessage miniMessage;
    private final ConfigManager configManager;

    public ClearChat(VelocityClearChat plugin) {
        miniMessage = plugin.miniMessage;
        configManager = plugin.configManager;
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();

        if (source instanceof Player player) {
            //no idea how to make this not so shitty
            ServerConnection server = player.getCurrentServer().orElse(null);
            if (server == null) {
                if (!configManager.getClearFailed().isEmpty()) player.sendMessage(miniMessage.deserialize(configManager.getClearFailed()));
                return;
            }
            RegisteredServer registeredServer = server.getServer();
            if (registeredServer == null) {
                if (!configManager.getClearFailed().isEmpty()) player.sendMessage(miniMessage.deserialize(configManager.getClearFailed()));
                return;
            }
            Collection<Player> players = registeredServer.getPlayersConnected();
            for (Player targetPlayer : players) {
                for (int i = 0; i < configManager.getLines(); i++) targetPlayer.sendMessage(miniMessage.deserialize(" ".repeat((i % 10) + 1)));
                if (!configManager.getClearedMessage().isEmpty()) targetPlayer.sendMessage(miniMessage.deserialize(configManager.getClearedMessage().replace("<player>", player.getUsername())));
            }
        }
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission(configManager.getClearPermission());
    }

    @Override
    public List<String> suggest(final Invocation invocation) {
        return List.of();
    }

    @Override
    public CompletableFuture<List<String>> suggestAsync(final Invocation invocation) {
        return CompletableFuture.completedFuture(List.of());
    }
}
