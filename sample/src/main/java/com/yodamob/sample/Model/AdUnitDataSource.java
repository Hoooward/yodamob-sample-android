package com.yodamob.sample.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yodamob.sample.R;
import com.yodamob.sample.utils.YodaSQLiteHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.yodamob.sample.Model.YodaSampleAdUnit.AdType.*;
import static com.yodamob.sample.utils.YodaSQLiteHelper.COLUMN_AD_TYPE;
import static com.yodamob.sample.utils.YodaSQLiteHelper.COLUMN_AD_UNIT_ID;
import static com.yodamob.sample.utils.YodaSQLiteHelper.COLUMN_DESCRIPTION;
import static com.yodamob.sample.utils.YodaSQLiteHelper.COLUMN_ID;
import static com.yodamob.sample.utils.YodaSQLiteHelper.COLUMN_USER_GENERATED;
import static com.yodamob.sample.utils.YodaSQLiteHelper.TABLE_AD_CONFIGURATIONS;

/**
 * Created by tychooo on 2017/6/6.
 */

public class AdUnitDataSource {

    private Context mContext;
    private YodaSQLiteHelper mYodaSQLiteHelper;
    private String[] mAllColums = {
            COLUMN_ID,
            COLUMN_AD_UNIT_ID,
            COLUMN_DESCRIPTION,
            COLUMN_USER_GENERATED,
            COLUMN_AD_TYPE
    };

    // 初始化
    public AdUnitDataSource(final Context context) {
        mContext = context;
        mYodaSQLiteHelper = new YodaSQLiteHelper(context);
        populateDefaultDemoAdUnits();
    }

    YodaSampleAdUnit createDefaultDemoAdUnit(final YodaSampleAdUnit adUnit) {
        return createDemoAdUnit(adUnit, false);
    }

    YodaSampleAdUnit createDemoAdUnit(final YodaSampleAdUnit adUnit) {
        return createDemoAdUnit(adUnit, true);
    }

    // 向数据库中写去新 unit
    private YodaSampleAdUnit createDemoAdUnit(final YodaSampleAdUnit demoAdUnit, final boolean isUserGenerated) {

        final ContentValues values = new ContentValues();
        final int userGenerated = isUserGenerated ? 1 : 0;
        values.put(COLUMN_AD_UNIT_ID, demoAdUnit.getSlotId());
        values.put(COLUMN_DESCRIPTION, demoAdUnit.getDescription());
        values.put(COLUMN_USER_GENERATED, userGenerated);
        values.put(COLUMN_AD_TYPE, demoAdUnit.getFragmentClassname());

        final SQLiteDatabase database = mYodaSQLiteHelper.getWritableDatabase();
        final long insertId = database.insert(TABLE_AD_CONFIGURATIONS, null, values);
        final Cursor cursor = database.query(TABLE_AD_CONFIGURATIONS, mAllColums, COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        final YodaSampleAdUnit newAdConfiguration = cursorToAdConfiguration(cursor);
        cursor.close();
        database.close();

        if (newAdConfiguration != null) {

        }
        return newAdConfiguration;

    }

    // 删除 unit
    void deleteDemoAdUnit(final YodaSampleAdUnit adConfiguration) {
        final long id = adConfiguration.getId();
        SQLiteDatabase database = mYodaSQLiteHelper.getWritableDatabase();
        database.delete(TABLE_AD_CONFIGURATIONS, COLUMN_ID + " = " + id, null);
        database.close();
    }

    // 获取所有 units
    public List<YodaSampleAdUnit> getAllAdUnits() {
        final List<YodaSampleAdUnit> adConfigurations = new ArrayList<>();
        SQLiteDatabase database = mYodaSQLiteHelper.getReadableDatabase();
        final Cursor cursor = database.query(TABLE_AD_CONFIGURATIONS, mAllColums, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            final YodaSampleAdUnit adConfiguration = cursorToAdConfiguration(cursor);
            adConfigurations.add(adConfiguration);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return adConfigurations;
    }

    // 默认 units, 第一次被读取后会被写入数据库
    List<YodaSampleAdUnit> getDefaultAdUnits() {
        final List<YodaSampleAdUnit> adUnitList = new ArrayList<>();
        adUnitList.add(
                new YodaSampleAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_banner), BANNER)
                        .description("YodaMob Banner Demo")
                        .build());

        adUnitList.add(
                new YodaSampleAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_interstitial), INTERSTITIAL)
                        .description("YodaMob Interstitial Demo")
                        .build());
        adUnitList.add(
                new YodaSampleAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_native), LIST_VIEW)
                        .description("YodaMob List View Demo")
                        .build());
        adUnitList.add(
                new YodaSampleAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_native), RECYCLER_VIEW)
                        .description("YodaMob Recycler View Demo")
                        .build());
        // TODO: - 添加静态 unit
        return adUnitList;
    }


    private void populateDefaultDemoAdUnits() {

        final HashSet<YodaSampleAdUnit> allAdUnits = new HashSet<>();
        for (final YodaSampleAdUnit adUnit : getAllAdUnits()) {
            allAdUnits.add(adUnit);
        }

        for (final YodaSampleAdUnit defaultAdUnit: getDefaultAdUnits()) {
            if (!allAdUnits.contains(defaultAdUnit)) {
                createDefaultDemoAdUnit(defaultAdUnit);
            }
        }
    }

    private YodaSampleAdUnit cursorToAdConfiguration(final Cursor cursor) {
        final long id = cursor.getLong(0);
        final String adUnitId = cursor.getString(1);
        final String description = cursor.getString(2);
        final int userGenerated = cursor.getInt(3);
        final YodaSampleAdUnit.AdType adType = YodaSampleAdUnit.AdType.fromFragmentClassName(cursor.getString(4));

        if (adType == null) {
            return null;
        }

        return new YodaSampleAdUnit.Builder(adUnitId, adType).description(description).isUserDefined(userGenerated == 1).id(id).build();

    }
}


