package com.geekbrains.lesson4_2.processors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class LsCommandProcessor implements CommandProcessor{
    private final String path;

    public LsCommandProcessor(String path) {
        this.path = path;
    }

    @Override
    public byte[] execute() {
        Path dir = Paths.get(path);

        StringBuilder outputMessage = new StringBuilder();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                outputMessage.append(file.getFileName());
                outputMessage.append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputMessage.toString().getBytes(StandardCharsets.UTF_8);
    }
}
