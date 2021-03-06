package org.ripple.bouncycastle.openpgp.operator.bc;

import java.security.SecureRandom;

import org.ripple.bouncycastle.crypto.AsymmetricBlockCipher;
import org.ripple.bouncycastle.crypto.InvalidCipherTextException;
import org.ripple.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.ripple.bouncycastle.crypto.params.ParametersWithRandom;
import org.ripple.bouncycastle.openpgp.PGPException;
import org.ripple.bouncycastle.openpgp.PGPPublicKey;
import org.ripple.bouncycastle.openpgp.operator.PublicKeyKeyEncryptionMethodGenerator;

/**
 * A method generator for supporting public key based encryption operations.
 */
public class BcPublicKeyKeyEncryptionMethodGenerator extends
		PublicKeyKeyEncryptionMethodGenerator {
	private SecureRandom random;
	private BcPGPKeyConverter keyConverter = new BcPGPKeyConverter();

	/**
	 * Create a public key encryption method generator with the method to be
	 * based on the passed in key.
	 * 
	 * @param key
	 *            the public key to use for encryption.
	 */
	public BcPublicKeyKeyEncryptionMethodGenerator(PGPPublicKey key) {
		super(key);
	}

	/**
	 * Provide a user defined source of randomness.
	 * 
	 * @param random
	 *            the secure random to be used.
	 * @return the current generator.
	 */
	public BcPublicKeyKeyEncryptionMethodGenerator setSecureRandom(
			SecureRandom random) {
		this.random = random;

		return this;
	}

	protected byte[] encryptSessionInfo(PGPPublicKey pubKey, byte[] sessionInfo)
			throws PGPException {
		try {
			AsymmetricBlockCipher c = BcImplProvider
					.createPublicKeyCipher(pubKey.getAlgorithm());

			AsymmetricKeyParameter key = keyConverter.getPublicKey(pubKey);

			if (random == null) {
				random = new SecureRandom();
			}

			c.init(true, new ParametersWithRandom(key, random));

			return c.processBlock(sessionInfo, 0, sessionInfo.length);
		} catch (InvalidCipherTextException e) {
			throw new PGPException("exception encrypting session info: "
					+ e.getMessage(), e);
		}
	}
}
