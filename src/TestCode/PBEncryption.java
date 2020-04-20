package TestCode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AlgorithmParameters;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import app.util.EncryptionUtil;
import app.util.FileUtil;

public class PBEncryption {

	public static void main(String[] args) {
		Security.setProperty("crypto.policy", "unlimited");
		PBEncryption app = new PBEncryption();
		try {
			app.run();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	String data = "This is the secret data need to Encrypt";
	String password = "123456";
	byte[] salt3DES = EncryptionUtil.generateSalt(8);
	byte[] saltSHA256 = EncryptionUtil.generateSalt(128);
	int noIterations = 65536;
	int keyLength = 256;

	public void run() throws UnsupportedEncodingException {

		System.out.println("rawData> " + data);
		byte[] encryptPBEWithMD5AndTripleDESData = encryptPBEWithMD5AndTripleDES(data.getBytes(),
				password.toCharArray(), salt3DES, noIterations);
		System.out.println("encryptPBEWithMD5AndTripleDES> " + encryptPBEWithMD5AndTripleDESData);

		FileUtil.exportByteArrayToFile(Paths.get("encryptPBEWithMD5AndTripleDESData.xml").toAbsolutePath().toString(),
				encryptPBEWithMD5AndTripleDESData);
		byte[] in = FileUtil.importByteArrayFromFile("encryptPBEWithMD5AndTripleDESData.xml");

		byte[] decryptedData0 = decryptPBEWithMD5AndTripleDES(in, password.toCharArray(), noIterations);
		System.out.println("decryptPBKDF2WithHmacSHA256> " + new String(decryptedData0));

		
		
		System.out.println("salt3DES: " + salt3DES + " Size> " + salt3DES.length);
		System.out.println("saltSHA256: " + saltSHA256 + " Size> " + saltSHA256.length);

		testPBKDF2ENED(data.getBytes(), password.toCharArray());

	}

	public void testPBKDF2ENED(byte[] data, char[] password) {
		byte[] en = encryptPBKDF2WithHmacSHA256(data, password);
		byte[] de = decryptPBKDF2WithHmacSHA256(en, password);
		System.out.println("encrypt PBKDF2WithHmacSHA256> " + en);
		System.out.println("decrypt PBKDF2WithHmacSHA256> " + new String(de));
	}

	public byte[] encryptPBEWithMD5AndTripleDES(byte[] data, char[] password, byte[] salt, int noIterations) {
		try {
			String method = "PBEWithMD5AndTripleDES";
			SecretKeyFactory kf = SecretKeyFactory.getInstance(method);
			PBEKeySpec keySpec = new PBEKeySpec(password);
			SecretKey key = kf.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance(method);
			PBEParameterSpec params = new PBEParameterSpec(salt, noIterations);
			cipher.init(Cipher.ENCRYPT_MODE, key, params);
			byte[] cipherText = cipher.doFinal(data);

			return EncryptionUtil.concateByte(salt, cipherText);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public byte[] decryptPBEWithMD5AndTripleDES(byte[] data, char[] password, int noIterations) {
		try {
			byte[] salt = Arrays.copyOfRange(data, 0, 8);
			byte[] ciphertext = Arrays.copyOfRange(data, 8, data.length);

			String method = "PBEWithMD5AndTripleDES";
			SecretKeyFactory kf = SecretKeyFactory.getInstance(method);
			PBEKeySpec keySpec = new PBEKeySpec(password);
			SecretKey key = kf.generateSecret(keySpec);
			Cipher ciph = Cipher.getInstance(method);
			PBEParameterSpec params = new PBEParameterSpec(salt, noIterations);
			ciph.init(Cipher.DECRYPT_MODE, key, params);

			return ciph.doFinal(ciphertext);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Spurious encryption error");
		}
	}

	public byte[] encryptPBKDF2WithHmacSHA256(byte[] data, char[] password) {
		return encryptPBKDF2WithHmacSHA256(data, password, EncryptionUtil.generateSalt(128), noIterations, keyLength);
	}

	public byte[] decryptPBKDF2WithHmacSHA256(byte[] data, char[] password) {
		return decryptPBKDF2WithHmacSHA256(data, password, noIterations, keyLength);
	}

	public byte[] encryptPBKDF2WithHmacSHA256(byte[] data, char[] password, byte[] salt, int noIterations,
			int keyLength) {
		try {
			/* Derive the key, given password and salt. */
			String method = "PBKDF2WithHmacSHA256";
			SecretKeyFactory factory = SecretKeyFactory.getInstance(method);
			PBEKeySpec spec = new PBEKeySpec(password, salt, noIterations, keyLength);
			SecretKey secretkey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

			/* Encrypt the message. */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			AlgorithmParameters params = cipher.getParameters();
			byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
			cipher.init(Cipher.ENCRYPT_MODE, secretkey, new IvParameterSpec(iv));
			byte[] cipherText = cipher.doFinal(data);

			return EncryptionUtil.concateByte(salt, iv, cipherText);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Spurious encryption error");
		}
	}

	public byte[] decryptPBKDF2WithHmacSHA256(byte[] data, char[] password, int noIterations, int keyLength) {
		try {
			byte[] salt = Arrays.copyOfRange(data, 0, 128);
			byte[] iv = Arrays.copyOfRange(data, 128, 128 + 16);
			byte[] ciphertext = Arrays.copyOfRange(data, 128 + 16, data.length);

			String method = "PBKDF2WithHmacSHA256";
			SecretKeyFactory factory = SecretKeyFactory.getInstance(method);
			PBEKeySpec spec = new PBEKeySpec(password, salt, noIterations, keyLength);
			SecretKey secretkey = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");

			/* Decrypt the message, given derived key and initialization vector. */
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, secretkey, new IvParameterSpec(iv));
			byte[] plaintext = cipher.doFinal(ciphertext);

			return plaintext;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Spurious encryption error");
		}
	}

}
