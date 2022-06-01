package com.geekbrains.lesson4_2.processors;

import com.geekbrains.lesson4_2.ClientDataContainer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class LsCommandProcessor implements CommandProcessor{
    private final Path path;

    LsCommandProcessor(ClientDataContainer clientDataContainer) {
        this.path = Paths.get(clientDataContainer.getCurrentFolder());
    }

    @Override
    public byte[] execute() {

        if (!Files.exists(path)) return "no such folder\n".getBytes(StandardCharsets.UTF_8);
        if (!Files.isDirectory(path)) return "can not ls files\n".getBytes(StandardCharsets.UTF_8);

        StringBuilder outputMessage = new StringBuilder();
        outputMessage.append(path.toString());
        outputMessage.append("\r\n");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path file : stream) {
                outputMessage.append("    ");
                outputMessage.append(file.getFileName());
                outputMessage.append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputMessage.toString().getBytes(StandardCharsets.UTF_8);
    }
}
