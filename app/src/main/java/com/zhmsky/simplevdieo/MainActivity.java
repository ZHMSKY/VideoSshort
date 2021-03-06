package com.zhmsky.simplevdieo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Adapter;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JzvdStd;

public class MainActivity extends AppCompatActivity {
    ListView mainLv;
    String url="http://baobab.kaiyanapp.com/api/v4/tabs/selected?udid=11111&vc=168&vn=3.3.1&deviceModel=Huawei6&first_channel=eyepetizer_baidu_market&last_channel=eyepetizer_baidu_market&system_version_code=20";
    List<VedioBean.ItemListBean> mDatas;
    private  VedioAdapter adapter;

    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what==1){
                String json=(String)msg.obj;
                //解析数据
                VedioBean vedioBean=new Gson().fromJson(json,VedioBean.class);
                //过滤不需要的数据
                List<VedioBean.ItemListBean> itemList=vedioBean.getItemList();
                for (int i = 0; i <itemList.size() ; i++) {
                    VedioBean.ItemListBean listBean=itemList.get(i);
                    if(listBean.getType().equals("video")){
                        mDatas.add(listBean);
                    }
                }
                        //提示适配器更新数据
                adapter.notifyDataSetChanged();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("开眼API提供");
        mainLv=findViewById(R.id.main_lv);
        //数据源
        mDatas=new ArrayList<>();
        //创建适配器对象
        adapter=new VedioAdapter(this,mDatas);
        //设置适配器
        mainLv.setAdapter(adapter);
        //加载网络数据
        loadData();
    }

    private void loadData() {
       //创建新的线程，完成数据的获取
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonContent=HttpUtils.getJsonContent(url);
                //子线程不能更新ui，需要通过handler发送数据回到主线程
                Message message=new Message();
                message.what=1;
                message.obj=jsonContent;
                handler.sendMessage(message);
            }
        }).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        JzvdStd.releaseAllVideos();   //释放正在被播放的视频信息
    }
}
