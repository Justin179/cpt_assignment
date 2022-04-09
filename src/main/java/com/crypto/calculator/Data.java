package com.crypto.calculator;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Data {
    private long t;

    private double o;

    private double h;

    private double l;

    private double c;

    private BigDecimal v;

    private BigDecimal tpv;




    public BigDecimal getTpv() {
        return tpv;
    }

    public void setTpv(BigDecimal tpv) {
        this.tpv = tpv;
    }

    public double getO() {
        return o;
    }

    public void setO(double o) {
        this.o = o;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getL() {
        return l;
    }

    public void setL(double l) {
        this.l = l;
    }

    public double getC() {
        return c;
    }

    public void setC(double c) {
        this.c = c;
    }

    public long getT() {
        return t;
    }

    public void setT(long t) {
        this.t = t;
    }

    public BigDecimal getV() {
        return v;
    }

    public void setV(BigDecimal v) {
        this.v = v;
    }
}
