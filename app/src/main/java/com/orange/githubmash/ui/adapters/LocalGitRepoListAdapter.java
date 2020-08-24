package com.orange.githubmash.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;
import com.orange.githubmash.R;
import com.orange.githubmash.data.local.LocalGitRepoModel;
import com.orange.githubmash.ui.home.FavGitRepoFragment;
import com.orange.githubmash.ui.settings.Settings;

import java.util.List;
public class LocalGitRepoListAdapter extends RecyclerView.Adapter<LocalGitRepoListAdapter.LocalRepoViewHolder> {
    private final LayoutInflater mInflater;
    private List<LocalGitRepoModel> mRepos;
    private SharedPreferences appsettings;
    private Boolean show_owner;
    private Boolean show_descr;
    private Boolean show_watchers;
     class LocalRepoViewHolder extends RecyclerView.ViewHolder {
        private final TextView repoDescription;
        private final TextView repoName;
        private final TextView repoOwner;
        private final TextView repoWatchers;

        private LocalRepoViewHolder(View itemView) {
            super(itemView);
            this.repoName = (TextView) itemView.findViewById(R.id.myrepname);
            this.repoOwner = (TextView) itemView.findViewById(R.id.Owner);
            this.repoDescription = (TextView) itemView.findViewById(R.id.created);
            this.repoWatchers = (TextView) itemView.findViewById(R.id.Updated);
        }
    }

    public LocalGitRepoListAdapter(Context context, Class clas) {
        this.mInflater = LayoutInflater.from(context);
        appsettings= PreferenceManager.getDefaultSharedPreferences(context);

        if(clas== FavGitRepoFragment.class) {
            show_owner = appsettings.getBoolean(Settings.fav_owner, false);
            show_descr = appsettings.getBoolean(Settings.fav_descr, false);
            show_watchers = appsettings.getBoolean(Settings.fav_watchers, false);
        }
        else {
            show_owner = appsettings.getBoolean(Settings.me_owner, false);
            show_descr = appsettings.getBoolean(Settings.me_descr, false);
            show_watchers = appsettings.getBoolean(Settings.me_watchers, false);
        }

    }

    public LocalRepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocalRepoViewHolder(this.mInflater.inflate(R.layout.myrepitem, parent, false));
    }

    public void onBindViewHolder(LocalRepoViewHolder holder, int position) {
        if (mRepos != null) {
            LocalGitRepoModel current = (LocalGitRepoModel) mRepos.get(position);
            holder.repoName.setText(current.getName());
            holder.repoOwner.setText("Owner: "+current.getOwner());
            holder.repoDescription.setText("Description: "+current.getDescription());
            holder.repoWatchers.setText("Number of Watchers: "+String.valueOf(current.getWatchers()));

            if(!show_owner)
            {
                holder.repoOwner.setVisibility(View.GONE);
            }
            if(!show_descr)
            {
                holder.repoDescription.setVisibility(View.GONE);
            }
            if(!show_watchers)
            {
                holder.repoWatchers.setVisibility(View.GONE);
            }
            return;
        }
        String str = "NA";
        holder.repoName.setText(str);
        holder.repoOwner.setText(str);
        holder.repoDescription.setText(str);
        holder.repoWatchers.setText(str);
    }

    public LocalGitRepoModel getRepAtPosition(int position) {
        return (LocalGitRepoModel) this.mRepos.get(position);
    }

    public void setReps(List<LocalGitRepoModel> reps) {
        this.mRepos = reps;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        List<LocalGitRepoModel> list = this.mRepos;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}