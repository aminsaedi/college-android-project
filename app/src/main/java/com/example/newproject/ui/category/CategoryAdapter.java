package com.example.newproject.ui.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.R;
import com.example.newproject.models.Category;

import java.util.List;

// ItemAdapter.java
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewHolder> {

    private List<Category> itemList;
    private OnDeleteItemClickListener deleteItemClickListener;

    public CategoryAdapter(List<Category> itemList, OnDeleteItemClickListener deleteItemClickListener) {
        this.itemList = itemList;
        this.deleteItemClickListener = deleteItemClickListener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Category item = itemList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.deleteButton.setOnClickListener(v -> deleteItemClickListener.onDeleteItem(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        ImageButton deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }

    public interface OnDeleteItemClickListener {
        void onDeleteItem(Category item);
    }
}
