package com.yodamob.sample.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yodamob.sample.R;
import com.yodamob.sample.utils.YodamobSQLiteHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static com.yodamob.sample.Model.YodamobDemoAdUnit.AdType.BANNER;
import static com.yodamob.sample.utils.YodamobSQLiteHelper.COLUMN_AD_TYPE;
import static com.yodamob.sample.utils.YodamobSQLiteHelper.COLUMN_AD_UNIT_ID;
import static com.yodamob.sample.utils.YodamobSQLiteHelper.COLUMN_DESCRIPTION;
import static com.yodamob.sample.utils.YodamobSQLiteHelper.COLUMN_ID;
import static com.yodamob.sample.utils.YodamobSQLiteHelper.COLUMN_USER_GENERATED;
import static com.yodamob.sample.utils.YodamobSQLiteHelper.TABLE_AD_CONFIGURATIONS;

/**
 * Created by tychooo on 2017/6/6.
 */

public class AdUnitDataSource {

    private Context mContext;
    private YodamobSQLiteHelper mYodamobSQLiteHelper;
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
        mYodamobSQLiteHelper = new YodamobSQLiteHelper(context);
        populateDefaultDemoAdUnits();
    }

    YodamobDemoAdUnit createDefaultDemoAdUnit(final YodamobDemoAdUnit adUnit) {
        return createDemoAdUnit(adUnit, false);
    }

    YodamobDemoAdUnit createDemoAdUnit(final YodamobDemoAdUnit adUnit) {
        return createDemoAdUnit(adUnit, true);
    }

    // 向数据库中写去新 unit
    private YodamobDemoAdUnit createDemoAdUnit(final YodamobDemoAdUnit demoAdUnit, final boolean isUserGenerated) {

        final ContentValues values = new ContentValues();
        final int userGenerated = isUserGenerated ? 1 : 0;
        values.put(COLUMN_AD_UNIT_ID, demoAdUnit.getAdUnitId());
        values.put(COLUMN_DESCRIPTION, demoAdUnit.getDescription());
        values.put(COLUMN_USER_GENERATED, userGenerated);
        values.put(COLUMN_AD_TYPE, demoAdUnit.getFragmentClassname());

        final SQLiteDatabase database = mYodamobSQLiteHelper.getWritableDatabase();
        final long insertId = database.insert(TABLE_AD_CONFIGURATIONS, null, values);
        final Cursor cursor = database.query(TABLE_AD_CONFIGURATIONS, mAllColums, COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();

        final YodamobDemoAdUnit newAdConfiguration = cursorToAdConfiguration(cursor);
        cursor.close();
        database.close();

        if (newAdConfiguration != null) {

        }
        return newAdConfiguration;

    }

    // 删除 unit
    void deleteDemoAdUnit(final YodamobDemoAdUnit adConfiguration) {
        final long id = adConfiguration.getId();
        SQLiteDatabase database = mYodamobSQLiteHelper.getWritableDatabase();
        database.delete(TABLE_AD_CONFIGURATIONS, COLUMN_ID + " = " + id, null);
        database.close();
    }

    // 获取所有 units
    public List<YodamobDemoAdUnit> getAllAdUnits() {
        final List<YodamobDemoAdUnit> adConfigurations = new ArrayList<>();
        SQLiteDatabase database = mYodamobSQLiteHelper.getReadableDatabase();
        final Cursor cursor = database.query(TABLE_AD_CONFIGURATIONS, mAllColums, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            final YodamobDemoAdUnit adConfiguration = cursorToAdConfiguration(cursor);
            adConfigurations.add(adConfiguration);
            cursor.moveToNext();
        }

        cursor.close();
        database.close();

        return adConfigurations;
    }

    // 默认 units, 第一次被读取后会被写入数据库
    List<YodamobDemoAdUnit> getDefaultAdUnits() {
        final List<YodamobDemoAdUnit> adUnitList = new ArrayList<>();
        adUnitList.add(
                new YodamobDemoAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_banner), BANNER)
                        .description("YodaMob Banner Demo")
                        .build());

        adUnitList.add(
                new YodamobDemoAdUnit
                        .Builder(mContext.getString(R.string.ad_unit_id_interstitial), BANNER)
                        .description("YodaMob Interstitial Demo")
                        .build());
        // TODO: - 添加静态 unit
        return adUnitList;
    }


    private void populateDefaultDemoAdUnits() {

        final HashSet<YodamobDemoAdUnit> allAdUnits = new HashSet<>();
        for (final YodamobDemoAdUnit adUnit : getAllAdUnits()) {
            allAdUnits.add(adUnit);
        }

        for (final YodamobDemoAdUnit defaultAdUnit: getDefaultAdUnits()) {
            if (!allAdUnits.contains(defaultAdUnit)) {
                createDefaultDemoAdUnit(defaultAdUnit);
            }
        }
    }

    private YodamobDemoAdUnit cursorToAdConfiguration(final Cursor cursor) {
        final long id = cursor.getLong(0);
        final String adUnitId = cursor.getString(1);
        final String description = cursor.getString(2);
        final int userGenerated = cursor.getInt(3);
        final YodamobDemoAdUnit.AdType adType = YodamobDemoAdUnit.AdType.fromFragmentClassName(cursor.getString(4));

        if (adType == null) {
            return null;
        }

        return new YodamobDemoAdUnit.Builder(adUnitId, adType).description(description).isUserDefined(userGenerated == 1).id(id).build();

    }
}

