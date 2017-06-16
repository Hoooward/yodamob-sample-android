package com.yodamob.sample.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yodamob.sample.R;

/**
 * Created by tychooo on 2017/6/6.
 * handler detail fragment 中对应的 subviews
 */

public class DetailFragmentViewHolder {

    public final TextView mDescriptionView;
    public final Button mloadButton;
    public final TextView mAdUnitIdView;
    public final EditText mKeywordsField;

    public DetailFragmentViewHolder(final TextView descriptionView, final TextView adUnitIdView, final EditText keywordsField, final Button loadButton) {
        mDescriptionView = descriptionView;
        mloadButton = loadButton;
        mAdUnitIdView = adUnitIdView;
        mKeywordsField = keywordsField;
    }

    public static DetailFragmentViewHolder fromView(final View view) {

        final TextView descriptionView = (TextView) view.findViewById(R.id.description);
        final TextView adUnitIdView = (TextView) view.findViewById(R.id.ad_unit_id);
        final EditText keywordsField = (EditText) view.findViewById(R.id.keywords_field);
        final Button loadButton = (Button) view.findViewById(R.id.load_button);

        return new DetailFragmentViewHolder(descriptionView, adUnitIdView, keywordsField, loadButton);
    }
}

