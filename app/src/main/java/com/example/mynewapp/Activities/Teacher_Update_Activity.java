package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mynewapp.Adapters.Utils;
import com.example.mynewapp.Model.Teacher_update;
import com.example.mynewapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;


public class Teacher_Update_Activity extends AppCompatActivity {
    CircleImageView circleImageView, circleImageView1;
    TextView BirthDate, Gender, fullname;
    ImageButton birthdate;
    TextInputLayout Fullname, Email, Phone;
    AutoCompleteTextView CourseName, Semisterno;
    ImageButton Back;
    Drawable olddrawable;
    ProgressDialog mprogressDialog;
    Button Update;
    private final static int REQUEST_CODE = 123;
    private final static int PERMISSION_CODE = 1234;
    String semail, parentvalue, link;
    String DfullName, DBirthdate, DGender, DCourseName, DSemester, DPhone, DPhone1;
    ArrayList<String> spinnerDataList, spinnerData;
    ArrayAdapter<String> adapter, ada;
    Uri FilePathUri, oldFilePathUri;
    int noOfchild = 0;
    DatePickerDialog.OnDateSetListener dateSetListener;
    DatabaseReference databaseReference, databaseReference1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_update_teacher);
        Back = (ImageButton) findViewById(R.id.BackUpdate);
        fullname = (TextView) findViewById(R.id.User_fullName_update);
        circleImageView = (CircleImageView) findViewById(R.id.profile_image_update);
        circleImageView1 = (CircleImageView) findViewById(R.id.addimage);
        BirthDate = (TextView) findViewById(R.id.user_birthdate_label_update);
        Gender = (TextView) findViewById(R.id.user_gender_label_update);
        Fullname = (TextInputLayout) findViewById(R.id.fullname_update);
        Email = (TextInputLayout) findViewById(R.id.user_email_update);
        Phone = (TextInputLayout) findViewById(R.id.user_phone_update);
        CourseName = (AutoCompleteTextView) findViewById(R.id.course_name_update);
        Semisterno = (AutoCompleteTextView) findViewById(R.id.semisterno_update);
        Update = (Button) findViewById(R.id.teacher_update_btn);
        birthdate = (ImageButton) findViewById(R.id.bdatebtn);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Teacher_Update_Activity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                BirthDate.setText(date);
            }
        };
        Intent intent = getIntent();
        Fullname.requestFocus();
        if (intent.getExtras() != null) {
            Teacher_update teacher_update = (Teacher_update) intent.getSerializableExtra("Email");
            semail = teacher_update.getEmail();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Registration");
        Query query = databaseReference.orderByChild("email").equalTo(semail);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    link = ds.child("imageURL").getValue(String.class);
                    Picasso.get().load(link).into(circleImageView);
                    olddrawable = circleImageView.getDrawable();
                    fullname.setText(ds.child("name").getValue(String.class));
                    Fullname.getEditText().setText(ds.child("name").getValue(String.class));
                    DfullName = ds.child("name").getValue(String.class);
                    BirthDate.setText(ds.child("birthDate").getValue(String.class));
                    DBirthdate = ds.child("birthDate").getValue(String.class);
                    Gender.setText(ds.child("gender").getValue(String.class));
                    DGender = ds.child("gender").getValue(String.class);
                    Email.getEditText().setText(ds.child("email").getValue(String.class));
                    DPhone = ds.child("phone").getValue(String.class);
                    DPhone1 = ds.child("phone").getValue(String.class);
                    final String needle = "+91";
                    final int needleSize = needle.length();
                    //String haystack = "";
                    DPhone = DPhone.startsWith(needle) ? DPhone.substring(needleSize) : DPhone;
                    Phone.getEditText().setText(DPhone);
                    CourseName.setText(ds.child("course").getValue(String.class));
                    DCourseName = ds.child("course").getValue(String.class);
                    Semisterno.setText(ds.child("semister").getValue(String.class));
                    DSemester = ds.child("semister").getValue(String.class);
                    parentvalue = ds.getKey();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Teacher_Update_Activity.this, Teacher_Update_1Activity.class);
                startActivity(intent);
            }
        });
        databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Member");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    noOfchild = (int) dataSnapshot.getChildrenCount();
                    spinnerDataList = new ArrayList<String>();
                    for (int i = 1; i <= noOfchild; i++) {
                        databaseReference1.child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String coursename = dataSnapshot.child("name").getValue().toString();
                                spinnerDataList.add(coursename);
                                spinnerDataList = Utils.removeDuplicatesFromList(spinnerDataList);
                                adapter = new ArrayAdapter<String>(Teacher_Update_Activity.this,
                                        android.R.layout.simple_spinner_dropdown_item, spinnerDataList);
                                CourseName.setAdapter(adapter);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        CourseName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Semisterno.setText("");
                String courseValue = CourseName.getText().toString();
                Query query1 = databaseReference1.orderByChild("name").equalTo(courseValue);
                spinnerData = new ArrayList<String>();
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name2 = ds.child("sem").getValue(String.class);
                            spinnerData.add(name2);
                        }
                        if (ada != null) {
                            ada.clear();
                        }
                        ada = new ArrayAdapter<String>(Teacher_Update_Activity.this,
                                android.R.layout.simple_spinner_dropdown_item, spinnerData);
                        Semisterno.setAdapter(ada);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                query1.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Update.setVisibility(View.INVISIBLE);
                if (Fullname.getEditText().getText().toString().equals("") || Phone.getEditText().getText().toString().equals("")) {
                    Toast.makeText(Teacher_Update_Activity.this, "Please Fill the details!", Toast.LENGTH_LONG).show();
                    Update.setVisibility(View.VISIBLE);
                } else {
                    if (isImageChanged()) {
                    } else if ((isCourseNameChanged() && isSemesterChanged())) {
                        Toast.makeText(Teacher_Update_Activity.this, "Data has been Updated!", Toast.LENGTH_LONG).show();
                        Update.setVisibility(View.VISIBLE);
                    } else if (isSemesterChanged() || isNameChanged() || isGenderChanged() || isCourseNameChanged() || isBirthdateChanged() || isPhoneChanged()) {
                        Toast.makeText(Teacher_Update_Activity.this, "Data has been Updated!", Toast.LENGTH_LONG).show();
                        Update.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(Teacher_Update_Activity.this, "Data is same and cannot be updated!", Toast.LENGTH_LONG).show();
                        Update.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        circleImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallary();
                    }
                } else {
                    pickImageFromGallary();
                }
            }
        });
    }

    private boolean isPhoneChanged() {
        String Phone1 = "+91" + Phone.getEditText().getText().toString();
        if (!DPhone1.equals(Phone1)) {
            databaseReference.child(parentvalue).child("phone").setValue(Phone1);
            DPhone1 = Phone1;
            return true;
        } else {
            return false;
        }
    }

    private boolean isSemesterChanged() {
        if (!DSemester.equals(Semisterno.getText().toString())) {
            databaseReference.child(parentvalue).child("semister").setValue(Semisterno.getText().toString());
            DSemester = Semisterno.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isBirthdateChanged() {
        if (!DBirthdate.equals(BirthDate.getText().toString())) {
            databaseReference.child(parentvalue).child("birthDate").setValue(BirthDate.getText().toString());
            DBirthdate = BirthDate.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isGenderChanged() {
        if (!DGender.equals(Gender.getText().toString())) {
            databaseReference.child(parentvalue).child("gender").setValue(Gender.getText().toString());
            DGender = Gender.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isNameChanged() {
        if (!DfullName.equals(Fullname.getEditText().getText().toString())) {
            databaseReference.child(parentvalue).child("name").setValue(Fullname.getEditText().getText().toString());
            DfullName = Fullname.getEditText().getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isCourseNameChanged() {
        if (!DCourseName.equals(CourseName.getText().toString())) {
            databaseReference.child(parentvalue).child("course").setValue(CourseName.getText().toString());
            DCourseName = CourseName.getText().toString();
            return true;
        } else {
            return false;
        }
    }

    private boolean isImageChanged() {
        if (circleImageView.getDrawable() != olddrawable) {
            mprogressDialog = new ProgressDialog(Teacher_Update_Activity.this);
            mprogressDialog.setTitle("Updating...");
            mprogressDialog.setCanceledOnTouchOutside(false);
            mprogressDialog.show();
            deletePreviousImage();
            databaseReference.child(parentvalue).child("imageURL").setValue(link);
            return true;
        } else {
            return false;
        }
    }

    public void deletePreviousImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageReference = storage.getReferenceFromUrl(link);
        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mprogressDialog.dismiss();
                uploadNewImage();
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Teacher_Update_Activity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        mprogressDialog.dismiss();
                    }
                });
    }

    private void uploadNewImage() {
        if (FilePathUri != null) {
            final ProgressDialog progressDialog1 = new ProgressDialog(Teacher_Update_Activity.this);
            progressDialog1.setTitle("Uploading...");
            progressDialog1.setCanceledOnTouchOutside(false);
            progressDialog1.show();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            final StorageReference storageReference1 = storage.getReference().child(System.currentTimeMillis() + "." + GetFileExtension(FilePathUri));
            storageReference1.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageURL = uri.toString();
                                    databaseReference.child(parentvalue).child("imageURL").setValue(imageURL);
                                    Toast.makeText(Teacher_Update_Activity.this, "Image Updated Successfully!", Toast.LENGTH_LONG).show();
                                    progressDialog1.dismiss();
                                    Intent intent = new Intent(Teacher_Update_Activity.this, Teacher_Update_1Activity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog1.dismiss();
                            Toast.makeText(Teacher_Update_Activity.this, "Failed " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog1.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        } else {
            FilePathUri = oldFilePathUri;
        }
    }

    private void pickImageFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            FilePathUri = data.getData();
            oldFilePathUri = data.getData();
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
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallary();
                } else {
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void onBackPressed() {
        //Display alert message when back button has been pressed
        backButtonHandler();
        return;
    }

    public void backButtonHandler() {
        finish();
        Intent intent = new Intent(Teacher_Update_Activity.this, Teacher_Update_1Activity.class);
        startActivity(intent);
    }
}
