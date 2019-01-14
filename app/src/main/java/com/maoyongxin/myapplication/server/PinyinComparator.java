package com.maoyongxin.myapplication.server;


import com.maoyongxin.myapplication.entity.FriendsInfo;

import java.util.Comparator;



/**
 *
 * @author
 *
 */
public class PinyinComparator implements Comparator<FriendsInfo.FriendList> {


    public int compare(FriendsInfo.FriendList o1,FriendsInfo.FriendList o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }
}
