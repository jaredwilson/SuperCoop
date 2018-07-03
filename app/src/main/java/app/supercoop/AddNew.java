package app.supercoop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddNew extends AppCompatActivity {


    private EditText editDeviceID;
    private EditText editDeviceName;
    private FirebaseAuth auth;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);

        editDeviceID = (EditText) findViewById(R.id.editTextDeviceID);
        editDeviceName = (EditText) findViewById(R.id.editTextDeviceNickname);

    }

    public void back(View view) {
        finish();
    }

    public void addDevice(View view) {


        String DeviceID = editDeviceID.getText().toString().trim();
        String DeviceNickname = editDeviceName.getText().toString().trim();

        if(DeviceID.isEmpty() || DeviceNickname.isEmpty())
        {
            Toast.makeText(this,"Please fill out both text fields",Toast.LENGTH_LONG).show();
            return;
        }
            else
        {
            DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            String path = "users/" + user.getUid() + "/device/" + DeviceID;
            dataRef.child("devices/" + DeviceID + "/uid").setValue(user.getUid());
            dataRef.child("devices/" + DeviceID + "/name").setValue(DeviceNickname);

            dataRef.child(path + "/sensors").setValue(new Sensor());
            dataRef.child(path + "/name").setValue(DeviceNickname);
            dataRef.child(path + "/peripherals").setValue(new Peripheral());
            dataRef.child(path + "/sensorNames").setValue(new FirebaseStringInit());
            dataRef.child(path + "/sensorValues").setValue(new FirebaseStringInit(1));
            finish();
        }
    }
}

class FirebaseStringInit {
    public String ADC1;
    public String ADC2;
    public String ADC3;
    public String ADC4;
    public String ADC5;
    public String ADC6;
    public String ADC7;
    public String ADC8;

    public FirebaseStringInit() {
        ADC1 = "ADC1";
        ADC2 = "ADC2";
        ADC3 = "ADC3";
        ADC4 = "ADC4";
        ADC5 = "ADC5";
        ADC6 = "ADC6";
        ADC7 = "ADC7";
        ADC8 = "ADC8";
    }

    public FirebaseStringInit(int x) {
        ADC1 = "no value";
        ADC2 = "no value";
        ADC3 = "no value";
        ADC4 = "no value";
        ADC5 = "no value";
        ADC6 = "no value";
        ADC7 = "no value";
        ADC8 = "no value";
    }
}
