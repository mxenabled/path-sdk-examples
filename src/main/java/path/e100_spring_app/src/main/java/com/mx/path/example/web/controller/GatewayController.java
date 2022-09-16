package com.mx.path.example.web.controller;

import com.mx.path.example.web.configuration.GatewayManager;
import com.mx.path.gateway.api.Gateway;

/**
 * An abstract controller that allows a Spring controller to interact with the Gateway SDK.
 */
public abstract class GatewayController {
  protected final Gateway gateway(String clientId) {
    return GatewayManager.gatewayForClient(clientId);
  }
}
