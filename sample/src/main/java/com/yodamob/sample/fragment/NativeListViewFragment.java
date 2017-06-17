package com.yodamob.sample.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.yodamob.nativeads.RequestParameters;
import com.yodamob.nativeads.YodaAdAdapter;
import com.yodamob.sample.Model.YodaSampleAdUnit;
import com.yodamob.sample.R;

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
    }
}
