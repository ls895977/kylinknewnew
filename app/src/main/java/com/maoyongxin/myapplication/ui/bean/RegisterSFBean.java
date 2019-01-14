package com.maoyongxin.myapplication.ui.bean;

public class RegisterSFBean {
    /**
     * code : 200
     * msg : 绑定成功！
     * info : {"userId":"10153","password":"JNz12L"}
     */
    private int code;
    private String msg;
    private InfoBean info;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * userId : 10153
         * password : JNz12L
         */

        private String userId;
        private String password;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
