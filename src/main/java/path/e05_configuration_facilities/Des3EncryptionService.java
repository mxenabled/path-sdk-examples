package path.e05_configuration_facilities;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import lombok.Getter;

import com.mx.path.core.common.collection.ObjectMap;
import com.mx.path.core.common.lang.Strings;
import com.mx.path.core.common.security.EncryptionService;

public class Des3EncryptionService implements EncryptionService {

  private static final String CIPHER_PREFIX = "rsa-";

  @Getter
  private final ObjectMap configuration;

  private Cipher cipher;
  private KeyPair pair;

  @SuppressWarnings("magicnumber")
  public Des3EncryptionService(ObjectMap configuration) {
    this.configuration = configuration;

    try {
      //Creating KeyPair generator object
      KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

      //Initializing the key pair generator
      keyPairGen.initialize(2048);

      //Generating the pair of keys
      pair = keyPairGen.generateKeyPair();

      //Creating a Cipher object
      cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

    } catch (RuntimeException | NoSuchAlgorithmException | NoSuchPaddingException ignored) {
    }
  }

  @Override
  public final String decrypt(String cipherText) {
    if (!isEncrypted(cipherText)) {
      return cipherText;
    }

    try {
      byte[] bytes = Base64.getDecoder().decode(cipherText.replaceFirst(CIPHER_PREFIX, ""));
      cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());

      byte[] decipheredText = cipher.doFinal(bytes);

      return new String(decipheredText, StandardCharsets.UTF_8);
    } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      throw new RuntimeException("Unable to decrypt", e);
    }
  }

  @Override
  public final String encrypt(String value) {
    try {
      cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
      cipher.update(value.getBytes(StandardCharsets.UTF_8));
      byte[] cipherText = cipher.doFinal();

      return CIPHER_PREFIX + new String(Base64.getEncoder().encode(cipherText), StandardCharsets.UTF_8);
    } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
      throw new RuntimeException("Unable to encrypt", e);
    }
  }

  @Override
  public final boolean isEncrypted(String value) {
    if (Strings.isBlank(value)) {
      return false;
    }

    return value.startsWith(CIPHER_PREFIX);
  }
}
