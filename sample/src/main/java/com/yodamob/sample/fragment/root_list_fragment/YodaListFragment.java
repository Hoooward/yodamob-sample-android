package com.yodamob.sample.fragment.root_list_fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yodamob.common.MoPub;
import com.yodamob.sample.Model.AdUnitDataSource;
import com.yodamob.sample.R;
import com.yodamob.sample.Model.YodaSampleAdUnit;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

import static com.yodamob.sample.Model.YodaSampleAdUnit.AdType;

/**
 * Created by tychooo on 2017/6/6.
 */

interface TrashCanClickListener {
    void onTrashCanClicked(YodaSampleAdUnit adUnit);
}

public class YodaListFragment extends android.support.v4.app.ListFragment implements TrashCanClickListener {

    private ListAdapter mAdapter;
    private AdUnitDataSource mAdUnitDataSource;

    private static final AdType[] adTypes = AdType.values();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        makeUI();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.list_fragment, container, false);
//        final Button button = (Button) view.findViewById(R.id.add_ad_unit_button);
        final TextView versionCodeView = (TextView) view.findViewById(R.id.version_code);
        versionCodeView.setText("SDK Version " + MoPub.SDK_VERSION);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onAddClicked(v);
//            }
//        });

        return view;
    }

    // 单击 item 跳转
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        final YodaSampleAdUnit adUnit = mAdapter.getItem(position);
        final FragmentTransaction fragmentTransaction =
                getActivity().getSupportFragmentManager().beginTransaction();

        final Class<? extends Fragment> fragmentClass = adUnit.getFragmentClass();
        final Fragment fragment;

        try {
            fragment = fragmentClass.newInstance();
        } catch (java.lang.InstantiationException e) {
//            YodaLog.e("创建 fragment 类出错" + fragmentClass, e);
            return;
        } catch (IllegalAccessException e) {
//            YodaLog.e("创建 fragment 类出错" + fragmentClass, e);
            return;
        }

        // 更换 fragment
        fragment.setArguments(adUnit.toBundle());

        fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();

    }

    private void makeUI() {

        mAdapter = new ListAdapter(getActivity(), this);
        mAdUnitDataSource = new AdUnitDataSource(getActivity());

        final List<YodaSampleAdUnit> adUnits = mAdUnitDataSource.getAllAdUnits();
        for (final YodaSampleAdUnit adUnit : adUnits) {
            mAdapter.add(adUnit);
        }
        //TODO: - 排序
        // mAdapter.sort()
        setListAdapter(mAdapter);

    }

    @Override
    public void onTrashCanClicked(YodaSampleAdUnit adUnit) {

    }
}
