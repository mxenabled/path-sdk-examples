package com.mx.path.example.web.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.models.Resources;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers an MDX-configured GSON serializer.
 */
@Configuration
@SuppressWarnings("checkstyle:designforextension")
public class MdxSerializerGsonBeanConfig {
  @Bean
  public Gson gson() throws Exception {
    GsonBuilder builder = new GsonBuilder();
    Resources.registerResources(builder);
    return builder.create();
  }
}
