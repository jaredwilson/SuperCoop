package app.supercoop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Device extends AppCompatActivity {

    private Sensor sensors = new Sensor();
    private Peripheral peri = new Peripheral();

    private String path;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference sensorRef;
    private DatabaseReference sensorNameRef;
    private DatabaseReference sensorControlRef;
    private FirebaseAuth auth;
    private String deviceID;
    private String deviceName;
    private List<String> sensorNames;
    private List<String> sensorValues;
    private TextView[] sensorNameList;
    private TextView[] sensorValueList;
    private ADCController adccontroller;
    private String[] SensorValuesArray;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            deviceID = extras.getString("ID");
            deviceName = extras.getString("Name");
        }
        if (!deviceName.isEmpty()) {
            TextView textView = (TextView) findViewById(R.id.DeviceName);
            textView.setText(deviceName);
        }

        SensorValuesArray = new String[9];
        adccontroller = new ADCController();
        sensorNameList = new TextView[9];
        sensorValueList = new TextView[9];
        sensorNames = new ArrayList<String>();
        sensorValues = new ArrayList<String>();


        defaultSensorList();
        setTextViewLists();


        auth = FirebaseAuth.getInstance();
        path = "users/" + auth.getUid() + "/" + deviceID;



        sensorNameRef = database.getReference(path + "/sensorNames/");
        sensorNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                sensorNames.clear();
                sensorNames.add(snap.child("ADC1").getValue().toString());
                sensorNames.add(snap.child("ADC2").getValue().toString());
                sensorNames.add(snap.child("ADC3").getValue().toString());
                sensorNames.add(snap.child("ADC4").getValue().toString());
                sensorNames.add(snap.child("ADC5").getValue().toString());
                sensorNames.add(snap.child("ADC6").getValue().toString());
                sensorNames.add(snap.child("ADC7").getValue().toString());
                sensorNames.add(snap.child("ADC8").getValue().toString());

                SensorValuesArray = adccontroller.getConverted(sensorNames.toArray(new String[sensorNames.size()]));
                sensorValues.clear();

                for (int i = 1; i < 9; i++) {
                    sensorValues.add(SensorValuesArray[i]);
                    sensorValueList[i].setText(sensorValues.get(i - 1));
                    sensorNameList[i].setText(sensorNames.get(i - 1));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        sensorRef = database.getReference(path + "/sensors/");
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                adccontroller.setADCValues(sensors.setSensors(dataSnapshot.getValue().toString()));
                SensorValuesArray = adccontroller.getConverted(sensorNames.toArray(new String[sensorNames.size()]));
                sensorValues.clear();

                for (int i = 1; i < 9; i++) {
                    //Log.i("test", sensorValues.get(i - 1));
                    sensorValues.add(SensorValuesArray[i]);
                    sensorValueList[i].setText(sensorValues.get(i - 1));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        sensorControlRef = database.getReference(path + "/peripherals/");
        sensorControlRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                peri.setPeripheral(snap.child("ControlState").getValue(Integer.class));
                sensorValueList[0].setText(peri.getPeripheral());
                sensorNameList[0].setText("ControlState");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setTextViewLists() {

        sensorNameList[0] = findViewById(R.id.ControlState_text);
        sensorValueList[0] = findViewById(R.id.ControlStateValue_text);
        sensorNameList[1] = findViewById(R.id.ADC1_text);
        sensorValueList[1] = findViewById(R.id.ADC1Value_text);
        sensorNameList[2] = findViewById(R.id.ADC2_text);
        sensorValueList[2] = findViewById(R.id.ADC2Value_text);
        sensorNameList[3] = findViewById(R.id.ADC3_text);
        sensorValueList[3] = findViewById(R.id.ADC3Value_text);
        sensorNameList[4] = findViewById(R.id.ADC4_text);
        sensorValueList[4] = findViewById(R.id.ADC4Value_text);
        sensorNameList[5] = findViewById(R.id.ADC5_text);
        sensorValueList[5] = findViewById(R.id.ADC5Value_text);
        sensorNameList[6] = findViewById(R.id.ADC6_text);
        sensorValueList[6] = findViewById(R.id.ADC6Value_text);
        sensorNameList[7] = findViewById(R.id.ADC7_text);
        sensorValueList[7] = findViewById(R.id.ADC7Value_text);
        sensorNameList[8] = findViewById(R.id.ADC8_text);
        sensorValueList[8] = findViewById(R.id.ADC8Value_text);
    }

    private void defaultSensorList() {
        sensorValues.clear();
        sensorNames.clear();
        for (int i = 1; i < 9; i++) {
            sensorValues.add("ADCValue" + i);
            sensorNames.add("ADC" + i);
        }
    }

    public void toggleControl(View v) {
        sensorControlRef.child("ControlState").setValue(peri.togglePeripheral());
    }

    public void ADConClick (final View view) {



        final CharSequence sensorSelection[] = new CharSequence[] {"Temperature", "Humidity", "Ammonia", "Light", "Door", "Fan", "Reset"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Sensor Type");
        builder.setItems(sensorSelection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                // the user clicked on colors[which]
                String myText = sensorSelection[i].toString();
                Boolean reset = false;

                if(myText.equalsIgnoreCase("reset")) {
                    myText = "ADC";
                    reset = true;
                }

                switch (view.getId()) {
                    case R.id.ADC1_button:
                        if(reset)
                            myText += "1";
                        sensorNameRef.child("ADC1").setValue(myText);
                        break;
                    case R.id.ADC2_button:
                        if(reset)
                            myText += "2";
                        sensorNameRef.child("ADC2").setValue(myText);
                        break;
                    case R.id.ADC3_button:
                        if(reset)
                            myText += "3";
                        sensorNameRef.child("ADC3").setValue(myText);
                        break;
                    case R.id.ADC4_button:
                        if(reset)
                            myText += "4";
                        sensorNameRef.child("ADC4").setValue(myText);
                        break;
                    case R.id.ADC5_button:
                        if(reset)
                            myText += "5";
                        sensorNameRef.child("ADC5").setValue(myText);
                        break;
                    case R.id.ADC6_button:
                        if(reset)
                            myText += "6";
                        sensorNameRef.child("ADC6").setValue(myText);
                        break;
                    case R.id.ADC7_button:
                        if(reset)
                            myText += "7";
                        sensorNameRef.child("ADC7").setValue(myText);
                        break;
                    case R.id.ADC8_button:
                        if(reset)
                            myText += "8";
                        sensorNameRef.child("ADC8").setValue(myText);
                        break;
                }
            }
        });
        builder.show();
    }

    public void back(View view) {
        finish();
    }
}

