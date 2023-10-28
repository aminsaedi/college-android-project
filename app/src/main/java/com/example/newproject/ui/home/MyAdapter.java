package com.example.newproject.ui.home;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private String[] titles;
    private String[] descriptions;

    public MyAdapter(Context context, String[] titles, String[] descriptions) {
        this.context = context;
        this.titles = titles;
        this.descriptions = descriptions;
    }

    public void updateData(String[] newTitles, String[] newDescriptions) {
        this.titles = newTitles;
        this.descriptions = newDescriptions;
        notifyDataSetChanged(); // Notify the RecyclerView to refresh its view
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.titleTextView.setText(titles[position]);
        holder.descriptionTextView.setText(descriptions[position]);

        int position1 = holder.getAdapterPosition();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, MyActivity.class);
//                intent.putExtra("title", titles[position1]);
//                intent.putExtra("description", descriptions[position1]);
//                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        TextView titleTextView;
        TextView descriptionTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
