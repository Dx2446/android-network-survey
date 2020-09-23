/*
 * Copyright (C) 2012-2018 Paul Watts (paulcwatts@gmail.com), Sean J. Barbeau (sjbarbeau@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.craxiom.networksurvey.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.RestrictionsManager;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.preference.PreferenceManager;

import com.craxiom.networksurvey.Application;
import com.craxiom.networksurvey.R;

import timber.log.Timber;

/**
 * A class containing utility methods related to preferences.
 * <p>
 * Originally from the GPS Test open source Android app.  https://github.com/barbeau/gpstest
 */
public class PreferenceUtils
{
    /**
     * Gets the scan rate preference associated with the provide preference key.
     * <p>
     * First, this method tries to pull the MQM provided scan rate. If it is not set (either because the device is not
     * under MDM control, or if that specific value is not set by the MDM administrator) then the value is pulled from
     * the Android Shared Preferences (aka from the user settings). If it is not set there then the provided default
     * value is used.
     *
     * @param scanRatePreferenceKey  The preference key to use when pulling the scan rate from MDM and Shared Preferences.
     * @param defaultScanRateSeconds The default scan rate to fall back on if the scan rate could not be found.
     * @param context                The context to use when getting the Shared Preferences and Restriction Manager
     * @return The scan rate to use.
     * @since 0.3.0
     */
    public static int getScanRatePreferenceMs(String scanRatePreferenceKey, int defaultScanRateSeconds, Context context)
    {
        final RestrictionsManager restrictionsManager = (RestrictionsManager) context.getSystemService(Context.RESTRICTIONS_SERVICE);

        // First try to use the MDM provided value.
        if (restrictionsManager != null)
        {
            final Bundle mdmProperties = restrictionsManager.getApplicationRestrictions();

            final int scanRateSeconds = mdmProperties.getInt(scanRatePreferenceKey);
            if (scanRateSeconds > 0)
            {
                return scanRateSeconds * 1_000;
            }
        }

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // Next, try to use the value from user preferences.
        final String gnssScanInterval = preferences.getString(scanRatePreferenceKey,
                String.valueOf(defaultScanRateSeconds));
        try
        {
            return Integer.parseInt(gnssScanInterval) * 1_000;
        } catch (Exception e)
        {
            Timber.e(e, "Could not convert the GNSS scan interval user preference (%s) to an int", gnssScanInterval);
            return defaultScanRateSeconds * 1_000;
        }
    }

    @TargetApi(9)
    public static void saveString(SharedPreferences prefs, String key, String value)
    {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(key, value);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            edit.apply();
        } else
        {
            edit.commit();
        }
    }

    public static void saveString(String key, String value)
    {
        saveString(Application.getPrefs(), key, value);
    }

    @TargetApi(9)
    public static void saveInt(SharedPreferences prefs, String key, int value)
    {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putInt(key, value);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            edit.apply();
        } else
        {
            edit.commit();
        }
    }

    public static void saveInt(String key, int value)
    {
        saveInt(Application.getPrefs(), key, value);
    }

    @TargetApi(9)
    public static void saveLong(SharedPreferences prefs, String key, long value)
    {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(key, value);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            edit.apply();
        } else
        {
            edit.commit();
        }
    }

    public static void saveLong(String key, long value)
    {
        saveLong(Application.getPrefs(), key, value);
    }

    @TargetApi(9)
    public static void saveBoolean(SharedPreferences prefs, String key, boolean value)
    {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean(key, value);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            edit.apply();
        } else
        {
            edit.commit();
        }
    }

    public static void saveBoolean(String key, boolean value)
    {
        saveBoolean(Application.getPrefs(), key, value);
    }

    @TargetApi(9)
    public static void saveFloat(SharedPreferences prefs, String key, float value)
    {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putFloat(key, value);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            edit.apply();
        } else
        {
            edit.commit();
        }
    }

    public static void saveFloat(String key, float value)
    {
        saveFloat(Application.getPrefs(), key, value);
    }

    @TargetApi(9)
    public static void saveDouble(SharedPreferences prefs, String key, double value)
    {
        SharedPreferences.Editor edit = prefs.edit();
        edit.putLong(key, Double.doubleToRawLongBits(value));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD)
        {
            edit.apply();
        } else
        {
            edit.commit();
        }
    }

    @TargetApi(9)
    public static void saveDouble(String key, double value)
    {
        saveDouble(Application.getPrefs(), key, value);
    }

    /**
     * Gets a double for the provided key from preferences, or the default value if the preference
     * doesn't currently have a value
     *
     * @param key          key for the preference
     * @param defaultValue the default value to return if the key doesn't have a value
     * @return a double from preferences, or the default value if it doesn't exist
     */
    public static Double getDouble(String key, double defaultValue)
    {
        if (!Application.getPrefs().contains(key))
        {
            return defaultValue;
        }
        return Double.longBitsToDouble(Application.getPrefs().getLong(key, 0));
    }

    /**
     * Gets a boolean for the provided key from preferences, or the default value if the preference
     * doesn't currently have a value
     *
     * @param key          key for the preference
     * @param defaultValue the default value to return if the key doesn't have a value
     * @return a boolean from preferences, or the default value if it doesn't exist
     */
    public static boolean getBoolean(String key, boolean defaultValue)
    {
        return Application.getPrefs().getBoolean(key, defaultValue);
    }

    public static String getString(String key)
    {
        return Application.getPrefs().getString(key, null);
    }

    public static long getLong(String key, long defaultValue)
    {
        return Application.getPrefs().getLong(key, defaultValue);
    }

    public static float getFloat(String key, float defaultValue)
    {
        return Application.getPrefs().getFloat(key, defaultValue);
    }

    /**
     * Returns the currently selected satellite sort order as the index in R.array.sort_sats
     *
     * @return the currently selected satellite sort order as the index in R.array.sort_sats
     */
    public static int getSatSortOrderFromPreferences()
    {
        Resources r = Application.get().getResources();
        SharedPreferences settings = Application.getPrefs();
        String[] sortOptions = r.getStringArray(R.array.sort_sats);
        String sortPref = settings.getString(r.getString(
                R.string.pref_key_default_sat_sort), sortOptions[0]);
        for (int i = 0; i < sortOptions.length; i++)
        {
            if (sortPref.equalsIgnoreCase(sortOptions[i]))
            {
                return i;
            }
        }
        return 0;  // Default to the first option
    }

    /**
     * Removes the specified preference by deleting it
     */
    public static void remove(String key)
    {
        SharedPreferences.Editor edit = Application.getPrefs().edit();
        edit.remove(key).apply();
    }
}
