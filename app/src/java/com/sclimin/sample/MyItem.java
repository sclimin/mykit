package com.sclimin.sample;

import android.widget.TextView;

import com.sclimin.mykit.app.Adapter;
import com.sclimin.mykit.app.Item;

/**
 * 作者：limin
 * <p>
 * 创建时间：2018/08/14
 */
public class MyItem extends Item {

    @Override
    protected int getItemLayout() {
        return R.layout.item_text;
    }

    @Override
    protected void onBind(Adapter adapter, Helper helper, int position) {
        TextView tv = helper.findViewById(R.id.text);
        tv.setText(String.valueOf(hashCode()));
    }
}
