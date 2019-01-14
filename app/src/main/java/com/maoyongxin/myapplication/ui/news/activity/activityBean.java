package com.maoyongxin.myapplication.ui.news.activity;

/**
 * Created by Administrator on 2018/5/25 0025.
 */

public class activityBean {


    /**
     * code : 200
     * msg : 获取成功
     * obj : {"newsDetail":{"id":16,"uid":1,"title":"最新新闻测试数据","imgCount":0,"abstractc":"最新新闻","source":"凤凰网","newsType":2,"newsLable":{"lableId":2,"lableName":"最新","sequence":1,"recordState":true,"createTime":1527066376000,"updateTime":null},"newsClassify":{"newsClassifyId":3,"newsClassifyName":"潮流","image":"uploads/photos/st_news_photos/20180329/a4be0776531bc208c51f642343190c03.jpg","sequence":0,"recordState":true,"createTime":1527066562000,"updateTime":null},"readNum":0,"createTime":1527217048000,"updateTime":null,"status":null,"recordState":null,"img":null,"content":"测试数据，仅供参考。"}}
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
        /**
         * newsDetail : {"id":16,"uid":1,"title":"最新新闻测试数据","imgCount":0,"abstractc":"最新新闻","source":"凤凰网","newsType":2,"newsLable":{"lableId":2,"lableName":"最新","sequence":1,"recordState":true,"createTime":1527066376000,"updateTime":null},"newsClassify":{"newsClassifyId":3,"newsClassifyName":"潮流","image":"uploads/photos/st_news_photos/20180329/a4be0776531bc208c51f642343190c03.jpg","sequence":0,"recordState":true,"createTime":1527066562000,"updateTime":null},"readNum":0,"createTime":1527217048000,"updateTime":null,"status":null,"recordState":null,"img":null,"content":"测试数据，仅供参考。"}
         */

        private NewsDetailBean newsDetail;

        public NewsDetailBean getNewsDetail() {
            return newsDetail;
        }

        public void setNewsDetail(NewsDetailBean newsDetail) {
            this.newsDetail = newsDetail;
        }

        public static class NewsDetailBean {
            /**
             * id : 16
             * uid : 1
             * title : 最新新闻测试数据
             * imgCount : 0
             * abstractc : 最新新闻
             * source : 凤凰网
             * newsType : 2
             * newsLable : {"lableId":2,"lableName":"最新","sequence":1,"recordState":true,"createTime":1527066376000,"updateTime":null}
             * newsClassify : {"newsClassifyId":3,"newsClassifyName":"潮流","image":"uploads/photos/st_news_photos/20180329/a4be0776531bc208c51f642343190c03.jpg","sequence":0,"recordState":true,"createTime":1527066562000,"updateTime":null}
             * readNum : 0
             * createTime : 1527217048000
             * updateTime : null
             * status : null
             * recordState : null
             * img : null
             * content : 测试数据，仅供参考。
             */

            private int id;
            private int uid;
            private String title;
            private int imgCount;
            private String abstractc;
            private String source;
            private int newsType;
            private NewsLableBean newsLable;
            private NewsClassifyBean newsClassify;
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

            public String getAbstractc() {
                return abstractc;
            }

            public void setAbstractc(String abstractc) {
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

            public NewsLableBean getNewsLable() {
                return newsLable;
            }

            public void setNewsLable(NewsLableBean newsLable) {
                this.newsLable = newsLable;
            }

            public NewsClassifyBean getNewsClassify() {
                return newsClassify;
            }

            public void setNewsClassify(NewsClassifyBean newsClassify) {
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

            public static class NewsLableBean {
                /**
                 * lableId : 2
                 * lableName : 最新
                 * sequence : 1
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

            public static class NewsClassifyBean {
                /**
                 * newsClassifyId : 3
                 * newsClassifyName : 潮流
                 * image : uploads/photos/st_news_photos/20180329/a4be0776531bc208c51f642343190c03.jpg
                 * sequence : 0
                 * recordState : true
                 * createTime : 1527066562000
                 * updateTime : null
                 */

                private int newsClassifyId;
                private String newsClassifyName;
                private String image;
                private int sequence;
                private boolean recordState;
                private long createTime;
                private Object updateTime;

                public int getNewsClassifyId() {
                    return newsClassifyId;
                }

                public void setNewsClassifyId(int newsClassifyId) {
                    this.newsClassifyId = newsClassifyId;
                }

                public String getNewsClassifyName() {
                    return newsClassifyName;
                }

                public void setNewsClassifyName(String newsClassifyName) {
                    this.newsClassifyName = newsClassifyName;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
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
}
