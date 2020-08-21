package com.orange.githubmash.data.Converters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orange.githubmash.data.local.GitHubRepository;
import com.orange.githubmash.data.remote.RemoteRepoModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.content.Context.MODE_PRIVATE;

public class RemoteToLocalRepository
{
    private String sharedPrefFile = "com.example.android.hellosharedprefs";
    private SharedPreferences mPreferences;

    public RemoteToLocalRepository(Context context) {
        this.mPreferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
    }

    public GitHubRepository LocalRep (RemoteRepoModel remote)
    {
        String entry_owner = mPreferences.getString("USER_NAME", "")+"#@local";
        GitHubRepository g=new GitHubRepository(entry_owner,remote.getOwner().getLogin(),remote.getmName(),remote.getmDescription(),remote.getmUrl(),remote.getmWatchers());
        return g;
    }

    public List<GitHubRepository> LocalRepList (List<RemoteRepoModel> remote)
    {
        List<GitHubRepository> l=new ArrayList<GitHubRepository>(null);
        for(int i=0;i<remote.size();++i)
        {
            l.add(LocalRep(remote.get(i)));
        }
        return l;
    }
}
