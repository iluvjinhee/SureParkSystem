package com.lge.sureparksystem.parkview;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;

public class TTSController {

	private TextToSpeech tts = null;
	
	public TTSController(Context context) {
		tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
			}
		}
		);
	}
	
	void speak(String str) {
		Locale locale = new Locale("en", "US");
		tts.setLanguage(locale);
		
		tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
	}
}
