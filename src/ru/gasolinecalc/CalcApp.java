package ru.gasolinecalc;

import android.app.Application;
import android.content.res.Configuration;

public class CalcApp extends Application {
	private static CalcApp singleton;
	// Возвращает экземпляр данного класса
	public static CalcApp getInstance() {
		return singleton;
	}
	@Override
	public final void onCreate() {
		super.onCreate();
		singleton = this;
	}
	@Override
	public final void onTerminate() {
		super.onTerminate();
	}
	@Override
	public final void onLowMemory() {
		super.onLowMemory();
	}
	@Override
	public final void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
