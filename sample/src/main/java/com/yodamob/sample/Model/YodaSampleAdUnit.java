package com.yodamob.sample.Model;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

//import com.yodamob.common.logging.YodaLog;
import com.yodamob.sample.fragment.banner_detail_fragment.BannerDetailFragment;

/**
 * Created by tychooo on 2017/6/6.
 * list fragment 中每个 item 使用的 model 数据
 */

public class YodaSampleAdUnit implements Comparable<YodaSampleAdUnit> {

    // keys
    public static final String AD_UNIT_ID = "adUnitId";
    public static final String DESCRIPTION = "description";
    public static final String AD_TYPE = "adType";
    public static final String IS_USER_DEFINED = "isCustom";
    public static final String ID = "id";

    // 广告类型枚举
    public enum AdType {

        BANNER("Banner", BannerDetailFragment.class);




        private final String name;
        private final Class<? extends Fragment> fragmentClass;

        AdType(final String name, final Class<? extends Fragment> fragmentClass) {
            this.name = name;
            this.fragmentClass = fragmentClass;
        }

        private Class<? extends Fragment> getFragmentClass() {
            return fragmentClass;
        }

        String getName() { return name; }

        //根据 classname 指定 adType
        static AdType fromFragmentClassName(final String fragmentClassName) {
            for (final AdType adType : values()) {
                if (adType.fragmentClass.getName().equals(fragmentClassName)) {
                    return adType;
                }
            }
            return null;
        }
    }

    static class Builder {

        private final String mAdUnitId;
        private final AdType mAdType;
        private String mDescription;
        private boolean mIsUserDefined;
        private long mId;

        Builder(final String adUnitId, final AdType adType) {
            mAdUnitId = adUnitId;
            mAdType = adType;
            mId = -1;
        }

        Builder description(final String description) {
            mDescription = description;
            return this;
        }

        Builder isUserDefined(boolean userDefined) {
            mIsUserDefined = userDefined;
            return this;
        }

        Builder id(final long id) {
            mId = id;
            return this;
        }

        YodaSampleAdUnit build() {
            return new YodaSampleAdUnit(this);
        }
    }

    private final String mAdUnitId;
    private final AdType mAdType;
    private final String mDescription;
    private final boolean mIsUserDefined;
    private final long mId;

    private YodaSampleAdUnit(final Builder builder) {
        mAdType = builder.mAdType;
        mAdUnitId = builder.mAdUnitId;
        mDescription = builder.mDescription;
        mIsUserDefined = builder.mIsUserDefined;
        mId = builder.mId;
    }

    public Class <? extends Fragment> getFragmentClass() {
        return mAdType.getFragmentClass();
    }

    public String getAdUnitId() {
        return mAdUnitId;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getFragmentClassname() {
        return mAdType.getFragmentClass().getName();
    }

    public String getHeaderName() {
        return mAdType.name;
    }

    long getId() {
        return mId;
    }

    public boolean isUserDefined() {
        return mIsUserDefined;
    }

    // 持久化
    public Bundle toBundle() {
        final Bundle bundle = new Bundle();
        bundle.putLong(ID, mId);
        bundle.putString(AD_UNIT_ID, mAdUnitId);
        bundle.putSerializable(DESCRIPTION, mDescription);
        bundle.putSerializable(AD_TYPE, mAdType);
        bundle.putBoolean(IS_USER_DEFINED, mIsUserDefined);

        return bundle;
    }

    // 从 bundle 中读取数据
    public static YodaSampleAdUnit fromBundle(final Bundle bundle) {
//        YodaLog.d("" + bundle, null);
        final Long id = bundle.getLong(ID, -1L);
        final String adUnitId = bundle.getString(AD_UNIT_ID);
        final AdType adType = (AdType) bundle.getSerializable(AD_TYPE);
        final String description = bundle.getString(DESCRIPTION);
        final boolean isUserDefined = bundle.getBoolean(IS_USER_DEFINED, false);
        final Builder builder = new YodaSampleAdUnit.Builder(adUnitId, adType);
        builder.description(description);
        builder.id(id);
        builder.isUserDefined(isUserDefined);

        return builder.build();
    }


    // Comparable
    @Override
    public int compareTo(@NonNull YodaSampleAdUnit o) {

        if (mAdType != o.mAdType) {
            return mAdType.ordinal() - o.mAdType.ordinal();
        }

        return mDescription.compareTo(o.mDescription);
    }

    @Override
    public int hashCode() {
        int result = 11;
        result = 31 * result + mAdType.ordinal();
        result = 31 * result + (mIsUserDefined ? 1 : 0);
        result = 31 * result + mDescription.hashCode();
        result = 31 * result + mAdUnitId.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof YodaSampleAdUnit)) {
            return false;
        }

        final YodaSampleAdUnit that = (YodaSampleAdUnit) obj;

        return that.mAdType.equals(this.mAdType) &&
                that.mIsUserDefined == this.mIsUserDefined &&
                that.mDescription.equals(this.mDescription) &&
                that.mAdUnitId.equals(this.mAdUnitId);
    }
}

