package com.maoyongxin.myapplication.ui;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.maoyongxin.myapplication.R;
import com.maoyongxin.myapplication.entity.PoiInfoBean;
import com.maoyongxin.myapplication.myapp.AppActivity;
import com.maoyongxin.myapplication.myapp.AppApplication;
import com.maoyongxin.myapplication.server.widget.ClearWriteEditText;
import com.maoyongxin.myapplication.ui.adapter.PoiListAdapter;

import java.io.Serializable;
import java.util.ArrayList;

public class PoiSearchActivity extends AppActivity {
    private ListView lv_poiList;
    private PoiSearch poiSearch;
    private PoiSearch.Query query;
    private PoiListAdapter adapter;
    private ImageView img_search_back;
    private ArrayList<PoiInfoBean> infoBeen;
    private ClearWriteEditText edit_search_title;
    private TextView tv_do_search;
    private boolean isSearched;


    @Override
    protected void initView() {
        super.initView();
        lv_poiList = (ListView) findViewById(R.id.lv_poiList);
        edit_search_title = (ClearWriteEditText) findViewById(R.id.edit_search_title);
        img_search_back = (ImageView) findViewById(R.id.img_search_back);
        edit_search_title.setHint("请输入企业名称");
        tv_do_search = (TextView) findViewById(R.id.tv_do_search);
        lv_poiList.setVisibility(View.GONE);
        isSearched = false;
        img_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected int bindLayout() {
        return R.layout.activity_poi_search;
    }

    private void searchPoi(String keyWord, String cityCode) {
        query = new PoiSearch.Query(keyWord, "企业", cityCode);
        //keyWord表示搜索字符串，
        // cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(1);//设置查询页码

        poiSearch = new PoiSearch(PoiSearchActivity.this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int j) {
                if (poiResult.getPois().size() != 0) {
                    isSearched = true;
                    infoBeen = new ArrayList<PoiInfoBean>();
                    for (int i = 0; i < poiResult.getPois().size(); i++) {
                        PoiInfoBean poi = new PoiInfoBean();
                        poi.setLatitude(poiResult.getPois().get(i).getLatLonPoint().getLatitude() + "");
                        poi.setLngtitude(poiResult.getPois().get(i).getLatLonPoint().getLongitude() + "");
                        poi.setPoiAddress(poiResult.getPois().get(i).getSnippet());
                        poi.setPoiDistance(poiResult.getPois().get(i).getDistance() + "公里");
                        poi.setPoiType(poiResult.getPois().get(i).getTypeDes());
                        poi.setPoiName(poiResult.getPois().get(i).getTitle());
                        infoBeen.add(poi);
                    }
                    adapter = new PoiListAdapter(PoiSearchActivity.this, infoBeen);
                    lv_poiList.setAdapter(adapter);
                    lv_poiList.setVisibility(View.VISIBLE);
                } else {
                    lv_poiList.setVisibility(View.GONE);
                }
                lv_poiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent();
                        intent.putExtra("poiName", infoBeen.get(position).getPoiName());
                        intent.putExtra("poiLat", infoBeen.get(position).getLatitude());
                        intent.putExtra("poiLng", infoBeen.get(position).getLngtitude());
                        intent.putExtra("poiDistance", infoBeen.get(position).getPoiDistance());
                        intent.putExtra("poiType", infoBeen.get(position).getPoiType());
                        intent.putExtra("poiAddress", infoBeen.get(position).getPoiAddress());
                        setResult(AppApplication.POISEARCH_OK, intent);
                        finish();
                    }
                });
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
        poiSearch.searchPOIAsyn();
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        edit_search_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    searchPoi(s.toString(), "贵阳");
                    searchPoi(s.toString(), AppApplication.getMyCurrentLocation().getCityName());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tv_do_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearched == true) {
                    Intent intent = new Intent();
                    intent.putExtra("poiList", (Serializable) infoBeen);
                    setResult(AppApplication.POISEARCH_LIST_OK, intent);
                    finish();
                } else {
                    Toast.makeText(PoiSearchActivity.this, "暂无数据", Toast.LENGTH_SHORT).show();
                    setResult(AppApplication.NORESULT);
                    finish();
                }
            }
        });
    }
}
