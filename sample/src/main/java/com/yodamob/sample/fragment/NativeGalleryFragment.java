package com.yodamob.sample.fragment;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.yodamob.nativeads.*;
import com.yodamob.sample.Model.YodaSampleAdUnit;
import com.yodamob.sample.R;

import java.util.EnumSet;

/**
 * Created by tychooo on 2017/6/17.
 */

public class NativeGalleryFragment extends Fragment implements YodaNativeAdLoadedListener {
    private YodaSampleAdUnit mAdConfiguration;
    private ViewPager mViewPager;
    private CustomPagerAdapter mPagerAdapter;
    private YodaStreamAdPlacer mStreamAdPlacer;
    private RequestParameters mRequestParameters;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAdConfiguration = YodaSampleAdUnit.fromBundle(getArguments());
        final View view = inflater.inflate(R.layout.native_gallery_fragment, container, false);
        final DetailFragmentViewHolder views = DetailFragmentViewHolder.fromView(view);
        views.mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // If your app already has location access, include it here.
                final Location location = null;
                final String keywords = views.mKeywordsField.getText().toString();

                // Setting desired assets on your request helps native ad networks and bidders
                // provide higher-quality ads.
                final EnumSet<RequestParameters.NativeAdAsset> desiredAssets = EnumSet.of(
                        RequestParameters.NativeAdAsset.TITLE,
                        RequestParameters.NativeAdAsset.TEXT,
                        RequestParameters.NativeAdAsset.ICON_IMAGE,
                        RequestParameters.NativeAdAsset.MAIN_IMAGE,
                        RequestParameters.NativeAdAsset.CALL_TO_ACTION_TEXT);

                mRequestParameters = new RequestParameters.Builder()
                        .location(location)
                        .keywords(keywords)
                        .desiredAssets(desiredAssets)
                        .build();

                if (mStreamAdPlacer != null) {
                    mStreamAdPlacer.loadAds(mAdConfiguration.getSlotId(), mRequestParameters);
                }
            }
        });

        final String adUnitId = mAdConfiguration.getSlotId();
        views.mDescriptionView.setText(mAdConfiguration.getDescription());
        views.mSlotIdView.setText(adUnitId);
        mViewPager = (ViewPager) view.findViewById(R.id.gallery_pager);


        // Set up a renderer for a static native ad.
        final YodaStaticNativeAdRenderer yodaStaticNativeAdRenderer = new YodaStaticNativeAdRenderer(
                new ViewBinder.Builder(R.layout.native_ad_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build()
        );
        // This ad placer is used to automatically insert ads into the ViewPager.
        mStreamAdPlacer = new YodaStreamAdPlacer(getActivity());
        mStreamAdPlacer.registerAdRenderer(yodaStaticNativeAdRenderer);
        mStreamAdPlacer.setAdLoadedListener(this);

        mPagerAdapter = new CustomPagerAdapter(getChildFragmentManager(), mStreamAdPlacer);
        mViewPager.setAdapter(mPagerAdapter);
        return view;
    }

    @Override
    public void onDestroyView() {
        // You must call this or the ad adapter may cause a memory leak.
        mStreamAdPlacer.destroy();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        // MoPub recommends reloading ads when the user returns to a view.
        mStreamAdPlacer.loadAds(mAdConfiguration.getSlotId(), mRequestParameters);
        super.onResume();
    }

    @Override
    public void onAdLoaded(final int position) {
        mViewPager.invalidate();
        mPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAdRemoved(final int position) {
        mViewPager.invalidate();
        mPagerAdapter.notifyDataSetChanged();
    }

    private static class CustomPagerAdapter extends FragmentStatePagerAdapter {
        private static final int ITEM_COUNT = 30;
        private YodaStreamAdPlacer mStreamAdPlacer;

        public CustomPagerAdapter(final FragmentManager fragmentManager,
                                  YodaStreamAdPlacer streamAdPlacer) {
            super(fragmentManager);
            this.mStreamAdPlacer = streamAdPlacer;
            streamAdPlacer.setItemCount(ITEM_COUNT);
        }

        @Override
        public int getItemPosition(final Object object) {
            // This forces all items to be recreated when invalidate() is called on the ViewPager.
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(final int i) {
            mStreamAdPlacer.placeAdsInRange(i - 5, i + 5);
            if (mStreamAdPlacer.isAd(i)) {
                return AdFragment.newInstance(i);
            }
            return ContentFragment.newInstance(mStreamAdPlacer.getOriginalPosition(i));
        }

        @Override
        public int getCount() {
            return mStreamAdPlacer.getAdjustedCount(ITEM_COUNT);
        }

        @Override
        public CharSequence getPageTitle(final int position) {
            if (mStreamAdPlacer.isAd(position)) {
                return "Advertisement";
            }
            return "Content Item " + mStreamAdPlacer.getOriginalPosition(position);
        }

    }

    public static class ContentFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public static ContentFragment newInstance(int sectionNumber) {
            ContentFragment fragment = new ContentFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 final Bundle savedInstanceState) {
            // Inflate the view.
            View rootView = inflater.inflate(R.layout.native_gallery_content, container, false);
            int contentNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            TextView textView = (TextView) rootView.findViewById(R.id.native_gallery_content_text);
            textView.setText("Content Item " + contentNumber);
            return rootView;
        }
    }

    public YodaStreamAdPlacer getAdPlacer() {
        return mStreamAdPlacer;
    }

    public static class AdFragment extends Fragment {
        private static final String ARG_AD_POSITION = "ad_position";
        private YodaStreamAdPlacer mAdPlacer;

        public static AdFragment newInstance(int adPosition) {
            AdFragment fragment = new AdFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ARG_AD_POSITION, adPosition);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public void onAttach(final Activity activity) {
            mAdPlacer = ((NativeGalleryFragment) getParentFragment()).getAdPlacer();
            super.onAttach(activity);
        }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 final Bundle savedInstanceState) {
            if (mAdPlacer != null) {
                int position = getArguments().getInt(ARG_AD_POSITION);
                mAdPlacer.placeAdsInRange(position - 5, position + 5);
                return mAdPlacer.getAdView(position, null, container);
            }

            return null;
        }
    }
}
