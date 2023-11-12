package com.example.newproject.ui.home;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.R;
import com.example.newproject.models.Issue;

import java.util.ArrayList;

public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Issue> issues = new ArrayList<>();

    public IssuesAdapter(Context context, ArrayList<Issue> issues) {
        this.context = context;
        this.issues = issues;
    }

    public void updateData(ArrayList<Issue> newIssues) {
        this.issues = newIssues;
        notifyDataSetChanged(); // Notify the RecyclerView to refresh its view
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.titleTextView.setText(issues.get(position).getTitle());
        holder.statusTextView.setText(issues.get(position).getStatusString());
        holder.assigneeTextView.setText(issues.get(position).getAssignee());

        int position1 = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open IssueEditFragment with the selected issue
                Bundle bundle = new Bundle();
                bundle.putString("id", issues.get(position1).getId());
                Navigation.findNavController(v).navigate(R.id.nav_issue_edit, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView titleTextView;
        TextView statusTextView;

        TextView assigneeTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            assigneeTextView = itemView.findViewById(R.id.assigneeTextView);
        }
    }
}
