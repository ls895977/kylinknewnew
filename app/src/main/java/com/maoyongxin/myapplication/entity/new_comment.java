package com.maoyongxin.myapplication.entity;

/**
 * Created by yusr on 2018/5/10.
 */

public class new_comment {

    private  String commentId;
    private  String commentUserName;
    private  String commentUserHead;
    private  String conmmentTime;
    private  String commentContent;
    private String commentType;
    private String commentUserId;
    private String communityName;
    private String CommunityId;
    private String group_id;
    private String parentuserId;
    private String is_read;

    public void sethuifuInfo( String commentId,String commentUserName,String commentUserHead,String commentTime,String commentContent,String commentType,String commentUserId,String Communityname,String CommunityId,String group_id,String parentuserId,String is_read)
    {
        this.commentId=commentId;
        this.commentUserName=commentUserName;
        this.commentUserHead=commentUserHead;
        this.conmmentTime=commentTime;
        this.commentContent=commentContent;
        this.commentType=commentType;
        this.communityName=Communityname;
        this.CommunityId=CommunityId;
        this.group_id=group_id;
        this.parentuserId=parentuserId;
        this.is_read=is_read;


    }



   public String getCommentId()
   {
       return  commentId;
   }
   public String getCommentUserName()
    {
        return commentUserName;
    }
    public String getCommentUserHead()
    {
        return  commentUserHead;
    }
    public String getConmmentTime()
    {
        return conmmentTime;
    }
    public String getCommentContent()
    {
        return commentContent;
    }
    public String getCommentType(){return  commentType;}
    public String getCommentUserId(){return  commentUserId;}
    public String getCommunityName(){return communityName;}
    public String getCommunityId(){return CommunityId;}
    public String getGroup_id()
    {
        return group_id;
    }
    public String getParentuserId(){return  parentuserId;}
    public String getIs_read(){ return is_read; }

}
