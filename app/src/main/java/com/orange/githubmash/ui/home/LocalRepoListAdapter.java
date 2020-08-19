package com.orange.githubmash.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.orange.githubmash.R;
import com.orange.githubmash.data.local.GitHubRepository;
import java.util.List;

/* renamed from: com.orange.githubmash.ui.home.LocalRepoListAdapter */
public class LocalRepoListAdapter extends Adapter<LocalRepoListAdapter.LocalRepoViewHolder> {
    private final LayoutInflater mInflater;
    private List<GitHubRepository> mRepos;

    /* renamed from: com.orange.githubmash.ui.home.LocalRepoListAdapter$LocalRepoViewHolder */
    class LocalRepoViewHolder extends ViewHolder {
        /* access modifiers changed from: private */
        public final TextView repoDescription;
        /* access modifiers changed from: private */
        public final TextView repoName;
        /* access modifiers changed from: private */
        public final TextView repoOwner;
        /* access modifiers changed from: private */
        public final TextView repoWatchers;

        private LocalRepoViewHolder(View itemView) {
            super(itemView);
            this.repoName = (TextView) itemView.findViewById(R.id.myrepname);
            this.repoOwner = (TextView) itemView.findViewById(R.id.Owner);
            this.repoDescription = (TextView) itemView.findViewById(R.id.created);
            this.repoWatchers = (TextView) itemView.findViewById(R.id.Updated);
        }
    }

    public LocalRepoListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public LocalRepoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocalRepoViewHolder(this.mInflater.inflate(R.layout.myrepitem, parent, false));
    }

    public void onBindViewHolder(LocalRepoViewHolder holder, int position) {
        List<GitHubRepository> list = this.mRepos;
        if (list != null) {
            GitHubRepository current = (GitHubRepository) list.get(position);
            holder.repoName.setText(current.getName());
            holder.repoOwner.setText(current.getOwner());
            holder.repoDescription.setText(current.getDescription());
            holder.repoWatchers.setText(String.valueOf(current.getWatchers()));
            return;
        }
        String str = "NA";
        holder.repoName.setText(str);
        holder.repoOwner.setText(str);
        holder.repoDescription.setText(str);
        holder.repoWatchers.setText(str);
    }

    public GitHubRepository getRepAtPosition(int position) {
        return (GitHubRepository) this.mRepos.get(position);
    }

    public void setReps(List<GitHubRepository> reps) {
        this.mRepos = reps;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        List<GitHubRepository> list = this.mRepos;
        if (list != null) {
            return list.size();
        }
        return 0;
    }
}