package org.ripple.bouncycastle.pqc.crypto.mceliece;

import java.security.SecureRandom;

import org.ripple.bouncycastle.crypto.KeyGenerationParameters;

public class McElieceKeyGenerationParameters extends KeyGenerationParameters {
	private McElieceParameters params;

	public McElieceKeyGenerationParameters(SecureRandom random,
			McElieceParameters params) {
		// XXX key size?
		super(random, 256);
		this.params = params;
	}

	public McElieceParameters getParameters() {
		return params;
	}
}
