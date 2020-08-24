package com.orange.githubmash.data.Converters;

import android.content.Context;
import android.content.SharedPreferences;

import com.orange.githubmash.data.local.LocalGitRepoModel;
import com.orange.githubmash.data.remote.RemoteGitRepoModel;
import com.orange.githubmash.utils.fields.GlobalFields;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GitRepoConverter
{
    private SharedPreferences mPreferences;
    public GitRepoConverter(Context context) {
        this.mPreferences = context.getSharedPreferences(GlobalFields.sharedPrefFile, MODE_PRIVATE);
    }

    public LocalGitRepoModel LocalRep (RemoteGitRepoModel remote)
    {
        String entry_owner = mPreferences.getString("USER_NAME", "")+"#@local";
        LocalGitRepoModel g=new LocalGitRepoModel(entry_owner,remote.getOwner().getLogin(),remote.getmName(),remote.getmDescription(),remote.getmUrl(),remote.getmWatchers());
        return g;
    }

    public List<LocalGitRepoModel> LocalRepList (List<RemoteGitRepoModel> remote)
    {
        List<LocalGitRepoModel> l=new ArrayList<LocalGitRepoModel>(null);
        for(int i=0;i<remote.size();++i)
        {
            l.add(LocalRep(remote.get(i)));
        }
        return l;
    }
}
