package xyz.lncvrt.velocityClearChat;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;
import xyz.lncvrt.velocityClearChat.commands.ClearChat;

import java.nio.file.Path;

@Plugin(id = "velocityclearchat", name = "VelocityClearChat", authors = {"Lncvrt"}, version = BuildConstants.VERSION)
public class VelocityClearChat {
    private final Logger logger;
    private final ProxyServer proxyServer;
    public ConfigManager configManager;
    public final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Inject
    public VelocityClearChat(Logger logger, ProxyServer proxyServer, @DataDirectory Path dataDirectory) {
        this.logger = logger;
        this.proxyServer = proxyServer;
        this.configManager = new ConfigManager(dataDirectory);
    }

    @Subscribe
    public void onProxyInitialize(ProxyInitializeEvent event) {
        CommandManager commandManager = proxyServer.getCommandManager();
        CommandMeta commandMeta = commandManager.metaBuilder("clearchat")
                .aliases("cc")
                .plugin(this)
                .build();

        SimpleCommand commandToRegister = new ClearChat(this);

        commandManager.register(commandMeta, commandToRegister);
    }

    @Subscribe
    public void onPlayerSwitchServer(ServerConnectedEvent event) {
        if (configManager.getClearOnSwap()) {
            Player player = event.getPlayer();
            for (int i = 0; i < configManager.getLines(); i++) player.sendMessage(miniMessage.deserialize(" ".repeat((i % 10) + 1)));
            if (!configManager.getClearOnSwapMessage().isEmpty()) {
                player.sendMessage(miniMessage.deserialize(configManager.getClearOnSwapMessage()));
            }
        }
    }
}
