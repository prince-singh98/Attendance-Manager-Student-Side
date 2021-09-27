package app.example.studentattendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static app.example.studentattendancemanager.MainActivity.classList;
import static app.example.studentattendancemanager.MainActivity.selectedClassIndex;

public class ApplyApplicationActivity extends AppCompatActivity {

    EditText fromEt, toEt, subjectEt, composeEt;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser currentUser;
    DatabaseReference applicationReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_application);

        Toolbar toolbar = findViewById(R.id.apply_application_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Compose");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        applicationReference = firebaseDatabase.getReference("Application").child(classList.get(selectedClassIndex).getClassCode());

        fromEt = findViewById(R.id.fromEt);
        toEt = findViewById(R.id.toEt);
        subjectEt = findViewById(R.id.subject_et);
        composeEt = findViewById(R.id.compose_email_et);

        fromEt.setText(mAuth.getCurrentUser().getEmail());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.compose_mail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.sendMail:
                if ((!toEt.getText().toString().isEmpty()) &&
                        (!subjectEt.getText().toString().isEmpty()) &&
                        (!composeEt.getText().toString().isEmpty()) ){

                    String id = applicationReference.push().getKey();
                    currentUser = mAuth.getCurrentUser();

                    ApplicationModel applicationModel = new ApplicationModel(id,toEt.getText().toString(),
                            currentUser.getEmail(),
                            subjectEt.getText().toString(),
                            composeEt.getText().toString());
                    applicationReference.child(id).setValue(applicationModel);

                    Toast.makeText(this, "Your requested has been submitted successfully.", Toast.LENGTH_SHORT).show();
                    finish();

                }else {
                    Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        if (item.getItemId() == android.R.id.home) {
            finish();

        }

        return super.onOptionsItemSelected(item);
    }



}