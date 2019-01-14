package com.maoyongxin.myapplication.ui.news.activity;

import java.util.List;

/**
 * Created by Administrator on 2018/5/25 0025.
 */

public class plbean {


    /**
     * code : 200
     * msg : 获取成功
     * obj : {"newsCommentList":[{"id":2,"content":"test1","createTime":1527151772000,"uid":1,"newsId":1},{"id":3,"content":"test22221","createTime":1527151785000,"uid":1,"newsId":1},{"id":4,"content":"test,gg","createTime":1527238008000,"uid":1,"newsId":1},{"id":5,"content":"test,ff","createTime":1527238470000,"uid":1,"newsId":1}]}
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
        private List<NewsCommentListBean> newsCommentList;

        public List<NewsCommentListBean> getNewsCommentList() {
            return newsCommentList;
        }

        public void setNewsCommentList(List<NewsCommentListBean> newsCommentList) {
            this.newsCommentList = newsCommentList;
        }

        public static class NewsCommentListBean {
            /**
             * id : 2
             * content : test1
             * createTime : 1527151772000
             * uid : 1
             * newsId : 1
             */

            private int id;
            private String content;
            private long createTime;
            private int uid;
            private int newsId;
            private String headimg;


            public String getHeadimg() {
                return headimg;
            }

            public void setHeadimg(String headimg) {
                this.headimg = headimg;
            }


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public int getNewsId() {
                return newsId;
            }

            public void setNewsId(int newsId) {
                this.newsId = newsId;
            }
        }
    }
}
