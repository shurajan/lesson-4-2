package com.geekbrains.lesson4_2.processors;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class CatCommandProcessor implements CommandProcessor{

    private final String path;

    public CatCommandProcessor(String path, String[] command) {
        this.path = path + "\\" + command[1];
    }

    @Override
    public byte[] execute() {
        byte[] content = new byte[0];
        try {
            content = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
