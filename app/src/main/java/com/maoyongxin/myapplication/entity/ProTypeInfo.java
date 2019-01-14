package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2017/12/14.
 */

public class ProTypeInfo {

    private List<ThingTypeList> thingTypeList;

    public List<ThingTypeList> getThingTypeList() {
        return thingTypeList;
    }

    public void setThingTypeList(List<ThingTypeList> thingTypeList) {
        this.thingTypeList = thingTypeList;
    }

    public static class ThingTypeList {
        /**
         * typeId : 1
         * typeName : 家具
         * typeLevel : 1
         */

        private int typeId;
        private String typeName;
        private int typeLevel;

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public int getTypeLevel() {
            return typeLevel;
        }

        public void setTypeLevel(int typeLevel) {
            this.typeLevel = typeLevel;
        }
    }
}
