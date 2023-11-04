package com.example.newproject.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Category  implements Serializable  {

    private String id;

    private String name;

    public Category(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void save() {
        DatabaseReference issuesDatabase = FirebaseDatabase.getInstance().getReference("categories");

        // get the length of total categories
        issuesDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int length = (int) snapshot.getChildrenCount();
                Log.d("Category", "length: " + length);

                // save the category
                Map<String, Object> categoryValues = new HashMap<>();
                categoryValues.put("name", name);
                issuesDatabase.child(String.valueOf(length)).setValue(categoryValues);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
