package com.awaken.android.systemui;

import android.content.Context;

import com.awaken.android.systemui.dagger.DaggerGlobalRootComponentAwaken;
import com.awaken.android.systemui.dagger.GlobalRootComponentAwaken;

import com.android.systemui.SystemUIFactory;
import com.android.systemui.dagger.GlobalRootComponent;

public class SystemUIAwakenFactory extends SystemUIFactory {
    @Override
    protected GlobalRootComponent buildGlobalRootComponent(Context context) {
        return DaggerGlobalRootComponentAwaken.builder()
                .context(context)
                .build();
    }
}
