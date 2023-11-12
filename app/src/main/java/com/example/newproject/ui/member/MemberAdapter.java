package com.example.newproject.ui.member;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.R;
import com.example.newproject.models.Member;

import java.util.List;

// ItemAdapter.java
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ItemViewHolder> {

    private List<Member> itemList;

    public MemberAdapter(List<Member> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Member item = itemList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.assignedIssueTextView.setText("Total: " + String.valueOf(item.getAssignedIssues()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView assignedIssueTextView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.memberNameTextView);
            assignedIssueTextView = itemView.findViewById(R.id.assignedIssueTextView);
        }
    }


}
