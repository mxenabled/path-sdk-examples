package com.mx.path.example.gateway.facility;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Data;

import com.mx.common.collections.ObjectMap;
import com.mx.common.store.Store;

/**
 * Provides an in-memory key/value store. Purges expired keys lazily, but has to iterate over all of the keys to determine
 * expiration. This is for example only and should not be used in production code.
 */
public final class MemoryStore implements Store {
  private static final int DEFAULT_EXPIRY_SECONDS = 300;

  private final Map<String, StoreRecord> store = new HashMap<>();

  @SuppressWarnings("PMD.UnusedFormalParameter")
  public MemoryStore(ObjectMap configurations) {
  }

  public MemoryStore() {
  }

  @Builder
  @Data
  static class ValueRecord {
    private String value;
    private Instant expiration;

    public boolean isExpired() {
      return getExpiration().toEpochMilli() < Instant.now().toEpochMilli();
    }
  }

  @Builder
  @Data
  static class StoreRecord {
    private ValueRecord valueRecord;
    private Set<ValueRecord> set;
  }

  @Override
  public void delete(String key) {
    store.remove(key);
  }

  @Override
  public void deleteSet(String key, String value) {
    if (store.containsKey(key) && store.get(key).getSet() != null) {
      store.get(key).getSet().removeIf(valueRecord -> valueRecord.value.equals(value));
    }
  }

  @Override
  public String get(String key) {
    purgeExpired();
    if (!store.containsKey(key) || store.get(key).getSet() != null) {
      return null;
    }
    return store.get(key).getValueRecord().getValue();
  }

  @Override
  public Set<String> getSet(String key) {
    purgeExpired();
    if (!store.containsKey(key) || store.get(key).getSet() == null) {
      return null;
    }
    return store.get(key)
        .getSet()
        .stream()
        .map(ValueRecord::getValue)
        .collect(Collectors.toSet());
  }

  @Override
  public boolean inSet(String key, String value) {
    purgeExpired();
    if (!store.containsKey(key) || store.get(key).getSet() == null) {
      return false;
    }
    return store.get(key)
        .getSet()
        .stream()
        .anyMatch(valueRecord -> valueRecord.getValue().equals(value));
  }

  @Override
  public void put(String key, String value, long expirySeconds) {
    purgeExpired();
    store.put(key,
        StoreRecord.builder()
            .valueRecord(ValueRecord.builder().value(value).expiration(Instant.now().plusSeconds(expirySeconds)).build())
            .build());
  }

  @Override
  public void put(String key, String value) {
    put(key, value, DEFAULT_EXPIRY_SECONDS);
  }

  @Override
  public void putSet(String key, String value, long expirySeconds) {
    purgeExpired();
    if (!store.containsKey(key)) {
      store.put(key, StoreRecord.builder().set(new HashSet<>()).build());
    }
    store.get(key).getSet().add(
        ValueRecord.builder()
            .value(value)
            .expiration(Instant.now().plusSeconds(expirySeconds))
            .build());
  }

  @Override
  public void putSet(String key, String value) {
    putSet(key, value, DEFAULT_EXPIRY_SECONDS);
  }

  @Override
  public boolean putIfNotExist(String key, String value, long expirySeconds) {
    purgeExpired();
    if (store.containsKey(key)) {
      return false;
    }
    store.put(key, StoreRecord.builder()
        .valueRecord(
            ValueRecord.builder()
                .value(value)
                .expiration(Instant.now().plusSeconds(expirySeconds))
                .build())
        .build());
    return true;
  }

  @Override
  public boolean putIfNotExist(String key, String value) {
    return putIfNotExist(key, value, DEFAULT_EXPIRY_SECONDS);
  }

  StoreRecord getStoreRecord(String key) {
    return store.get(key);
  }

  private void purgeExpired() {
    for (Map.Entry<String, StoreRecord> entry : store.entrySet()) {
      StoreRecord record = entry.getValue();
      if (record.getValueRecord() != null && record.getValueRecord().isExpired()) {
        delete(entry.getKey());
      } else if (record.getSet() != null) {
        record.getSet().removeIf(ValueRecord::isExpired);
      }
    }
  }
}
