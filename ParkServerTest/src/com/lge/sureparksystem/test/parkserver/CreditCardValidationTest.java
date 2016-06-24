package com.lge.sureparksystem.test.parkserver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.lge.sureparksystem.creditcardpaymentsystem.regex.CreditCardNumberRegEx;
import com.lge.sureparksystem.parkserver.paymentremoteproxy.PaymentRemoteProxy;

public class CreditCardValidationTest {
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void remoteProxyTest1() {
		String cardNumber = "553146000224xxxx";

		Assert.assertTrue(PaymentRemoteProxy.isVaildCreditCard(cardNumber));
	}
	
	@Test
	public void remoteProxyTest2() {
		String cardNumber = "1234567890123456";

		Assert.assertFalse(PaymentRemoteProxy.isVaildCreditCard(cardNumber));
	}
	
	@Test
	public void creditCardNumberRegExTest1() {
		String cardNumber = "5531460002241234";

		Assert.assertTrue(CreditCardNumberRegEx.isValid(cardNumber));
	}
	
	@Test
	public void creditCardNumberRegExTest2() {
		String cardNumber = "553146000224";

		Assert.assertFalse(CreditCardNumberRegEx.isValid(cardNumber));
	}
}
