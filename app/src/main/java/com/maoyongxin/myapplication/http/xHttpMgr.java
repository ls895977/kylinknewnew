package com.maoyongxin.myapplication.http;

import android.content.Context;
import android.util.Log;

import com.jky.baselibrary.BuildConfig;
import com.jky.baselibrary.http.BaseHttpMgr;
import com.jky.baselibrary.http.Request;
import com.jky.baselibrary.http.callback.HttpCommonCallback;
import com.jky.baselibrary.http.response.StringResponse;
import com.jky.baselibrary.util.common.Logger;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.app.RequestInterceptListener;
import org.xutils.http.request.UriRequest;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import static org.xutils.x.http;

/**
 * 基于xUtils的Http管理器
 * Created by dingke on 2017/8/3.
 */

public class xHttpMgr extends BaseHttpMgr {
    private static final String TAG = xHttpMgr.class.getSimpleName();

    private static xHttpMgr sXHttpMgr;

    /**
     * Https 证书验证对象
     */
    private static SSLContext s_sSLContext = null;


    static {
        sXHttpMgr = new xHttpMgr();
    }

    private xHttpMgr() {
    }

    public static xHttpMgr getMgr() {
        return sXHttpMgr;
    }

    @Override
    public void stringRequest(final Context context, final Request request, final HttpCommonCallback callback) {
        super.stringRequest(context, request, callback);
        RequestParams rp = new RequestParams(request.getUrl());

        if (request.isFormData()) {
            rp.setMultipart(true);
        }
        // 设置不重试
        rp.setMaxRetryCount(request.getRetryCount());
        rp.setConnectTimeout(request.getTimeoutMillis());
        rp.setReadTimeout(request.getTimeoutMillis());
         /* 判断https证书是否成功验证 */
//        if (context != null) {
//            SSLContext sslContext = getSSLContext(context);
//            if (null == sslContext) {
//                if (BuildConfig.DEBUG)
//                    Log.d("HttpUtil", "Error:Can't Get SSLContext!");
//            }
//            //绑定SSL证书
////            rp.setSslSocketFactory(sslContext.getSocketFactory());
//        }

        for (String key : request.getHeader().keySet()) {
            String value = request.getHeader().get(key);
            rp.addHeader(key, value);
        }

        xCommonCallback<String> commonCallback = new xCommonCallback<String>() {

            private StringResponse mResponse = new StringResponse();

            @Override
            public void beforeRequest(UriRequest request) throws Throwable {
                Logger.i(TAG, "beforeRequest");
            }

            @Override
            public void afterRequest(UriRequest request) throws Throwable {
                Logger.i(TAG, "afterRequest");
                mResponse.setCode(request.getResponseCode());
                mResponse.setMessage(request.getResponseMessage());
                mResponse.setHeaders(request.getResponseHeaders());
            }

            @Override
            public void onSuccess(String result) {
                if (!isCancelled(request)) {
                    mResponse.setString(result);
                    Logger.i(TAG, mResponse.toString());
                    callback.onSuccess(mResponse);
                    deleteRequest(request);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                if (!isCancelled(request)) {
                    Logger.w(TAG, ex.toString());
                    callback.onFailure(ex);
                    deleteRequest(request);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                if (!isCancelled(request)) {
                    Logger.w(TAG, cex.toString());
                    callback.onCancelled(cex);
                    deleteRequest(request);
                }
            }

            @Override
            public void onFinished() {
                if (!isCancelled(request)) {
                    Logger.w(TAG, "onFinished");
                    callback.onFinished();
                    deleteRequest(request);
                }
            }
        };

        switch (request.getMethod()) {
            case BaseHttpMgr.GET: {
                Logger.i(TAG, BaseHttpMgr.GET);
                Logger.i(TAG, rp.getUri());
                http().get(rp, commonCallback);
                break;
            }
            case BaseHttpMgr.POST: {
                Logger.i(TAG, BaseHttpMgr.POST);
                Logger.i(TAG, rp.getUri());
                for (String key : request.getBody().keySet()) {
                    Object value = request.getBody().get(key);
                    if (value instanceof String)
                        rp.addBodyParameter(key, (String) value);
                    else if (value instanceof File)
                        rp.addBodyParameter(key, (File) value);
                }
                x.http().post(rp, commonCallback);
                break;
            }
        }
    }

    /**
     * 获取Https的证书
     *
     * @param context Activity（fragment）的上下文
     * @return SSL的上下文对象
     */
    private static SSLContext getSSLContext(Context context) {
        if (null != s_sSLContext) {
            return s_sSLContext;
        }

        //以下代码来自百度 参见http://www.tuicool.com/articles/vmUZf2
        CertificateFactory certificateFactory = null;

        InputStream inputStream = null;
        KeyStore keystore = null;
        String tmfAlgorithm = null;
        TrustManagerFactory trustManagerFactory = null;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");

            inputStream = context.getAssets().open("tomcat.crt");//这里导入SSL证书文件
//            inputStream = context.getAssets().open("51p2b_server_bs.pem");//这里导入SSL证书文件

            Certificate ca = certificateFactory.generateCertificate(inputStream);

            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null);
            keystore.setCertificateEntry("ca", ca);

            tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
            trustManagerFactory.init(keystore);

            // Create an SSLContext that uses our TrustManager
            s_sSLContext = SSLContext.getInstance("TLS");
            s_sSLContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            //信任所有证书 （官方不推荐使用）
//            s_sSLContext.init(null, new TrustManager[]{new X509TrustManager() {
//
//                @Override
//                public X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//
//                @Override
//                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
//                        throws CertificateException {
//
//                }
//
//                @Override
//                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
//                        throws CertificateException {
//
//                }
//            }}, new SecureRandom());
            return s_sSLContext;
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface xCommonCallback<ResultType> extends Callback.CommonCallback<ResultType>, RequestInterceptListener {
    }
}
