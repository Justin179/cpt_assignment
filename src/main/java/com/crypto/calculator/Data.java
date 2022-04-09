package com.crypto.calculator;

import java.math.BigDecimal;

public class Data {
    private long t; // timestamp

    private double o; // open

    private double h; // high

    private double l; // low

    private double c; // close

    private double v; // volume

    private BigDecimal tpv; // could save tpv, but currently unused

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

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }
}
