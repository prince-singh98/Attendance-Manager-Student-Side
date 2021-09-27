package app.example.studentattendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static app.example.studentattendancemanager.MainActivity.classList;
import static app.example.studentattendancemanager.MainActivity.selectedClassIndex;

public class ApplicationActivity extends AppCompatActivity {

    public static int selectedApplicationIndex = 0;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser currentUser;
    DatabaseReference applicationReference;

    public static List<ApplicationModel> applicationList;
    RecyclerView applicationRecycler;
    ApplicationAdapter applicationAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);


        Toolbar toolbar = findViewById(R.id.application_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leave Applications");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        applicationReference = firebaseDatabase.getReference("Application")
                .child(classList.get(selectedClassIndex).getClassCode());

        applicationRecycler = findViewById(R.id.application_recycler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        applicationRecycler.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        applicationList = new ArrayList<>();
        applicationAdapter = new ApplicationAdapter(ApplicationActivity.this , applicationList);
        applicationRecycler.setAdapter(applicationAdapter);

        applicationReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                applicationList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ApplicationModel applicationModel = dataSnapshot.getValue(ApplicationModel.class);

                    if (Objects.equals(mAuth.getCurrentUser().getEmail() , applicationModel.getFrom() )){
                        applicationList.add(applicationModel);

                        applicationAdapter.notifyDataSetChanged();
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ApplicationActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void goToApplyApplication(View view) {
        Intent intent = new Intent(ApplicationActivity.this, ApplyApplicationActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

}