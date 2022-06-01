package com.geekbrains.lesson4_2.processors;


import java.nio.charset.StandardCharsets;

final class DefaultCommandProcessor implements CommandProcessor {
    private final String message;

    DefaultCommandProcessor(){
        this.message = "Unrecognized command\n\r->";
    }

    public DefaultCommandProcessor(String message){
        this.message = message;
    }
    @Override
    public byte[] execute() {
        return message.getBytes(StandardCharsets.UTF_8);
    }
}
