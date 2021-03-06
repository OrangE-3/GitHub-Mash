package com.orange.githubmash.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.orange.githubmash.R;
import com.orange.githubmash.data.remote.RemoteOwner;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RemoteOwnerListAdapter extends RecyclerView.Adapter<RemoteOwnerListAdapter.UserViewHolder>
{
    private final LayoutInflater mInflater;
    private List<RemoteOwner> mOwners; // Cached copy of words

    public RemoteOwnerListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.myuseritem, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {

        if (mOwners != null) {
            RemoteOwner current = mOwners.get(position);
            holder.userName.setText(current.getLogin());
            holder.url.setText(current.getHtmlUrl());
            Picasso.get().load(current.getAvatarUrl()).resize(250,250).into(holder.avatarurl);
        } else {
            // Covers the case of data not being ready yet.
            holder.userName.setText("NA");
            holder.url.setText("NA");
            holder.avatarurl.setContentDescription("NA");
        }
    }

    public RemoteOwner getUserAtPosition (int position) {
        return mOwners.get(position);
    }

    public void setUsers(List<RemoteOwner> reps){
        mOwners = reps;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mOwners != null)
            return mOwners.size();
        else return 0;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView userName;
        private final TextView url;
        private final ImageView avatarurl;


        private UserViewHolder(View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.login_text);
            url = itemView.findViewById(R.id.url_text);
            avatarurl = itemView.findViewById(R.id.avatar_url);
        }
    }
}
