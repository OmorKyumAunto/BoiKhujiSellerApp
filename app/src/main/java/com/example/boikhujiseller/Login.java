package com.example.boikhujiseller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.boikhujiseller.DashboardActions.Edit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText suser,spass;
    Button slogin;
    private FirebaseAuth sAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        changeStatusBarColor();
        suser=findViewById(R.id.userid);
        spass=findViewById(R.id.password);
        sAuth=FirebaseAuth.getInstance();
        slogin=findViewById(R.id.loginbtn);
        DatabaseReference table_user= FirebaseDatabase.getInstance().getReference("SellerDetails");

        slogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = suser.getText().toString().trim();
                String password = spass.getText().toString().trim();
                if(TextUtils.isEmpty(id)){
                    suser.setError("Email Is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    spass.setError("Password Is Required");
                    return;
                }
                if(password.length()<6){
                    spass.setError("Password must be large then 6 Character");
                    return;
                }

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(suser.getText().toString()).exists()) {

                            Agent agent = snapshot.child(suser.getText().toString()).getValue(Agent.class);
                            if (agent.getPassword().equals(spass.getText().toString())) {
                                Toast.makeText(Login.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                                finish();
                            } else {
                                Toast.makeText(Login.this, "Password Wrong!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(Login.this, "User Not Exist!", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


    }
    private void changeStatusBarColor() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.toolbar));

        }
    }
}