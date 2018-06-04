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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceManager extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manager);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user != null)
        {
            userEmail = user.getEmail();
        }
        else
        {
            userEmail = "Not found yet?";
        }



        final ListView lv = (ListView) findViewById(R.id.deviceList);

        String[] deviceNames = new String[] {
                "Chicken Coop",
                "Green House",
                "Add New",
                userEmail
        };

        final List<String> deviceList = new ArrayList<String>(Arrays.asList(deviceNames));

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_selectable_list_item, deviceList);

        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                switch (itemPosition) {
                    case 0:
                    case 1:
                        launchDevice();
                        break;



                    case 2:
                        addNew();
                        break;
                }
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }


    private void launchDevice() {
        Intent device = new Intent(this, Device.class);

        startActivity(device);
    }



    private void addNew() {
        Intent addNew = new Intent(this, AddNew.class);

        startActivity(addNew);
    }

    public void back(View view) {
        auth.signOut();
        finish();
    }

    public void refresh(View view) {
        Intent deviceManager = new Intent(this, DeviceManager.class);

        startActivity(deviceManager);
    }
}
