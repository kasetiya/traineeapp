package com.softices.traineeapp.constants;

public interface UrlEndPoints {
    String pointUrl = "http://192.168.0.113:3001"; // mamta's server
//    String pointUrl = "https://vishwas.group"; // production server

    String baseUrl = "/api/v1/";
    String parentUrl = pointUrl + baseUrl;

    String urlMaster = parentUrl + "master.json";
}