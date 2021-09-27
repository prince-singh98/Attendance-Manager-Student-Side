package app.example.studentattendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import static app.example.studentattendancemanager.ApplicationActivity.applicationList;
import static app.example.studentattendancemanager.ApplicationActivity.selectedApplicationIndex;

public class ViewApplicationActivity extends AppCompatActivity {

    TextView toTv, fromTv, subjectTv, composeTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_application);

        Toolbar toolbar = findViewById(R.id.view_application_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Application");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toTv = findViewById(R.id.toTvVa);
        fromTv = findViewById(R.id.fromTvVa);
        subjectTv = findViewById(R.id.subjectTvVa);
        composeTv = findViewById(R.id.compose_email_tv_va);

        toTv.setText(applicationList.get(selectedApplicationIndex).getTo());
        fromTv.setText(applicationList.get(selectedApplicationIndex).getFrom());
        subjectTv.setText(applicationList.get(selectedApplicationIndex).getSubject());
        composeTv.setText(applicationList.get(selectedApplicationIndex).getMessage());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

}