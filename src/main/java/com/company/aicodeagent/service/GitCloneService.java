package com.company.aicodeagent.service;

import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GitCloneService {

    public File cloneRepository(String repoUrl) throws Exception {

        String repoName = repoUrl.substring(repoUrl.lastIndexOf('/') + 1)
                .replace(".git", "");

        File repoDir = new File("repos/" + repoName);

        if (repoDir.exists()) {
            return repoDir;
        }

        Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(repoDir)
                .call();

        return repoDir;
    }
}
