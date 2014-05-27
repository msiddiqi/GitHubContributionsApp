package com.githubcardandroidapp.app;

import java.util.List;

public class GitHubUserRepositoriesImpl implements GitHubUserRepositories {
    private List<String> userRepositories;

    public GitHubUserRepositoriesImpl(List<String> userRepositories)
    {
        this.userRepositories = userRepositories;
    }

    @Override
    public List<String> getRepositories() {
        return this.userRepositories;
    }
}
