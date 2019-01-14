package com.maoyongxin.myapplication.ui.editapp.minefragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jky.baselibrary.util.common.TimeUtil;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.http.callback.EntityCallBack;
import com.maoyongxin.myapplication.http.request.ReqCommunity;
import com.maoyongxin.myapplication.http.request.ReqGetFollowList;
import com.maoyongxin.myapplication.http.request.ReqGetMyFriendsList;
import com.maoyongxin.myapplication.http.request.ReqUserServer;
import com.maoyongxin.myapplication.http.response.ChangeUserInfoResponse;
import com.maoyongxin.myapplication.http.response.FollowListResponse;
import com.maoyongxin.myapplication.http.response.FriendsResponse;
import com.maoyongxin.myapplication.http.response.MyCommunityResponse;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.utils.NToast;
import com.maoyongxin.myapplication.tool.CompressPhotoUtils;
import com.maoyongxin.myapplication.tool.ToastUtil;
import com.maoyongxin.myapplication.ui.AddressHomeCheckActivity;
import com.maoyongxin.myapplication.ui.CommunityDetailActivity;
import com.maoyongxin.myapplication.ui.InterestChoiceActivity;
import com.maoyongxin.myapplication.ui.MyPayActivity;

import com.maoyongxin.myapplication.ui.UserEditNewActivity;
import com.maoyongxin.myapplication.ui.UserNumSettingActivity;
import com.maoyongxin.myapplication.ui.chat.MyFriendsActivity;
import com.maoyongxin.myapplication.ui.list_follow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONObject;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;
import okhttp3.Call;
import okhttp3.OkHttpClient;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by AMing on 16/6/21.
 * Company RongCloud
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private static final int COMPARE_VERSION = 54;
    public static final String SHOW_RED = "SHOW_RED";
    private SharedPreferences sp;
    private ImageView imageView;
    //    private TextView mName;
    private boolean isHasNewVersion;
    private String url;
    private boolean isDebug;
    private TextView sgs,tv_userName, tv_idNum, tv_myNote, tv_doPay, tv_numsetting, tv_myhobby, tv_myschool, tv_my_communty, tv_my_personal,tv_vipNum,friendnum,tv_myFollow;
    private Button btn_logout;
    private ProgressDialog mProgressDialog;
    private String birthDay;
    private String nickName;
    private String myHeadPath;
    private String note;
    private int sex;
    private String local="/storage/emulated/0/UCDownloads/pictures/128oT5N1YHlEk%2FJ.jpg";
    private String localhead="";
    private Boolean firsligin=true;
    private LinearLayout myfriend,my_follow;
    private Boolean headhasChanged,isUpable=false;
    Boolean isSuperManager,isManager;
    private TextView tv_newedtion;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.mine_fragment_main, container, false);
        isDebug = getContext().getSharedPreferences("config", getContext().MODE_PRIVATE).getBoolean("isDebug", false);
        //第四界面
        initData();
        initViews(mView);
        return mView;
    }


    private void initData() {
        sp = getActivity().getSharedPreferences("config", MODE_PRIVATE);

        sex = 0;



    }

    private void initViews(View mView) {
        sgs=(TextView)mView.findViewById(R.id.sgs);
        imageView = (ImageView) mView.findViewById(R.id.mine_header);
        tv_userName = (TextView) mView.findViewById(R.id.tv_userName);
        tv_idNum = (TextView) mView.findViewById(R.id.tv_idNum);
        tv_myNote = (TextView) mView.findViewById(R.id.tv_myNote);
        tv_vipNum = (TextView) mView.findViewById(R.id.tv_vipNum);
        tv_doPay = (TextView) mView.findViewById(R.id.tv_doPay);
        tv_numsetting = (TextView) mView.findViewById(R.id.tv_numsetting);
        tv_myhobby = (TextView) mView.findViewById(R.id.tv_myhobby);
        tv_myschool = (TextView) mView.findViewById(R.id.tv_myschool);
        tv_my_communty = (TextView) mView.findViewById(R.id.tv_my_communty);
        tv_my_personal = (TextView) mView.findViewById(R.id.tv_my_personal);
        btn_logout = (Button) mView.findViewById(R.id.btn_logout);
        friendnum=(TextView)mView.findViewById(R.id.frendnum);
        tv_myFollow=(TextView)mView.findViewById(R.id.tv_myFollow);
        myfriend=(LinearLayout)mView.findViewById(R.id.ll_myfriend);
        my_follow=(LinearLayout)mView.findViewById(R.id.ll_myfollow);

        tv_newedtion=(TextView)mView.findViewById(R.id.tv_newedtion);

        myfriend.setOnClickListener(this);
        my_follow.setOnClickListener(this);
        imageView.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        tv_doPay.setOnClickListener(this);
        tv_my_personal.setOnClickListener(this);
        tv_my_communty.setOnClickListener(this);
        tv_myschool.setOnClickListener(this);
        tv_myhobby.setOnClickListener(this);
        tv_numsetting.setOnClickListener(this);
        mProgressDialog = new ProgressDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog);
        mProgressDialog.setCancelable(true);
        sgs.setOnClickListener(this);
        initUserData();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(firsligin)
        {
            initUserData();

        }
        else
        {
            reinitUserData();
        }

    }

    private void initUserData() {

        if (AppApplication.getCurrentUserInfo().getHeadImg() == null || AppApplication.getCurrentUserInfo().getHeadImg().equals("")) {
            Glide.with(getActivity()).load(R.mipmap.user_head_img).into(imageView);
        } else {

            myHeadPath =  AppApplication.getCurrentUserInfo().getHeadImg();

            Glide.with(getActivity()).load(myHeadPath).into(imageView);
        }
        if(AppApplication.getmDownloadUrl()!=null)
        {
            tv_newedtion.setText("可更新");
        }
        else
        {
            tv_newedtion.setVisibility(View.GONE);
        }
        tv_userName.setText(AppApplication.getCurrentUserInfo().getUserName());
        tv_myNote.setText(AppApplication.getCurrentUserInfo().getNote());
        tv_idNum.setText("ID:" + AppApplication.getCurrentUserInfo().getUserId());
        tv_myNote.setText("个人简介：" + AppApplication.getCurrentUserInfo().getNote());
        tv_vipNum.setText("会员等级：" + AppApplication.getCurrentUserInfo().getVipNum());
        nickName = AppApplication.getCurrentUserInfo().getUserName();
        myHeadPath = AppApplication.getCurrentUserInfo().getHeadImg();
        birthDay = AppApplication.getCurrentUserInfo().getBrithday();
        note= AppApplication.getCurrentUserInfo().getNote();
        getMyFollowList();
        getFriendList();

        RongIM.getInstance().refreshUserInfoCache(new UserInfo(AppApplication.getCurrentUserInfo().getUserId(),
                AppApplication.getCurrentUserInfo().getUserName(),
                Uri.parse(myHeadPath)));
    }



    @Override

    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

            // 相当于onResume()方法
            getMyFollowList();
        } else {

            // 相当于onpause()方法

        }

    }



    private void getMyFollowList() {
        ReqGetFollowList.getList(getActivity(), getTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<FollowListResponse>() {
            @Override
            public Class<FollowListResponse> getEntityClass() {
                return FollowListResponse.class;
            }

            @Override
            public void onSuccess(final FollowListResponse resp) {
                if (resp.is200()) {

                    //  followLists = resp.obj.getFollowList();
                    tv_myFollow.setText(resp.obj.getFollowList().size()+"");
                } else {

                }
            }

            @Override
            public void onFailure(Throwable e) {

            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void getFriendList() {
        ReqGetMyFriendsList.getFriends(getActivity(), getTag(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<FriendsResponse>() {
            @Override
            public Class<FriendsResponse> getEntityClass() {
                return FriendsResponse.class;
            }

            @Override
            public void onSuccess(FriendsResponse resp) {
                if (resp.is200()) {
                    AppApplication.getCurrentUserInfo().setFriendNum(resp.obj.getFriendList().size()+"");
                    friendnum.setText(resp.obj.getFriendList().size()+"");



                } else {
                    NToast.shortToast(getActivity(), resp.msg);
                }
            }

            @Override
            public void onFailure(Throwable e) {

            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
    private void reinitUserData() {
        Glide.with(getActivity()).load(localhead).into(imageView);
        tv_userName.setText(AppApplication.getCurrentUserInfo().getUserName());
        tv_myNote.setText(AppApplication.getCurrentUserInfo().getNote());
        tv_idNum.setText("ID:" + AppApplication.getCurrentUserInfo().getUserId());
        tv_myNote.setText("个人简介：" + AppApplication.getCurrentUserInfo().getNote());
        tv_vipNum.setText("会员等级：" + AppApplication.getCurrentUserInfo().getVipNum());
        nickName = AppApplication.getCurrentUserInfo().getUserName();
        myHeadPath = AppApplication.getCurrentUserInfo().getHeadImg();
        birthDay = AppApplication.getCurrentUserInfo().getBrithday();
        note= AppApplication.getCurrentUserInfo().getNote();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mine_header:
                startActivityForResult(new Intent(getActivity(), UserEditNewActivity.class), AppApplication.GETMY_USERINFO);
                break;
            case R.id.tv_numsetting:
                startActivity(new Intent(getActivity(), UserNumSettingActivity.class));
                break;
            case R.id.btn_logout:
                doLogOut();
                break;
            case R.id.tv_my_communty:



                getMyOwnCommunity();



                break;


            case R.id.tv_myschool:

                tomicroweb();



                break;


            case R.id.tv_myhobby:
                Toast.makeText(getActivity(),"工程师努力建设中，暂未开通",Toast.LENGTH_SHORT).show();
                // startActivityForResult(new Intent(getActivity(), InterestChoiceActivity.class), 1);
                break;
            case R.id.tv_my_personal:
                startActivityForResult(new Intent(getActivity(), UserEditNewActivity.class), AppApplication.GETMY_USERINFO);
                break;
            case R.id.tv_doPay:
                startActivity(new Intent(getActivity(), MyPayActivity.class));
                break;
            case R.id.ll_myfriend:
                startActivity(new Intent(getActivity(),MyFriendsActivity.class));
                break;

            case R.id.ll_myfollow:
                startActivity(new Intent(getActivity(), list_follow.class));
                break;
            case R.id.sgs:
                startActivity(new Intent(getActivity(), activity_sgs.class));
                //反馈建议
                break;


        }
    }

    private void tomicroweb()
    {
        ReqCommunity.getMyCommunity(getActivity(), getClass().getSimpleName(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<MyCommunityResponse>() {
            @Override
            public Class<MyCommunityResponse> getEntityClass() {
                return MyCommunityResponse.class;
            }

            @Override
            public void onSuccess(MyCommunityResponse resp) {
                if (resp.is200()) {


                    getmymicroweb(resp.obj.getCommunityId()+"");
                }
                else
                {
                    startActivity(new Intent(getActivity(), empty_micro_web.class));
                }
            }

            @Override
            public void onFailure(Throwable e) {
                startActivity(new Intent(getActivity(), AddressHomeCheckActivity.class));
            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getMyOwnCommunity() {
        ReqCommunity.getMyCommunity(getActivity(), getClass().getSimpleName(), AppApplication.getCurrentUserInfo().getUserId(), new EntityCallBack<MyCommunityResponse>() {
            @Override
            public Class<MyCommunityResponse> getEntityClass() {
                return MyCommunityResponse.class;
            }

            @Override
            public void onSuccess(MyCommunityResponse resp) {
                if (resp.is200()) {
                    // startCommunityDetail();
                    Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
                    intent.putExtra("myCommunityIcon", resp.obj.getCommunityImg());
                    intent.putExtra("myCommunityName", resp.obj.getCommunityName());
                    intent.putExtra("myCommunityNote", resp.obj.getCommunityNote());
                    intent.putExtra("myCommunityAddress", resp.obj.getAddress());
                    intent.putExtra("myCommunityCreatTime", TimeUtil.getFormatYMDFromMillis(resp.obj.getCreatTime(), "yyyy-MM-dd"));
                    intent.putExtra("myCommunityId", resp.obj.getCommunityId()+"");
                    startActivity(intent);


                }
                else
                {
                    startActivity(new Intent(getActivity(), AddressHomeCheckActivity.class));
                }
            }

            @Override
            public void onFailure(Throwable e) {
                startActivity(new Intent(getActivity(), AddressHomeCheckActivity.class));
            }

            @Override
            public void onCancelled(Throwable e) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void doLogOut() {
        CompressPhotoUtils.deleteFolderFile("mnt/sdcard/situ/", true);//清空图片操作缓存
        SharedPreferences preferences = getActivity().getSharedPreferences("first_open", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("is_first_open", true);
        editor.commit();
        makeSpNoAuto(getActivity());
        final Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage(getActivity().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void makeSpNoAuto(Context context) {
        SharedPreferences sharedPreferences=context.getSharedPreferences("userInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("isAuto", false);
        editor.putString("psw", null);
        editor.putString("num", null);
        editor.commit();
    }
    private void changeMyUserInfo() {
        firsligin=false;

        ReqUserServer.changeUserInfo(getActivity(), getTag(), AppApplication.getCurrentUserInfo().getUserId(), note+"", sex + "",  "1991-11-01", nickName + "", new EntityCallBack<ChangeUserInfoResponse>() {
            @Override
            public Class<ChangeUserInfoResponse> getEntityClass() {
                return ChangeUserInfoResponse.class;
            }

            @Override
            public void onSuccess(ChangeUserInfoResponse resp) {
                mProgressDialog.dismiss();
                if (resp.is200()) {
                    AppApplication.setCurrentUserInfo(resp.obj);
                    reinitUserData();
                    NToast.shortToast(getActivity(), "保存成功");
                } else {
                    NToast.shortToast(getActivity(), "获取个人信息失败");
                }
            }

            @Override
            public void onFailure(Throwable e) {
                // Log.e("???", "Throwable:111 " +e);
            }

            @Override
            public void onCancelled(Throwable e) {
                // Log.e("???", "Throwable: " +e);
            }

            @Override
            public void onFinished() {
                //Log.e("???", "onFinished: " );
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppApplication.UPLOAD_INTERESTOK) {
            sex = data.getIntExtra("sex", 0);
            birthDay = data.getStringExtra("birthDay");
            nickName = data.getStringExtra("nickName");
            localhead = data.getStringExtra("localhead");
            note= data.getStringExtra("note");
            changeMyUserInfo();

        }
        else if (requestCode == AppApplication.GETMY_USERINFO || resultCode == AppApplication.GETMY_USERINFO_OK) {
            sex = data.getIntExtra("sex", 0);
            birthDay = data.getStringExtra("birthDay");
            nickName = data.getStringExtra("nickName");
            localhead = data.getStringExtra("localhead");
            note= data.getStringExtra("note");
            changeMyUserInfo();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void getmymicroweb(final String myCommunityId) {

        final String XkOne = "http://st.3dgogo.com/index/enterprise_info/get_enterprise_publicity_details_api.html";
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                try {
                    OkHttpUtils.post().url(XkOne).addParams("community_id", myCommunityId).build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {

                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    try {
                                        JSONObject shangjiaInfo = new JSONObject(response);
                                        if (shangjiaInfo.getInt("code") == 200) {
                                            Toast.makeText(getContext(), "欢迎查看您的微官网", Toast.LENGTH_SHORT).show();
                                            //  hasadvertise = true;
                                            Intent intent = new Intent(getContext(), xiangqingye.class);
                                            intent.putExtra("shangjiaInfo", response);
                                            intent.putExtra("myCommunityId", myCommunityId);
                                            getContext().startActivity(intent);
                                        } else if (shangjiaInfo.getInt("code") == 500) {
                                            //  hasadvertise = false;


                                            Toast.makeText(getContext(), "管理员没开通微官网", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {

                                    }
                                }


                            });}
                catch (Exception e)
                {
                    e.printStackTrace();}
            }


        }).start();

    }
}
