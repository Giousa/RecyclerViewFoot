package com.zmm.recyclerviewfoot;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xdandroid.simplerecyclerview.Adapter;

import java.util.List;

public abstract class BasicAdapter extends Adapter {

    List<SampleBean> mSampleList;

    public void setList(List<SampleBean> sampleList) {
        mSampleList = sampleList;
        notifyDataSetChanged();
    }

    private OnShortItemClickListener mOnShortItemClickListener;

    public interface OnShortItemClickListener{
        void OnShortItemClick(String title,String content,int position);
    }

    public void setOnShortItemClickListener(OnShortItemClickListener onShortItemClickListener) {
        mOnShortItemClickListener = onShortItemClickListener;
    }

    @Override
    protected RecyclerView.ViewHolder onViewHolderCreate(ViewGroup parent, int viewType) {
        if (viewType == SampleBean.TYPE_BANNER) {
            return new BannerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner, parent, false));
        } else {
            return new TextVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text, parent, false));
        }
    }

    @Override
    protected void onViewHolderBind(RecyclerView.ViewHolder holder, final int position, int viewType) {
        if (viewType == SampleBean.TYPE_BANNER) {
            BannerVH bannerVH = (BannerVH) holder;
            bannerVH.image.setImageResource(mSampleList.get(position).imageResId);

        } else {
            TextVH textVH = (TextVH) holder;
            textVH.title.setText(mSampleList.get(position).title);
            textVH.content.setText(mSampleList.get(position).content);
            textVH.ll_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnShortItemClickListener != null){
                        mOnShortItemClickListener.OnShortItemClick(mSampleList.get(position).title,mSampleList.get(position).content,position);
                    }
                }
            });
        }
    }

    @Override
    protected int getViewType(int position) {
        return mSampleList.get(position).type;
    }

    @Override
    protected int getCount() {
        return mSampleList != null ? mSampleList.size() : 0;
    }

    static final class TextVH extends RecyclerView.ViewHolder {

        LinearLayout ll_item;
        TextView title, content;

        TextVH(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.tv_title);
            content = (TextView) v.findViewById(R.id.tv_content);
            ll_item = (LinearLayout) v.findViewById(R.id.ll_item);
        }

    }

    static final class BannerVH extends RecyclerView.ViewHolder {
        BannerVH(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.iv_image);
        }
        ImageView image;
    }

    @Override
    protected int getItemSpanSizeForGrid(int position, int viewType, int spanSize) {
        return 1;
    }
}
