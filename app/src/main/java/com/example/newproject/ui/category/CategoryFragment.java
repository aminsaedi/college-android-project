package com.example.newproject.ui.category;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.MainActivity;
import com.example.newproject.databinding.FragmentGalleryBinding;
import com.example.newproject.models.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private FragmentGalleryBinding binding;


    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> itemList;
    private DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        CategoryViewModel galleryViewModel =
//                new ViewModelProvider(this).get(CategoryViewModel.class);
//
        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = binding.recyclerView;
        itemList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(itemList, this::deleteItem);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("categories");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String name = dataSnapshot.child("name").getValue(String.class);
                    String id = dataSnapshot.getKey();

                    Category category = new Category(id, name);

                    itemList.add(category);

                }
                categoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

//        final TextView textView = binding.textGallery;
//        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void deleteItem(Category item) {
        databaseReference.child(item.getId()).removeValue();
//                .addOnSuccessListener(aVoid -> Toast.makeText(MainActivity., "Item deleted", Toast.LENGTH_SHORT).show())
//                .addOnFailureListener(e -> Toast.makeText(MainActivity.this, "Delete failed", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}