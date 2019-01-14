package com.maoyongxin.myapplication.ui.news.activity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/25 0025.
 */

public class fragment6bean {


    /**
     * code : 200
     * msg : 获取成功
     * obj : {"newsList":[{"id":12,"uid":1,"title":"111111","imgCount":0,"abstractc":null,"source":"1111111111","newsType":1,"newsLable":null,"newsClassify":null,"readNum":0,"createTime":1527152975000,"updateTime":null,"status":null,"recordState":null,"img":null,"content":"11111111111"}]}
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
        private List<NewsListBean> newsList;

        public List<NewsListBean> getNewsList() {
            return newsList;
        }

        public void setNewsList(List<NewsListBean> newsList) {
            this.newsList = newsList;
        }

        public static class NewsListBean {
            /**
             * id : 12
             * uid : 1
             * title : 111111
             * imgCount : 0
             * abstractc : null
             * source : 1111111111
             * newsType : 1
             * newsLable : null
             * newsClassify : null
             * readNum : 0
             * createTime : 1527152975000
             * updateTime : null
             * status : null
             * recordState : null
             * img : null
             * content : 11111111111
             */

            private int id;
            private int uid;
            private String title;
            private int imgCount;
            private Object abstractc;
            private String source;
            private int newsType;
            private Object newsLable;
            private Object newsClassify;
            private int readNum;
            private long createTime;
            private Object updateTime;
            private Object status;
            private Object recordState;
            private Object img;
            private String content;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getImgCount() {
                return imgCount;
            }

            public void setImgCount(int imgCount) {
                this.imgCount = imgCount;
            }

            public Object getAbstractc() {
                return abstractc;
            }

            public void setAbstractc(Object abstractc) {
                this.abstractc = abstractc;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public int getNewsType() {
                return newsType;
            }

            public void setNewsType(int newsType) {
                this.newsType = newsType;
            }

            public Object getNewsLable() {
                return newsLable;
            }

            public void setNewsLable(Object newsLable) {
                this.newsLable = newsLable;
            }

            public Object getNewsClassify() {
                return newsClassify;
            }

            public void setNewsClassify(Object newsClassify) {
                this.newsClassify = newsClassify;
            }

            public int getReadNum() {
                return readNum;
            }

            public void setReadNum(int readNum) {
                this.readNum = readNum;
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

            public Object getStatus() {
                return status;
            }

            public void setStatus(Object status) {
                this.status = status;
            }

            public Object getRecordState() {
                return recordState;
            }

            public void setRecordState(Object recordState) {
                this.recordState = recordState;
            }

            public Object getImg() {
                return img;
            }

            public void setImg(Object img) {
                this.img = img;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }
}
