package com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean;

import java.util.List;

public class ZhiHuBean {

    /**
     * code : 200
     * info : {"total":1,"per_page":15,"current_page":1,"last_page":1,"data":[{"id":"1","uid":"10073","community_id":"1","img":"","resource_type":"1","resource_name":"成都星空三维科技有限公司","advantage":"我帅我骄傲！","intro":"不服来战！","area_code":"500010","area_type":"1","create_time":"2018-12-06 15:03:13","update_time":"2018-12-06 16:37:53"}]}
     */

    private int code;
    private InfoBean info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * total : 1
         * per_page : 15
         * current_page : 1
         * last_page : 1
         * data : [{"id":"1","uid":"10073","community_id":"1","img":"","resource_type":"1","resource_name":"成都星空三维科技有限公司","advantage":"我帅我骄傲！","intro":"不服来战！","area_code":"500010","area_type":"1","create_time":"2018-12-06 15:03:13","update_time":"2018-12-06 16:37:53"}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
        private List<DataBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 1
             * uid : 10073
             * community_id : 1
             * img :
             * resource_type : 1
             * resource_name : 成都星空三维科技有限公司
             * advantage : 我帅我骄傲！
             * intro : 不服来战！
             * area_code : 500010
             * area_type : 1
             * create_time : 2018-12-06 15:03:13
             * update_time : 2018-12-06 16:37:53
             */

            private String id;
            private String uid;
            private String community_id;
            private String img;
            private String resource_type;
            private String resource_name;
            private String advantage;
            private String intro;
            private String area_code;
            private String area_type;
            private String create_time;
            private String update_time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUid() {
                return uid;
            }

            public void setUid(String uid) {
                this.uid = uid;
            }

            public String getCommunity_id() {
                return community_id;
            }

            public void setCommunity_id(String community_id) {
                this.community_id = community_id;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public String getResource_type() {
                return resource_type;
            }

            public void setResource_type(String resource_type) {
                this.resource_type = resource_type;
            }

            public String getResource_name() {
                return resource_name;
            }

            public void setResource_name(String resource_name) {
                this.resource_name = resource_name;
            }

            public String getAdvantage() {
                return advantage;
            }

            public void setAdvantage(String advantage) {
                this.advantage = advantage;
            }

            public String getIntro() {
                return intro;
            }

            public void setIntro(String intro) {
                this.intro = intro;
            }

            public String getArea_code() {
                return area_code;
            }

            public void setArea_code(String area_code) {
                this.area_code = area_code;
            }

            public String getArea_type() {
                return area_type;
            }

            public void setArea_type(String area_type) {
                this.area_type = area_type;
            }

            public String getCreate_time() {
                return create_time;
            }

            public void setCreate_time(String create_time) {
                this.create_time = create_time;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }
        }
    }
}
