package com.yodamob.sample.activity;

import android.support.v4.app.Fragment;

import com.yodamob.sample.fragment.root_list_fragment.YodaListFragment;

public class MainActivity extends BaseFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new YodaListFragment();
    }
}
