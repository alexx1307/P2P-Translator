package peers;

import java.security.Key;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;




/**
 * Klasa szyfrujaca dane przeslylane pomiedzy klientem a tlumaczem.
 * 
 * @author lukasz
 * 
 */
public class Encrypter {
	Cipher cipher;

	public Encrypter(){
		
	}
	
	public String code(byte[] input,String publicKey) {
		return publicKey;	
	}

	public String decode(byte[] input,String publicKey) {
		return publicKey;
		
	}
	public String getPublicKey(){
		return null;
		
	
	}
}
