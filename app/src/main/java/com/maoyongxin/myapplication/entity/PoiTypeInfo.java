package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/12.
 */

public class PoiTypeInfo {

    private List<PoiList> poiList;

    public List<PoiList> getPoiList() {
        return poiList;
    }

    public void setPoiList(List<PoiList> poiList) {
        this.poiList = poiList;
    }

    public static class PoiList {
        /**
         * poiId : 1
         * poiValue : 企业
         */

        private int poiId;
        private String poiValue;

        public int getPoiId() {
            return poiId;
        }

        public void setPoiId(int poiId) {
            this.poiId = poiId;
        }

        public String getPoiValue() {
            return poiValue;
        }

        public void setPoiValue(String poiValue) {
            this.poiValue = poiValue;
        }
    }
}
