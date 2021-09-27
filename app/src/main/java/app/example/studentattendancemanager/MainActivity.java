package app.example.studentattendancemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Dialog joinClassDialog, loadingDialog;
    private EditText classCodeDialogEt, rollDialogEt, nameDialogEt, classNameDialogEt;
    private Button joinClassDialogBtn;

    public static int selectedClassIndex = 0;

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser currentUser;
    DatabaseReference classReference, studentReference;

    public static List<JoinClassModel> classList;
    RecyclerView recyclerView;
    MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Smart Attendance (Student)");

        recyclerView = findViewById(R.id.class_recycler);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        studentReference = firebaseDatabase.getReference("Students");
        classReference = firebaseDatabase.getReference("Class");


        loadingDialog = new Dialog(MainActivity.this);
        loadingDialog.setContentView(R.layout.loading_progressbar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawableResource(R.drawable.progress_background);
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        joinClassDialog = new Dialog(MainActivity.this);
        joinClassDialog.setContentView(R.layout.join_class_dialog);
        joinClassDialog.setCancelable(true);
        joinClassDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        classCodeDialogEt = joinClassDialog.findViewById(R.id.jc_class_code);
        rollDialogEt = joinClassDialog.findViewById(R.id.jc_roll);
        nameDialogEt = joinClassDialog.findViewById(R.id.jc_name);
        classNameDialogEt = joinClassDialog.findViewById(R.id.jc_class_name);

        joinClassDialogBtn = joinClassDialog.findViewById(R.id.jc_join_btn);
        joinClassDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String c = classCodeDialogEt.getText().toString();

                Query query = firebaseDatabase.getReference().child("Class").orderByChild("id").equalTo(c);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){

                            Query query1 = firebaseDatabase.getReference().child("Students").child(c)
                                    .orderByChild("roll").equalTo(Integer.parseInt(rollDialogEt.getText().toString()));
                            query1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        Toast.makeText(MainActivity.this, "Roll is already taken ", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Query query2 = firebaseDatabase.getReference().child("Students").child(c)
                                                .orderByChild("id").equalTo(currentUser.getEmail());
                                        query2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    Toast.makeText(MainActivity.this, "You are already added to this class.", Toast.LENGTH_SHORT).show();
                                                }else {
                                                    Query query3 = firebaseDatabase.getReference("Class").orderByChild("classTitle")
                                                            .equalTo(classNameDialogEt.getText().toString().trim());
                                                    query3.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (!snapshot.exists()){
                                                                Toast.makeText(MainActivity.this, "Class name does not matched.", Toast.LENGTH_SHORT).show();
                                                            }else {
                                                                joinClass(classCodeDialogEt.getText().toString(),
                                                                        Integer.parseInt(rollDialogEt.getText().toString()),
                                                                        nameDialogEt.getText().toString(),
                                                                        classNameDialogEt.getText().toString());

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {
                                                            Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                    Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });


                        }else {
                            Toast.makeText(MainActivity.this, "Class code is invalid.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        loadingDialog.show();

        classList = new ArrayList<>();
        mainAdapter = new MainAdapter(classList);
        recyclerView.setAdapter(mainAdapter);

        studentReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                classList.clear();

                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            JoinClassModel joinClassModel = dataSnapshot1.getValue(JoinClassModel.class);

                            if (Objects.equals(currentUser.getEmail() , joinClassModel.getId() )){
                                classList.add(joinClassModel);

                                mainAdapter.notifyDataSetChanged();

                            }

                        }
                    }

                }
                loadingDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void joinClass(String code, int roll, String name, String className) {
        joinClassDialog.dismiss();
        loadingDialog.show();

        if (!TextUtils.isEmpty(code) && (!TextUtils.isEmpty(String.valueOf(roll))) && (!TextUtils.isEmpty(name)) && (!TextUtils.isEmpty(className))){

            currentUser = mAuth.getCurrentUser();

            JoinClassModel joinClassModel = new JoinClassModel(code,currentUser.getEmail(),roll, name, className);
            studentReference.child(code).child(String.valueOf(roll)).setValue(joinClassModel);

            onStart();

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }

        loadingDialog.dismiss();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.student_main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.joinClass:
                classCodeDialogEt.getText().clear();
                rollDialogEt.getText().clear();
                nameDialogEt.getText().clear();
                classNameDialogEt.getText().clear();
                joinClassDialog.show();
                break;

            case R.id.logOut:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }

}