package peers;

import java.math.BigInteger;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Hex;

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
		try {
			cipher = Cipher.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CreateKeys();

	}

	private void CreateKeys() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair kp = kpg.genKeyPair();
			publicKey = kp.getPublic();
			privateKey = kp.getPrivate();
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String code(String input, PublicKey otherPublicKey) {
		byte[] in;
		try {
			//in = new BASE64Decoder().decodeBuffer(input);
			in = input.getBytes("UTF-8");

			//PublicKey otherPub = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec((new BASE64Decoder().decodeBuffer(otherPublicKey))));
			cipher.init(Cipher.ENCRYPT_MODE, otherPublicKey);
			
			byte[] encrypted = blockCipher(in,Cipher.ENCRYPT_MODE);

			char[] encryptedTranspherable = Hex.encodeHex(encrypted);
			
			return new String(encryptedTranspherable);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
	}

	public String decode(String input, PublicKey otherPublicKey) {
		byte[] in;
		try {
		
			in = Hex.decodeHex(input.toCharArray());
			//System.out.println(in.length);

			//PublicKey otherPub = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec((new BASE64Decoder().decodeBuffer(otherPublicKey))));
			//cipher.init(Cipher.DECRYPT_MODE, otherPub);
			//in = cipher.doFinal(in);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decrypted = blockCipher(in,Cipher.DECRYPT_MODE);

			return new String(decrypted,"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private byte[] append(byte[] prefix, byte[] suffix){
		byte[] toReturn = new byte[prefix.length + suffix.length];
		for (int i=0; i< prefix.length; i++){
			toReturn[i] = prefix[i];
		}
		for (int i=0; i< suffix.length; i++){
			toReturn[i+prefix.length] = suffix[i];
		}
		return toReturn;
	}

	private byte[] blockCipher(byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException{
		// string initialize 2 buffers.
		// scrambled will hold intermediate results
		byte[] scrambled = new byte[0];

		// toReturn will hold the total result
		byte[] toReturn = new byte[0];
		// if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
		int length = (mode == Cipher.ENCRYPT_MODE)? 100 : 128;

		// another buffer. this one will hold the bytes that have to be modified in this step
		byte[] buffer = new byte[length];

		for (int i=0; i< bytes.length; i++){

			// if we filled our buffer array we have our block ready for de- or encryption
			if ((i > 0) && (i % length == 0)){
				//execute the operation
				scrambled = cipher.doFinal(buffer);
				// add the result to our total result.
				toReturn = append(toReturn,scrambled);
				// here we calculate the length of the next buffer required
				int newlength = length;

				// if newlength would be longer than remaining bytes in the bytes array we shorten it.
				if (i + length > bytes.length) {
					 newlength = bytes.length - i;
				}
				// clean the buffer array
				buffer = new byte[newlength];
			}
			// copy byte into our buffer.
			buffer[i%length] = bytes[i];
		}

		// this step is needed if we had a trailing buffer. should only happen when encrypting.
		// example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
		scrambled = cipher.doFinal(buffer);

		// final step before we can return the modified data.
		toReturn = append(toReturn,scrambled);

		return toReturn;
	}
	public Key getPublicKey() {
		return publicKey;

	}
	
	public static void main(String[] args) {
		
		Encrypter enc1 = new Encrypter();
		Encrypter enc2 = new Encrypter();
		String text;
		String mid;
		String res;
		
		text = "abcdefghij";
		mid = enc1.code(text, (PublicKey) enc2.getPublicKey());
		res = enc2.decode(mid, (PublicKey) enc1.getPublicKey());
		System.out.println(res);
	}

	public static PublicKey makePublicKey(BigInteger a , BigInteger b) throws InvalidKeySpecException, NoSuchAlgorithmException {
		
		return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(a, b));
	}
}
