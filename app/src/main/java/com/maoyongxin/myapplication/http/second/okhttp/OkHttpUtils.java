package com.maoyongxin.myapplication.http.second.okhttp;

import com.maoyongxin.myapplication.http.second.okhttp.helper.ProgressHelper;
import com.maoyongxin.myapplication.http.second.okhttp.listener.impl.UIProgressListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by admin on 2017/2/17.
 */

public class OkHttpUtils {
    private static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 官方建议尽量减少OkHttpClient实例创建
     *
     * @param okHttpClient
     */
    public OkHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient.Builder()
                    //设置超时，不设置可能会报异常
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .build();
        } else {
            mOkHttpClient = okHttpClient;
        }

    }

    public static OkHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    /**
     * get请求
     *
     * @param url
     * @param onOkHttpCallBackListener
     */
    public void doGet(String url, final OnOkHttpCallBackListener onOkHttpCallBackListener) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onSuccess(response.body().string());
                }
            }
        });
    }


    /**
     * post提交json
     *
     * @param url
     * @param json
     * @param onOkHttpCallBackListener
     */
    public void doPost(String url, String json, final OnOkHttpCallBackListener onOkHttpCallBackListener) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onSuccess(response.body().string());
                }
            }
        });
    }

    /**
     * post请求 提交键值对
     *
     * @param url
     * @param params
     * @param onOkHttpCallBackListener
     */
    public void doPost(String url, Map<String, String> params, final OnOkHttpCallBackListener onOkHttpCallBackListener) {
        //构造上传请求，类似web表单
        FormBody.Builder builder = new FormBody.Builder();
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onSuccess(response.body().string());
                }
            }

        });
    }

    /**
     * 带进度文件上传
     *
     * @param url
     * @param params
     * @param fileParams
     * @param uiProgressRequestListener
     * @param onOkHttpCallBackListener
     */
    public void uploadFile(String url, Map<String, String> params, Map<String, File> fileParams, UIProgressListener uiProgressRequestListener, final OnOkHttpCallBackListener onOkHttpCallBackListener) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //设置超时，不设置可能会报异常
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        //构造上传请求，类似web表单
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        ;

        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<String, File> entryFile : fileParams.entrySet()) {

            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), entryFile.getValue());
            builder.addFormDataPart(entryFile.getKey(), entryFile.getValue().getName(), fileBody);
        }

        RequestBody requestBody = builder.build();


        //进行包装，使其支持进度回调
        final Request request = new Request.Builder()
                .addHeader("Accept-Encoding", "identity")
                .url(url)
                .post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener))
                .build();
        //开始请求
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onSuccess(response.body().string());
                }
            }
        });
    }

    /**
     * 带进度文件上传============================批量上传
     *
     * @param url
     * @param params
     * @param fileParams
     * @param uiProgressRequestListener
     * @param onOkHttpCallBackListener
     */
    public void uploadFile(final String url, final Map<String, String> params, final List<File> fileParams, final UIProgressListener uiProgressRequestListener, final OnOkHttpCallBackListener onOkHttpCallBackListener) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //设置超时，不设置可能会报异常
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
                .build();
        //构造上传请求，类似web表单
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        ;
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        for (File file : fileParams) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            builder.addFormDataPart("file", file.getName(), fileBody);
        }

        RequestBody requestBody = builder.build();


        //进行包装，使其支持进度回调
        final Request request = new Request.Builder()
                .addHeader("Accept-Encoding", "identity")
                .url(url)
                .post(ProgressHelper.addProgressRequestListener(requestBody, uiProgressRequestListener))
                .build();
        //开始请求
        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onSuccess(response.body().string());
                }
            }

        });

    }

    /**
     * 带进度下载文件
     *
     * @param url
     * @param uiProgressListener 进度处理
     */
    public void downloadFile(String url, final String filePath, final String fileName, UIProgressListener uiProgressListener, final OnOkHttpCallBackListener onOkHttpCallBackListener) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //设置超时，不设置可能会报异常
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        //构造请求
        Request request = new Request.Builder()
                .url(url)
                .build();

        //包装Response使其支持进度回调
        ProgressHelper.addProgressResponseListener(okHttpClient, uiProgressListener).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                try {
                    is = response.body().byteStream();
                    File file = new File(filePath, fileName);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    if (onOkHttpCallBackListener != null) {
                        onOkHttpCallBackListener.onSuccess("下载成功");
                    }
                } catch (Exception e) {
                    if (onOkHttpCallBackListener != null) {
                        onOkHttpCallBackListener.onFail();
                    }
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }

            }
        });
    }

    /**
     * 获取身份证图片文字  针对阿里云服务所做
     *
     * @param url
     * @param appcode
     * @param json
     * @param onOkHttpCallBackListener
     */
    public void doCheckCard(String url, String appcode, String json, final OnOkHttpCallBackListener onOkHttpCallBackListener) {
        //构造上传请求，类似web表单
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //设置超时，不设置可能会报异常
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .addHeader("Authorization", "APPCODE " + appcode)
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .url(url)
                .post(body)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onFail();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (onOkHttpCallBackListener != null) {
                    onOkHttpCallBackListener.onSuccess(response.body().string());
                }
            }
        });
    }

    public interface OnOkHttpCallBackListener {
        void onFail();

        void onSuccess(String response);
    }
}
