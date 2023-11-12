package com.example.newproject.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.R;
import com.example.newproject.databinding.FragmentHomeBinding;
import com.example.newproject.models.Issue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private FragmentHomeBinding binding;

    private ArrayList<Issue> issues = new ArrayList<>();

    private IssuesAdapter adapter;

    private String username;

    DatabaseReference issuesDatabase;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // get username key from shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = sharedPreferences.getString("username", "default");

        // set username in the header
        if (username != null) {
            TextView usernameTextView = root.findViewById(R.id.welcomeTextView);
            usernameTextView.setText(username + ", Welcome to Tracklit!");
        }


        issuesDatabase = FirebaseDatabase.getInstance().getReference("issues");

        // TODO: Retrieve data from Firebase and populate the titles and descriptions arrays
        // Add a ValueEventListener to retrieve data from Firebase
        issuesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing data
                issues.clear();


                for (DataSnapshot issueSnapshot : dataSnapshot.getChildren()) {
                    // Assuming your Firebase structure has 'title' and 'description' fields
                    String id = issueSnapshot.getKey();
                    String title = issueSnapshot.child("title").getValue(String.class);
                    String description = issueSnapshot.child("description").getValue(String.class);
                    String status = issueSnapshot.child("status").getValue(String.class);
                    String assignee = issueSnapshot.child("assignee").getValue(String.class);
                    String categoryId = issueSnapshot.child("categoryId").getValue(String.class);
                    Issue.Status statusEnum = Issue.Status.valueOf(status);

                    Issue issue = new Issue(id, title, description, assignee, statusEnum, categoryId);
                    issues.add(issue);
                }

                // Update the RecyclerView adapter with the new data
                adapter.updateData(issues);
                // if username is set, get the total count of issues assigned to the user
                if (username != null) {
                    int count = 0;
                    int pendingCount = 0;
                    for (Issue issue : issues) {
                        if (issue.getAssignee().equals(username)) {
                            count++;
                            if (issue.getStatus() == Issue.Status.PENDING) {
                                pendingCount++;
                            }
                        }
                    }
                    TextView countTextView = root.findViewById(R.id.statusTextView);
                    countTextView.setText("You have " + count + " issues assigned to you, (" + pendingCount + " pending)");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors here
            }
        });

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new IssuesAdapter(getContext(), issues);
        recyclerView.setAdapter(adapter);




        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}