package app.supercoop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceManager extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userEmail;
    private List<String> deviceIDs;
    private List<String> deviceNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);



        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        deviceIDs = new ArrayList<String>();
        deviceNames = new ArrayList<String>();
        DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference().child("users")
                .child(user.getUid()).child("device");


        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                deviceIDs.clear();
                deviceNames.clear();

                for(DataSnapshot snap:dataSnapshot.getChildren()) {

                    deviceIDs.add(snap.getKey());
                    deviceNames.add(snap.child("name").getValue(String.class));

                    //System.out.println(snap.getKey() + " -> " + snap.child("name").getValue(String.class));
                }

                final ListView lv = (ListView) findViewById(R.id.deviceList);

                // List<String> deviceList = deviceNames;

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                        (DeviceManager.this, android.R.layout.simple_selectable_list_item, deviceNames);

                lv.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final ListView lv = (ListView) findViewById(R.id.deviceList);

        // List<String> deviceList = deviceNames;

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_selectable_list_item, deviceNames);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                launchDevice(deviceIDs.get(itemPosition), deviceNames.get(itemPosition));
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }


    private void launchDevice(String deviceID, String deviceName) {
        Intent device = new Intent(this, Device.class);
        device.putExtra("ID", deviceID);
        device.putExtra("Name", deviceName);
        startActivity(device);
    }



    public void addNew(View view) {
        Intent addNew = new Intent(this, AddNew.class);


        startActivity(addNew);
    }

    public void back(View view) {
        auth.signOut();
        finish();
    }

    public void refresh(View view) {
//        Intent deviceManager = new Intent(this, DeviceManager.class);

        finish();
        startActivity(getIntent());
    }
}
