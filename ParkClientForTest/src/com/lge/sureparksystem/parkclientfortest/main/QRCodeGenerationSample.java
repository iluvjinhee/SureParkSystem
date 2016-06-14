package com.lge.sureparksystem.parkclientfortest.main;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class QRCodeGenerationSample {
	public static void main(String[] args) {
		try {
			String codeurl = new String("Shi Dda Yo!");

			QRCodeWriter qrCodeWriter = new QRCodeWriter();
			BitMatrix bitMatrix = qrCodeWriter.encode(codeurl, BarcodeFormat.QR_CODE, 200, 200);

			MatrixToImageConfig matrixToImageConfig = new MatrixToImageConfig(0xFF2e4e96, 0xFFFFFFFF);
			BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix, matrixToImageConfig);

			ImageIO.write(bufferedImage, "png", new File("C:\\qrcode.png"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
