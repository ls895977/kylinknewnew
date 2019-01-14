package com.jky.baselibrary.http;


import java.util.HashMap;
import java.util.Map;

/**
 * 通用网络请求类
 */
public class Request {

    private boolean mFormData;
    private String mMethod;
    private String mUrl;
    private Map<String, String> mHeader;
    private Map<String, Object> mBody;
    private Object mTag;
    private int mRetryCount;
    private int mTimeoutMillis;

    private Request() {
    }

    public boolean isFormData() {
        return mFormData;
    }

    public String getMethod() {
        return mMethod;
    }

    public String getUrl() {
        return mUrl;
    }

    public Map<String, String> getHeader() {
        return mHeader;
    }

    public Map<String, Object> getBody() {
        return mBody;
    }

    public Object getTag() {
        return mTag;
    }

    public int getRetryCount() {
        return mRetryCount;
    }

    public int getTimeoutMillis() {
        return mTimeoutMillis;
    }

    public static class Builder {
        private boolean formData;
        private String method = BaseHttpMgr.GET;
        private String url;
        private Map<String, String> header = new HashMap<>();
        private Map<String, Object> body = new HashMap<>();
        private Object tag;
        private int retryCount = 0;// 默认请求失败不重试
        private int timeoutMillis = 15 * 1000;//默认请求超时15秒

        public Builder formData(boolean formData) {
            this.formData = formData;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder addHeader(String key, String value) {
            this.header.put(key, value);
            return this;
        }

        public Builder header(Map<String, String> header) {
            if (header != null)
                this.header = header;
            return this;
        }

        public Builder addBody(String key, Object value) {
            this.body.put(key, value);
            return this;
        }

        public Builder body(Map<String, Object> body) {
            if (body != null)
                this.body = body;
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }

        public Builder retryCount(int retryCount) {
            this.retryCount = retryCount;
            return this;
        }

        public Builder timeoutMillis(int timeoutMillis) {
            this.timeoutMillis = timeoutMillis;
            return this;
        }

        public Request build() {
            Request req = new Request();
            req.mFormData = formData;
            req.mMethod = method;
            req.mUrl = url;
            req.mHeader = header;
            req.mBody = body;
            req.mTag = tag;
            req.mRetryCount = retryCount;
            req.mTimeoutMillis = timeoutMillis;
            return req;
        }
    }
}
