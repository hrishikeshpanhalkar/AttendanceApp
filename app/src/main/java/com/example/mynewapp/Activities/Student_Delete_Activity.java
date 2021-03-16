package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mynewapp.Adapters.Student_Update_Adapter;
import com.example.mynewapp.Model.Student;
import com.example.mynewapp.Model.Student_Update;
import com.example.mynewapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_Delete_Activity extends AppCompatActivity implements Student_Update_Adapter.SelectedUser {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<Student_Update> list;
    String Course, Semester;
    Student_Update_Adapter student_update_adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student__delete_);
        toolbar = findViewById(R.id.delete_student_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.delete_student_recyclerview);
        Course = getIntent().getStringExtra("Course");
        Semester = getIntent().getStringExtra("Semester");
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
                student_update_adapter = new Student_Update_Adapter(Student_Delete_Activity.this, list, Student_Delete_Activity.this);
                recyclerView.setAdapter(student_update_adapter);
                invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Student_Delete_Activity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void selectedUser(Student_Update student_update) {
        String Rollno = student_update.getRollno();
        showDeleteDataDialog(Rollno, Course, Semester);
    }

    private void showDeleteDataDialog(final String Rollno, final String Course, final String Semester) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Student_Delete_Activity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete this post?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query query = databaseReference.orderByChild("rollno").equalTo(Rollno);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ds.getRef().removeValue();
                            }
                            Intent intent = new Intent(Student_Delete_Activity.this, Student_Delete_Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("Course",Course);
                            bundle.putString("Semester",Semester);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(Student_Delete_Activity.this,"data not exist!",Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Student_Delete_Activity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();

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
}
