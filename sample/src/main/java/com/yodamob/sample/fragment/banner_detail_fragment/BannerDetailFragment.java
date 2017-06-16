package com.yodamob.sample.fragment.banner_detail_fragment;

import com.yodamob.sample.R;

/**
 * Created by tychooo on 2017/6/6.
 */

public class BannerDetailFragment extends AbstractBannerDetailFragment {

    @Override
    public int getWidth() {
        return (int) getResources().getDimension(R.dimen.banner_width);
    }

    @Override
    public int getHeight() {
        return (int) getResources().getDimension(R.dimen.banner_height);
    }
}
