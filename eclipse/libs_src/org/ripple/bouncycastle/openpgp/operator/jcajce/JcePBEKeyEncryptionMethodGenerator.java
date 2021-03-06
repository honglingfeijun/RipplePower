package org.ripple.bouncycastle.openpgp.operator.jcajce;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Provider;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.ripple.bouncycastle.jcajce.DefaultJcaJceHelper;
import org.ripple.bouncycastle.jcajce.NamedJcaJceHelper;
import org.ripple.bouncycastle.jcajce.ProviderJcaJceHelper;
import org.ripple.bouncycastle.openpgp.PGPException;
import org.ripple.bouncycastle.openpgp.operator.PBEKeyEncryptionMethodGenerator;
import org.ripple.bouncycastle.openpgp.operator.PGPDigestCalculator;

/**
 * JCE based generator for password based encryption (PBE) data protection
 * methods.
 */
public class JcePBEKeyEncryptionMethodGenerator extends
		PBEKeyEncryptionMethodGenerator {
	private OperatorHelper helper = new OperatorHelper(
			new DefaultJcaJceHelper());

	/**
	 * Create a PBE encryption method generator using the provided calculator
	 * for key calculation.
	 * 
	 * @param passPhrase
	 *            the passphrase to use as the primary source of key material.
	 * @param s2kDigestCalculator
	 *            the digest calculator to use for key calculation.
	 */
	public JcePBEKeyEncryptionMethodGenerator(char[] passPhrase,
			PGPDigestCalculator s2kDigestCalculator) {
		super(passPhrase, s2kDigestCalculator);
	}

	/**
	 * Create a PBE encryption method generator using the default SHA-1 digest
	 * calculator for key calculation.
	 * 
	 * @param passPhrase
	 *            the passphrase to use as the primary source of key material.
	 */
	public JcePBEKeyEncryptionMethodGenerator(char[] passPhrase) {
		this(passPhrase, new SHA1PGPDigestCalculator());
	}

	/**
	 * Create a PBE encryption method generator using the provided calculator
	 * and S2K count for key calculation.
	 * 
	 * @param passPhrase
	 *            the passphrase to use as the primary source of key material.
	 * @param s2kDigestCalculator
	 *            the digest calculator to use for key calculation.
	 * @param s2kCount
	 *            the S2K count to use.
	 */
	public JcePBEKeyEncryptionMethodGenerator(char[] passPhrase,
			PGPDigestCalculator s2kDigestCalculator, int s2kCount) {
		super(passPhrase, s2kDigestCalculator, s2kCount);
	}

	/**
	 * Create a PBE encryption method generator using the default SHA-1 digest
	 * calculator and a S2K count other than the default of 0x60 for key
	 * calculation
	 * 
	 * @param passPhrase
	 *            the passphrase to use as the primary source of key material.
	 * @param s2kCount
	 *            the S2K count to use.
	 */
	public JcePBEKeyEncryptionMethodGenerator(char[] passPhrase, int s2kCount) {
		super(passPhrase, new SHA1PGPDigestCalculator(), s2kCount);
	}

	public JcePBEKeyEncryptionMethodGenerator setProvider(Provider provider) {
		this.helper = new OperatorHelper(new ProviderJcaJceHelper(provider));

		return this;
	}

	public JcePBEKeyEncryptionMethodGenerator setProvider(String providerName) {
		this.helper = new OperatorHelper(new NamedJcaJceHelper(providerName));

		return this;
	}

	/**
	 * Provide a user defined source of randomness.
	 * 
	 * @param random
	 *            the secure random to be used.
	 * @return the current generator.
	 */
	public PBEKeyEncryptionMethodGenerator setSecureRandom(SecureRandom random) {
		super.setSecureRandom(random);

		return this;
	}

	protected byte[] encryptSessionInfo(int encAlgorithm, byte[] key,
			byte[] sessionInfo) throws PGPException {
		try {
			String cName = PGPUtil.getSymmetricCipherName(encAlgorithm);
			Cipher c = helper.createCipher(cName + "/CFB/NoPadding");
			SecretKey sKey = new SecretKeySpec(key,
					PGPUtil.getSymmetricCipherName(encAlgorithm));

			c.init(Cipher.ENCRYPT_MODE, sKey,
					new IvParameterSpec(new byte[c.getBlockSize()]));

			return c.doFinal(sessionInfo, 0, sessionInfo.length);
		} catch (IllegalBlockSizeException e) {
			throw new PGPException("illegal block size: " + e.getMessage(), e);
		} catch (BadPaddingException e) {
			throw new PGPException("bad padding: " + e.getMessage(), e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new PGPException("IV invalid: " + e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new PGPException("key invalid: " + e.getMessage(), e);
		}
	}
}
