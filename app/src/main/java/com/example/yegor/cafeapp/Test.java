package com.example.yegor.cafeapp;

import com.example.yegor.cafeapp.models.YmlCatalogModel;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;

public class Test {

    public static void main(String[] args) throws Exception {

        Serializer serializer = new Persister();
        File source = new File("/home/yegor/Видео/sss.xml");
        YmlCatalogModel example = serializer.read(YmlCatalogModel.class, source);

        System.out.println(example);

    }

}
