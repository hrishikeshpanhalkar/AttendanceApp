package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.mynewapp.Adapters.Teacher_Update_Adapter;
import com.example.mynewapp.Model.Teacher_update;
import com.example.mynewapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Teacher_Delete_Activity extends AppCompatActivity implements Teacher_Update_Adapter.SelectedUser {
    RecyclerView recyclerView;
    Toolbar toolbar;
    ProgressBar progressBar;
    ArrayList<Teacher_update> list;
    Teacher_Update_Adapter teacher_delete_adapter;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    String spassword,smobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_teacher__delete_);
        toolbar = findViewById(R.id.delete_teacher_toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.delete_teacher_recyclerview);
        this.setSupportActionBar(toolbar);
        progressBar=(ProgressBar) findViewById(R.id.progressbar123);
        progressBar.setVisibility(View.INVISIBLE);
        this.getSupportActionBar().setTitle("");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.hasFixedSize();
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        firebaseAuth = FirebaseAuth.getInstance();
        list = new ArrayList<Teacher_update>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Registration");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Teacher_update teacher_delete = dataSnapshot1.getValue(Teacher_update.class);
                    list.add(teacher_delete);
                }
                teacher_delete_adapter = new Teacher_Update_Adapter(Teacher_Delete_Activity.this, list, Teacher_Delete_Activity.this);
                recyclerView.setAdapter(teacher_delete_adapter);
                invalidateOptionsMenu();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Teacher_Delete_Activity.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void selectedUser(Teacher_update teacher_delete) {
        String semail = teacher_delete.getEmail();
        String imageurl = teacher_delete.getImageURL();
        showDeleteDataDialog(semail, imageurl);
    }

    private void showDeleteDataDialog(final String semail, final String imageurl) {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Registration");
        Query query1 = databaseReference1.orderByChild("email").equalTo(semail);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    spassword=ds.child("password").getValue(String.class);
                    smobile=ds.child("phone").getValue(String.class);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query1.addListenerForSingleValueEvent(valueEventListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(Teacher_Delete_Activity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are you sure to delete this post?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressBar.setVisibility(View.VISIBLE);
                String email=semail;
                final String password=spassword;
                final String mobile=smobile;
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           firebaseAuth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Query query = databaseReference.orderByChild("email").equalTo(semail);
                                       query.addListenerForSingleValueEvent(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                               for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                   ds.getRef().removeValue();
                                               }
                                               FirebaseStorage storage = FirebaseStorage.getInstance();
                                               final StorageReference storageReference1 = storage.getReferenceFromUrl(imageurl);
                                               storageReference1.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void aVoid) {
                                                       Toast.makeText(Teacher_Delete_Activity.this, "User Data deleted Successfully!", Toast.LENGTH_LONG).show();
                                                       progressBar.setVisibility(View.INVISIBLE);
                                                       startActivity(new Intent(Teacher_Delete_Activity.this,Teacher_Delete_Activity.class));
                                                       finish();
                                                   }
                                               }).addOnFailureListener(new OnFailureListener() {
                                                   @Override
                                                   public void onFailure(@NonNull Exception e) {
                                                       Toast.makeText(Teacher_Delete_Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                                       progressBar.setVisibility(View.INVISIBLE);
                                                       startActivity(new Intent(Teacher_Delete_Activity.this,Teacher_Delete_Activity.class));
                                                       finish();
                                                   }
                                               });
                                           }
                                           @Override
                                           public void onCancelled(@NonNull DatabaseError databaseError) {
                                               Toast.makeText(Teacher_Delete_Activity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                                               progressBar.setVisibility(View.INVISIBLE);
                                           }
                                       });
                                   }
                               }
                           }) ;
                        }
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
        if (teacher_delete_adapter == null || teacher_delete_adapter.getItemCount() == 0) {
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
                    teacher_delete_adapter.getFilter().filter(newText);
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

