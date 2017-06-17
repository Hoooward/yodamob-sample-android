package com.yodamob.sample.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.yodamob.nativeads.*;
import com.yodamob.sample.Model.YodaSampleAdUnit;
import com.yodamob.sample.R;

import java.util.EnumSet;

/**
 * Created by tychooo on 2017/6/17.
 */

public class NativeListViewFragment extends Fragment {
    private YodaAdAdapter mAdAdapter;
    private YodaSampleAdUnit mAdConfiguration;
    private RequestParameters mRequestParameters;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mAdConfiguration = YodaSampleAdUnit.fromBundle(getArguments());
        final View view = inflater.inflate(R.layout.native_list_view_fragment, container, false);
        final ListView listView = (ListView) view.findViewById(R.id.native_list_view);
        final DetailFragmentViewHolder views = DetailFragmentViewHolder.fromView(view);
        views.mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                mAdAdapter.loadAds(mAdConfiguration.getSlotId(), mRequestParameters);
            }
        });

        final String adSlotId = mAdConfiguration.getSlotId();
        views.mDescriptionView.setText(mAdConfiguration.getDescription());
        views.mSlotIdView.setText(adSlotId);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);

        for (int i = 0; i < 100; ++i) {
            adapter.add("Item" + i);
        }

        // Create an ad adapter that gets its positioning information from the YodaMob Server.
        // This adapter will be used in place of the original adapter for the ListView.
        mAdAdapter = new YodaAdAdapter(getActivity(), adapter, new MoPubNativeAdPositioning.MoPubServerPositioning());

        // Set up a renderer that knows how to put ad data in your custom native view.
        final YodaStaticNativeAdRenderer staticAdRender = new YodaStaticNativeAdRenderer(
                new ViewBinder.Builder(R.layout.native_ad_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mainImageId(R.id.native_main_image)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build());

        // Set up a renderer for a video native ad.
        final YodaVideoNativeAdRenderer videoAdRenderer = new YodaVideoNativeAdRenderer(
                new MediaViewBinder.Builder(R.layout.video_ad_list_item)
                        .titleId(R.id.native_title)
                        .textId(R.id.native_text)
                        .mediaLayoutId(R.id.native_media_layout)
                        .iconImageId(R.id.native_icon_image)
                        .callToActionId(R.id.native_cta)
                        .privacyInformationIconImageId(R.id.native_privacy_information_icon_image)
                        .build());


        // Register the renderers with the YodaAdAdapter and then set the adapter on the ListView.

        mAdAdapter.registerAdRenderer(videoAdRenderer);
        mAdAdapter.registerAdRenderer(staticAdRender);
        listView.setAdapter(mAdAdapter);

        mAdAdapter.loadAds(mAdConfiguration.getSlotId(), mRequestParameters);
        return view;
    }

    @Override
    public void onDestroyView() {
        // You must call this or the ad adapter may cause a memory leak.
        mAdAdapter.destroy();
        super.onDestroyView();
    }
}
