package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.Digest;

interface TlsHandshakeHash extends Digest {
	void init(TlsContext context);

	TlsHandshakeHash notifyPRFDetermined();

	void trackHashAlgorithm(short hashAlgorithm);

	void sealHashAlgorithms();

	TlsHandshakeHash stopTracking();

	Digest forkPRFHash();

	byte[] getFinalHash(short hashAlgorithm);
}
