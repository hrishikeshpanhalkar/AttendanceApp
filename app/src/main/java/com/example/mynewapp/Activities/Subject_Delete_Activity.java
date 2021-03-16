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

import com.example.mynewapp.Adapters.SubjectAdapter;
import com.example.mynewapp.Model.Subject;
import com.example.mynewapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Subject_Delete_Activity extends AppCompatActivity implements SubjectAdapter.SelectedUser  {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ProgressBar progressBar;
    ArrayList<Subject> list;
    String Course, Semester;
    SubjectAdapter subjectAdapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_subject__delete_);
        toolbar = findViewById(R.id.delete_subject_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.delete_subject_recyclerview);
        Course = getIntent().getStringExtra("Course");
        Semester = getIntent().getStringExtra("Semester");
        this.setSupportActionBar(toolbar);
        progressBar=(ProgressBar) findViewById(R.id.subjectDeleteProgressbar);
        progressBar.setVisibility(View.INVISIBLE);
        this.getSupportActionBar().setTitle("");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        list = new ArrayList<Subject>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Subject").child(Course).child(Semester);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Subject subject = dataSnapshot1.getValue(Subject.class);
                    list.add(subject);
                }
                subjectAdapter = new SubjectAdapter(Subject_Delete_Activity.this, list, Subject_Delete_Activity.this);
                recyclerView.setAdapter(subjectAdapter);
                invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Subject_Delete_Activity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void selectedUser(Subject subject) {
        String Code = subject.getCode();
        showDeleteDataDialog(Code, Course, Semester);
    }

    private void showDeleteDataDialog(final String Code, final String Course, final String Semester) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Subject_Delete_Activity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete this post?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.setVisibility(View.VISIBLE);
                Query query = databaseReference.orderByChild("code").equalTo(Code);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                ds.getRef().removeValue();
                            }
                            Intent intent = new Intent(Subject_Delete_Activity.this, Subject_Delete_Activity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("Course", Course);
                            bundle.putString("Semester", Semester);
                            intent.putExtras(bundle);
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Subject_Delete_Activity.this, "data not exist!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Subject_Delete_Activity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
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
        if (subjectAdapter == null || subjectAdapter.getItemCount() == 0) {
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
                    subjectAdapter.getFilter().filter(newText);
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

