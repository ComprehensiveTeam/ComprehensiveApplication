package com.example.comprehensiveapplication.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.example.comprehensiveapplication.R;
import com.example.comprehensiveapplication.Utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class LBSActivity extends BaseActivity {

    private TextView positionText;
    private TextView debug;
    private MapView mapView;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    private AMap aMap;
    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lbs);
        requestPermission();
        positionText = findViewById(R.id.position_text_view);
        debug = findViewById(R.id.debug);
        initLocation();
        startLocation();
        //Log.d("cxdebug","sha1:"+sHA1(this));
        mapView = findViewById(R.id.amap_view);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.setMyLocationEnabled(true);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);
        aMap.setMyLocationStyle(myLocationStyle);
        Button refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stopLocation())
                    //initLocation();
                    startLocation();

            }
        });

    }

    private void navigateTo(AMapLocation aMapLocation) {
        if (isFirstLocate) {
            LatLng ll = new LatLng(aMapLocation.getLatitude(),aMapLocation.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
            aMap.animateCamera(update);
            update = CameraUpdateFactory.zoomTo(16f);
            aMap.animateCamera(update);
            isFirstLocate = false;

        }
    }

   /* public static String sHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i]).toUpperCase(Locale.US);
                if (appendString.length() == 1) hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } return null;
    }*/

    private void requestPermission() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }

    GeocodeSearch.OnGeocodeSearchListener listener = new GeocodeSearch.OnGeocodeSearchListener() {
        @Override
        public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

        }

        @Override
        public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

        }
    };

    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyLocation();
        mapView.onDestroy();
    }


    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(final AMapLocation aMapLocation) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (null != aMapLocation) {
                        navigateTo(aMapLocation);
                        StringBuffer sb = new StringBuffer();
                        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                        if(aMapLocation.getErrorCode() == 0){
                            sb.append("定位成功" + "\n");
                            sb.append("定位类型: " + aMapLocation.getLocationType() + "\n");
                            sb.append("经    度    : " + aMapLocation.getLongitude() + "\n");
                            sb.append("纬    度    : " + aMapLocation.getLatitude() + "\n");
                            sb.append("精    度    : " + aMapLocation.getAccuracy() + "米" + "\n");
                            sb.append("提供者    : " + aMapLocation.getProvider() + "\n");

                            sb.append("速    度    : " + aMapLocation.getSpeed() + "米/秒" + "\n");
                            sb.append("角    度    : " + aMapLocation.getBearing() + "\n");
                            // 获取当前提供定位服务的卫星个数
                            sb.append("星    数    : " + aMapLocation.getSatellites() + "\n");
                            sb.append("国    家    : " + aMapLocation.getCountry() + "\n");
                            sb.append("省            : " + aMapLocation.getProvince() + "\n");
                            sb.append("市            : " + aMapLocation.getCity() + "\n");
                            sb.append("城市编码 : " + aMapLocation.getCityCode() + "\n");
                            sb.append("区            : " + aMapLocation.getDistrict() + "\n");
                            sb.append("区域 码   : " + aMapLocation.getAdCode() + "\n");
                            sb.append("地    址    : " + aMapLocation.getAddress() + "\n");
                            sb.append("兴趣点    : " + aMapLocation.getPoiName() + "\n");
                            //定位完成的时间
                            sb.append("定位时间: " + Utils.formatUTC(aMapLocation.getTime(), "yyyy-MM-dd HH:mm:ss") + "\n");
                        } else {
                            //定位失败
                            sb.append("定位失败" + "\n");
                            sb.append("错误码:" + aMapLocation.getErrorCode() + "\n");
                            sb.append("错误信息:" + aMapLocation.getErrorInfo() + "\n");
                            sb.append("错误描述:" + aMapLocation.getLocationDetail() + "\n");
                        }
                        sb.append("***定位质量报告***").append("\n");
                        sb.append("* WIFI开关：").append(aMapLocation.getLocationQualityReport().isWifiAble() ? "开启":"关闭").append("\n");
                        sb.append("* GPS状态：").append(getGPSStatusString(aMapLocation.getLocationQualityReport().getGPSStatus())).append("\n");
                        sb.append("* GPS星数：").append(aMapLocation.getLocationQualityReport().getGPSSatellites()).append("\n");
                        sb.append("* 网络类型：" + aMapLocation.getLocationQualityReport().getNetworkType()).append("\n");
                        sb.append("* 网络耗时：" + aMapLocation.getLocationQualityReport().getNetUseTime()).append("\n");
                        sb.append("****************").append("\n");
                        //定位之后的回调时间
                        sb.append("回调时间: " + Utils.formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss") + "\n");

                        //解析定位结果，
                        String result = sb.toString();
                        positionText.setText(result);
                    } else {
                        positionText.setText("定位失败，loc is null");
                    }
                    //debug.setText("debug:"+locationOption.isNeedAddress());
                }
            });




        }
    };

    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "你必须统一所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    startLocation();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用程序", Toast.LENGTH_SHORT).show();
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    private void startLocation() {
        //根据控件的选择，重新设置定位参数
        //resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    private boolean stopLocation() {
        // 停止定位
        locationClient.stopLocation();
        return true;
    }

    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}

