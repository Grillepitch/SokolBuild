package com.example.alex.freemusic;

import java.util.ArrayList;



public class Result {
    public String result;
    public  Data data;

    public Result() {
    }

    public Result(String result, Data data) {
        this.result = result;
        this.data = data;
    }

    public String getResult() {
        return result;
    }

    public Data getData() {
        return data;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
