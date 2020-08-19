package com.orange.githubmash.ui.logout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.orange.githubmash.Login;
import com.orange.githubmash.MainActivity;
import com.orange.githubmash.R;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogoutFragment extends Fragment {

    private SharedPreferences mPreferences;
    private String sharedPrefFile = "com.example.android.hellosharedprefs";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mPreferences = this.getActivity().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        new AlertDialog.Builder(requireContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Logout?")
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                        preferencesEditor.putString("TOKEN_NAME", "");
                        preferencesEditor.putString("TOKEN_TYPE", "");
                        preferencesEditor.apply();
                        startActivity(new Intent(getActivity(), Login.class));
                        requireActivity().finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();

        return inflater.inflate(R.layout.fragment_logout, container, false);
    }
}