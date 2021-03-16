package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mynewapp.Adapters.Teacher_Update_Adapter;
import com.example.mynewapp.Model.Teacher_update;
import com.example.mynewapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Teacher_Update_1Activity extends AppCompatActivity implements Teacher_Update_Adapter.SelectedUser {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ArrayList<Teacher_update> list;
    Teacher_Update_Adapter teacher_update_adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_teacher__update_1);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        list = new ArrayList<Teacher_update>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Registration");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Teacher_update teacher_update = dataSnapshot1.getValue(Teacher_update.class);
                    list.add(teacher_update);
                }
                teacher_update_adapter = new Teacher_Update_Adapter(Teacher_Update_1Activity.this, list, Teacher_Update_1Activity.this);
                recyclerView.setAdapter(teacher_update_adapter);
                invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Teacher_Update_1Activity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void selectedUser(Teacher_update teacher_update) {
        finish();
        startActivity(new Intent(Teacher_Update_1Activity.this, Teacher_Update_Activity.class).putExtra("Email", teacher_update));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (teacher_update_adapter == null || teacher_update_adapter.getItemCount() == 0) {
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
                    teacher_update_adapter.getFilter().filter(newText);
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
