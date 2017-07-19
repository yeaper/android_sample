package com.xh.sun.ui.test.data;

public class Test {

    String test_image;
    String test_type;

    public Test(){

    }

    public Test(String image, String type){
        this.test_image = image;
        this.test_type = type;
    }

    public String getTest_image() {
        return test_image;
    }

    public void setTest_image(String test_image) {
        this.test_image = test_image;
    }

    public String getTest_type() {
        return test_type;
    }

    public void setTest_type(String test_type) {
        this.test_type = test_type;
    }
}
