package org.ripple.bouncycastle.jcajce.provider.symmetric;

import org.ripple.bouncycastle.crypto.CipherKeyGenerator;
import org.ripple.bouncycastle.crypto.engines.SkipjackEngine;
import org.ripple.bouncycastle.crypto.macs.CBCBlockCipherMac;
import org.ripple.bouncycastle.crypto.macs.CFBBlockCipherMac;
import org.ripple.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.ripple.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.ripple.bouncycastle.jcajce.provider.symmetric.util.BaseKeyGenerator;
import org.ripple.bouncycastle.jcajce.provider.symmetric.util.BaseMac;
import org.ripple.bouncycastle.jcajce.provider.symmetric.util.IvAlgorithmParameters;
import org.ripple.bouncycastle.jcajce.provider.util.AlgorithmProvider;

public final class Skipjack {
	private Skipjack() {
	}

	public static class ECB extends BaseBlockCipher {
		public ECB() {
			super(new SkipjackEngine());
		}
	}

	public static class KeyGen extends BaseKeyGenerator {
		public KeyGen() {
			super("Skipjack", 80, new CipherKeyGenerator());
		}
	}

	public static class AlgParams extends IvAlgorithmParameters {
		protected String engineToString() {
			return "Skipjack IV";
		}
	}

	public static class Mac extends BaseMac {
		public Mac() {
			super(new CBCBlockCipherMac(new SkipjackEngine()));
		}
	}

	public static class MacCFB8 extends BaseMac {
		public MacCFB8() {
			super(new CFBBlockCipherMac(new SkipjackEngine()));
		}
	}

	public static class Mappings extends AlgorithmProvider {
		private static final String PREFIX = Skipjack.class.getName();

		public Mappings() {
		}

		public void configure(ConfigurableProvider provider) {

			provider.addAlgorithm("Cipher.SKIPJACK", PREFIX + "$ECB");
			provider.addAlgorithm("KeyGenerator.SKIPJACK", PREFIX + "$KeyGen");
			provider.addAlgorithm("AlgorithmParameters.SKIPJACK", PREFIX
					+ "$AlgParams");
			provider.addAlgorithm("Mac.SKIPJACKMAC", PREFIX + "$Mac");
			provider.addAlgorithm("Alg.Alias.Mac.SKIPJACK", "SKIPJACKMAC");
			provider.addAlgorithm("Mac.SKIPJACKMAC/CFB8", PREFIX + "$MacCFB8");
			provider.addAlgorithm("Alg.Alias.Mac.SKIPJACK/CFB8",
					"SKIPJACKMAC/CFB8");

		}
	}
}
