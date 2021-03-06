package com.lge.sureparksystem.test.parkserver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.lge.sureparksystem.parkserver.manager.securitymanager.AES;
import com.lge.sureparksystem.parkserver.manager.securitymanager.XXTEA;

public class AESTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void trueTest() {
		String plaintext = "test text 123\0\0\0";
		String encryptionKey = "0123456789abcdef";
		
		byte[] decrypted = null;
		try {
			byte[] cipher = XXTEA.encrypt(plaintext, encryptionKey);
			decrypted = XXTEA.decrypt(cipher, encryptionKey);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Assert.assertTrue(plaintext.equals(decrypted));
	}
	
	@Test
	public void falseTest() {
		String plaintext = "{\"EventType\":\"OPEN_THE_GATE\"";
		String encryptionKey = "1234567890";
		
		byte[] decrypted = null;
		try {
			byte[] cipher = XXTEA.encrypt(plaintext, encryptionKey);
			decrypted = XXTEA.decrypt(cipher, encryptionKey);
		} catch (Exception e) {
			;
		}

		Assert.assertFalse(plaintext.equals(decrypted));
	}
}
