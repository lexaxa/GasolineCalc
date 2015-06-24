package ru.gasolinecalc;

//import android.app.Activity;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.text.NumberFormat;

public class GasolineCalcActivity extends Activity implements OnKeyListener, OnClickListener{
	
    EditText days;
 	EditText cost;
    EditText kmDays;
    EditText litres;
    EditText consumption;
    
    TextView calcLitres;
    TextView calcConsumption;
    TextView calcMileage1;
    TextView calcMileage2;
    TextView calcTotalValue1;
    TextView calcTotalValue2;
	
	TextView log;
	private Toolbar toolbar;
	SharedPreferences sPref;
    private NumberFormat numberFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc);

		initToolbar();
        numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);

        cost = (EditText)findViewById(R.id.cost);
        cost.setText("36.6");
        days = (EditText)findViewById(R.id.editDays);
        kmDays = (EditText)findViewById(R.id.editKmDays);
        litres = (EditText)findViewById(R.id.editLitres);
        consumption = (EditText)findViewById(R.id.editConsumption);
        
        calcLitres = (TextView)findViewById(R.id.calcLitres);
        calcConsumption = (TextView)findViewById(R.id.calcConsumption);
        calcMileage1 = (TextView)findViewById(R.id.calcMileage1);
        calcMileage2 = (TextView)findViewById(R.id.calcMileage2);
        calcTotalValue1 = (TextView)findViewById(R.id.calcTotalValue1);
        calcTotalValue2 = (TextView)findViewById(R.id.calcTotalValue2);
        
        log = (TextView)findViewById(R.id.log);

        days.setOnKeyListener(this);
        kmDays.setOnKeyListener(this);
        litres.setOnKeyListener(this);
        consumption.setOnKeyListener(this);
        findViewById(R.id.btnAbout).setOnClickListener(this);
        findViewById(R.id.btnSettings).setOnClickListener(this);
        findViewById(R.id.btnSave).setOnClickListener(this);
        findViewById(R.id.btnLoad).setOnClickListener(this);
        
        loadText();
    }
	private void initToolbar() {
        if (findViewById(R.id.toolbar).isInEditMode()) return;
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.app_name);
		toolbar.setOnMenuItemClickListener( new Toolbar.OnMenuItemClickListener(){
			public boolean onMenuItemClick(MenuItem menuItem){
				return false;
			}
		});

		toolbar.inflateMenu(R.menu.settings);

	}
	/*
 дней км/день editDays						editKmDays
 литры		editLitres						calcConsumption1*editConsumption/100
 расход		editLitres/calcMileage1*100		editConsumption
 пробег		editDays*editKmDays				editLitres/editConsumption*100
 общ. ст.	cost*editLitres					cost*calcLitres
*//*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	outState.putString("editKmDays", ((EditText)findViewById(R.id.editKmDays)).getText().toString());
    	outState.putString("editDays", ((EditText)findViewById(R.id.editDays)).getText().toString());
    	outState.putString("editLitres", ((EditText)findViewById(R.id.editLitres)).getText().toString());
    	outState.putString("editConsumption", ((EditText)findViewById(R.id.editConsumption)).getText().toString());
    }
    */
    @Override
    public void onClick(View v) {
    	Intent intent;
    	switch (v.getId()) {
		case R.id.btnAbout:
	    	//Log.d("DEV","about");
			intent = new Intent(this, About.class);
			startActivity(intent);
			break;
		case R.id.btnSettings:
	    	//Log.d("DEV","settings");
			intent = new Intent(this, Settings.class);
			startActivity(intent);
			break;
		case R.id.btnSave:
			saveText();
			break;
		case R.id.btnLoad:
			loadText();
			break;
		}
    }
    
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
		writeLog("onKey:" + event.getAction() + "=" + keyCode);
		int length = 0;
		if (keyCode == KeyEvent.KEYCODE_BACK){
			writeLog("onKey:" + event.getAction() + "= back");
			onBackPressed();
			return true;
		}
		if (event.getAction() == KeyEvent.ACTION_UP) {
			switch (v.getId()) {
				case R.id.editDays:
					length = kmDays.getText().length();
					break;
				case R.id.editKmDays:
					length = days.getText().length();
					break;
				case R.id.editLitres:
					length = litres.getText().length();
					break;
				case R.id.editConsumption:
					length = consumption.getText().length();
					break;
				case R.id.cost:
					length = cost.getText().length();
					break;
				default:
					break;
			}
		}

		if (length > 0) {
			recalcAll();
			return true;
		}

    	return false;
    }

    public void saveText(){
    	sPref = getPreferences(MODE_PRIVATE);
    	Editor ed = sPref.edit();
		ed.putString("days",days.getText().toString());
		ed.putString("cost",cost.getText().toString());
		ed.putString("kmdays",kmDays.getText().toString());
		ed.putString("litres",litres.getText().toString());
		ed.putString("consumption",consumption.getText().toString());
    	ed.commit();
    	Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }
    
    public void loadText(){
    	sPref = getPreferences(MODE_PRIVATE);
    	days.setText(sPref.getString("days", ""));
		cost.setText(sPref.getString("cost", ""));
		kmDays.setText(sPref.getString("kmdays", ""));
		litres.setText(sPref.getString("litres", ""));
		consumption.setText(sPref.getString("consumption", ""));
    	Toast.makeText(this, "Data loaded", Toast.LENGTH_SHORT).show();
    }
    
    @Override
    protected void onDestroy() {
    	saveText();
    	super.onDestroy();
    }
    public void recalcAll(){
		setCalcMileage1();
		setCalcMileage2();
		setCalcConsumption();
		setCalcLitres();
		setCalcTotalValue1();
		setCalcTotalValue2();
    }
    
    public void setCalcMileage1(){
    	//editDays*editKmDays
		double a = getVal(days);
		double b = getVal(kmDays);
		double res = (a*b);
		calcMileage1.setText(numberFormat.format(res));
    }
    
    public void setCalcMileage2(){
    	//editLitres/editConsumption*100
    	double a = getVal(litres);
		double b = getVal(consumption);
		double res = 100*(a/b);
		calcMileage2.setText(numberFormat.format(res));
    }
    
    public void setCalcConsumption(){
    	//editLitres/calcMileage1*100
		double a = getVal(litres);
        double b = getVal(calcMileage1);
		double res = 100*(a/b);
		calcConsumption.setText(numberFormat.format(res));
    }

    public void setCalcLitres(){
    	//calcMileage1*editConsumption/100
		double a = getVal(calcMileage1);
		double b = getVal(consumption);
		double res = (a*b)/100;
		calcLitres.setText(numberFormat.format(res));
    }
    
    public void setCalcTotalValue1(){
    	//cost*editLitres
    	double a = getVal(cost);
		double b = getVal(litres);
		double res = (a*b);
		calcTotalValue1.setText(numberFormat.format(res));
    }
    
    public void setCalcTotalValue2(){
    	//cost*calcLitres
		double a = getVal(cost);
		double b = getVal(calcLitres);
		double res = (a*b);
		calcTotalValue2.setText(numberFormat.format(res));
    }

    public double getVal(TextView item){
    	if(item.getText().length() > 0){
        	return Double.parseDouble(item.getText().toString().replace(",","."));
    	}else{
    		return 1.0;
    	}
    }

    public void writeLog(String s){
    	log.setText(s);
    }
}