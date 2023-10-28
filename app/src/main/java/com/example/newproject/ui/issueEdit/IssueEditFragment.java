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
import android.widget.Button;
import android.widget.TextView;

import com.example.newproject.R;
import com.example.newproject.databinding.FragmentHomeBinding;
import com.example.newproject.databinding.FragmentIssueEditBinding;
import com.example.newproject.models.Issue;

public class IssueEditFragment extends Fragment {
    private static final int CONTACT_PICKER_RESULT = 1001;
    private TextView selectedContactTextView;
    private FragmentIssueEditBinding binding;

    private Issue selectedItem;

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
                // Map the status to the related radio button
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