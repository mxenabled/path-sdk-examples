package com.mx.path.example.gateway.configuration;

import lombok.Data;

import com.mx.common.configuration.ConfigurationField;

@Data
public class OriginationConfiguration {
  @ConfigurationField(value = "minRequestDelayMillis")
  private int minRequestDelayMillis = 0;

  @ConfigurationField(value = "maxRequestDelayMillis")
  private int maxRequestDelayMillis = 0;
}
