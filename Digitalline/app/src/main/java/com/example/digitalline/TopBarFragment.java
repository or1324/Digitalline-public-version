package com.example.digitalline;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

public class TopBarFragment extends Fragment {

    public TopBarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean isLogout = getArguments().getBoolean("isLogout");
        boolean isBack = getArguments().getBoolean("isBack");
        View view = inflater.inflate(R.layout.fragment_top_bar, container, false);
        if (isLogout) {
            AppCompatButton logout = view.findViewById(R.id.logout);
            logout.setVisibility(View.VISIBLE);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    SharedPreferencesStorage.clearDatabase(getContext());
                    ExternalStorage.saveObject(getContext(), "devices", null);
                    ExternalStorage.saveObject(getContext(), "labs", null);
                    Intent intent = new Intent(getActivity(), SignInActivity.class);
                    startActivity(intent);
                }
            });
        }
        if (isBack) {
            AppCompatButton back = view.findViewById(R.id.back);
            back.setVisibility(View.VISIBLE);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
        return view;
    }
}