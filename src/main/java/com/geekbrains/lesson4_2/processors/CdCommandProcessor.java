package com.geekbrains.lesson4_2.processors;


import com.geekbrains.lesson4_2.ClientDataContainer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

final class CdCommandProcessor implements CommandProcessor {
    private final ClientDataContainer clientDataContainer;
    private final Path firstPath, secondPath;

    CdCommandProcessor(ClientDataContainer clientDataContainer) {
        this.clientDataContainer = clientDataContainer;
        //We try to move to subfolder in current folder
        String newFolder = clientDataContainer.getCommand()[1];
        if (newFolder.equals("..")) {
            Path currentPath = Paths.get(clientDataContainer.getCurrentFolder());
            this.firstPath = currentPath.getParent() == null ? currentPath : currentPath.getParent();
        } else {
            this.firstPath = Paths.get(clientDataContainer.getCurrentFolder(), newFolder);
        }
        //We may check whether we got absolute path
        this.secondPath = Paths.get(newFolder);
    }

    @Override
    public byte[] execute() {
        if (Files.exists(firstPath) && Files.isDirectory(firstPath)) {
            clientDataContainer.setCurrentFolder(firstPath.toString());
            return ("switched to " + firstPath + "\n").getBytes(StandardCharsets.UTF_8);
        } else if (Files.exists(secondPath) && Files.isDirectory(secondPath)) {
            clientDataContainer.setCurrentFolder(secondPath.toString());
            return ("switched to " + secondPath + "\n").getBytes(StandardCharsets.UTF_8);
        }

        return "can not find dir\n".getBytes(StandardCharsets.UTF_8);

    }
}
