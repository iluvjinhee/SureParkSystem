package com.lge.sureparksystem.parkview.tts;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

public class TTSWrapper {
	private final String TAG = "ParkView";

	private Context context = null;
	private TextToSpeech tts = null;
	
	private int initStatus = TextToSpeech.ERROR;
	
	public TTSWrapper(final Context context) {
		this.context = context;
		
		tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				Log.d(TAG, "TTS Inited: " + status);
				
				initStatus = status;
			}
		}
		);
	}
	
	public void speak(String str) {
		if(initStatus != TextToSpeech.SUCCESS) {
			Log.d(TAG, "TTS is not ready!!");
			
			return;
		}
		Locale locale = new Locale("en", "US");
		tts.setLanguage(locale);
		
		Log.d(TAG, "TTS: " + str);
		
		tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
	}
}
