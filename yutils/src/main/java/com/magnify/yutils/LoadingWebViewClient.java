package com.magnify.yutils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Timer;
import java.util.TimerTask;

/**
 * WebView���ض���������
 *
 * @author ����
 */
public class LoadingWebViewClient extends WebViewClient {

	/** ҳ����س�ʱʱ�� */
	public static final int TIMEOUT = 30000;

	protected boolean isError = false;

	protected long lastErrorTime = 0L;

	protected WebView webView;

	protected LoadingListener listener;

	protected String currentUrl;

	protected Timer timer;

	public LoadingWebViewClient(WebView webView, LoadingListener listener) {
		this.webView = webView;
		this.listener = listener;
	}

	@SuppressLint("HandlerLeak")
	protected Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// ��ʱ��,�����ж�ҳ����ؽ���,��ʱ���ҽ���С��100,��ִ�г�ʱ��Ķ���
			if (webView.getProgress() < 100) {
				webView.stopLoading();
				onReceivedError(webView, ERROR_TIMEOUT, "", currentUrl);
			}
		};
	};

	/**
	 * ��������������趨��ʱʱ��
	 *
	 * @return
	 */
	protected int getTimeout() {
		return TIMEOUT;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);

		// ����֮����ܻ�����һ��onPageStarted���ڴ˺���
		if (System.currentTimeMillis() - lastErrorTime < 100) {
			return;
		}

		isError = false;
		currentUrl = url;
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				Message.obtain(handler).sendToTarget();
				cancelTimer();
			}
		}, getTimeout());
		if (listener != null) {
			listener.onPageStarted(view, url);
		}
	}

	@Override
	public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
		super.onReceivedError(view, errorCode, description, failingUrl);
		isError = true;
		lastErrorTime = System.currentTimeMillis();
		cancelTimer();
		if (listener != null) {
			String res = "";
			switch (errorCode) {
				case WebViewClient.ERROR_HOST_LOOKUP:
					res = "����ʧ��";
					break;
				case WebViewClient.ERROR_CONNECT:
					res = "�޷�������������";
					break;
				case WebViewClient.ERROR_TIMEOUT:
					res = "���ӳ�ʱ";
					break;
				case WebViewClient.ERROR_REDIRECT_LOOP:
					res = "�����ض���";
					break;
				case WebViewClient.ERROR_BAD_URL:
					res = "��ַ����";
					break;
				default:
					res = "����ʧ��(" + errorCode + ")";
					break;
			}
			listener.onReceivedError(view, errorCode, description, failingUrl, res + "����������");
		}
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		super.onPageFinished(view, url);
		cancelTimer();
		if (!isError && listener != null) {
			listener.onPageFinished(view, url);
		}
	}

	/**
	 * ���ص�ǰ��ַ
	 *
	 * @return
	 */
	public String getCurrentUrl() {
		return currentUrl;
	}

	/**
	 * ȡ����ʱ��
	 */
	public synchronized void cancelTimer() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
			timer = null;
		}
	}

	/**
	 * ����WebViewClient�����ɷ���
	 */
	public interface LoadingListener {

		public void onPageStarted(WebView view, String url);

		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl, String detailMessage);

		public void onPageFinished(WebView view, String url);
	}

}
