package app.supercoop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AddNew extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
    }

    public void back(View view) {
        finish();
    }
}
