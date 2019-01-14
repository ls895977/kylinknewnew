package com.maoyongxin.myapplication.ui.editapp.findfragment.zhihu.bean;

import java.util.List;

public class ZhiHuGridBean {

    /**
     * code : 200
     * info : [{"id":"1","name":"G","create_time":"2018-12-06 11:43:32","update_time":"2018-12-17 16:35:19","type":"1","sequence":"0","record_state":"1"},{"id":"2","name":"A","create_time":"2018-12-17 15:47:05","update_time":"2018-12-17 15:47:05","type":"1","sequence":"0","record_state":"1"},{"id":"3","name":"B","create_time":"2018-12-17 15:47:12","update_time":"2018-12-17 15:47:12","type":"1","sequence":"0","record_state":"1"},{"id":"4","name":"C","create_time":"2018-12-17 15:47:17","update_time":"2018-12-17 15:47:17","type":"1","sequence":"0","record_state":"1"},{"id":"5","name":"D","create_time":"2018-12-17 15:47:22","update_time":"2018-12-17 15:47:22","type":"1","sequence":"0","record_state":"1"},{"id":"6","name":"E","create_time":"2018-12-17 15:47:28","update_time":"2018-12-17 15:47:28","type":"1","sequence":"0","record_state":"1"},{"id":"7","name":"F","create_time":"2018-12-17 15:47:33","update_time":"2018-12-17 15:47:33","type":"1","sequence":"0","record_state":"1"}]
     */

    private int code;
    private List<InfoBean> info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * id : 1
         * name : G
         * create_time : 2018-12-06 11:43:32
         * update_time : 2018-12-17 16:35:19
         * type : 1
         * sequence : 0
         * record_state : 1
         */
        private boolean isof;
        public boolean isIsof() {
            return isof;
        }

        public void setIsof(boolean isof) {
            this.isof = isof;
        }

        private String id;
        private String name;
        private String create_time;
        private String update_time;
        private String type;
        private String sequence;
        private String record_state;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSequence() {
            return sequence;
        }

        public void setSequence(String sequence) {
            this.sequence = sequence;
        }

        public String getRecord_state() {
            return record_state;
        }

        public void setRecord_state(String record_state) {
            this.record_state = record_state;
        }
    }
}
