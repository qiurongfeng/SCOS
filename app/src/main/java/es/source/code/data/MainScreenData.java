package es.source.code.data;

/**
 * es.source.code.data
 * SCOS
 * funtcion:
 * remark:
 * 2017/9/30 By Sherlock.
 */

public class MainScreenData {
    private int res;
    private String name;

    public MainScreenData(int res, String name) {
        this.res = res;
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
