package com.example.digitalline;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class FragmentUtils {
    public static void openTopBarFragment(boolean isBack, boolean isLogout, AppCompatActivity activity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isLogout", isLogout);
        bundle.putBoolean("isBack", isBack);
        activity.getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.fragment_container_view, TopBarFragment.class, bundle)
                .commit();
    }
}
