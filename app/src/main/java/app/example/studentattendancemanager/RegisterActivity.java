package app.example.studentattendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    TextView tvLogin;
    EditText email, password, confirmPassword;
    Button signUpBtn;

    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Profile");

        tvLogin = findViewById(R.id.tvLogin);
        email = findViewById(R.id.emailRegister);
        password = findViewById(R.id.passwordRegister);
        confirmPassword = findViewById(R.id.confirm_password);
        signUpBtn = findViewById(R.id.signUpBtn);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });



        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(confirmPassword.getText().toString())){
                    registerUser();
                }else {
                    Toast.makeText(RegisterActivity.this, "Password does not matched", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void registerUser() {

        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            mAuth.signOut();
                            Toast.makeText(RegisterActivity.this, "SignUp successful & verification link is sent to your email.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);

                        }else {
                            Toast.makeText(RegisterActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Error:",""+task.getException().getMessage());

                        }
                    }
                });

    }
}