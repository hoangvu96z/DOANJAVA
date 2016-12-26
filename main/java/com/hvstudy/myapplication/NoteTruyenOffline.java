package com.hvstudy.myapplication;

/**
 * Created by hoang on 12/25/2016.
 */

public class NoteTruyenOffline
{
    public String username;
    public int idtruyen;
    public int idchuong;
    public String tuadetruyen;
    public String tuadechuong;
    public String noidung;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIdtruyen() {
        return idtruyen;
    }

    public void setIdtruyen(int idtruyen) {
        this.idtruyen = idtruyen;
    }

    public int getIdchuong() {
        return idchuong;
    }

    public void setIdchuong(int idchuong) {
        this.idchuong = idchuong;
    }

    public String getTuadetruyen() {
        return tuadetruyen;
    }

    public void setTuadetruyen(String tuadetruyen) {
        this.tuadetruyen = tuadetruyen;
    }

    public String getTuadechuong() {
        return tuadechuong;
    }

    public void setTuadechuong(String tuadechuong) {
        this.tuadechuong = tuadechuong;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public  NoteTruyenOffline ()
    {

    }

    public NoteTruyenOffline(String username, int idtruyen, int idchuong, String tuadetruyen, String tuadechuong, String noidung) {
        this.username = username;
        this.idtruyen = idtruyen;
        this.idchuong = idchuong;
        this.tuadetruyen = tuadetruyen;
        this.tuadechuong = tuadechuong;
        this.noidung = noidung;
    }



}
