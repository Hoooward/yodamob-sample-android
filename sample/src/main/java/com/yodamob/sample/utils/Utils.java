package com.yodamob.sample.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
/**
 * Created by tychooo on 2017/6/6.
 */

public class Utils {

    static final String LOGTAG = "Yodamob Demo App";

    private Utils() {};

    static void validateAdUnitId(String adUnitId) throws  IllegalArgumentException {
        if (adUnitId == null) {
            throw new IllegalArgumentException("无效的广告位 id: null ad unit.");
        } else if (adUnitId.length() == 0) {
            throw new IllegalArgumentException("无效的广告位 id: empty ad unit.");
        } else if (adUnitId.length() > 256) {
            throw new IllegalArgumentException("无效的广告位 id: length too long.");
        } else if (!isAlphaNumeric(adUnitId)) {
            throw new IllegalArgumentException("无效的广告位 id: contains non-alphanumeric characters.");
        }
    }

    public static void hideSoftKeyboard(final View view) {
        final InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    static boolean isAlphaNumeric(final String input) {
        return input.matches("^[a-zA-Z0-9-_]*$");
    }

    static void logToast(Context context, String message) {
        Log.d(LOGTAG, message);

        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
    }
}
