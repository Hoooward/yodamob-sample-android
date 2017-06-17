package com.yodamob.sample.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.view.Window;
import android.view.WindowManager;
import com.yodamob.sample.R;

/**
 * Created by tychooo on 2017/6/6.
 * base activity
 */

public abstract class BaseFragmentActivity extends FragmentActivity {

    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.main);

        if (findViewById(R.id.fragment_container) != null) {
            Fragment fragment = createFragment();

            fragment.setArguments(getIntent().getExtras());

            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}
