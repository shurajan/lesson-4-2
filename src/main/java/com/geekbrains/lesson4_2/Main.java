package com.geekbrains.lesson4_2;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.start();
    }
}
