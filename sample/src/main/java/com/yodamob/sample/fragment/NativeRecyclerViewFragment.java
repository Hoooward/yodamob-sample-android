package com.yodamob.sample.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.yodamob.nativeads.*;
import com.yodamob.sample.Model.YodaSampleAdUnit;
import com.yodamob.sample.R;

import java.util.EnumSet;
import java.util.Locale;

/**
 * Created by tychooo on 2017/6/17.
 */

public class NativeRecyclerViewFragment extends Fragment {
    private YodaRecyclerAdapter mRecyclerAdapter;
    private YodaSampleAdUnit mAdConfiguration;
    private RequestParameters mRequestParameters;
    private enum LayoutType { LINEAR, GRID }
    private LayoutType mLayoutType;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mAdConfiguration = YodaSampleAdUnit.fromBundle(getArguments());
        final View view = inflater.inflate(R.layout.recycler_view_fragment, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.native_recycler_view);
        final DetailFragmentViewHolder viewHolder = DetailFragmentViewHolder.fromView(view);
        final Button switchButton = (Button) view.findViewById(R.id.switch_button);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                toggleRecyclerLayout();
            }
        });

        viewHolder.mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // If your app already has location access, include it here.
                final Location location = null;
                final String keywords = viewHolder.mKeywordsField.getText().toString();

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

                if (mRecyclerAdapter != null) {
                    mRecyclerAdapter.refreshAds(mAdConfiguration.getSlotId(), mRequestParameters);
                }
            }
        });
        final String adSlotId = mAdConfiguration.getSlotId();
        viewHolder.mDescriptionView.setText(mAdConfiguration.getDescription());
        viewHolder.mSlotIdView.setText(adSlotId);

        final RecyclerView.Adapter originalAdapter = new DemoRecyclerAdapter();

        mRecyclerAdapter = new YodaRecyclerAdapter(getActivity(), originalAdapter,
                new MoPubNativeAdPositioning.MoPubServerPositioning());

        YodaStaticNativeAdRenderer yodaStaticNativeAdRenderer = new YodaStaticNativeAdRenderer(
                new ViewBinder.Builder(R.layout.native_ad_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build()
        );


        mRecyclerAdapter.registerAdRenderer(yodaStaticNativeAdRenderer);

        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLayoutType = LayoutType.LINEAR;
        mRecyclerAdapter.loadAds(mAdConfiguration.getSlotId());
        return view;
    }

    void toggleRecyclerLayout() {
        if (mLayoutType == LayoutType.LINEAR) {
            mLayoutType = LayoutType.GRID;
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        } else {
            mLayoutType = LayoutType.LINEAR;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
    }

    @Override
    public void onDestroyView() {
        // You must call this or the ad adapter may cause a memory leak.
        mRecyclerAdapter.destroy();
        super.onDestroyView();
    }

    private static class DemoRecyclerAdapter extends RecyclerView.Adapter<DemoViewHolder> {
        private static final int ITEM_COUNT = 150;
        @Override
        public DemoViewHolder onCreateViewHolder(final ViewGroup parent,
                                                 final int viewType) {
            final View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new DemoViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final DemoViewHolder holder, final int position) {
            holder.textView.setText(String.format(Locale.US, "Content Item #%d", position));
        }

        @Override
        public long getItemId(final int position) {
            return (long) position;
        }

        @Override
        public int getItemCount() {
            return ITEM_COUNT;
        }
    }

    /**
     * A view holder for R.layout.simple_list_item_1
     */
    private static class DemoViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public DemoViewHolder(final View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
