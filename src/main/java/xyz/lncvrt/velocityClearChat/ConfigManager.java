package xyz.lncvrt.velocityClearChat;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ConfigManager {
    private final Path configPath;
    private ConfigurationLoader<CommentedConfigurationNode> loader;
    private CommentedConfigurationNode root;

    @Inject
    public ConfigManager(@DataDirectory Path dataDirectory) {
        this.configPath = dataDirectory.resolve("config.conf");
        this.loader = HoconConfigurationLoader.builder().path(configPath).build();
        ensureConfigExists();
        loadConfig();
    }

    private void ensureConfigExists() {
        if (Files.notExists(configPath)) {
            try (InputStream defaultConfig = getClass().getClassLoader().getResourceAsStream("config.conf")) {
                if (defaultConfig != null) {
                    Files.createDirectories(configPath.getParent());
                    Files.copy(defaultConfig, configPath, StandardCopyOption.REPLACE_EXISTING);
                } else {
                    System.err.println("Default config.conf not found in resources!");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadConfig() {
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
