package com.awaken.android.systemui.dagger;

import com.android.systemui.dagger.DefaultComponentBinder;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.SystemUIBinder;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.SystemUIModule;

import com.awaken.android.systemui.keyguard.KeyguardSliceProviderAwaken;
import com.awaken.android.systemui.smartspace.KeyguardSmartspaceController;

import dagger.Subcomponent;

@SysUISingleton
@Subcomponent(modules = {
        DefaultComponentBinder.class,
        DependencyProvider.class,
        SystemUIBinder.class,
        SystemUIModule.class,
        SystemUIAwakenModule.class})
public interface SysUIComponentAwaken extends SysUIComponent {
    @SysUISingleton
    @Subcomponent.Builder
    interface Builder extends SysUIComponent.Builder {
        SysUIComponentAwaken build();
    }

    /**
     * Member injection into the supplied argument.
     */
    void inject(KeyguardSliceProviderAwaken keyguardSliceProviderAwaken);

    @SysUISingleton
    KeyguardSmartspaceController createKeyguardSmartspaceController();
}
