package com.example.pinalmeruliya.myapplication.helper;



import com.example.pinalmeruliya.myapplication.helper.DataType;

import java.util.List;



public class Jclass {
    //    public List<DataType> predictions;
    public String status;


    public List<DataType> predictions;

    public List<DataType> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<DataType> predictions) {
        this.predictions = predictions;
    }
    //    public List<DataType> getPredictions() {
//        return predictions;
//    }

//    public void setPredictions(List<DataType> predictions) {
//        this.predictions = predictions;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}