package com.lge.sureparksystem.parkview.tts;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

public class TTSWrapper {

	private Context context = null;
	private TextToSpeech tts = null;
	
	public TTSWrapper(final Context context) {
		this.context = context;
		
		tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
			}
		}
		);
	}
	
	public void speak(String str) {
		Locale locale = new Locale("en", "US");
		tts.setLanguage(locale);
		
		tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
	}
}
