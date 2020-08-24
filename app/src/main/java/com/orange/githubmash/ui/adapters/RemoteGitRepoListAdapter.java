package com.orange.githubmash.ui.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.orange.githubmash.R;
import com.orange.githubmash.data.remote.RemoteGitRepoModel;

import java.util.List;

/* renamed from: com.orange.githubmash.ui.adapters.RepoListAdapter */
public class RemoteGitRepoListAdapter extends Adapter<RemoteGitRepoListAdapter.RepoViewHolder> {
    private final LayoutInflater mInflater;
    private List<RemoteGitRepoModel> mRepos;
    private SharedPreferences appsettings;
    public Integer iscreated=0;
    /* renamed from: com.orange.githubmash.ui.adapters.RepoListAdapter$RepoViewHolder */
    class RepoViewHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public final TextView repoDescription;
        /* access modifiers changed from: private */
        public final TextView repoName;
        /* access modifiers changed from: private */
        public final TextView repoOwner;
        /* access modifiers changed from: private */
        public final TextView repoWatchers;

        private RepoViewHolder(View itemView) {
            super(itemView);
            this.repoName = (TextView) itemView.findViewById(R.id.myrepname);
            this.repoOwner = (TextView) itemView.findViewById(R.id.Owner);
            this.repoDescription = (TextView) itemView.findViewById(R.id.created);
            this.repoWatchers = (TextView) itemView.findViewById(R.id.Updated);
        }
    }

    public RemoteGitRepoListAdapter(Context context) {
        iscreated=1;
        this.mInflater = LayoutInflater.from(context);
        appsettings= PreferenceManager.getDefaultSharedPreferences(context);
    }

    public RepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RepoViewHolder(this.mInflater.inflate(R.layout.myrepitem, parent, false));
    }

    public void onBindViewHolder(RepoViewHolder holder, int position) {
        List<RemoteGitRepoModel> list = this.mRepos;
        if (list != null) {
            RemoteGitRepoModel current = (RemoteGitRepoModel) list.get(position);
            holder.repoName.setText(current.getmName());
            holder.repoOwner.setText("Owner: "+current.getOwner().getLogin());
            holder.repoDescription.setText("Description: "+current.getmDescription());
            holder.repoWatchers.setText("Number of Watchers: "+String.valueOf(current.getmWatchers()));
            return;
        }
        String str = "NA";
        holder.repoName.setText(str);
        holder.repoOwner.setText(str);
        holder.repoDescription.setText(str);
        holder.repoWatchers.setText(str);
    }

    public RemoteGitRepoModel getRepAtPosition(int position) {
        return (RemoteGitRepoModel) this.mRepos.get(position);
    }

    public void setReps(List<RemoteGitRepoModel> reps) {
        this.mRepos = reps;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        List<RemoteGitRepoModel> list = this.mRepos;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}