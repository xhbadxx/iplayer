package com.google.android.exoplayer2.felix;

import androidx.annotation.Nullable;

public interface IDrmCallback {
    void onKeyLoaded(@Nullable byte[] offlineLicenseKeySetId);
}
