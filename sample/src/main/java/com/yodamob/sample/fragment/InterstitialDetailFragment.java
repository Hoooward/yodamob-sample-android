package com.yodamob.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.yodamob.mobileads.YodaErrorCode;
import com.yodamob.mobileads.YodaInterstitial;
import com.yodamob.sample.Model.YodaSampleAdUnit;
import com.yodamob.sample.R;
import com.yodamob.sample.utils.Utils;

/**
 *
 * Created by tychooo on 2017/6/16.
 */

public class InterstitialDetailFragment extends Fragment implements YodaInterstitial.InterstitialAdListener {

    private YodaInterstitial mYodaInterstitial;
    private Button mShowButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        final YodaSampleAdUnit adConfiguration = YodaSampleAdUnit.fromBundle(getArguments());
        final View view = inflater.inflate(R.layout.interstitial_detail_fragment, container, false);
        final DetailFragmentViewHolder views = DetailFragmentViewHolder.fromView(view);
        Utils.hideSoftKeyboard(views.mKeywordsField);

        final String adSlotId = adConfiguration.getSlotId();
        views.mDescriptionView.setText(adConfiguration.getDescription());
        views.mSlotIdView.setText(adSlotId);
        views.mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShowButton.setEnabled(false);
                if (mYodaInterstitial == null) {
                    mYodaInterstitial = new YodaInterstitial(getActivity(), adSlotId);
                    mYodaInterstitial.setInterstitialAdListener(InterstitialDetailFragment.this);
                }
                final String keywords = views.mKeywordsField.getText().toString();
                mYodaInterstitial.setKeywords(keywords);
                mYodaInterstitial.load();
            }
        });
        mShowButton = (Button) view.findViewById(R.id.interstitial_show_button);
        mShowButton.setEnabled(false);
        mShowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYodaInterstitial.show();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mYodaInterstitial != null) {
            mYodaInterstitial.destroy();
            mYodaInterstitial = null;
        }
    }

    // InterstitialAdListener implementation
    @Override
    public void onInterstitialLoaded(YodaInterstitial interstitial) {
        mShowButton.setEnabled(true);
        Utils.logToast(getActivity(), "Interstitial loaded.");
    }

    @Override
    public void onInterstitialFailed(YodaInterstitial interstitial, YodaErrorCode errorCode) {
        mShowButton.setEnabled(false);
        final String errorMessage = (errorCode != null) ? errorCode.toString() : "";
        Utils.logToast(getActivity(), "Interstitial failed to load: " + errorMessage);
    }

    @Override
    public void onInterstitialShown(YodaInterstitial interstitial) {
        mShowButton.setEnabled(false);
        Utils.logToast(getActivity(), "Interstitial shown.");
    }

    @Override
    public void onInterstitialClicked(YodaInterstitial interstitial) {
        Utils.logToast(getActivity(), "Interstitial clicked.");
    }

    @Override
    public void onInterstitialDismissed(YodaInterstitial interstitial) {
        Utils.logToast(getActivity(), "Interstitial dismissed.");
    }
}
