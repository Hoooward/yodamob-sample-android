package com.yodamob.sample.fragment.root_list_fragment;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yodamob.sample.R;
import com.yodamob.sample.Model.YodaSampleAdUnit;

import java.util.ArrayList;

/**
 * Created by tychooo on 2017/6/6.
 * list fragment Adapter
 */

class ListAdapter extends ArrayAdapter<YodaSampleAdUnit> {

    private final TrashCanClickListener mListener;

    private static class  ViewHolder {
        TextView separator;
        TextView description;
        TextView adUnitId;
        ImageView trashCan;
    }

    private final LayoutInflater mLayoutInflater;

    ListAdapter(final Context context, TrashCanClickListener listener) {
        super(context, 0, new ArrayList<YodaSampleAdUnit>());
        mListener = listener;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final View view;
        final ViewHolder viewHolder;

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.list_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.separator = (TextView) view.findViewById(R.id.separator);
            viewHolder.description = (TextView) view.findViewById(R.id.banner_description);
            viewHolder.adUnitId = (TextView) view.findViewById(R.id.banner_ad_unit_id);
            viewHolder.trashCan = (ImageView) view.findViewById(R.id.banner_delete);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        view.setTag(viewHolder);

        final YodaSampleAdUnit adUnit = getItem(position);

        if (adUnit == null) {
            return view;
        }

        viewHolder.description.setText(adUnit.getDescription());
        viewHolder.adUnitId.setText(adUnit.getAdUnitId());

        if (isFirstInSection(position)) {
            viewHolder.separator.setVisibility(View.VISIBLE);
            viewHolder.separator.setText(adUnit.getHeaderName());
        } else {
            viewHolder.separator.setVisibility(View.GONE);
        }

        if (adUnit.isUserDefined()) {
            viewHolder.trashCan.setVisibility(View.VISIBLE);
            viewHolder.trashCan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onTrashCanClicked(adUnit);
                }
            });
        } else {
            viewHolder.trashCan.setVisibility(View.INVISIBLE);
            viewHolder.trashCan.setOnClickListener(null);
        }


        return view;
    }

    private boolean isFirstInSection(int position) {
        if (position <= 0) {
            return true;
        }

        final YodaSampleAdUnit previous = getItem(position - 1);
        final YodaSampleAdUnit current = getItem(position);

        if (previous == null || current == null) {
            return false;
        }

        return !previous.getHeaderName().equals(current.getHeaderName());
    }
}

