package com.quicktutorialz.nio;

import com.mawashi.nio.jetty.ReactiveJ;

public class MainApplication {
    public static void main(String[] args) throws Exception {
        //we will start our Jetty embedded through our ReactiveJ library
        new ReactiveJ().port(8383)
                       .endpoints(new com.quicktutorialz.nio.endpoints.v1.ToDoEndpoints())
                       .endpoints(new com.quicktutorialz.nio.endpoints.v2.ToDoEndpoints())
                       .start();
    }
}
