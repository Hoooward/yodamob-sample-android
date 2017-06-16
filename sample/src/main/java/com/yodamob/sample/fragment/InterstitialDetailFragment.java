package com.yodamob.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import com.yodamob.mobileads.YodaInterstitial;

/**
 *
 * Created by tychooo on 2017/6/16.
 */

public class InterstitialDetailFragment extends Fragment {

    private YodaInterstitial mYodaInterstitial;
    private Button mShowButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
