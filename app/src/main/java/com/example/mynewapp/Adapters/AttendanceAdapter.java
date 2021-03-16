package com.example.mynewapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynewapp.R;
import com.example.mynewapp.Model.Attendance;

import java.util.ArrayList;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder> {

    public List<Attendance> studentList;
    public ArrayList<Attendance> attendanceList = new ArrayList<>();
    private Context context;
    public static List<String> presentList = new ArrayList<>();
    public static List<String> absentList = new ArrayList<>();

    public AttendanceAdapter(Context context, ArrayList<Attendance> studentList) {

        this.context = context;
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public AttendanceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.attendance_layout, viewGroup, false);
        return new AttendanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AttendanceViewHolder attendanceViewHolder, final int i) {
        attendanceViewHolder.Student_name.setText(studentList.get(attendanceViewHolder.getAdapterPosition()).getName());
        attendanceViewHolder.Student_Id.setText(studentList.get(attendanceViewHolder.getAdapterPosition()).getRollno());
        attendanceViewHolder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.recyclerview_animation));
        final Attendance attendance = studentList.get(i);
        attendanceViewHolder.presentRBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!presentList.contains(studentList.get(attendanceViewHolder.getAdapterPosition()).getRollno())) {
                        presentList.add(studentList.get(attendanceViewHolder.getAdapterPosition()).getRollno());
                        attendance.setRollno(studentList.get(attendanceViewHolder.getAdapterPosition()).getRollno());
                        attendance.setName(studentList.get(attendanceViewHolder.getAdapterPosition()).getName());
                        attendance.setAttendance("present");
                        attendanceList.add(studentList.get(i));
                    }
                } else {
                    presentList.remove(studentList.get(attendanceViewHolder.getAdapterPosition()).getRollno());
                    attendanceList.remove(studentList.get(i));
                }
            }
        });
        attendanceViewHolder.absentRBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!absentList.contains(studentList.get(attendanceViewHolder.getAdapterPosition()).getRollno())) {
                        absentList.add(studentList.get(attendanceViewHolder.getAdapterPosition()).getRollno());
                        attendance.setRollno(studentList.get(attendanceViewHolder.getAdapterPosition()).getRollno());
                        attendance.setName(studentList.get(attendanceViewHolder.getAdapterPosition()).getName());
                        attendance.setAttendance("absent");
                        attendanceList.add(studentList.get(i));
                    }
                } else {
                    absentList.remove(studentList.get(attendanceViewHolder.getAdapterPosition()).getRollno());
                    attendanceList.remove(studentList.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder {
        TextView Student_name;
        TextView Student_Id;
        RadioButton presentRBtn;
        RadioButton absentRBtn;
        LinearLayout linearLayout;

        public AttendanceViewHolder(@NonNull View itemView) {
            super(itemView);
            Student_name = itemView.findViewById(R.id.takeattendanceStudentName);
            Student_Id = itemView.findViewById(R.id.takeattendanceStudentID);
            presentRBtn = itemView.findViewById(R.id.presentRadioBtn);
            absentRBtn = itemView.findViewById(R.id.absentRadioBtn);
            linearLayout=itemView.findViewById(R.id.attendance_linearLayout);
        }
    }

    public void updateCollection(List<Attendance> studentList) {
        this.studentList = studentList;
        notifyDataSetChanged();
    }
}
