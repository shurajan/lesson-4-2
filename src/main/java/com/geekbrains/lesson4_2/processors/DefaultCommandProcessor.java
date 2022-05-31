package com.geekbrains.lesson4_2.processors;


import java.nio.charset.StandardCharsets;

public final class DefaultCommandProcessor implements CommandProcessor {
    @Override
    public byte[] execute() {
        return "Unrecognized command\n\r->".getBytes(StandardCharsets.UTF_8);
    }
}
