package com.lge.sureparksystem.creditcardpaymentsystem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.validator.CreditCardValidator;

public class Main {
	static CreditCardValidator creditCardValidator = null;

	public static void main(String[] args) throws IOException {
		creditCardValidator = new CreditCardValidator();

		while (true) {
			ServerSocket listener = new ServerSocket(9090);
			BufferedReader in = null;
			PrintWriter out = null;

			Socket socket = listener.accept();
			try {
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new PrintWriter(socket.getOutputStream(), true);

				String input = in.readLine();

				if (input != null) {
					boolean isValid = validateCard(input);
					if (isValid) {
						out.println("OK");
					} else {
						out.println("ERROR");
					}
				}
			} finally {
				socket.close();
			}

			listener.close();
		}
	}

	private static boolean validateCard(String card) {
		return creditCardValidator.isValid(card);
	}
}
