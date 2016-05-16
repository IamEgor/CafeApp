package com.example.yegor.cafeapp.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root
public class Param {

    @Attribute
    private String name;
    @Text
    private String text;

    public Param() {
    }

    public Param(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "Param{" +
                "name='" + name + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}