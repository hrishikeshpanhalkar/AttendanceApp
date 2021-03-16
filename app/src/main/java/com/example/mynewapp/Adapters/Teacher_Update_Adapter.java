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

import com.example.mynewapp.Model.Teacher_update;
import com.example.mynewapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;



public class Teacher_Update_Adapter extends RecyclerView.Adapter<Teacher_Update_Adapter.MyViewHolder> implements Filterable {

    Context context;
    ArrayList<Teacher_update> teacher_updates;
    private List<Teacher_update> filteroutput;
    private SelectedUser selectedUser;

    public Teacher_Update_Adapter(Context c,ArrayList<Teacher_update> t, SelectedUser selectedUser){
        this.context=c;
        this.teacher_updates=t;
        this.filteroutput=teacher_updates;
        this.selectedUser=selectedUser;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.teacher_update_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            holder.Name.setText(teacher_updates.get(position).getName());
            holder.Email.setText(teacher_updates.get(position).getEmail());
            holder.profilePic.setBackground(null);
            Picasso.get().load(teacher_updates.get(position).getImageURL()).into(holder.profilePic);
            //holder.linearLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.recyclerview_animation));
        }

    @Override
    public int getItemCount() {
        return teacher_updates.size();
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
                    List<Teacher_update> resultData=new ArrayList<>();
                    for(Teacher_update teacher_update: filteroutput){
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
                teacher_updates= (ArrayList<Teacher_update>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    public interface SelectedUser{
        void selectedUser(Teacher_update teacher_update);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Name,Email;
        CircleImageView profilePic;
       // LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            Name=(TextView)itemView.findViewById(R.id.textname);
            Email=(TextView)itemView.findViewById(R.id.textemail);
            profilePic=(CircleImageView) itemView.findViewById(R.id.profilepic);
           // linearLayout=(LinearLayout) itemView.findViewById(R.id.linearlayout);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    selectedUser.selectedUser(teacher_updates.get(getAdapterPosition()));
                }
            });
        }
    }

}