class ADCController {
    private int[] before;
    private String[] after;
    private double humanVoltage;
    private final double TI_VoltageRef = 4.2;
    private final double ADC12 = 4095;

    public ADCController () {
        before = new int[9];
        after = new String[9];
        humanVoltage = 0;
    }

    public void setADCValues (int [] sensorADCValues) {
        before = sensorADCValues.clone();

    }



    public String[] getConverted (String [] sensorADCNames) {

        for(int i = 1; i <= sensorADCNames.length; i++)
        {
            String tempStr = sensorADCNames[i-1];
            if(tempStr.equals("ADC" + i))
            {
                after[i] = "No Value";
            }
            else if (tempStr.equals("Temperature"))
            {
                after[i] = convertTemp(i);
            }
            else if (tempStr.equals("Humidity")) {
                after[i] = convertHumidity(i);
            }
            else if (tempStr.equals("Ammonia")) {
                after[i] = convertAmmonia(i);
            }
            else if (tempStr.equals("Light")) {
                after[i] = convertLight(i);
            }
            else if (tempStr.equals("Door")) {
                after[i] = convertDoor(i);
            }
            else if (tempStr.equals("Fan")) {
                after[i] = convertFan(i);
            }
            else {
                after[i + 1] = "No Value";
            }
        }

        return after.clone();
    }

    private String convertTemp (int i) {
        humanVoltage = 30*(before[i]/ADC12)*TI_VoltageRef;

        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(humanVoltage) + "\u200E°";
    }

    private String convertHumidity (int i) {

        humanVoltage = 30 * (before[i]/ADC12)*TI_VoltageRef;
        DecimalFormat df = new DecimalFormat("##.##");
        return df.format(humanVoltage) + "%";
    }

    private String convertAmmonia (int i) {
        humanVoltage = (before[i]/ADC12)*TI_VoltageRef;
        DecimalFormat df = new DecimalFormat("#.##");
        return df.format(humanVoltage) + "%";
    }

    private String convertLight (int i) {
        humanVoltage = (before[i]/ADC12)*TI_VoltageRef;

        if (humanVoltage > 3)
            return "BRIGHT";
        else if (humanVoltage > 2)
            return "MODERATE";
        else if (humanVoltage > 1)
            return "DIM";
        else
            return "DARK";

    }

    private String convertDoor (int i) {
        humanVoltage = (before[i]/ADC12)*TI_VoltageRef;

        if(humanVoltage > 1)
            return "OPEN";
        else
            return "CLOSED";
    }

    private String convertFan (int i) {
        humanVoltage = (before[i]/ADC12)*TI_VoltageRef;

        if (humanVoltage > 1)
            return "ON";
        else
            return  "OFF";
    }

}