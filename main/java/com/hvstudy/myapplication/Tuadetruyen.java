package com.hvstudy.myapplication;

/**
 * Created by hoang on 12/2/2016.
 */

public class Tuadetruyen {
   public String tentruyen;
    public int id;
    public float danhgia;
    public String URLhinh;
    public Tuadetruyen (int i, String t, float d, String u)
    {
        this.id = i;
        this.tentruyen = t;
        this.danhgia = d;
        this.URLhinh = u;
    }
    public String getTentruyen ()
    {
        return this.tentruyen;
    }
}
