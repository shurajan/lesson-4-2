package com.geekbrains.lesson4_2;

public class ClientDataContainer {
    private StringBuilder command;
    private String currentFolder;

    ClientDataContainer() {
        this.command = new StringBuilder();
        this.currentFolder = System.getProperty("user.home");
    }

    public StringBuilder getCommand() {
        return command;
    }

    public void setCommand(StringBuilder command) {
        this.command = command;
    }

    public String getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(String currentFolder) {
        this.currentFolder = currentFolder;
    }
}
