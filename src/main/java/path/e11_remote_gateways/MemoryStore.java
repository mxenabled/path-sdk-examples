package path.e11_remote_gateways;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.store.Store;

/**
 * This facility shows how to partially implement a Store using an in-memory Hash.
 */
public class MemoryStore implements Store {
  @Getter
  private final ObjectMap configuration;

  // NOTE: A constructor that takes an ObjectMap is required.
  public MemoryStore(ObjectMap configuration) {
    this.configuration = configuration;
  }

  // Behaviors are singletons. This only needs to be an instance member.
  private final Map<String, String> store = new HashMap<>();

  @Override
  public final void delete(String key) {
    store.remove(key);
  }

  @Override
  public final String get(String key) {
    return store.get(key);
  }

  @Override
  public final void put(String key, String value, long expirySeconds) {
    put(key, value);
  }

  @Override
  public final void put(String key, String value) {
    store.put(key, value);
  }

  // NOTE: Not implementing the whole interface for this example. All of these operations need to be implemented
  //       for the facility to be compatible with existing behaviors and accessors.

  @Override
  public final void deleteSet(String key, String value) {
  }

  @Override
  public final Set<String> getSet(String key) {
    return null;
  }

  @Override
  public final boolean inSet(String key, String value) {
    return false;
  }

  @Override
  public final void putSet(String key, String value, long expirySeconds) {
  }

  @Override
  public final void putSet(String key, String value) {

  }

  @Override
  public final boolean putIfNotExist(String key, String value, long expirySeconds) {
    return false;
  }

  @Override
  public final boolean putIfNotExist(String key, String value) {
    return false;
  }
}
