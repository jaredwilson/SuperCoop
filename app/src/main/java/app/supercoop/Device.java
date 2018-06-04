package app.supercoop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Device extends AppCompatActivity {

    private Sensor sensors = new Sensor();
    private boolean fanOn = false;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference tempRef = database.getReference("sensors/temp");
    private DatabaseReference humidRef = database.getReference("sensors/humid");
    private DatabaseReference ammoniaRef = database.getReference("sensors/ammonia");
    private DatabaseReference fanRef = database.getReference("sensors/fan");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);

        tempRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                sensors.setTemp((String)dataSnapshot.getValue());
                ((TextView)findViewById(R.id.var_temp)).setText(sensors.getTemp());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        humidRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sensors.setHumid((String)dataSnapshot.getValue());
                ((TextView)findViewById(R.id.var_humid)).setText(sensors.getHumid());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        ammoniaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                sensors.setAmmonia((String)dataSnapshot.getValue());
                ((TextView)findViewById(R.id.var_ammonia)).setText(sensors.getAmmonia());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void fanClick(View v) {
        fanOn = !fanOn;

        if(fanOn)
            ; // ((TextView)findViewById(R.id.button)).setText("Fan is On");
        else
            ; // ((TextView)findViewById(R.id.button)).setText("Fan is Off");

        writeFanFirebase();

    }

    private void writeFanFirebase()
    {
        fanRef.setValue(fanOn);
    }

    public void back(View view) {
        finish();
    }
}
