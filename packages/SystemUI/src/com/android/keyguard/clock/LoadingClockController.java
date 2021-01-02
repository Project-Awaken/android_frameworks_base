/*
 * Copyright (C) 2020 The ZenX-OS Project
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

package com.android.keyguard.clock;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint.Style;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextClock;
import android.content.Context;
import com.android.internal.util.awaken.Utils;
import android.text.Html;
import com.airbnb.lottie.LottieAnimationView;

import com.android.systemui.R;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.plugins.ClockPlugin;

import java.util.TimeZone;

/**
 * Plugin for the default clock face used only to provide a preview.
 */
public class LoadingClockController implements ClockPlugin {

    /**
     * Resources used to get title and thumbnail.
     */
    private final Resources mResources;

    /**
     * LayoutInflater used to inflate custom clock views.
     */
    private final LayoutInflater mLayoutInflater;

    /**
     * Renders preview from clock view.
     */
    private final ViewPreviewer mRenderer = new ViewPreviewer();

    /**
     * Root view of clock.
     */
    private ClockLayout mView;

    /**
     * Text clock in preview view hierarchy.
     */
    private TextClock mClock;

    /**
     * Swing animation
     */
     private LottieAnimationView mLogo;

    private Context mContext;

    /**
     * Helper to extract colors from wallpaper palette for clock face.
     */
    private final ClockPalette mPalette = new ClockPalette();

    /**
     * Create a DefaultClockController instance.
     *
     * @param res Resources contains title and thumbnail.
     * @param inflater Inflater used to inflate custom clock views.
     * @param colorExtractor Extracts accent color from wallpaper.
     */
    public LoadingClockController(Resources res, LayoutInflater inflater,
            SysuiColorExtractor colorExtractor, Context context) {
        mResources = res;
        mLayoutInflater = inflater;
        mContext = context;
    }

    private void createViews() {
        mView = (ClockLayout) mLayoutInflater
                .inflate(R.layout.loading_animation_clock, null);
        mClock = (TextClock) mView.findViewById(R.id.clock);
        mLogo = (LottieAnimationView) mView.findViewById(R.id.logo);
        setClockColors();
    }


    private void setClockColors() {
        int mAccentColor = mContext.getResources().getColor(R.color.lockscreen_clock_accent_color);
        int mWhiteColor = mContext.getResources().getColor(R.color.lockscreen_clock_white_color);

        if(Utils.useLockscreenClockMinuteAccentColor(mContext) && Utils.useLockscreenClockHourAccentColor(mContext)) {
             mClock.setFormat12Hour(Html.fromHtml("<font color=" + mAccentColor + ">hh</font>:<font color=" + mAccentColor + ">mm</font>"));
             mClock.setFormat24Hour(Html.fromHtml("<font color=" + mAccentColor + ">kk</font>:<font color=" + mAccentColor + ">mm</font>"));
        } else if(Utils.useLockscreenClockHourAccentColor(mContext)) {
             mClock.setFormat12Hour(Html.fromHtml("<font color=" + mAccentColor + ">hh</font>:<font color=" + mWhiteColor + ">mm</font>"));
             mClock.setFormat24Hour(Html.fromHtml("<font color=" + mAccentColor + ">kk</font>:<font color=" + mWhiteColor + ">mm</font>"));
        } else if(Utils.useLockscreenClockMinuteAccentColor(mContext)) {
             mClock.setFormat12Hour(Html.fromHtml("<font color=" + mWhiteColor + ">hh</font>:<font color=" + mAccentColor + ">mm</font>"));
             mClock.setFormat24Hour(Html.fromHtml("<font color=" + mWhiteColor + ">kk</font>:<font color=" + mAccentColor + ">mm</font>"));
        } else {
            mClock.setTextColor(mWhiteColor);
        }
    }

    @Override
    public void onDestroyView() {
        mView = null;
        mClock = null;
    }

    @Override
    public String getName() {
        return "swing";
    }

    @Override
    public String getTitle() {
        return mResources.getString(R.string.clock_title_loading);
    }

    @Override
    public int getPreferredY(int totalHeight) {
        return totalHeight / 4;
    }

    @Override
    public Bitmap getThumbnail() {
        return BitmapFactory.decodeResource(mResources, R.drawable.default_thumbnail);
    }

    @Override
    public Bitmap getPreview(int width, int height) {

        View previewView = getView();
        TextClock clock = previewView.findViewById(R.id.clock);
        clock.setFormat12Hour("hh:mm");
        clock.setFormat24Hour("kk:mm");
        onTimeTick();
        return mRenderer.createPreview(previewView, width, height);
    }

    @Override
    public View getView() {
        if (mView == null) {
            createViews();
        }
        return mView;
    }

    @Override
    public View getBigClockView() {
        return null;
    }

    @Override
    public void setStyle(Style style) {}

    @Override
    public void setTextColor(int color) {
        setClockColors();
    }

    @Override
    public void setColorPalette(boolean supportsDarkText, int[] colorPalette) {
        mPalette.setColorPalette(supportsDarkText, colorPalette);
    }

    @Override
    public void onTimeTick() {
        mView.onTimeChanged();
        mClock.refresh();
    }

    @Override
    public void setDarkAmount(float darkAmount) {
        mPalette.setDarkAmount(darkAmount);
        mView.setDarkAmount(darkAmount);
    }

    @Override
    public void onTimeZoneChanged(TimeZone timeZone) {}

    @Override
    public boolean shouldShowStatusArea() {
        return true;
    }
}
