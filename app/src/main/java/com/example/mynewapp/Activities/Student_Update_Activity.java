package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mynewapp.Adapters.Student_Update_Adapter;
import com.example.mynewapp.Model.Student_Update;
import com.example.mynewapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_Update_Activity extends AppCompatActivity implements Student_Update_Adapter.SelectedUser{
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<Student_Update> list;
    Student_Update_Adapter student_update_adapter;
    DatabaseReference databaseReference;
    String Course, Semester;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update__student_);
        Course=getIntent().getStringExtra("Course");
        Semester=getIntent().getStringExtra("Semester");
        toolbar = findViewById(R.id.student_update_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.student_update_recyclerview);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        list = new ArrayList<Student_Update>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Student").child(Course).child(Semester);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Student_Update student_update = dataSnapshot1.getValue(Student_Update.class);
                    list.add(student_update);
                }
                student_update_adapter = new Student_Update_Adapter(Student_Update_Activity.this, list, Student_Update_Activity.this);
                recyclerView.setAdapter(student_update_adapter);
                invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Student_Update_Activity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void selectedUser(Student_Update student_update) {
        Intent intent=new Intent(Student_Update_Activity.this, Student_Update2_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Course",Course);
        bundle.putString("Semester",Semester);
        intent.putExtras(bundle);
        intent.putExtra("Email",student_update);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (student_update_adapter == null || student_update_adapter.getItemCount() == 0) {
            return super.onCreateOptionsMenu(menu);
        } else {
            getMenuInflater().inflate(R.menu.menu, menu);
            MenuItem menuItem = menu.findItem(R.id.search_view);
            SearchView searchView = (SearchView) menuItem.getActionView();
            searchView.setMaxWidth(Integer.MAX_VALUE);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    student_update_adapter.getFilter().filter(newText);
                    return true;
                }
            });
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_view) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void onBackPressed() {
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        finish();
        Intent intent = new Intent(Student_Update_Activity.this, Student_Update1_Activity.class);
        startActivity(intent);
    }
}
