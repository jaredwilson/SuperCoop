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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewUser extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser user;
    private EditText editEmail;
    private EditText editPassword;
    private EditText editPasswordAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();


        if(user != null)
        {
            auth.signOut();
        }

        editEmail = (EditText) findViewById(R.id.editTextEmail);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
        editPasswordAgain = (EditText) findViewById(R.id.editTextPasswordAgain);

    }

    public void createNewUser(View view) {

        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String passwordAgain = editPasswordAgain.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }
        else if(TextUtils.isEmpty(passwordAgain)){
            Toast.makeText(this,"Please enter matching password",Toast.LENGTH_LONG).show();
            return;
        }
        else if(!TextUtils.equals(password, passwordAgain)){
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_LONG).show();
            return;
        }
        else {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                finish();

                                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference();
                                auth = FirebaseAuth.getInstance();
                                user = auth.getCurrentUser();

                                dataRef.child("users/" + user.getUid() + "/device/example/sensors").setValue(new Sensor());

                                startActivity(new Intent(getApplicationContext(), DeviceManager.class));

                                Toast.makeText(NewUser.this,"Registration Complete", Toast.LENGTH_LONG).show();

                            }else{
                                try {
                                    throw task.getException();
                                }
                                catch (FirebaseAuthUserCollisionException existEmail)
                                {
                                    Toast.makeText(NewUser.this,"User Already Exists", Toast.LENGTH_LONG).show();
                                }
                                catch (FirebaseAuthWeakPasswordException weak)
                                {
                                    Toast.makeText(NewUser.this,"Password is too weak", Toast.LENGTH_LONG).show();
                                }
                                catch (FirebaseAuthInvalidCredentialsException invalid){
                                    Toast.makeText(NewUser.this,"Email is not a good one", Toast.LENGTH_LONG).show();
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
/*


            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //stuff
                            }
                            else {
                                Toast.makeText(NewUser.this,"Not Successful???", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

            auth = FirebaseAuth.getInstance();
            user = auth.getCurrentUser();
            if(user != null) {
                Intent deviceManager = new Intent(this, DeviceManager.class);
                startActivity(deviceManager);
            }
            else {
                Toast.makeText(NewUser.this, "what is going on", Toast.LENGTH_LONG).show();
            }*/


        }


    }

    public void backMain(View view) {
        finish();
    }
}
