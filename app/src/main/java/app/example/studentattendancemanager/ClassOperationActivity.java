package app.example.studentattendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static app.example.studentattendancemanager.MainActivity.classList;
import static app.example.studentattendancemanager.MainActivity.selectedClassIndex;

public class ClassOperationActivity extends AppCompatActivity {

    DatabaseReference presentRef;
    TextView rollTvVa, presentTvVa, totalTvVa, percentTvVa, absentTvVa, classNameTvVa, classCodeTvVa;

    private List<String> presentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_operation);

        Toolbar toolbar = findViewById(R.id.view_attendance_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Attendance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        rollTvVa = findViewById(R.id.tv_roll_va);
        final int roll = classList.get(selectedClassIndex).getRoll();
        Log.d("ROll",String.valueOf(roll));
        rollTvVa.setText(String.valueOf(roll));

        totalTvVa = findViewById(R.id.tv_total_va);
        presentTvVa = findViewById(R.id.tv_present_va);
        absentTvVa = findViewById(R.id.tv_absent_va);
        percentTvVa = findViewById(R.id.tv_percent_va);
        classNameTvVa = findViewById(R.id.tv_className_va);
        classCodeTvVa = findViewById(R.id.tv_classCode_va);

        String className = classList.get(selectedClassIndex).getClassName();
        String classCode = classList.get(selectedClassIndex).getClassCode();
        classNameTvVa.setText(className);
        classCodeTvVa.setText(classCode);

        presentRef = FirebaseDatabase.getInstance().getReference("Attendance")
                .child(classList.get(selectedClassIndex).getClassCode());


        presentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                presentList.clear();
                if (dataSnapshot.exists()){
                    int totalClass = 0;

                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        totalClass++;

                        for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){

                            String key = dataSnapshot2.getKey();
                            Log.d("keyVal",key);

                            AttendanceModel attendanceModel = dataSnapshot2.getValue(AttendanceModel.class);

                            if (Integer.parseInt(String.valueOf(attendanceModel.getRoll())) == Integer.parseInt(String.valueOf(roll)) &&
                                    attendanceModel.getValue() == 1){
                                presentList.add(String.valueOf(roll));
                            }

                        }
                    }
                    Log.d("keySIZE3", String.valueOf(presentList.size()));

                    presentTvVa.setText(Integer.toString(presentList.size()));
                    totalTvVa.setText(Integer.toString(totalClass));

                    int presentClass = presentList.size();

                    double value = ((double)presentClass / totalClass);
                    double percent = (value * 100);

                    int absent = totalClass - presentClass;
                    absentTvVa.setText(Integer.toString(absent));


                    DecimalFormat numberFormat = new DecimalFormat("#.00");

                    percentTvVa.setText(String.valueOf(numberFormat.format(percent)) + "%");
                }
                else {
                    presentTvVa.setText("0");
                    totalTvVa.setText("0");
                    percentTvVa.setText("0.00%");
                    Toast.makeText(ClassOperationActivity.this, "No records found", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ClassOperationActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.application_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.application:
                startActivity(new Intent(this, ApplicationActivity.class));
                break;

            case R.id.signOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;



        }

        if (item.getItemId() == android.R.id.home) {
            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}