package com.example.mynewapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.mynewapp.Model.Student_Update;
import com.example.mynewapp.R;

import java.util.ArrayList;
import java.util.List;


public class Student_Update_Adapter extends RecyclerView.Adapter<Student_Update_Adapter.MyHolder> implements Filterable {
    Context context;
    ArrayList<Student_Update> student_updates;
    private List<Student_Update> filteroutput;
    private SelectedUser selectedUser;

    public Student_Update_Adapter(Context c,ArrayList<Student_Update> t, SelectedUser selectedUser){
        this.context=c;
        this.student_updates=t;
        this.filteroutput=student_updates;
        this.selectedUser=selectedUser;
    }

    @Override
    public Filter getFilter() {
        Filter filter=new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults=new FilterResults();
                if(constraint == null | constraint.length() == 0){
                    filterResults.count=filteroutput.size();
                    filterResults.values=filteroutput;
                }else {
                    String searchchar=constraint.toString().toLowerCase();
                    List<Student_Update> resultData=new ArrayList<>();
                    for(Student_Update teacher_update: filteroutput){
                        if(teacher_update.getName().toLowerCase().contains(searchchar)){
                            resultData.add(teacher_update);
                        }
                    }
                    filterResults.count=resultData.size();
                    filterResults.values=resultData;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                student_updates= (ArrayList<Student_Update>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Student_Update_Adapter.MyHolder(LayoutInflater.from(context).inflate(R.layout.student_update_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.Rollno.setText(String.valueOf(student_updates.get(position).getRollno()));
        holder.Name.setText(student_updates.get(position).getName());
        holder.Email.setText(student_updates.get(position).getEmail());
       // holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.recyclerview_animation));

    }

    @Override
    public int getItemCount() {
        return student_updates.size();
    }

    public interface SelectedUser{
        void selectedUser(Student_Update student_update);
    }
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView Rollno, Name,Email;
        //LinearLayout linearLayout;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            Rollno=(TextView)itemView.findViewById(R.id.student_textrollno);
            Name=(TextView)itemView.findViewById(R.id.student_textname);
            Email=(TextView)itemView.findViewById(R.id.student_textemail);
            //linearLayout=(LinearLayout) itemView.findViewById(R.id.student_linearlayout);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    selectedUser.selectedUser(student_updates.get(getAdapterPosition()));
                }
            });
        }
    }
}
