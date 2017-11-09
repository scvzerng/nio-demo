package com.zero.selector;

import com.zero.nio.server.Worker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class NioServer extends Thread {

    public static Map<SocketChannel, StringBuilder> channelData = new ConcurrentHashMap<>();

    public NioServer() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        new NioServer().start();
    }

    Selector selector = Selector.open();

    private void initServer(Consumer<Set<SelectionKey>> events) {
        try {
            ServerSocketChannel serverSocket = ServerSocketChannel.open();
            serverSocket.bind(new InetSocketAddress(9999));
            serverSocket.configureBlocking(false);
            serverSocket.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                if (selector.select() == 0) {
                    continue;
                }
                events.accept(selector.selectedKeys());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        initServer(events -> {
            Iterator<SelectionKey> keyIterable = events.iterator();

            while (keyIterable.hasNext()) {
                SelectionKey selectionKey = keyIterable.next();
                keyIterable.remove();
                if (selectionKey.isValid()) {
                    try {

                        if (selectionKey.isAcceptable()) {
                            handlerConnect(selectionKey);

                        }

                        if (selectionKey.isReadable()) {
                            handlerReader(selectionKey);

                        }
                    } catch (IOException e) {
                        //客户端关闭强制关闭是通过读取数据时抛出异常来表示
                        close((SocketChannel) selectionKey.channel(), 1);
                    }
                }

            }

        });

    }

    /**
     * 处理读取事件
     *
     * @param event
     * @throws IOException
     */
    private void handlerReader(SelectionKey event) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(64);
        SocketChannel channel = (SocketChannel) event.channel();
        StringBuilder result = getOrDefault(channel);
        int count;
        while ((count = channel.read(buffer)) > 0) {
            buffer.flip();
            byte[] contents = new byte[buffer.limit()];
            buffer.get(contents, 0, buffer.limit());
            result.append(new String(contents));
            buffer.clear();
            System.out.println("receiving:" + result.toString());
        }
        if (count == -1) {
            //数据读取完毕  客户端正常退出
            close(channel, count);
        }


    }

    private void close(SocketChannel channel, int state) {
        try {
            channel.close();
            System.out.println((state > 0 ? "客户端强制断开" : "") + "all received:" + channelData.get(channel));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取通道结果存储介质
     *
     * @param channel
     * @return
     */
    private StringBuilder getOrDefault(SocketChannel channel) {
        StringBuilder builder = channelData.get(channel);
        if (builder == null) {
            synchronized (this) {
                builder = new StringBuilder();
                channelData.put(channel, builder);
                builder = channelData.get(channel);
            }
        }
        return builder;
    }

    /**
     * 处理连接事件
     *
     * @param key
     * @throws IOException
     */
    private void handlerConnect(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel client = channel.accept();
        if (client != null) {
            client.configureBlocking(false);
            client.register(selector, SelectionKey.OP_READ);
        }
    }


}
