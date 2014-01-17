package peers;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Klasa szyfrujaca dane przeslylane pomiedzy klientem a tlumaczem.
 * 
 * @author lukasz
 * 
 */
public class Encrypter {
	Cipher cipher;
	Key publicKey;
	Key privateKey;

	public Encrypter() {

		CreateKeys();

	}

	private void CreateKeys() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.genKeyPair();
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String code(String input, String otherPublicKey) {
		byte[] in;
		try {
			//in = new BASE64Decoder().decodeBuffer(input);
			in = input.getBytes("UTF-8");
			System.out.println(in.length);
			Cipher cipher;
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			PublicKey otherPub = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec((new BASE64Decoder().decodeBuffer(otherPublicKey))));
			cipher.init(Cipher.ENCRYPT_MODE, otherPub);
			in = cipher.doFinal(in);
			//cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			//in = cipher.doFinal(in);
			//return new BASE64Encoder().encode(in);
			return new String(in,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}

	public String decode(String input, String otherPublicKey) {
		byte[] in;
		try {
			//in = new BASE64Decoder().decodeBuffer(input);
			in = input.getBytes("UTF-8");
			Cipher cipher;
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			PublicKey otherPub = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec((new BASE64Decoder().decodeBuffer(otherPublicKey))));
			//cipher.init(Cipher.DECRYPT_MODE, otherPub);
			//in = cipher.doFinal(in);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			in = cipher.doFinal(in);
			//return new BASE64Encoder().encode(in);
			return new String(in,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPublicKey() {
		return new BASE64Encoder().encode( publicKey.getEncoded());

	}
	
	public static void main(String[] args) {
		
		Encrypter enc1 = new Encrypter();
		Encrypter enc2 = new Encrypter();
		String text = "a";
		String mid = enc1.code(text, enc2.getPublicKey());
		String res = enc2.decode(mid, enc1.getPublicKey());
		System.out.println(res);
		text = "ab";
		mid = enc1.code(text, enc2.getPublicKey());
		res = enc2.decode(mid, enc1.getPublicKey());
		System.out.println(res);
		text = "abc";
		mid = enc1.code(text, enc2.getPublicKey());
		res = enc2.decode(mid, enc1.getPublicKey());
		System.out.println(res);
		text = "abcd";
		mid = enc1.code(text, enc2.getPublicKey());
		res = enc2.decode(mid, enc1.getPublicKey());
		System.out.println(res);
		text = "abcde";
		mid = enc1.code(text, enc2.getPublicKey());
		res = enc2.decode(mid, enc1.getPublicKey());
		System.out.println(res);
		text = "abcdef";
		mid = enc1.code(text, enc2.getPublicKey());
		res = enc2.decode(mid, enc1.getPublicKey());
		System.out.println(res);
		text = "abcdefg";
		mid = enc1.code(text, enc2.getPublicKey());
		res = enc2.decode(mid, enc1.getPublicKey());
		System.out.println(res);
		text = "abcdefgh";
		mid = enc1.code(text, enc2.getPublicKey());
		res = enc2.decode(mid, enc1.getPublicKey());
		System.out.println(res);
		text = "abcdefghi";
		mid = enc1.code(text, enc2.getPublicKey());
		res = enc2.decode(mid, enc1.getPublicKey());
		System.out.println(res);
		text = "abcdefghij";
		mid = enc1.code(text, enc2.getPublicKey());
		res = enc2.decode(mid, enc1.getPublicKey());
		System.out.println(res);
	}
}
