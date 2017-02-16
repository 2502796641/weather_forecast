package com.example.acer.weather_forecast.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.acer.weather_forecast.R;
import com.example.acer.weather_forecast.adapter.AddcityAdapter;
import com.example.acer.weather_forecast.adapter.FirecastAdapter;
import com.example.acer.weather_forecast.app.App;
import com.example.acer.weather_forecast.db.DBManager;
import com.example.acer.weather_forecast.entity.Futures;
import com.example.acer.weather_forecast.entity.Sk;
import com.example.acer.weather_forecast.entity.Today;
import com.example.acer.weather_forecast.entity.WeatherFromJuhe;

import com.example.acer.weather_forecast.utils.GetDataUtils;
import com.example.acer.weather_forecast.view.MyListview;


import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private TextView tv0,tv1,tv2,tv3,tv4,tv5,tv6,tv7,tv8,tv9,tv10,tv11,tv_headcity,tv_city,tv_toolbar;
    private ImageView iv,iv_mytoolbar;
    private ListView lv;
    private MyListview lv1;
    private ScrollView sc;
    private List<Futures> data;
    private FirecastAdapter adapter;
    private AddcityAdapter addcityAdapter;
    private String str;
    private int[] nums1 = new int[7];
    private int[] nums2 = new int[7];
    private String[] dates = new String[7];
    private LineChartView lineChart;
    List<PointValue> valuesLow = new ArrayList<PointValue>();//每天的最低温度
    List<PointValue> valuesHigh = new ArrayList<PointValue>();//每天的最高温度
    private List<AxisValue> mAxisXValues = new ArrayList<>();
    private DBManager manager = new DBManager(this);
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg != null){
                switch (msg.what){
                    case App.LOAD_SUC:
                        WeatherFromJuhe wfj = (WeatherFromJuhe) msg.obj;
                        Sk sk = wfj.getResult().getSk();
                        Today today = wfj.getResult().getToday();
                        tv0.setText(today.getDate_y());
                        tv1.setText(sk.getTemp()+"℃");
                        tv2.setText(today.getTemperature());
                        tv3.setText(sk.getWind_direction() + "：" + sk.getWind_strength());
                        tv4.setText("空气湿度：" + sk.getHumidity());
                        tv5.setText("最新更新时间：" + sk.getTime());
                        tv6.setText(today.getDressing_index());
                        tv7.setText(today.getWash_index());
                        tv8.setText(today.getUv_index());
                        tv9.setText(today.getTravel_index());
                        tv10.setText("舒适");
                        tv11.setText(today.getExercise_index());
                        data = wfj.getResult().getFuture();
                        //清除折线图的数据源
                        valuesLow.clear();
                        valuesHigh.clear();
                        //获取数据绘制折线图
                        for(int i = 0;i < data.size();i++){
                            String s = data.get(i).getTemperature().split("~")[0];
                            String s1 = s.substring(0,s.length() - 1);
                            int num = Integer.parseInt(s1);
                            nums1[i] = num;
                            dates[i] = data.get(i).getDate().substring(4,8);
                            String s2 = data.get(i).getTemperature().split("~")[1];
                            String s3 = s2.substring(0,s2.length() - 1);
                            int num1 = Integer.parseInt(s3);
                            valuesLow.add(new PointValue(i,num));
                            valuesHigh.add(new PointValue(i,num1));
                            nums2[i] = num1;
                        }
                        getAxisXLables(dates);//获取x轴的标注
                        initLineChart(valuesHigh,valuesLow);
                        adapter = new FirecastAdapter(data,MainActivity.this);
                        lv.setAdapter(adapter);
                }
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initView();
        ShareSDK.initSDK(this,"1b1619165499c");
        SharedPreferences sp = getSharedPreferences("mycity", MODE_PRIVATE);
        str = sp.getString("mycity", "广州");
        GetDataUtils.getResult(App.BASE_URL + str + App.APP_KEY,handler);
        tv_toolbar.setText(str);
    }
    /**
     * X 轴的显示
     */
    private void getAxisXLables(String[] dates){
        for (int i = 0; i < dates.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(dates[i]));
        }
    }
    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart(List<PointValue> highPointValues,List<PointValue> lowPointValues){
        List<Line> lines = new ArrayList<Line>();
        Line line = new Line(highPointValues).setColor(Color.parseColor("#FFFFFF")).setStrokeWidth(2);  //折线的颜色、粗细
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(true);//曲线是否平滑
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        line.setPointRadius(6);//座标点大小
        line.setHasLabelsOnlyForSelected(false);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        Line lineLow = new Line(lowPointValues).setColor(Color.parseColor("#FFFFFF")).setStrokeWidth(2);
        lineLow.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        lineLow.setCubic(true);//曲线是否平滑
        lineLow.setFilled(false);//是否填充曲线的面积
        lineLow.setHasLabels(true);//曲线的数据坐标是否加上备注
        lineLow.setPointRadius(6);
        lineLow.setHasLabelsOnlyForSelected(false);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        lineLow.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        lineLow.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(lineLow);
        LineChartData data = new LineChartData();
        data.setValueLabelBackgroundColor(Color.TRANSPARENT);//此处设置坐标点旁边的文字背景
        data.setValueLabelBackgroundEnabled(false);
        data.setValueLabelsTextColor(Color.WHITE);//此处设置坐标点旁边的文字颜色
        data.setValueLabelTextSize(16);
        data.setLines(lines);
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(true);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.parseColor("#FFFFFF"));//灰色
//	    axisX.setName("未来几天的天气");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线
        Axis axisY = new Axis();  //Y轴
        //axisY.setName("温度");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setScrollEnabled(true);
        lineChart.setLineChartData(data);
        lineChart.setValueTouchEnabled(false);
        lineChart.setFocusableInTouchMode(false);
        lineChart.setVisibility(View.VISIBLE);
        lineChart.startDataAnimation();
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right= 7;
        lineChart.setCurrentViewport(v);
    }
    //找控件
    private void initUI() {
        tv0 = (TextView) findViewById(R.id.today_date_tv);
        tv1 = (TextView) findViewById(R.id.today_now_temperature);
        tv2 = (TextView) findViewById(R.id.today_today_temperature);
        tv3 = (TextView) findViewById(R.id.today_today_windlevel);
        tv4 = (TextView) findViewById(R.id.today_today_airhumidity);
        tv5 = (TextView) findViewById(R.id.today_today_updatetime);
        tv6 = (TextView) findViewById(R.id.suggest_clother_tv);
        tv7 = (TextView) findViewById(R.id.suggest_cleancar_tv);
        tv8 = (TextView) findViewById(R.id.suggest_ziwaixian_tv);
        tv9 = (TextView) findViewById(R.id.suggest_travel_tv);
        tv10 = (TextView) findViewById(R.id.suggest_comfortable_tv);
        tv11 = (TextView) findViewById(R.id.suggest_monpractice_tv);
        tv_toolbar = (TextView) findViewById(R.id.mytoolbar_tv);
        iv_mytoolbar = (ImageView) findViewById(R.id.mytoolbar_iv);
        iv_mytoolbar.setOnClickListener(this);
        lv = (ListView) findViewById(R.id.forecast_lv);
        lineChart = (LineChartView) findViewById(R.id.line_chart);
        lv.setFocusable(false);
        sc = (ScrollView) findViewById(R.id.scroll_main);
        sc.smoothScrollTo(0,0);
    }

    //初始化布局
    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_custom);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.share_lyric_activity_shre_btn);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

       NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //侧滑菜单点击事件
        drawer.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }
            //侧滑菜单开启时执行的方法
            @Override
            public void onDrawerOpened(View drawerView) {
                tv_headcity = (TextView) findViewById(R.id.head_addcity_tv);
                tv_headcity.setText(str);
                lv1 = (MyListview) findViewById(R.id.head_addcity_lv);
                List<String> list = manager.select();
                addcityAdapter = new AddcityAdapter(list,MainActivity.this);
                lv1.setAdapter(addcityAdapter);
                iv = (ImageView) findViewById(R.id.head_addcity_iv);
                //侧滑菜单listview点击事件
                lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        str = (String) lv1.getItemAtPosition(position);
                        GetDataUtils.getResult(App.BASE_URL+ str + App.APP_KEY,handler);
                        tv_toolbar.setText(str);
                        drawer.closeDrawers();
                    }
                });
                //侧滑菜单listview长按事件
                lv1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("是否删除");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                manager.delete((String) lv1.getItemAtPosition(position));
                                List<String> data = manager.select();
                                addcityAdapter = new AddcityAdapter(data,MainActivity.this);
                                lv1.setAdapter(addcityAdapter);
                                addcityAdapter.notifyDataSetChanged();
                                dialog.dismiss();

                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                        return true;
                    }
                });
                //侧滑菜单imageview监听事件


                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //跳转至SelectCityActitvity返回的数据
                        startActivityForResult(new Intent(MainActivity.this,SelectCityActitvity.class),0x10);
                        drawer.closeDrawers();
                    }
                });
            }
            @Override
            public void onDrawerClosed(View drawerView) {}
            @Override
            public void onDrawerStateChanged(int newState) {}
        });
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    //获取由跳转至SelectCityActitvity返回的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                Bundle b = data.getExtras(); //data为B中回传的Intent
                str = b.getString("address");//str即为回传的值
                manager.add(str);
                break;
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(str != null){
            GetDataUtils.getResult(App.BASE_URL+ str + App.APP_KEY,handler);
            tv_toolbar.setText(str);
            tv_headcity.setText(str);
        }
    }

    //sp存储，保留最后退出应用时的数据
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = getSharedPreferences("mycity",MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("mycity",str);
        edit.commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mytoolbar_iv:
                //点击分享图标进行共享
                showShare();
                break;
        }
    }
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

}
