package com.example.newproject.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.R;
import com.example.newproject.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private FragmentHomeBinding binding;

    private String[] titles = new String[1];
    private String[] descriptions = new String[1];

    private MyAdapter adapter;

    DatabaseReference issuesDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        issuesDatabase = FirebaseDatabase.getInstance().getReference("issues");

        // TODO: Retrieve data from Firebase and populate the titles and descriptions arrays
        // Add a ValueEventListener to retrieve data from Firebase
        issuesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing data
                titles = new String[(int) dataSnapshot.getChildrenCount()];
                descriptions = new String[(int) dataSnapshot.getChildrenCount()];

                int i = 0;
                for (DataSnapshot issueSnapshot : dataSnapshot.getChildren()) {
                    // Assuming your Firebase structure has 'title' and 'description' fields
                    String title = issueSnapshot.child("title").getValue(String.class);
                    String description = issueSnapshot.child("description").getValue(String.class);

                    titles[i] = title;
                    descriptions[i] = description;
                    i++;
                }
                // Update the RecyclerView adapter with the new data
                adapter.updateData(titles, descriptions);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MyAdapter(getContext(), titles, descriptions);
        recyclerView.setAdapter(adapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}