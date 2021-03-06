package org.ripple.bouncycastle.jcajce.provider.symmetric;

import java.io.IOException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.spec.PBEParameterSpec;

import org.ripple.bouncycastle.asn1.ASN1Encoding;
import org.ripple.bouncycastle.asn1.ASN1Primitive;
import org.ripple.bouncycastle.asn1.pkcs.PBKDF2Params;
import org.ripple.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.ripple.bouncycastle.jcajce.provider.config.ConfigurableProvider;
import org.ripple.bouncycastle.jcajce.provider.symmetric.util.BaseAlgorithmParameters;
import org.ripple.bouncycastle.jcajce.provider.util.AlgorithmProvider;

public class PBEPBKDF2 {
	private PBEPBKDF2() {

	}

	public static class AlgParams extends BaseAlgorithmParameters {
		PBKDF2Params params;

		protected byte[] engineGetEncoded() {
			try {
				return params.getEncoded(ASN1Encoding.DER);
			} catch (IOException e) {
				throw new RuntimeException("Oooops! " + e.toString());
			}
		}

		protected byte[] engineGetEncoded(String format) {
			if (this.isASN1FormatString(format)) {
				return engineGetEncoded();
			}

			return null;
		}

		protected AlgorithmParameterSpec localEngineGetParameterSpec(
				Class paramSpec) throws InvalidParameterSpecException {
			if (paramSpec == PBEParameterSpec.class) {
				return new PBEParameterSpec(params.getSalt(), params
						.getIterationCount().intValue());
			}

			throw new InvalidParameterSpecException(
					"unknown parameter spec passed to PBKDF2 PBE parameters object.");
		}

		protected void engineInit(AlgorithmParameterSpec paramSpec)
				throws InvalidParameterSpecException {
			if (!(paramSpec instanceof PBEParameterSpec)) {
				throw new InvalidParameterSpecException(
						"PBEParameterSpec required to initialise a PBKDF2 PBE parameters algorithm parameters object");
			}

			PBEParameterSpec pbeSpec = (PBEParameterSpec) paramSpec;

			this.params = new PBKDF2Params(pbeSpec.getSalt(),
					pbeSpec.getIterationCount());
		}

		protected void engineInit(byte[] params) throws IOException {
			this.params = PBKDF2Params.getInstance(ASN1Primitive
					.fromByteArray(params));
		}

		protected void engineInit(byte[] params, String format)
				throws IOException {
			if (this.isASN1FormatString(format)) {
				engineInit(params);
				return;
			}

			throw new IOException(
					"Unknown parameters format in PBKDF2 parameters object");
		}

		protected String engineToString() {
			return "PBKDF2 Parameters";
		}
	}

	public static class Mappings extends AlgorithmProvider {
		private static final String PREFIX = PBEPBKDF2.class.getName();

		public Mappings() {
		}

		public void configure(ConfigurableProvider provider) {
			provider.addAlgorithm("AlgorithmParameters.PBKDF2", PREFIX
					+ "$AlgParams");
			provider.addAlgorithm("Alg.Alias.AlgorithmParameters."
					+ PKCSObjectIdentifiers.id_PBKDF2, "PBKDF2");
		}
	}
}
