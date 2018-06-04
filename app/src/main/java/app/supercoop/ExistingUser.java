package app.supercoop;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class ExistingUser extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private EditText editTextEmail;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_user);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
    }


    public void loginExistingUser(View view) {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        else {
        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            user = auth.getCurrentUser();
                            finish();
                            startActivity(new Intent(getApplicationContext(), DeviceManager.class));

                        }else{
                            Toast.makeText(ExistingUser.this,"Nope something wrong", Toast.LENGTH_LONG).show();
                            try {
                                throw task.getException();
                            }
                            catch (Exception e) {

                            }
                        }
                    }
                });
        }


        Intent deviceManager = new Intent(this, DeviceManager.class);

        startActivity(deviceManager);
    }

    public void backMain(View view) {
        finish();
    }
}
