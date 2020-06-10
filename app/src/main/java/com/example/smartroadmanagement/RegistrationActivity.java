package com.example.smartroadmanagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private EditText fullName,userPassword,userEmail;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth fAuth;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
            finish();
        }

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    // Upload data to database
                    final String name = fullName.getText().toString();
                    final String email = userEmail.getText().toString().trim();
                    String password = userPassword.getText().toString().trim();

                    fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this, "User Created", Toast.LENGTH_SHORT).show();
                                String user_id = fAuth.getCurrentUser().getUid();
                                int key = radioGroup.getCheckedRadioButtonId();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
                                Map newPost = new HashMap();
                                newPost.put("name",name);
                                newPost.put("email",email);
                                if(key == R.id.rbCivilian){
                                    newPost.put("type","Civilian");
                                    newPost.put("status",0);
                                    newPost.put("numberOfReports",0);
                                    newPost.put("numberOfTrueReports",0);
                                }
                                else if(key == R.id.rbContractor){
                                    newPost.put("type","Contractor");
                                }
                                reference.setValue(newPost);

                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                            else{
                                Toast.makeText(RegistrationActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
    }

    private void setupUIViews(){
        fullName = (EditText)findViewById(R.id.etUserName);
        userPassword = (EditText)findViewById(R.id.etUserPassword);
        userEmail = (EditText)findViewById(R.id.etUserEmail);
        regButton = (Button)findViewById(R.id.btnRegister);
        userLogin = (TextView)findViewById(R.id.tvUserLogin);
        radioGroup = (RadioGroup)findViewById(R.id.rg);
    }

    private boolean validate(){
        Boolean result = false;

        String name = fullName.getText().toString();
        String password = userPassword.getText().toString();
        String email = userEmail.getText().toString();

        if(name.isEmpty() || password.isEmpty() || email.isEmpty()){
            Toast.makeText(this,"Please enter all the details.", Toast.LENGTH_SHORT).show();
        }
        else{
            if(radioGroup.getCheckedRadioButtonId()==(-1)){
                result = false;
                Toast.makeText(this, "Select User Type.", Toast.LENGTH_SHORT).show();
            }
            else{
                result = true;
            }
        }

        return result;
    }
}
