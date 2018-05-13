package com.ff.wei;

import bean.OverLay;
import party.CreatePartyActivity;
import util.FileOperation;
import util.GetPermission;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.util.Log.d;

public class MapActivity extends Activity implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback {
    private BaiduMap.OnMarkerClickListener markListener=null;
    public static  MapView mMapView = null;
    public static BaiduMap mBaiduMap;
    //模式切换，正常模式
    private boolean modeFlag = true;
    //当前地图缩放级别
    private float zoomLevel;
    //定位相关
    public static LocationClient mLocationClient;
    private MyLocationListener mLocationListener;
    //创建方的圆的图标
    private BitmapDescriptor bitmapDescriptorRound,bitmapDescriptorSquare;
    //用户
    public static OverLay Myself=new OverLay();
    //方向传感器监听
    private float mLastDirect;
    //显示marker
    private boolean showMarker = false;

    private List<OverLay> overLays=new ArrayList<>();
    private Button addOver;

    private View tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetPermission.makePermission(MapActivity.this,MapActivity.this);// 请求权限
        setMapCustomStyle();//设置地图风格

        addTestData();

        //注方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        //设置隐藏标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);

        //初始化控件
        initView();
        //初始化地图
        initMap();
        initLocation();
    }

    private void initMyLoc() {
        //初始化图标
        bitmapDescriptorRound = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round);
        bitmapDescriptorSquare = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
    }
    private void initMap() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        // 不显示缩放比例尺
        mMapView.showZoomControls(false);
        // 不显示百度地图Logo
        mMapView.removeViewAt(1);
        //百度地图
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                MapView.setMapCustomEnable(true);
            }
        });

        // 改变地图状态
        MapStatus mMapStatus = new MapStatus.Builder().zoom(15).build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        mBaiduMap.setMapStatus(mMapStatusUpdate);
        //设置地图状态改变监听器
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
            }
            @Override
            public void onMapStatusChange(MapStatus arg0) {
                //当地图状态改变的时候，获取放大级别
                zoomLevel = arg0.zoom;
            }
        });
        //地图点击事件
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public boolean onMapPoiClick(MapPoi arg0) {
                return false;
            }
            @Override
            public void onMapClick(LatLng arg0) {
                Log.d("myTest","onMapClick:"+arg0.toString());
                GeoCoder mSearch = GeoCoder.newInstance();
                OnGetGeoCoderResultListener listener = new OnGetGeoCoderResultListener() {
                    public void onGetGeoCodeResult(GeoCodeResult result) {
                        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                            //没有检索到结果
                        }
                        //获取地理编码结果
                    }
                    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                            //没有找到检索结果
                        }
                        //获取反向地理编码结果
                        //showInfo("addr:"+result.getAddress());
                        createView(result.getAddress());
                    }
                };
                mSearch.setOnGetGeoCodeResultListener(listener);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(new LatLng(arg0.latitude,arg0.longitude)));
                mSearch.destroy();
            }
        });
    }
    private void initLocation() {
        //定位客户端的设置
        mLocationClient = new LocationClient(this);
        mLocationListener = new MyLocationListener();
        //注册监听
        mLocationClient.registerLocationListener(mLocationListener);
        //配置定位
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");//坐标类型
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//打开Gps
        option.setScanSpan(1000);//毫秒定位一次
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }
    private void initView() {
        //地图控制按钮
        addOver=(Button)findViewById(R.id.testBT);
        addOver.setOnClickListener(this);
    }

    public void addTestData(){
        Myself.setName("nickcle");
        Myself.setType("user");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        GetPermission.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.testBT:
                backToCenter();
                break;
            case R.id.createPartyBT:
                Intent intent=new Intent(this, CreatePartyActivity.class);
                startActivity(intent);
                break;
        }
    }
    //显示marker
    private void addOverlay(final OverLay over) {
        //清空地图
        mBaiduMap.clear();
        BitmapDescriptor bitmap;
        //创建marker的显示图标
        if(over.getType().equals("user")){
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher_round);
        }else{
            bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher);
        }
        LatLng latLng = new LatLng(over.getLat(),over.getLng());
        Marker marker;
        OverlayOptions options;
            //设置marker
        options = new MarkerOptions()
                    .position(latLng)//设置位置
                    .icon(bitmap)//设置图标样式
                    .zIndex(9) // 设置marker所在层级
                    .draggable(false); // 设置手势拖拽;
            //添加marker
        marker = (Marker) mBaiduMap.addOverlay(options);

        //添加上方文字
        OverlayOptions textOption = new TextOptions()
                .bgColor(0xAAFFFF00)
                .fontSize(50)
                .fontColor(0xFFFF00FF)
                .text(over.getName())
                .rotate(0)
                .position(latLng);
        mBaiduMap.addOverlay(textOption);
        //使用marker携带info信息，当点击事件的时候可以通过marker获得info信息
        Bundle bundle = new Bundle();
            //info必须实现序列化接口
        bundle.putSerializable("overLays", over);
        marker.setExtraInfo(bundle);
        //添加marker点击事件的监听
        if(markListener!=null){
            mBaiduMap.removeMarkerClickListener(markListener); //防止监听重叠
        }
        markListener= new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //从marker中获取info信息
                Bundle bundle = marker.getExtraInfo();
                OverLay u = (OverLay) bundle.getSerializable("overLays");
                //将信息显示在界面上
                if(u==null){
                    Log.d("info","no getting");
                    return false;
                }
                Log.d("info","clickTest:"+u.getName());

                //添加消息窗口
                if(u.showInfoWindow){
                    u.showInfoWindow=false;
                    bundle.putSerializable("overLays",u);
                    mBaiduMap.hideInfoWindow();
                }else{
                    u.showInfoWindow=true;
                    //创建InfoWindow展示的view
                    Button button = new Button(getApplicationContext());
                    button.setText(u.getName());

                    LatLng pt = new LatLng(over.getLat(), over.getLng());
                    InfoWindow mInfoWindow = new InfoWindow(button, pt, -150);
                    mBaiduMap.showInfoWindow(mInfoWindow);
                }
                return true;
            }
        };
        mBaiduMap.setOnMarkerClickListener(markListener);
    }
    //自定义的定位监听
    class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //若位置并未改变则直接跳出函数
            if(Myself.getLat()==location.getLatitude()&&Myself.getLng()==location.getLongitude()){
                return ;
            }
            Log.d("testInfo","first:"+Myself.getLat()+"~"+Myself.getLng());
            Log.d("myTest","location changed");
            //将获取的location信息给百度map
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(data);
            //更新经纬度
            if(Myself.getLat()==Myself.getLng()&&Myself.getLng()==0.0){
                Myself.setLat(location.getLatitude());
                Myself.setLng(location.getLongitude());
                backToCenter();
            }else{
                Myself.setLat(location.getLatitude());
                Myself.setLng(location.getLongitude());
            }
            addOverlay(Myself);
            //配置定位图层显示方式，使用自己的定位图标
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptorRound);
            mBaiduMap.setMyLocationConfigeration(configuration);
            if(location.getAddrStr()!=null){
                Log.d("testInfo","位置已变:"+location.getAddrStr());
            }
            //mBaiduMap.clear();
            //addOverlay(overLays);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开启定位
        mBaiduMap.setMyLocationEnabled(true);
        if(!mLocationClient.isStarted()){
            mLocationClient.start();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        //关闭定位
        mBaiduMap.setMyLocationEnabled(false);
        if(mLocationClient.isStarted()){
            mLocationClient.stop();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
    //显示消息
    private void showInfo(String str){
        Toast.makeText(MapActivity.this, str, Toast.LENGTH_SHORT).show();
    }

    public void setMapCustomStyle() {
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("custom_config.txt");
            String dst="/storage/emulated/0/map_style.txt";
            if(FileOperation.copyFile(inputStream,dst)){
                MapView.setCustomMapStylePath(dst);
            }else{
                showInfo("filecopyfailed");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showInfo("exception");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createView(String str){
        if(tips==null){
            LayoutInflater inflater=getLayoutInflater();
            LinearLayout view= (LinearLayout) inflater.inflate(R.layout.loction_click_tip,null);
            TextView text= (TextView) view.getChildAt(0);
            LinearLayout  linearLayoutChild= (LinearLayout) view.getChildAt(1);
            Button bt = (Button) linearLayoutChild.getChildAt(0);
            bt.setOnClickListener(this);

            text.setText(str);
            LinearLayout thiss= (LinearLayout) findViewById(R.id.map);
            LinearLayout thisss= (LinearLayout) thiss.getChildAt(0);
            thisss.addView(view);
            tips=view;
        }else{
            LinearLayout thiss= (LinearLayout) findViewById(R.id.map);
            LinearLayout thisss= (LinearLayout) thiss.getChildAt(0);
            thisss.removeView(tips);
            tips=null;
        }


    }

    public void backToCenter(){
        LatLng ll = new LatLng(Myself.getLat(),Myself.getLng());
        addOverlay(Myself);
        MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
        //mBaiduMap.setMapStatus(status);//直接到中间
        mBaiduMap.animateMapStatus(status);//动画的方式到中间
    }

}
