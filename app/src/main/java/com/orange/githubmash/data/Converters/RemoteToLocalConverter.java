package com.orange.githubmash.data.Converters;

import android.content.Context;
import android.content.SharedPreferences;

import com.orange.githubmash.data.local.LocalGitRepoModel;
import com.orange.githubmash.data.local.LocalOwner;
import com.orange.githubmash.data.remote.RemoteGitRepoModel;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.orange.githubmash.utils.fields.GlobalFields;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RemoteToLocalConverter
{
    private SharedPreferences mPreferences;
    public RemoteToLocalConverter(Context context) {
        this.mPreferences = context.getSharedPreferences(GlobalFields.sharedPrefFile, MODE_PRIVATE);
    }

    public LocalGitRepoModel LocalRep (RemoteGitRepoModel remote)
    {
        String entry_owner = mPreferences.getString("USER_NAME", "")+GlobalFields.localmin;
        LocalGitRepoModel g=new LocalGitRepoModel(entry_owner,remote.getOwner().getLogin(),remote.getmName(),remote.getmDescription(),remote.getmUrl(),remote.getmWatchers());
        return g;
    }
    public LocalGitRepoModel LocalRepfav (RemoteGitRepoModel remote)
    {
        String entry_owner = mPreferences.getString("USER_NAME", "")+GlobalFields.localfav;
        LocalGitRepoModel g=new LocalGitRepoModel(entry_owner,remote.getOwner().getLogin(),remote.getmName(),remote.getmDescription(),remote.getmUrl(),remote.getmWatchers());
        return g;
    }
    public LocalOwner LocalOwnerfav (RemoteOwner remote)
    {
        String entry_owner = mPreferences.getString("USER_NAME", "");
        LocalOwner g=new LocalOwner(entry_owner,remote.getLogin(),remote.getHtmlUrl(),remote.getAvatarUrl());
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
    public List<LocalGitRepoModel> LocalRepfavList (List<RemoteGitRepoModel> remote)
    {
        List<LocalGitRepoModel> l=new ArrayList<LocalGitRepoModel>();
        for(int i=0;i<remote.size();++i)
        {
            l.add(LocalRepfav(remote.get(i)));
        }
        return l;
    }
}
