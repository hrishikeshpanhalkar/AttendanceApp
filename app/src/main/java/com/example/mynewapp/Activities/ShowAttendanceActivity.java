package com.example.mynewapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.mynewapp.Adapters.Utils;
import com.example.mynewapp.BuildConfig;
import com.example.mynewapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.Pattern;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class ShowAttendanceActivity extends AppCompatActivity {
    private Button button;
    private AutoCompleteTextView Course, Semester,Subject;
    private ArrayList<String> spinnerDataList2, spinnerData,spinnerDataList;
    private ArrayAdapter<String> adapter2, ada,adapter;
    private DatabaseReference reff, attendanceref,reference;
    private MaterialButton datebtn;
    private int noOf = 0;
    private final static int PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_show_attendance);
        button = (Button) findViewById(R.id.file_create);
        datebtn = (MaterialButton) findViewById(R.id.attendancedate);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        createExcelFile();
                    }
                } else {
                    createExcelFile();
                }
            }
        });
        reff = FirebaseDatabase.getInstance().getReference().child("Member");
        reference = FirebaseDatabase.getInstance().getReference().child("Subject");
        Course = (AutoCompleteTextView) findViewById(R.id.show_attendance_course);
        Semester = (AutoCompleteTextView) findViewById(R.id.show_attendance_semester);
        Subject = (AutoCompleteTextView) findViewById(R.id.show_attendance_subject);
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    noOf = (int) dataSnapshot.getChildrenCount();
                    spinnerDataList2 = new ArrayList<String>();
                    for (int i = 1; i <= noOf; i++) {
                        reff.child(String.valueOf(i)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String sem = dataSnapshot.child("name").getValue().toString();
                                spinnerDataList2.add(sem);
                                spinnerDataList2 = Utils.removeDuplicatesFromList(spinnerDataList2);
                                adapter2 = new ArrayAdapter<String>(ShowAttendanceActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item, spinnerDataList2);
                                Course.setAdapter(adapter2);
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
        Course.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Semester.setText("");
                Subject.setText("");
                String courseValue = Course.getText().toString();
                Query query = reff.orderByChild("name").equalTo(courseValue);
                spinnerData = new ArrayList<String>();
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name2 = ds.child("sem").getValue(String.class);
                            spinnerData.add(name2);
                        }
                        Collections.sort(spinnerData);
                        if (ada != null) {
                            ada.clear();
                        }
                        ada = new ArrayAdapter<String>(ShowAttendanceActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, spinnerData);
                        Semester.setAdapter(ada);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                query.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        Semester.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Subject.setText("");
                String courseValue = Course.getText().toString();
                final String semesterValue = Semester.getText().toString();
                Query query = reference.child(courseValue).child(semesterValue).orderByChild("course").equalTo(courseValue);
                spinnerDataList = new ArrayList<String>();
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String sem = ds.child("semester").getValue(String.class);
                            if(sem.equals(semesterValue)){
                                spinnerDataList.add(ds.child("name").getValue(String.class));
                            }
                        }
                        Collections.sort(spinnerDataList);
                        if (adapter != null) {
                            adapter.clear();
                        }
                        adapter = new ArrayAdapter<String>(ShowAttendanceActivity.this,
                                android.R.layout.simple_spinner_dropdown_item, spinnerDataList);
                        Subject.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                query.addListenerForSingleValueEvent(valueEventListener);
            }
        });
        datebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ShowAttendanceActivity.this,
                        new MonthPickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(int selectedMonth, int selectedYear) {
                                String months[] = {"January" , "February" , "March" , "April", "May",
                                                "June", "July", "August", "September", "October",
                                                "November", "December"};
                                datebtn.setText((months[selectedMonth]) + "-" + selectedYear);
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
                builder.setMinYear(1990)
                        .setActivatedYear(calendar.get(Calendar.YEAR))
                        .setActivatedMonth(calendar.get(Calendar.MONTH))
                        .setMaxYear(calendar.get(Calendar.YEAR))
                        .setTitle("Select Month and Year")
                        .build().show();

            }
        });
    }

    private void createExcelFile() {
        final String course = Course.getText().toString();
        final String semester = Semester.getText().toString();
        final String subject = Subject.getText().toString();
        final String findMe = datebtn.getText().toString();
        if (course.equals("") || semester.equals("") || findMe.equals("") || subject.equals("")) {
            Toast.makeText(ShowAttendanceActivity.this, "Please Fill the Details!", Toast.LENGTH_LONG).show();
        } else {
            DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference().child("Attendance").child(course).child(semester).child(subject);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    outer:
                    for(int i=1;i<2;i++) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if (ds.getKey().endsWith(findMe)) {
                                final String filename = subject + ".xls";
                                File filepath = Environment.getExternalStorageDirectory();
                                File dir = new File(filepath.getAbsolutePath() + File.separator + "Documents");
                                final File file = new File(dir.getAbsolutePath(), filename);
                                final WorkbookSettings wbSettings = new WorkbookSettings();
                                wbSettings.setLocale(new Locale("en", "EN"));
                                attendanceref = FirebaseDatabase.getInstance().getReference().child("Attendance").child(course).child(semester).child(subject);
                                if (file.exists()) {
                                    attendanceref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Workbook workbook1;
                                            WritableWorkbook copy;
                                            try {
                                                workbook1 = Workbook.getWorkbook(new File(file.getAbsolutePath()));
                                                copy = Workbook.createWorkbook(new File(file.getAbsolutePath()), workbook1);
                                                int sheetno = copy.getNumberOfSheets();
                                                Sheet[] sheets = copy.getSheets();
                                                WritableSheet sheet;
                                                if (sheetExist(sheets, findMe)) {
                                                    sheet = copy.getSheet(findMe);
                                                } else {
                                                    sheet = copy.createSheet(findMe, (sheetno + 1));
                                                }
                                                int a = 2;
                                                int count = 0;
                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                    if (ds.getKey().endsWith(findMe)) {
                                                        Label label = new Label(a, 0, ds.getKey(), getCellFormat(Colour.GREEN, Pattern.GRAY_25));
                                                        sheet.setColumnView(a,12);
                                                        sheet.addCell(label);
                                                        for (int i = 0; i < ds.getChildrenCount(); i++) {
                                                            String attendance = ds.child(String.valueOf(i)).child("attendance").getValue(String.class);
                                                            sheet.addCell(new Label(a, (i + 1), attendance));
                                                        }
                                                        a++;
                                                    }
                                                    count++;
                                                    if (snapshot.getChildrenCount() == count) {
                                                        int i = 1;
                                                        for (DataSnapshot dataSnapshot : ds.getChildren()) {
                                                            try {
                                                                sheet.addCell(new Label(0, i, dataSnapshot.child("rollno").getValue(String.class)));
                                                                sheet.addCell(new Label(1, i, dataSnapshot.child("name").getValue(String.class)));
                                                            } catch (RowsExceededException e) {
                                                                Toast.makeText(ShowAttendanceActivity.this, "First Error " + e, Toast.LENGTH_LONG).show();
                                                            } catch (WriteException e) {
                                                                Toast.makeText(ShowAttendanceActivity.this, "Second Error " + e, Toast.LENGTH_LONG).show();
                                                            }
                                                            i = i + 1;
                                                        }
                                                    }
                                                }
                                                try {
                                                    Label label = new Label(0, 0, "Roll No", getCellFormat(Colour.GREEN, Pattern.GRAY_25));
                                                    sheet.setColumnView(1, 15);
                                                    Label label1 = new Label(1, 0, "Name", getCellFormat(Colour.GREEN, Pattern.GRAY_25));
                                                    sheet.addCell(label);
                                                    sheet.addCell(label1);
                                                    copy.write();
                                                    Toast.makeText(getApplicationContext(), "File Created Successfully!", Toast.LENGTH_SHORT).show();
                                                    copy.close();
                                                } catch (Exception e) {
                                                    Toast.makeText(ShowAttendanceActivity.this, "Third Error " + e, Toast.LENGTH_LONG).show();
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(ShowAttendanceActivity.this, "Fourth Error " + e, Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                    String message = "New File Created Successfully!";
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ShowAttendanceActivity.this)
                                            .setSmallIcon(R.drawable.ic_file_download)
                                            .setContentTitle("Attendance App")
                                            .setContentText(message)
                                            .setAutoCancel(true);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    Uri photoURI = FileProvider.getUriForFile(ShowAttendanceActivity.this,
                                            BuildConfig.APPLICATION_ID + ".provider", file);
                                    intent.setDataAndType(photoURI, "application/vnd.ms-excel");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    PendingIntent pendingIntent = PendingIntent.getActivity(ShowAttendanceActivity.this, 0, intent, 0);
                                    builder.setContentIntent(pendingIntent);

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(0, builder.build());
                                } else {
                                    attendanceref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            final WritableWorkbook workbook;
                                            try {
                                                workbook = Workbook.createWorkbook(file, wbSettings);
                                                final WritableSheet sheet = workbook.createSheet(findMe, 0);
                                                int a = 2;
                                                int count = 0;
                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                    if (ds.getKey().endsWith(findMe)) {
                                                        Label label = new Label(a, 0, ds.getKey(), getCellFormat(Colour.GREEN, Pattern.GRAY_25));
                                                        sheet.setColumnView(a,12);
                                                        sheet.addCell(label);
                                                        for (int i = 0; i < ds.getChildrenCount(); i++) {
                                                            String attendance = ds.child(String.valueOf(i)).child("attendance").getValue(String.class);
                                                            sheet.addCell(new Label(a, (i + 1), attendance));
                                                        }
                                                        a++;
                                                    }
                                                    count++;
                                                    if (snapshot.getChildrenCount() == count) {
                                                        int i = 1;
                                                        for (DataSnapshot dataSnapshot : ds.getChildren()) {
                                                            try {
                                                                sheet.addCell(new Label(0, i, dataSnapshot.child("rollno").getValue(String.class)));
                                                                sheet.addCell(new Label(1, i, dataSnapshot.child("name").getValue(String.class)));
                                                            } catch (RowsExceededException e) {
                                                                Toast.makeText(ShowAttendanceActivity.this, "First Error " + e, Toast.LENGTH_LONG).show();
                                                            } catch (WriteException e) {
                                                                Toast.makeText(ShowAttendanceActivity.this, "Second Error " + e, Toast.LENGTH_LONG).show();
                                                            }
                                                            i = i + 1;
                                                        }
                                                    }
                                                }
                                                try {
                                                    Label label = new Label(0, 0, "Roll No", getCellFormat(Colour.GREEN, Pattern.GRAY_25));
                                                    sheet.setColumnView(1, 15);
                                                    Label label1 = new Label(1, 0, "Name", getCellFormat(Colour.GREEN, Pattern.GRAY_25));
                                                    sheet.addCell(label);
                                                    sheet.addCell(label1);
                                                    workbook.write();
                                                    Toast.makeText(getApplicationContext(), "File Created Successfully!", Toast.LENGTH_SHORT).show();
                                                    workbook.close();
                                                } catch (Exception e) {
                                                    Toast.makeText(ShowAttendanceActivity.this, "Third Error " + e, Toast.LENGTH_LONG).show();
                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(ShowAttendanceActivity.this, "Fourth Error " + e, Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                                    String message = "New File Created Successfully!";
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(ShowAttendanceActivity.this)
                                            .setSmallIcon(R.drawable.ic_file_download)
                                            .setContentTitle("Attendance App")
                                            .setContentText(message)
                                            .setAutoCancel(true);
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    Uri photoURI = FileProvider.getUriForFile(ShowAttendanceActivity.this,
                                            BuildConfig.APPLICATION_ID + ".provider", file);
                                    intent.setDataAndType(photoURI, "application/vnd.ms-excel");
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    PendingIntent pendingIntent = PendingIntent.getActivity(ShowAttendanceActivity.this, 0, intent, 0);
                                    builder.setContentIntent(pendingIntent);

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    notificationManager.notify(0, builder.build());
                                }
                                break outer;
                            }
                        }
                        Toast.makeText(getApplicationContext(),"Data Not Found!",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
    }
    private boolean sheetExist(Sheet[] sheets, String findMe) {
        for (int p = 0; p < sheets.length; p++) {
            if (sheets[p].getName().equals(findMe)) {
                return true;
            }
        }
        return false;
    }


    public static WritableCellFormat getCellFormat(Colour colour, Pattern pattern) throws WriteException {
        WritableFont cellFonts = new WritableFont(WritableFont.createFont("TIMES NEW ROMANS"), 11, WritableFont.BOLD, false,
                UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
        WritableCellFormat cellFormat = new WritableCellFormat(cellFonts);
        cellFormat.setAlignment(Alignment.CENTRE);
        cellFormat.setBorder(Border.ALL, BorderLineStyle.THICK);
        cellFormat.setBackground(colour, pattern);
        return cellFormat;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    createExcelFile();
                } else {
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
