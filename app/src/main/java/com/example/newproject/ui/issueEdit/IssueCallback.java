package com.example.newproject.ui.issueEdit;

import com.example.newproject.models.Issue;

public interface IssueCallback {
    void onIssueReceived(Issue issue);
    void onError(String error);
}