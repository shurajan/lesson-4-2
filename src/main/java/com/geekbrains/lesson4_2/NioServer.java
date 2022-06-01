package com.geekbrains.lesson4_2;

import com.geekbrains.lesson4_2.processors.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
                    String command = cd.getCommand()[0]
                            .replaceAll("(\\r|\\n)", "")
                            .trim();
                    if (command.equals("q")) {
                        channel.close();
                        return;
                    } else {
                        byte[] commandOutput = new CommandProcessorFactory()
                                .createProcessor(cd)
                                .execute();
                        channel.write(ByteBuffer.wrap(commandOutput));
                        channel.write(ByteBuffer.wrap("\r->".getBytes(StandardCharsets.UTF_8)));
                        cd.clearCommand();
                    }
                } else {
                    cd.append(symbol);
                }
            }
            buf.clear();
        }
        //

    }


    private void handleAccept() throws IOException {
        SocketChannel channel = server.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        clientsData.put(channel, new ClientDataContainer());
        channel.write(ByteBuffer.wrap("Welcome to a terminal (q to disconnect)!\n\r->".getBytes(StandardCharsets.UTF_8)));
    }
}