package com.example.newproject.ui.member;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newproject.databinding.FragmentSlideshowBinding;
import com.example.newproject.models.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;

    private ArrayList<Member> members = new ArrayList<>();
    DatabaseReference issuesDatabase;

    private MemberAdapter adapter;

    private RecyclerView recyclerView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        adapter = new MemberAdapter(members);

        recyclerView = binding.membersRecyclerView;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        issuesDatabase = FirebaseDatabase.getInstance().getReference("issues");

        // TODO: Retrieve data from Firebase and populate the titles and descriptions arrays
        // Add a ValueEventListener to retrieve data from Firebase
        issuesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing data
                members.clear();


                for (DataSnapshot issueSnapshot : dataSnapshot.getChildren()) {

                    String assignee = issueSnapshot.child("assignee").getValue(String.class);


                    // if the assignee exists in the members list, increment the assignedIssues
                    // otherwise, add the member to the list

                    if (assignee != null) {
                        boolean found = false;
                        for (Member member : members) {
                            if (member.getName().equals(assignee)) {
                                member.incrementAssignedIssues();
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            Member member = new Member(assignee);
                            members.add(member);
                        }


                    }

                }

                // sort members based on the number of assigned issues

                Collections.sort(members, new Comparator<Member>() {
                            @Override
                            public int compare(Member o1, Member o2) {
                                return  o2.getAssignedIssues() - o1.getAssignedIssues();
                            }
                        });

                        adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}