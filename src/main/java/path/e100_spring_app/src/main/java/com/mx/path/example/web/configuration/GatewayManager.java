package com.mx.path.example.web.configuration;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import com.mx.path.gateway.api.Gateway;
import com.mx.path.gateway.api.GatewayConfigurator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Configures the gateway(s) defined in the gateway.yaml and provides a way to retrieve a gateway by a client ID.
 */
@Component
public final class GatewayManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(GatewayManager.class);
  private static final String[] STATIC_CONFIG_PATHS = {
      "gateway.yaml",
      "gateway.yml",
      "resources/gateway.yaml",
      "resources/gateway.yml",
      "src/main/resources/gateway.yaml",
      "src/main/resources/gateway.yml",
  };
  private static Map<String, Gateway> gatewayMap;

  /**
   * Returns a Gateway that has been configured for the provided clientId.
   * @param clientId
   * @return
   */
  public static Gateway gatewayForClient(String clientId) {
    return gatewayMap.get(clientId);
  }

  /**
   * Reloads the gateway.yaml configuration when Spring publishes a ContextRefreshedEvent. This usually only happens
   * when Spring starts up, but it can happen again if the context gets refreshed.
   */
  @EventListener(ContextRefreshedEvent.class)
  public void onContextRefreshed() {
    Path configPath = resolveConfigPath();
    LOGGER.info("Reloading gateway file from " + configPath);
    String fileContents = readConfigFile(configPath);
    gatewayMap = new GatewayConfigurator().buildFromYaml(fileContents);
  }

  /**
   * Searches the possible config paths for a gateway configuration file.
   * @return
   */
  private Path resolveConfigPath() {
    return Arrays.stream(STATIC_CONFIG_PATHS)
        .sequential()
        .map(Paths::get)
        .filter(Files::exists)
        .findFirst()
        .orElse(null);
  }

  /**
   * Reads a file from a path into a string. This doesn't provide any IO buffering and will read the entire file into
   * memory, so this should only be used for reading the gateway configuration file. If a larger file needs to be read
   * a different method should be used.
   * @param configPath
   * @return
   */
  private String readConfigFile(Path configPath) {
    String fileContents;
    try {
      fileContents = new String(Files.readAllBytes(configPath), StandardCharsets.UTF_8);
    } catch (Exception e) {
      throw new RuntimeException("Unable to read " + configPath, e);
    }
    return fileContents;
  }
}
