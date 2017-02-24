package com.gionee.multipletest.testDownload;

/**
 * Created by xionghg on 17-2-16.
 */

public interface DownloadListener {
    void onProgress(int progress);

    void onSuccess();

    void onFailed();

    void onPaused();

    void onCanceled();
}
