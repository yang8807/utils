package com.magnify.yutils;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.magnify.yutils.app.NonProguard;


/**
 * Webview��ȡ�ļ�������
 *
 * @author ����
 */
public class GetFileWebChromeClient extends WebChromeClient implements NonProguard {

	private OnGetFileListener mListener;

	public GetFileWebChromeClient(OnGetFileListener listener) {
		if (listener == null)
			throw new IllegalArgumentException();
		mListener = listener;
	}

	// Android < 3.0 �����������
	public void openFileChooser(ValueCallback<Uri> uploadMsg) {
		mListener.onGetFile(uploadMsg);
	}

	// 3.0 + �����������
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
		mListener.onGetFile(uploadMsg);
	}

	// Android > 4.1.1 �����������
	public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
		mListener.onGetFile(uploadMsg);
	}

	public interface OnGetFileListener {

		public void onGetFile(ValueCallback<Uri> callback);
	}

}
