package com.example.mynewapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mynewapp.Model.Subject;
import com.example.mynewapp.R;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> implements Filterable {
    Context context;
    ArrayList<Subject> subjects;
    private List<Subject> filteroutput;
    private SelectedUser selectedUser;

    public SubjectAdapter(Context c,ArrayList<Subject> s, SelectedUser selectedUser){
        this.context=c;
        this.subjects=s;
        this.filteroutput=subjects;
        this.selectedUser=selectedUser;
    }
    @NonNull
    @Override
    public SubjectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.subject_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.MyViewHolder holder, int position) {
        holder.Code.setText(subjects.get(position).getCode());
        holder.Name.setText(subjects.get(position).getName());
        holder.Course.setText(subjects.get(position).getCourse());
    }

    @Override
    public int getItemCount() {
        return subjects.size();
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
                    List<Subject> resultData=new ArrayList<>();
                    for(Subject subject: filteroutput){
                        if(subject.getName().toLowerCase().contains(searchchar)){
                            resultData.add(subject);
                        }
                    }
                    filterResults.count=resultData.size();
                    filterResults.values=resultData;
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                subjects= (ArrayList<Subject>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public interface SelectedUser{
        void selectedUser(Subject subject);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Code,Name,Course;
        // LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Code=(TextView)itemView.findViewById(R.id.subject_code);
            Name=(TextView)itemView.findViewById(R.id.subject_name);
            Course=(TextView)itemView.findViewById(R.id.subject_course);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    selectedUser.selectedUser(subjects.get(getAdapterPosition()));
                }
            });
        }
    }
}
