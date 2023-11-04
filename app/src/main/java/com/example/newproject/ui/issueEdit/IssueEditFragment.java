package com.example.newproject.ui.issueEdit;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newproject.R;
import com.example.newproject.databinding.FragmentHomeBinding;
import com.example.newproject.databinding.FragmentIssueEditBinding;
import com.example.newproject.models.Category;
import com.example.newproject.models.Issue;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class IssueEditFragment extends Fragment {
    private static final int CONTACT_PICKER_RESULT = 1001;
    private TextView selectedContactTextView;
    private FragmentIssueEditBinding binding;

    private Issue selectedItem;

    private DatabaseReference databaseReference;
    private Spinner categorySpinner;

    private ArrayList<Category> categories = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentIssueEditBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        String id = getArguments().getString("id");
        Issue.getIssueById(id, new IssueCallback() {
            @Override
            public void onIssueReceived(Issue issue) {
                selectedItem = issue;
                binding.issueTitleEditText.setText(issue.getTitle());
                binding.descriptionEditText.setText(issue.getDescription());
                binding.selectedContactTextView.setText(issue.getAssignee());

                // TODO: set the spinner to the correct category
                for (int i = 0; i < categories.size(); i++) {
                    Category category = categories.get(i);
                    if (category.getId().equals(selectedItem.getCategoryId())) {
                        categorySpinner.setSelection(i);
                        break;
                    }
                }

                switch (issue.getStatus()) {
                    case PENDING:
                        binding.radioPending.setChecked(true);
                        break;
                    case IN_PROGRESS:
                        binding.radioInProgress.setChecked(true);
                        break;
                    case COMPLETED:
                        binding.radioCompleted.setChecked(true);
                        break;
                }
            }

            @Override
            public void onError(String error) {

            }
        });


        // add event listener to the save button
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem.setTitle(binding.issueTitleEditText.getText().toString());
                selectedItem.setDescription(binding.descriptionEditText.getText().toString());
                if (binding.radioPending.isChecked()) {
                    selectedItem.setStatus(Issue.Status.PENDING);
                } else if (binding.radioInProgress.isChecked()) {
                    selectedItem.setStatus(Issue.Status.IN_PROGRESS);
                } else if (binding.radioCompleted.isChecked()) {
                    selectedItem.setStatus(Issue.Status.COMPLETED);
                }
                selectedItem.setAssignee(selectedContactTextView.getText().toString());
                selectedItem.setCategoryId(categories.get(categorySpinner.getSelectedItemPosition()).getId());
                selectedItem.save();
                //navigate back to the home fragment
                getActivity().onBackPressed();
            }
        });


        selectedContactTextView = view.findViewById(R.id.selectedContactTextView);
        Button selectContactButton = view.findViewById(R.id.selectContactButton);

        selectContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the contact picker intent
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
            }
        });

        categorySpinner = view.findViewById(R.id.categorySpinner);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("categories");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> itemNames = new ArrayList<>();;
                categories = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    String name = dataSnapshot.child("name").getValue(String.class);
                    String id = dataSnapshot.getKey();
                    itemNames.add(name);

                    Category category = new Category(id, name);

                    categories.add(category);

                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, itemNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                categorySpinner.setAdapter(spinnerAdapter);

                // Set a listener to handle item selection from the Spinner
                categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Handle item selection here
                        String selectedItemName = itemNames.get(position);
                        // You can use selectedItemName to do further actions
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Handle nothing selected, if needed
                    }
                });

                if (selectedItem != null) {
                    for (int i = 0; i < categories.size(); i++) {
                        Category category = categories.get(i);
                        if (category.getId().equals(selectedItem.getCategoryId())) {
                            categorySpinner.setSelection(i);
                            break;
                        }
                    }
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACT_PICKER_RESULT) {
            if (resultCode == getActivity().RESULT_OK) {
                // Contact has been selected
                Uri contactUri = data.getData();
                Cursor cursor = getActivity().getContentResolver().query(contactUri, null, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    String contactName = cursor.getString(nameColumnIndex);
                    cursor.close();

                    // Display the selected contact in your TextView
                    selectedContactTextView.setText(contactName);
                    if (selectedItem != null) {
                        selectedItem.setAssignee(contactName);
                    }
                }
            }
        }
    }

}