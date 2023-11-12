package com.example.newproject.models;

public class Member {
    private String name;
    private int assignedIssues;

    public Member(String name) {
        this.name = name;
        this.assignedIssues = 1;
    }

    public String getName() {
        return name;
    }

    public int getAssignedIssues() {
        return assignedIssues;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAssignedIssues(int assignedIssues) {
        this.assignedIssues = assignedIssues;
    }

    public void incrementAssignedIssues() {
        this.assignedIssues++;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", assignedIssues=" + assignedIssues +
                '}';
    }

}
