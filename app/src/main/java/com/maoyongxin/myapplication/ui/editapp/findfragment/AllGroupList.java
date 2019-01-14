package com.maoyongxin.myapplication.ui.editapp.findfragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.allGroupinfo;
import com.maoyongxin.myapplication.entity.shanghuiInfo;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.ui.adapter.GridView_allgrouplist_adapter;
import com.maoyongxin.myapplication.ui.widget.mylistview.AntGrideVIew;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class AllGroupList extends AppActivity {
    @BindView(R.id.gv_allgrouplist)
    AntGrideVIew gvAllgrouplist;




     private GridView_allgrouplist_adapter gridView_allgrouplist_adapter;
    private List<allGroupinfo> grouplist = new ArrayList<>();

    @Override
    protected int bindLayout() {
        return (R.layout.activity_all_group_list);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//白底黑字+沉浸式导航栏
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initData() {
        //super.initData();

    }

    @Override
    protected void initView() {
        //super.initView();
        getallGroupList();







    }

    private  void  getallGroupList()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpUtils.get().url("http://st.3dgogo.com/index/chatgroup/get_all_chatgroup_api.html")
                        .addParams("userid", AppApplication.getCurrentUserInfo().getUserId())
                        .build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try
                        {
                            JSONObject data=new JSONObject(response);

                           JSONArray datadetail=data.getJSONArray("info");

                            for(int i=0;i<datadetail.length();i++)
                            {
                                allGroupinfo datainfo=new allGroupinfo();
                                JSONObject newList = datadetail.getJSONObject(i);
                                datainfo.setGroupId(newList.getString("groupId"));
                                datainfo.setGroupName(newList.getString("groupName"));
                                datainfo.setImage(newList.getString("image"));
                                datainfo.setGroupNote(newList.getString("groupNote"));
                                grouplist.add(datainfo);
                            }
                            gridView_allgrouplist_adapter=new GridView_allgrouplist_adapter(getActivity(),grouplist);
                            gvAllgrouplist.setAdapter(gridView_allgrouplist_adapter);
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }



                    }
                });




            }
        }).start();
    }
}
