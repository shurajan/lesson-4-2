package com.geekbrains.lesson4_2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class NioServer {

    private ServerSocketChannel server;
    private Selector selector;
    private Map<SocketChannel, ClientDataContainer> clientsData;

    public NioServer() throws IOException {
        server = ServerSocketChannel.open();
        selector = Selector.open();
        server.bind(new InetSocketAddress(8189));
        server.configureBlocking(false);
        server.register(selector, SelectionKey.OP_ACCEPT);
        clientsData = new HashMap<>();
    }

    public void start() throws IOException {
        while (server.isOpen()) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    handleAccept();
                }
                if (key.isReadable()) {
                    handleRead(key);
                }
                iterator.remove();
            }

        }
    }

    private void handleRead(SelectionKey key) throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        SocketChannel channel = (SocketChannel) key.channel();
        ClientDataContainer cd = clientsData.get(channel);

        while (channel.isOpen()) {
            int read = channel.read(buf);
            if (read < 0) {
                channel.close();
                return;
            }
            if (read == 0) {
                break;
            }
            buf.flip();
            while (buf.hasRemaining()) {
                char symbol = (char) buf.get();
                if (symbol == '\n') {
                    String command = cd.getCommand().toString()
                            .replaceAll("(\\r|\\n)", "")
                            .trim();

                    if (command.equals("q")) {
                        channel.close();
                        return;
                    } else {
                        byte[] commandOutput = processCommamd(cd);
                        channel.write(ByteBuffer.wrap(commandOutput));
                        cd.setCommand(new StringBuilder());
                        //cmdLineBuffers.put(channel, new StringBuilder());
                    }
                } else {
                    cd.getCommand().append(symbol);
                }
            }
            buf.clear();
        }
        //

    }

    private byte[] processCommamd(ClientDataContainer cd) throws IOException {
        StringBuilder outputMessage = new StringBuilder();

        String command = cd.getCommand()
                .toString().replaceAll("(\\r|\\n)", "")
                .trim();
        ;
        String[] commandInfo = command.split(" ");

        String typeOfDay;
        switch (commandInfo[0]) {
            case "ls":
                Path dir = Paths.get(cd.getCurrentFolder());
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
                    for (Path file : stream) {
                        outputMessage.append(file.getFileName());
                        outputMessage.append("\r\n");
                    }
                }
                break;
            case "cat":

                //byte[] content = Files.re(Paths.get(cd.getCurrentFolder()+ "\\" +commandInfo[1]));
                //outputMessage.append(content.toString());

                break;
            case "cd":
                outputMessage.append("cd command");
                break;
            case "\n":
                break;
            default: {
                outputMessage.append("Unknown command - \"");
                outputMessage.append(command);
                outputMessage.append("\"");
                break;
            }
        }

        outputMessage.append("\n\r->");
        return outputMessage.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void handleAccept() throws IOException {
        SocketChannel channel = server.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        clientsData.put(channel, new ClientDataContainer());
        channel.write(ByteBuffer.wrap("Welcome to a terminal (q to disconnect)!\n\r->".getBytes(StandardCharsets.UTF_8)));
    }
}