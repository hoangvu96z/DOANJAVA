package com.hvstudy.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

import static java.lang.Float.valueOf;

/**
 * Created by hoang on 12/2/2016.
 */

public class AdapterTruyen extends BaseAdapter {
    Context MyContext;
    int MyLayout;
    List<Tuadetruyen> MyArray;
    public AdapterTruyen (Context c, int l, List<Tuadetruyen> a)
    {
        MyContext = c;
        MyLayout = l;
        MyArray = a;
    }

    @Override
    public int getCount() {
        return MyArray.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) MyContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(MyLayout,null);
        TextView td,id,dg;
        RatingBar rb;
        final ImageView im;
        rb = (RatingBar) view.findViewById(R.id.ratingBar);
        im = (ImageView) view.findViewById(R.id.imgAvatar);
        td = (TextView) view.findViewById(R.id.txtTittle);
        id = (TextView) view.findViewById(R.id.txtID);

        td.setText(MyArray.get(i).tentruyen);
        id.setText("Mã truyện: "+String.valueOf(MyArray.get(i).id));
        rb.setRating(Math.round(valueOf(MyArray.get(i).danhgia)*10)/10);
        Picasso.with(MyContext).load(MyArray.get(i).URLhinh).into(im);
        return view;
    }

}
