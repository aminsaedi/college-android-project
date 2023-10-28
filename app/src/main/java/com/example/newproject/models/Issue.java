package com.example.newproject.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.newproject.ui.issueEdit.IssueCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Issue implements Serializable {

    private String id;
    private String title;
    private String description;
    private String assignee;

    public enum Status {
        PENDING,
        IN_PROGRESS,
        COMPLETED
    }

    private Status status;


    private String categoryId;

    public Issue(String id, String title, String description, String assignee, Status status, String categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.assignee = assignee;
        this.status = status;
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Status getStatus() {
        return status;
    }

    public String getStatusString() {
        if (status == Status.PENDING) {
            return "Pending";
        } else if (status == Status.IN_PROGRESS) {
            return "In Progress";
        } else {
            return "Completed";
        }
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void save() {
        DatabaseReference issuesDatabase = FirebaseDatabase.getInstance().getReference("issues");

        DatabaseReference issueRef = issuesDatabase.child(id); // Use the provided ID

        Log.d("This:", this.toString());

        // Create a map to store the issue data
        Map<String, Object> issueData = new HashMap<>();
        issueData.put("title", title);
        issueData.put("description", description);
        issueData.put("status", status.toString()); // Convert status to a string
        issueData.put("assignee", assignee);
        issueData.put("categoryId", categoryId);

        // Use setValue to save the data to Firebase
        issueRef.setValue(issueData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                if (error == null) {
                    // Data was successfully saved
                    Log.d("Issue", "Issue saved to Firebase");
                } else {
                    // There was an error
                    Log.e("Issue", "Error saving issue to Firebase: " + error.getMessage());
                }
            }
        });
    }

    static public void getIssueById(String id, final IssueCallback callback) {
        DatabaseReference issuesDatabase = FirebaseDatabase.getInstance().getReference("issues");

        DatabaseReference issueRef = issuesDatabase.child(id); // Use the provided ID

        issueRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String title = snapshot.child("title").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String status = snapshot.child("status").getValue(String.class);
                    String assignee = snapshot.child("assignee").getValue(String.class);
                    String categoryId = snapshot.child("categoryId").getValue(String.class);
                    Status statusEnum = Status.valueOf(status);

                    Issue issue = new Issue(id, title, description, assignee, statusEnum, categoryId);
                    callback.onIssueReceived(issue);
                } else {
                    callback.onError("Issue not found in Firebase");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onError("Error retrieving data from Firebase: " + error.getMessage());
            }
        });
    }

    @Override
    public String toString() {
        return "Issue{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", assignee='" + assignee + '\'' +
                ", status=" + status +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }

}