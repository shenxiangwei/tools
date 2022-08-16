/*
 *
 *  * Copyright (c) 2019. tdc.shangri-la.com. All Rights Reserved.
 *
 */

package work;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.jwk.JsonWebKey;
import org.jose4j.lang.JoseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author ying.pan
 * @date 2021/7/9 10:17 AM.
 */
@Component
public final class EncryptionUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtils.class);
    private static JsonWebKey jwk = null;

    static {
        try {
            // The shared secret or shared symmetric key represented as a octet sequence JSON Web Key (JWK)
            jwk = JsonWebKey.Factory.newJwk("{\"kty\":\"oct\",\"k\":\"qcglpgka07kL2ESoSPNBkGzhfII8p6vBfuVLNXH86fL\"}");
        } catch (JoseException e) {
            LOGGER.error("加解密工具类初始化失败");
        }
    }

    private EncryptionUtils() {

    }

    public static String encryption(String content) {
        String compactSerialization = "";
        try {
            // Create a new Json Web Encryption object
            JsonWebEncryption senderJwe = new JsonWebEncryption();

            // The plaintext of the JWE is the message that we want to encrypt.
            senderJwe.setPlaintext(content);

            // Set the "alg" header, which indicates the key management mode for this JWE.
            // In this example we are using the direct key management mode, which means
            // the given key will be used directly as the content encryption key.
            senderJwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.DIRECT);

            // Set the "enc" header, which indicates the content encryption algorithm to be used.
            // This example is using AES_128_CBC_HMAC_SHA_256 which is a composition of AES CBC
            // and HMAC SHA2 that provides authenticated encryption.
            senderJwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);

            // Set the key on the JWE. In this case, using direct mode, the key will used directly as
            // the content encryption key. AES_128_CBC_HMAC_SHA_256, which is being used to encrypt the
            // content requires a 256 bit key.
            senderJwe.setKey(jwk.getKey());

            // Produce the JWE compact serialization, which is where the actual encryption is done.
            // The JWE compact serialization consists of five base64url encoded parts
            // combined with a dot ('.') character in the general format of
            // <header>.<encrypted key>.<initialization vector>.<ciphertext>.<authentication tag>
            // Direct encryption doesn't use an encrypted key so that field will be an empty string
            // in this case.
            compactSerialization = senderJwe.getCompactSerialization();

            // Do something with the JWE. Like send it to some other party over the clouds
            // and through the interwebs.
        } catch (JoseException e) {
            LOGGER.error("加密异常,异常信息: e - ", e);
        }
        return compactSerialization;

    }

    public static String decryption(String compactSerialization) {
        String plaintext = "";
        try {
            JsonWebEncryption receiverJwe = new JsonWebEncryption();

            // Set the algorithm constraints based on what is agreed upon or expected from the sender
            AlgorithmConstraints algConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, KeyManagementAlgorithmIdentifiers.DIRECT);
            receiverJwe.setAlgorithmConstraints(algConstraints);
            AlgorithmConstraints encConstraints = new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT, ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
            receiverJwe.setContentEncryptionAlgorithmConstraints(encConstraints);

            // Set the compact serialization on new Json Web Encryption object
            receiverJwe.setCompactSerialization(compactSerialization);

            // Symmetric encryption, like we are doing here, requires that both parties have the same key.
            // The key will have had to have been securely exchanged out-of-band somehow.
            receiverJwe.setKey(jwk.getKey());

            // Get the message that was encrypted in the JWE. This step performs the actual decryption steps.
            plaintext = receiverJwe.getPlaintextString();

            // And do whatever you need to do with the clear text message.
        } catch (JoseException e) {
            LOGGER.error("解密异常,异常信息: e - ", e);
        }
        return plaintext;
    }

    public static void main(String[] args) {
        System.out.println(encryption("Tdc@2022"));
    }
}
