package com.geekbrains.lesson4_2.processors;

import com.geekbrains.lesson4_2.ClientDataContainer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class CatCommandProcessor implements CommandProcessor{

    private final Path path;

    CatCommandProcessor(ClientDataContainer clientDataContainer) {
        this.path = Paths.get(clientDataContainer.getCurrentFolder(), clientDataContainer.getCommand()[1]);
    }

    @Override
    public byte[] execute() {

        if (!Files.exists(path)) return "no such file\n".getBytes(StandardCharsets.UTF_8);
        if (Files.isDirectory(path)) return "can not cat dirs\n".getBytes(StandardCharsets.UTF_8);

        byte[] content = new byte[0];
        try {
            content = Files.readAllBytes(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
