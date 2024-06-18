package com.google.android.exoplayer2.util;

import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSession;
import com.google.android.exoplayer2.drm.DrmSessionEventListener;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.OfflineLicenseHelper;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.common.primitives.Ints;
import com.sigma.packer.SigmaMediaDrm;

public class IUtils {
    public static boolean SIGMA_DRM = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Nullable
    public byte[] downloadKeyOffsetId(
            Format format,
            MediaItem.DrmConfiguration drmConfiguration,
            HttpDataSource.Factory httpDataSourceFactory
    ) {
        OfflineLicenseHelper offlineLicenseHelper = new OfflineLicenseHelper(
                new DefaultDrmSessionManager.Builder()
                        .setUuidAndExoMediaDrmProvider(Assertions.checkNotNull(Util.getDrmUuid("widevine")),
                                SigmaMediaDrm.DEFAULT_PROVIDER)
                        .setKeyRequestParameters(drmConfiguration.licenseRequestHeaders)
                        .setMultiSession(drmConfiguration.multiSession)
                        .setPlayClearSamplesWithoutKeys(drmConfiguration.playClearContentWithoutKey)
                        .setUseDrmSessionsForClearContent(Ints.toArray(drmConfiguration.sessionForClearTypes))
                        .build(
                                new HttpMediaDrmCallback(
                                        drmConfiguration.licenseUri.toString(),
                                        drmConfiguration.forceDefaultLicenseUri,
                                        httpDataSourceFactory)),
                new DrmSessionEventListener.EventDispatcher());
        try {
            return offlineLicenseHelper.downloadLicense(format);
        } catch (DrmSession.DrmSessionException e) {
            e.printStackTrace();
        } finally {
            offlineLicenseHelper.release();
        }
        return null;
    }
}
