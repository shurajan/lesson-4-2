package com.geekbrains.lesson4_2.processors;

import com.geekbrains.lesson4_2.ClientDataContainer;

public class CommandProcessorFactory {

    public CommandProcessor createProcessor(ClientDataContainer clientDataContainer) {
        String[] commandInfo = clientDataContainer.getCommand();

        //пропускаем пустую команду
        if (commandInfo[0].equals("")) return new DefaultCommandProcessor("");

        return switch (commandInfo[0]) {
            case "ls" -> {
                if (commandInfo.length > 1) yield new DefaultCommandProcessor("ls has no parameters\n");
                yield new LsCommandProcessor(clientDataContainer);
            }
            case "cat" -> {
                if (commandInfo.length != 2) yield new DefaultCommandProcessor("cat <file_name_in_current_folder>\n");
                yield new CatCommandProcessor(clientDataContainer);
            }
            case "cd" -> {
                if (commandInfo.length != 2) yield new DefaultCommandProcessor("cd <path>\n");
                yield new CdCommandProcessor(clientDataContainer);
            }
            default -> new DefaultCommandProcessor();
        };
    }
}