package app.supercoop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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

        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        dataRef.child("devices/" + DeviceID + "/uid").setValue(user.getUid());
        dataRef.child("devices/" + DeviceID + "/name").setValue(DeviceNickname);

        dataRef.child("users/" + user.getUid() + "/device/" + DeviceID + "/sensors").setValue(new Sensor());
        dataRef.child("users/" + user.getUid() + "/device/" + DeviceID + "/name").setValue(DeviceNickname);

        finish();
    }
}
