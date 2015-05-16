package ru.gasolinecalc;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GasolineCalcActivity extends Activity implements OnKeyListener{
	
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calc);
        
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

        Button btnSettings = (Button) findViewById(R.id.btnSettings);
        btnSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
			}
		});
        
        Button btnAbout = (Button) findViewById(R.id.btnAbout);
        btnSettings.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View v) {
				
				
			}
        });
    }
/*
 дней км/день editDays						editKmDays
 литры		editLitres						calcConsumption1*editConsumption/100
 расход		editLitres/calcMileage1*100		editConsumption
 пробег		editDays*editKmDays				editLitres/editConsumption*100
 общ. ст.	cost*editLitres					cost*calcLitres
*/
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event){
    	int length = 0;
		if(event.getAction() == KeyEvent.ACTION_UP){
	    	switch(v.getId()){
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
	    	}
		}
		if(length > 0){
			recalcAll();
			return true;
    	}
    	return false;
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
		calcMileage1.setText(String.format("%.2f", res) + "");
    }
    
    public void setCalcMileage2(){
    	//editLitres/editConsumption*100
    	double a = getVal(litres);
		double b = getVal(consumption);
		double res = 100*(a/b);
		calcMileage2.setText(String.format("%.2f", res) + "");
    }
    
    public void setCalcConsumption(){
    	//editLitres/calcMileage1*100
		double a = getVal(litres);
		double b = getVal(calcMileage1);
		double res = 100*(a/b);
		calcConsumption.setText(String.format("%.2f", res) + "");
    }

    public void setCalcLitres(){
    	//calcMileage1*editConsumption/100
		double a = getVal(calcMileage1);
		double b = getVal(consumption);
		double res = (a*b)/100;
		calcLitres.setText(String.format("%.2f", res)+"");
    }
    
    public void setCalcTotalValue1(){
    	//cost*editLitres
    	double a = getVal(cost);
		double b = getVal(litres);
		double res = (a*b);
		calcTotalValue1.setText(String.format("%.2f", res)+"");
    }
    
    public void setCalcTotalValue2(){
    	//cost*calcLitres
		double a = getVal(cost);
		double b = getVal(calcLitres);
		double res = (a*b);
		calcTotalValue2.setText(String.format("%.2f", res)+"");
    }
    
    public double getVal(EditText item){
    	if(item.getText().length() > 0){
        	return Double.parseDouble(item.getText().toString());
    	}else{
    		return 1.0;
    	}
    }

    public double getVal(TextView item){
    	if(item.getText().length() > 0){
        	return Double.parseDouble(item.getText().toString());
    	}else{
    		return 1.0;
    	}
    }
    
    public int getValInt(EditText item){
    	if(item.getText().length() > 0){
    		return Integer.parseInt(item.getText().toString());
    	}else{
    		return 1;
    	}
    }
    public int getValInt(TextView item){
    	if(item.getText().length() > 0){
    		return Integer.parseInt(item.getText().toString());
    	}else{
    		return 1;
    	}
    }
    public void writeLog(String s){
    	log.setText(s);
    }
}