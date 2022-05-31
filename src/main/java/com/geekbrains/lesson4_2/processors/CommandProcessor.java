package com.geekbrains.lesson4_2.processors;

sealed public interface CommandProcessor
    permits CatCommandProcessor, CdCommandProcessor, LsCommandProcessor, DefaultCommandProcessor {
    byte[] execute();
}
