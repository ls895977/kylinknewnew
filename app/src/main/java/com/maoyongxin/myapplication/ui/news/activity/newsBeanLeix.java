package com.maoyongxin.myapplication.ui.news.activity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

public class newsBeanLeix {


    /**
     * code : 200
     * msg : 获取成功
     * obj : {"newsLableList":[{"lableId":2,"lableName":"最新","sequence":89,"recordState":true,"createTime":1527066376000,"updateTime":null},{"lableId":3,"lableName":"科技","sequence":0,"recordState":true,"createTime":1527066380000,"updateTime":null},{"lableId":4,"lableName":"时尚","sequence":26,"recordState":true,"createTime":1527066382000,"updateTime":null},{"lableId":5,"lableName":"潮流","sequence":0,"recordState":true,"createTime":1527066385000,"updateTime":null}]}
     */

    private int code;
    private String msg;
    private ObjBean obj;

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

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        private List<NewsLableListBean> newsLableList;

        public List<NewsLableListBean> getNewsLableList() {
            return newsLableList;
        }

        public void setNewsLableList(List<NewsLableListBean> newsLableList) {
            this.newsLableList = newsLableList;
        }

        public static class NewsLableListBean {
            /**
             * lableId : 2
             * lableName : 最新
             * sequence : 89
             * recordState : true
             * createTime : 1527066376000
             * updateTime : null
             */

            private int lableId;
            private String lableName;
            private int sequence;
            private boolean recordState;
            private long createTime;
            private Object updateTime;

            public int getLableId() {
                return lableId;
            }

            public void setLableId(int lableId) {
                this.lableId = lableId;
            }

            public String getLableName() {
                return lableName;
            }

            public void setLableName(String lableName) {
                this.lableName = lableName;
            }

            public int getSequence() {
                return sequence;
            }

            public void setSequence(int sequence) {
                this.sequence = sequence;
            }

            public boolean isRecordState() {
                return recordState;
            }

            public void setRecordState(boolean recordState) {
                this.recordState = recordState;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public Object getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(Object updateTime) {
                this.updateTime = updateTime;
            }
        }
    }
}
