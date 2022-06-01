package com.geekbrains.lesson4_2;

public class ClientDataContainer {
    private StringBuilder command;
    private String currentFolder;

    ClientDataContainer() {
        this.command = new StringBuilder();
        this.currentFolder = System.getProperty("user.home");
    }

    public String[] getCommand() {
        String command = this.command
                .toString()
                .replaceAll("(\\r|\\n)", "")
                .replaceAll("\\s+"," ")
                .trim();

       return command.split(" ");
    }

    public void clearCommand() {
        this.command = new StringBuilder();
    }

    public String getCurrentFolder() {
        return currentFolder;
    }

    public void setCurrentFolder(String currentFolder) {
        this.currentFolder = currentFolder;
    }

    public void append(char symbol) {
        this.command.append(symbol);
    }
}
