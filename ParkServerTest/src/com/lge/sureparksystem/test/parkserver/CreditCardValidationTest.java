package com.lge.sureparksystem.test.parkserver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.lge.sureparksystem.parkserver.util.cardvalidation.PaymentRemoteProxy;

public class CreditCardValidationTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void trueTest() {
		String cardNumber = "553146000224xxxx";

		Assert.assertTrue(PaymentRemoteProxy.isVaildCreditCard(cardNumber));
	}
	
	@Test
	public void falseTest() {
		String cardNumber = "1234567890123456";

		Assert.assertFalse(PaymentRemoteProxy.isVaildCreditCard(cardNumber));
	}
}
