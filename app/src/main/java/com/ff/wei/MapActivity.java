package com.ff.wei;

import bean.OverLay;
import util.FileOperation;
import util.GetPermission;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.util.Log.d;

public class MapActivity extends AppCompatActivity implements View.OnClickListener,ActivityCompat.OnRequestPermissionsResultCallback {
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
    //是否第一次定位，如果是第一次定位的话要将自己的位置显示在地图 中间
    private boolean isFirstLocation = true;
    //创建自己的箭头定位
    private BitmapDescriptor bitmapDescriptorRound,bitmapDescriptorSquare;
    //经纬度
    public static OverLay Myself=new OverLay();
    //方向传感器监听
    private float mLastX;
    //显示marker
    private boolean showMarker = false;

    private List<OverLay> overLays=new ArrayList<>();
    private Button addOver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GetPermission.makePermission(MapActivity.this,MapActivity.this);// 请求权限
        setMapCustomStyle();//设置地图风格

        addTestData();

        //在意该使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        //初始化控件
        initView();
        //初始化地图
        initMap();
        initLocation();
        //创建自己的定位图标，结合方向传感器，定位的时候显示自己的方向
        //initMyLoc();
        //创建marker信息
        d("status1","finished Created");
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
        addOver=(Button)findViewById(R.id.addOverLay);
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
            case R.id.addOverLay:
                isFirstLocation=true;
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
        LatLng latLng = null;
        Marker marker;
        OverlayOptions options;
        latLng = new LatLng(over.getLat(),over.getLng());
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
                .fontSize(20)
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
                    d("info","no getting");
                    return false;
                }
                d("info","clickTest:"+u.getName());

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
                    InfoWindow mInfoWindow = new InfoWindow(button, pt, -47);
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
            //将获取的location信息给百度map
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mLastX)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude())
                    .build();
            mBaiduMap.setMyLocationData(data);
            //更新经纬度
            Myself.setLat(location.getLatitude());
            Myself.setLng(location.getLongitude());
            //配置定位图层显示方式，使用自己的定位图标
            MyLocationConfiguration configuration = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, bitmapDescriptorRound);
            mBaiduMap.setMyLocationConfigeration(configuration);
            if(isFirstLocation&&location.getAddrStr()!=null){
                showInfo("loc:"+location+"\nlat:"+location.getLatitude()+"\nlng:"+location.getLongitude());
                showInfo("位置：" + location.getAddrStr());
                Log.d("testt","loc:"+location+"\nlat:"+location.getLatitude()+"\nlng:"+location.getLongitude());
                //获取经纬度
                LatLng ll = new LatLng(location.getLatitude(),location.getLongitude());
                addOverlay(Myself);
                MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(ll);
                //mBaiduMap.setMapStatus(status);//直接到中间
                mBaiduMap.animateMapStatus(status);//动画的方式到中间
                isFirstLocation = false;
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
}
