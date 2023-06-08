package path.lib;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mx.path.core.common.serialization.LocalDateTimeDeserializer;

public class Logger {
  private static Gson gson;

  static {
    GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, LocalDateTimeDeserializer.builder().build()).setPrettyPrinting();
    com.mx.path.model.mdx.model.Resources.registerResources(gsonBuilder);
    gson = gsonBuilder.create();
  }

  public static void log(Object obj) {
    if (obj instanceof String) {
      System.out.println("\n=> " + obj);
    } else {
      System.out.println("\n" + gson.toJson(obj));
    }
  }
}
