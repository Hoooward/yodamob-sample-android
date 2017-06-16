package com.yodamob.sample.fragment.banner_detail_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

//import com.yodamob.mobileads.YodaView;
import com.yodamob.mobileads.YodaView;
import com.yodamob.sample.R;
import com.yodamob.sample.Model.YodamobDemoAdUnit;
import com.yodamob.sample.fragment.DetailFragmentViewHolder;

import static com.yodamob.sample.utils.Utils.hideSoftKeyboard;

/**
 * Created by tychooo on 2017/6/6.
 */


public abstract class AbstractBannerDetailFragment extends Fragment {

    private YodaView mYodaView;
    private YodamobDemoAdUnit mYodamobDemoAdUnit;

    public abstract int getWidth();
    public abstract int getHeight();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        // 获取当前 fragment 对应的 view
        final View view = inflater.inflate(R.layout.banner_detail_fragment, container, false);
        // 获取 view 中所包含的 subviews
        final DetailFragmentViewHolder views = DetailFragmentViewHolder.fromView(view);

        // 从 bundle 中读取当前的 unit
        mYodamobDemoAdUnit = YodamobDemoAdUnit.fromBundle(getArguments());
        mYodaView = (YodaView) view.findViewById(R.id.banner_yodaview);

        // 修改 Banner 的 frame
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mYodaView.getLayoutParams();
        layoutParams.weight = getWidth();
        layoutParams.height = getHeight();
        mYodaView.setLayoutParams(layoutParams);

        // 隐藏 keyword textField 键盘
        hideSoftKeyboard(views.mKeywordsField);

        views.mDescriptionView.setText(mYodamobDemoAdUnit.getDescription());
        views.mAdUnitIdView.setText(mYodamobDemoAdUnit.getAdUnitId());
        views.mloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // load AD
                mYodaView.loadAd();
            }
        });

        return view;
    }

}
