package com.mx.testing.fakes;

import lombok.Getter;

import com.mx.common.collections.ObjectMap;
import com.mx.common.security.EncryptionService;

public class FakeEncryptionService implements EncryptionService {
  @Getter
  ObjectMap configurations;

  public FakeEncryptionService(ObjectMap configurations) {
    this.configurations = configurations;
  }

  @Override
  public String encrypt(String plaintext) {
    return plaintext;
  }

  @Override
  public String decrypt(String ciphertext) {
    return ciphertext;
  }

  @Override
  public boolean isEncrypted(String text) {
    return true;
  }
}
