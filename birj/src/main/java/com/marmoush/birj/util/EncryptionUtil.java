package com.marmoush.birj.util;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtil {
    public static String encrypt(String str, String alg, String charset)
	    throws NoSuchAlgorithmException {
	MessageDigest md = MessageDigest.getInstance(alg);
	byte[] array = md.digest(str.getBytes(Charset.forName(charset)));
	StringBuffer sb = new StringBuffer();
	for (int i = 0; i < array.length; ++i) {
	    sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(
		    1, 3));
	}
	return sb.toString();
    }

    public static String md5(String str) {
	try {
	    return EncryptionUtil.encrypt(str, "MD5", "UTF8");
	} catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	    return null;
	}
    }
}
