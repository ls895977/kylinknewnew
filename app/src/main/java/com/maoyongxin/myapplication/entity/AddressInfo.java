package com.maoyongxin.myapplication.entity;

import java.util.List;

/**
 * Created by maoyongxin on 2018/1/10.
 */

public class AddressInfo {

    private List<AddressList> addressList;

    public List<AddressList> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<AddressList> addressList) {
        this.addressList = addressList;
    }

    public static class AddressList {
        /**
         * addressId : 110000
         * addressName : 北京市
         * parentId : 0
         */

        private int addressId;
        private String addressName;
        private int parentId;

        public int getAddressId() {
            return addressId;
        }

        public void setAddressId(int addressId) {
            this.addressId = addressId;
        }

        public String getAddressName() {
            return addressName;
        }

        public void setAddressName(String addressName) {
            this.addressName = addressName;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }
    }
}
