package org.ripple.bouncycastle.asn1.cms;

import org.ripple.bouncycastle.asn1.ASN1Encodable;
import org.ripple.bouncycastle.asn1.ASN1EncodableVector;
import org.ripple.bouncycastle.asn1.ASN1Object;
import org.ripple.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.ripple.bouncycastle.asn1.ASN1Primitive;
import org.ripple.bouncycastle.asn1.ASN1Sequence;
import org.ripple.bouncycastle.asn1.ASN1TaggedObject;
import org.ripple.bouncycastle.asn1.DERSequence;

public class OtherRecipientInfo extends ASN1Object {
	private ASN1ObjectIdentifier oriType;
	private ASN1Encodable oriValue;

	public OtherRecipientInfo(ASN1ObjectIdentifier oriType,
			ASN1Encodable oriValue) {
		this.oriType = oriType;
		this.oriValue = oriValue;
	}

	/**
	 * @deprecated use getInstance().
	 * @param seq
	 */
	public OtherRecipientInfo(ASN1Sequence seq) {
		oriType = ASN1ObjectIdentifier.getInstance(seq.getObjectAt(0));
		oriValue = seq.getObjectAt(1);
	}

	/**
	 * return a OtherRecipientInfo object from a tagged object.
	 * 
	 * @param obj
	 *            the tagged object holding the object we want.
	 * @param explicit
	 *            true if the object is meant to be explicitly tagged false
	 *            otherwise.
	 * @exception IllegalArgumentException
	 *                if the object held by the tagged object cannot be
	 *                converted.
	 */
	public static OtherRecipientInfo getInstance(ASN1TaggedObject obj,
			boolean explicit) {
		return getInstance(ASN1Sequence.getInstance(obj, explicit));
	}

	/**
	 * return a OtherRecipientInfo object from the given object.
	 * 
	 * @param obj
	 *            the object we want converted.
	 * @exception IllegalArgumentException
	 *                if the object cannot be converted.
	 */
	public static OtherRecipientInfo getInstance(Object obj) {
		if (obj instanceof OtherRecipientInfo) {
			return (OtherRecipientInfo) obj;
		}

		if (obj != null) {
			return new OtherRecipientInfo(ASN1Sequence.getInstance(obj));
		}

		return null;
	}

	public ASN1ObjectIdentifier getType() {
		return oriType;
	}

	public ASN1Encodable getValue() {
		return oriValue;
	}

	/**
	 * Produce an object suitable for an ASN1OutputStream.
	 * 
	 * <pre>
	 * OtherRecipientInfo ::= SEQUENCE {
	 *    oriType OBJECT IDENTIFIER,
	 *    oriValue ANY DEFINED BY oriType }
	 * </pre>
	 */
	public ASN1Primitive toASN1Primitive() {
		ASN1EncodableVector v = new ASN1EncodableVector();

		v.add(oriType);
		v.add(oriValue);

		return new DERSequence(v);
	}
}
