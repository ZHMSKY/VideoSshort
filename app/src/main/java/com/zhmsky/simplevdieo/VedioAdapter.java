package com.zhmsky.simplevdieo;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cn.jzvd.JzvdStd;

public class VedioAdapter extends BaseAdapter {
    Context context;
    List<VedioBean.ItemListBean> mDatas;

    public VedioAdapter(Context context, List<VedioBean.ItemListBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if(convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_mainlv,parent,false);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        //获取指定位置的数据源
        VedioBean.ItemListBean.DataBean dataBean=mDatas.get(position).getData();
        VedioBean.ItemListBean.DataBean.AuthorBean authorBean=dataBean.getAuthor();
        holder.name.setText(authorBean.getName());
        holder.desc.setText(authorBean.getDescription());
        String iconUrl=authorBean.getIcon();
        if(!TextUtils.isEmpty(iconUrl)){
            Picasso.with(context).load(iconUrl).into(holder.icons);
        }
        //获取点赞数和评论数
        VedioBean.ItemListBean.DataBean.ConsumptionBean consumptionBean=dataBean.getConsumption();
        holder.herat.setText(consumptionBean.getRealCollectionCount()+"");
        holder.comment.setText(consumptionBean.getReplyCount()+"");
        //设置视频播放器的信息
        holder.jzvdStd.setUp(dataBean.getPlayUrl(),dataBean.getTitle(),JzvdStd.SCREEN_NORMAL);
        String thumbUrl=dataBean.getCover().getFeed();   //缩略图的网络地址
        Picasso.with(context).load(thumbUrl).into(holder.jzvdStd.thumbImageView);
        holder.jzvdStd.positionInList=position;

        return convertView;
    }

    class ViewHolder{
       JzvdStd jzvdStd;
       ImageView icons;
       TextView name,desc,herat,comment;
       public ViewHolder(View view){
           jzvdStd=view.findViewById(R.id.item_main_jzvd);
           icons=view.findViewById(R.id.item_main_iv);
           name=view.findViewById(R.id.item_main_tv_name);
           desc=view.findViewById(R.id.item_main_tv_des);
           herat=view.findViewById(R.id.item_main_tv_heart);
           comment=view.findViewById(R.id.item_main_tv_zan);

       }
    }
}
