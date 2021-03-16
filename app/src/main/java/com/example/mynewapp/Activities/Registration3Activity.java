package com.example.mynewapp.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mynewapp.R;
import com.example.mynewapp.Model.Registration;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration3Activity extends AppCompatActivity {
    CircleImageView circleImageView,circleImageView1;
    Button Browse, FinalRegistration;
    Uri FilePathUri;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    Registration registration;
    private final static int REQUEST_CODE=123;
    private final static int PERMISSION_CODE=1234;
    ProgressDialog progressDialog;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration3);
        circleImageView1=(CircleImageView)findViewById(R.id.layout_three);
        storageReference = FirebaseStorage.getInstance().getReference("Images");
        progressDialog = new ProgressDialog(Registration3Activity.this);
        Bundle bundle = getIntent().getExtras();
        final String Name = bundle.getString("FullName");
        final String Email = bundle.getString("Email");
        final String Password = bundle.getString("Password");
        final String Phone = bundle.getString("Phone");
        final String Phone1 = "+91" + Phone;
        final String Course = bundle.getString("Course");
        final String Semister = bundle.getString("Semister");
        final String Birthdate = bundle.getString("BirthDate");
        final String Gender = bundle.getString("Gender");
        circleImageView = (CircleImageView) findViewById(R.id.profilepicture);
        Browse = (Button) findViewById(R.id.Browse);
        FinalRegistration = (Button) findViewById(R.id.FinalRegistration);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Registration");
        firebaseAuth = FirebaseAuth.getInstance();
        registration = new Registration();
        Browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                   if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                           == PackageManager.PERMISSION_DENIED){
                       String[] permissions={Manifest.permission.READ_EXTERNAL_STORAGE};
                       requestPermissions(permissions,PERMISSION_CODE);
                   }else {
                       pickImageFromGallary();
                   }
               }else {
                   pickImageFromGallary();
               }
            }
        });
        FinalRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FilePathUri != null) {
                    FinalRegistration.setVisibility(View.INVISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                firebaseAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            final ProgressDialog progressDialog = new ProgressDialog(Registration3Activity.this);
                                            progressDialog.setTitle("Uploading...");
                                            progressDialog.setCanceledOnTouchOutside(false);
                                            progressDialog.show();
                                            FirebaseStorage storage = FirebaseStorage.getInstance();
                                            final StorageReference storageReference = storage.getReference().child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
                                            storageReference.putFile(FilePathUri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    String imageURL=uri.toString();
                                                                    registration.setName(Name);
                                                                    registration.setEmail(Email);
                                                                    registration.setPassword(Password);
                                                                    registration.setPhone(Phone1);
                                                                    registration.setCourse(Course);
                                                                    registration.setSemister(Semister);
                                                                    registration.setBirthDate(Birthdate);
                                                                    registration.setGender(Gender);
                                                                    registration.setImageURL(imageURL);
                                                                    databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(registration);
                                                                    Toast.makeText(Registration3Activity.this, "User Created Successfully, Please verify your email address!", Toast.LENGTH_LONG).show();
                                                                    progressDialog.dismiss();
                                                                    Intent intent = new Intent(Registration3Activity.this, Home1Activity.class);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(Registration3Activity.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    })
                                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                                                        }
                                                    });
                                        }else {
                                            Toast.makeText(Registration3Activity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(Registration3Activity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                FinalRegistration.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(Registration3Activity.this, "Please Select Image", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void pickImageFromGallary() {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), FilePathUri);
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String GetFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallary();
                }else {
                    Toast.makeText(this,"Permission Denied...",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
