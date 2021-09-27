package app.example.studentattendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateProfileActivity extends AppCompatActivity {

    EditText name;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        name = findViewById(R.id.update_name);
        update = findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });

    }
    private void updateProfile() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(""+name.getText().toString())
                .build();

        currentUser.updateProfile(profileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UpdateProfileActivity.this, "User Profile Updated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}