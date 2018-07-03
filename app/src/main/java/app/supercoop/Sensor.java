package app.supercoop;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Sensor {
    public int ADC1;
    public int ADC2;
    public int ADC3;
    public int ADC4;
    public int ADC5;
    public int ADC6;
    public int ADC7;
    public int ADC8;
    private String[] ADC_identifier;


    public Sensor() {
        ADC1 = 0;
        ADC2 = 0;
        ADC3 = 0;
        ADC4 = 0;
        ADC5 = 0;
        ADC6 = 0;
        ADC7 = 0;
        ADC8 = 0;
    }


    public void setSensors(String jsonString) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);

            Log.i("info", json.toString());

            ADC1 = json.getInt("ADC1");
            ADC2 = json.getInt("ADC2");
            ADC3 = json.getInt("ADC3");
            ADC4 = json.getInt("ADC4");
            ADC5 = json.getInt("ADC5");
            ADC6 = json.getInt("ADC6");
            ADC7 = json.getInt("ADC7");
            ADC8 = json.getInt("ADC8");
            //Log.i("info", "Hi " + ADC7 );
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return;
    }

}
