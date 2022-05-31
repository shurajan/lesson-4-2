package com.geekbrains.lesson4_2.processors;


public final class CdCommandProcessor implements CommandProcessor{

    private String path;

    public CdCommandProcessor(String path, String[] command) {
        this.path = command[1];
    }

    @Override
    public byte[] execute() {
        return new byte[0];
    }
}
