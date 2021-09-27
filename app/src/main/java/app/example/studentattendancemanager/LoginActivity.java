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

public class LoginActivity extends AppCompatActivity {

    TextView registerTv ,forgetPasswordBtn;
    EditText email, password;
    Button loginBtn ;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        registerTv = findViewById(R.id.tvRegister);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        loginBtn = findViewById(R.id.login);
        forgetPasswordBtn = findViewById(R.id.forgetPasswordBtn);


        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        forgetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        if (!(mAuth.getCurrentUser() == null)){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void loginUser() {
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            FirebaseUser currentUser = mAuth.getCurrentUser();
                            if (currentUser.isEmailVerified()){

                                Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(LoginActivity.this, "Please verify your mail first:", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("Error:",""+task.getException().getMessage());

                        }
                    }});

    }

}

