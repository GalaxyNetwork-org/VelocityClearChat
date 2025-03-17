package xyz.lncvrt.velocityClearChat;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private final Path configPath;
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode root;

    @Inject
    public ConfigManager(@DataDirectory Path dataDirectory) {
        this.configPath = dataDirectory.resolve("config.conf");
        this.loader = HoconConfigurationLoader.builder().path(configPath).build();
        loadConfig();
    }

    private void loadConfig() {
        try {
            if (Files.notExists(configPath)) {
                saveDefaultConfig();
            }
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveDefaultConfig() throws IOException {
        Files.createDirectories(configPath.getParent());

        root = loader.load();
        root.node("lines")
                .set(100)
                .comment("The number of lines the chat clearer will send.");
        root.node("chat-cleared-message")
                .set("<green>Chat has been cleared by an admin</green>")
                .comment("The message to send when the chat is cleared by an admin.\nYou can use <player> if you would like to display who cleared the chat.");
        root.node("chat-clear-failed-message")
                .set("<red>Failed to get server info</red>")
                .comment("If velocity returns invalid information, this message will be displayed.");
        root.node("clear-permission")
                .set("lncvrt.velocityclearchat.clear")
                .comment("The permission an admin needs to clear the chat.");
        root.node("clear-on-server-swap")
                .set(true)
                .comment("On player swap, should we clear the chat history?");
        root.node("clear-on-server-swap-message")
                .set("")
                .comment("The message to display if \"clear-on-server-swap\" is enabled, set to \"\" to disable it.");
        loader.save(root);
    }

    public int getLines() {
        return root.node("lines").getInt(1000);
    }

    public String getClearedMessage() {
        return root.node("chat-cleared-message").getString("<green>Chat has been cleared by an admin</green>");
    }

    public String getClearFailed() {
        return root.node("chat-clear-failed-message").getString("<red>Failed to get server info</red>");
    }

    public String getClearPermission() {
        return root.node("clear-permission").getString("lncvrt.velocityclearchat.clear");
    }

    public Boolean getClearOnSwap() {
        return root.node("clear-on-server-swap").getBoolean(false);
    }

    public String getClearOnSwapMessage() {
        return root.node("clear-on-server-swap-message").getString("\"\"");
    }
}
