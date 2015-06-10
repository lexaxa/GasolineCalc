package ru.gasolinecalc;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class About extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.about);

		TextView tvAbout = (TextView) findViewById(R.id.about);
		String aboutText = getResources().getString(R.string.sAbout);
		tvAbout.setText(Html.fromHtml(aboutText));
	}

}
