package app.supercoop;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import org.json.*;
import org.w3c.dom.Text;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Device extends AppCompatActivity {

    private Sensor sensors = new Sensor();
    private Peripheral peri = new Peripheral();
    private boolean fanOn = false;

    private String path;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference sensorRef; // = database.getReference("sensors/temp");
    private DatabaseReference sensorNameRef;
    private DatabaseReference sensorValueRef;
    private DatabaseReference sensorControlRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String deviceID;
    private String deviceName;
    private List<String> sensorNames;
    private List<String> sensorValues;
    private TextView[] sensorNameList;
    private TextView[] sensorValueList;


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

        sensorNameList = new TextView[9];
        sensorValueList = new TextView[9];
        sensorNames = new ArrayList<String>();
        sensorValues = new ArrayList<String>();

        defaultSensorList();
        setTextViewLists();


        auth = FirebaseAuth.getInstance();
        path = "users/" + auth.getUid() + "/device/" + deviceID;

        sensorRef = database.getReference(path + "/sensors/");
        sensorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                sensors.setSensors(dataSnapshot.getValue().toString());
                //((TextView)findViewById(R.id.var_temp)).setText(sensors.getTemp());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

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

                for (int i = 1; i < 9; i++) {
                    String str = sensorNames.get(i - 1);
                    Log.i("sensorName", "hi" + str);
                    sensorNameList[i].setText(sensorNames.get(i - 1));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        sensorValueRef = database.getReference(path + "/sensorValues/");
        sensorValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snap) {
                sensorValues.clear();

                sensorValues.add(snap.child("ADC1").getValue().toString());
                sensorValues.add(snap.child("ADC2").getValue().toString());
                sensorValues.add(snap.child("ADC3").getValue().toString());
                sensorValues.add(snap.child("ADC4").getValue().toString());
                sensorValues.add(snap.child("ADC5").getValue().toString());
                sensorValues.add(snap.child("ADC6").getValue().toString());
                sensorValues.add(snap.child("ADC7").getValue().toString());
                sensorValues.add(snap.child("ADC8").getValue().toString());


                for (int i = 1; i < 9; i++) {
                    Log.i("test", sensorValues.get(i - 1));
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

        sensorNameList[0] = (TextView) findViewById(R.id.ControlState_text);
        sensorValueList[0] = (TextView) findViewById(R.id.ControlStateValue_text);
        sensorNameList[1] = (TextView) findViewById(R.id.ADC1_text);
        sensorValueList[1] = (TextView) findViewById(R.id.ADC1Value_text);
        sensorNameList[2] = (TextView) findViewById(R.id.ADC2_text);
        sensorValueList[2] = (TextView) findViewById(R.id.ADC2Value_text);
        sensorNameList[3] = (TextView) findViewById(R.id.ADC3_text);
        sensorValueList[3] = (TextView) findViewById(R.id.ADC3Value_text);
        sensorNameList[4] = (TextView) findViewById(R.id.ADC4_text);
        sensorValueList[4] = (TextView) findViewById(R.id.ADC4Value_text);
        sensorNameList[5] = (TextView) findViewById(R.id.ADC5_text);
        sensorValueList[5] = (TextView) findViewById(R.id.ADC5Value_text);
        sensorNameList[6] = (TextView) findViewById(R.id.ADC6_text);
        sensorValueList[6] = (TextView) findViewById(R.id.ADC6Value_text);
        sensorNameList[7] = (TextView) findViewById(R.id.ADC7_text);
        sensorValueList[7] = (TextView) findViewById(R.id.ADC7Value_text);
        sensorNameList[8] = (TextView) findViewById(R.id.ADC8_text);
        sensorValueList[8] = (TextView) findViewById(R.id.ADC8Value_text);
    }

    private void defaultSensorList() {
        sensorValues.clear();
        sensorNames.clear();
        for (int i = 0; i < 8; i++) {
            sensorValues.add("ADCValue" + (i+1));
            sensorNames.add("ADC" + (i+1));
        }
    }

    private void setSensorValues() {

    }

    private void setSensorNames() {

    }

    public void toggleControl(View v) {

        sensorControlRef.child("ControlState").setValue(peri.togglePeripheral());

      //  writeFanFirebase();

    }

   // private void writeFanFirebase()
   // {
    //    fanRef.setValue(fanOn);
    //}

    public void back(View view) {
        finish();
    }
}
