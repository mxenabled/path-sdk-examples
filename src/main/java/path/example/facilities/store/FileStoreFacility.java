package path.example.facilities.store;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.gson.Gson;
import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.common.store.Store;

import org.apache.commons.io.FileUtils;

public class FileStoreFacility implements Store {

  private final String filePath;
  private final ObjectMap configurations;

  public final ObjectMap getConfigurations() {
    return configurations;
  }

  public FileStoreFacility(ObjectMap configurations) {
    this.configurations = configurations;
    this.filePath = configurations.getAsString("path");
  }

  private static Gson gson = new Gson();

  @SuppressWarnings("unchecked")
  private Map<String, String> loadTable() {
    String json;
    try {
      if (FileUtils.fileExists(filePath)) {
        json = FileUtils.fileRead(filePath);
        return (Map<String, String>) gson.fromJson(json, Map.class);
      } else {
        return new HashMap<>();
      }
    } catch (IOException e) {
      throw new RuntimeException("Unable to load store file " + filePath, e);
    }
  }

  private void saveTable(Map<String, String> table) {
    try {
      FileUtils.fileWrite(filePath, gson.toJson(table));
    } catch (Exception e) {
      throw new RuntimeException("Unable to write store file " + filePath, e);
    }
  }

  private void withWritableTable(java.util.function.Consumer<Map<String, String>> consumer) {
    Map<String, String> table = loadTable();
    try {
      consumer.accept(table);
    } finally {
      saveTable(table);
    }
  }

  private String withTable(Function<Map<String, String>, String> consumer) {
    Map<String, String> table = loadTable();
    return consumer.apply(table);
  }

  private void saveSet(String key, Set<String> set) {
    withWritableTable(table -> {
      table.put(key, gson.toJson(set));
    });
  }

  @Override
  public final void delete(String key) {
    withWritableTable(table -> table.remove(key));
  }

  @Override
  public final void deleteSet(String key, String value) {
    Set<String> set = getSet(key);
    set.remove(value);
    saveSet(key, set);
  }

  @Override
  public final String get(String key) {
    return withTable(table -> table.get(key));
  }

  @SuppressWarnings("unchecked")
  @Override
  public final Set<String> getSet(String key) {
    Map<String, String> table = loadTable();
    String setJson = table.get(key);
    if (Strings.isNotBlank(setJson)) {
      return (Set<String>) gson.fromJson(setJson, Set.class);
    }

    return Collections.EMPTY_SET;
  }

  @Override
  public final boolean inSet(String key, String value) {
    return getSet(key).contains(value);
  }

  @Override
  public final void put(String key, String value, long expirySeconds) {
    put(key, value);
  }

  @Override
  public final void put(String key, String value) {
    withWritableTable(table -> table.put(key, value));
  }

  @Override
  public final void putSet(String key, String value, long expirySeconds) {
    putSet(key, value);
  }

  @SuppressWarnings("unchecked")
  @Override
  public final void putSet(String key, String value) {
    withWritableTable(table -> {
      String setJson = table.get(key);
      if (Strings.isNotBlank(setJson)) {
        Set<String> set = (Set<String>) gson.fromJson(setJson, Set.class);
        set.remove(value);
        table.put(key, gson.toJson(set));
      }
    });
  }

  @Override
  public final boolean putIfNotExist(String key, String value, long expirySeconds) {
    return putIfNotExist(key, value);
  }

  @Override
  public final boolean putIfNotExist(String key, String value) {
    if (get(key) == null) {
      put(key, value);
      return true;
    }

    return false;
  }
}
