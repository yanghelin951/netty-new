package cn.yhl.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NIOClinet {
    //创建SocketChannel连接到服务器
    private SocketChannel socketChannel = null;
    private Selector selector;

    public NIOClinet() {
        try {
            selector= Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 6667));
            //设置非阻塞
            socketChannel.configureBlocking(false);
            //注册到Selector中
            socketChannel.register(selector, SelectionKey.OP_READ);
            Set<SelectionKey> keys = selector.keys();
            keys.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //向服务器发生消息
    public void wirteServerMsg(String str) throws IOException {
        str=socketChannel.getLocalAddress().toString().substring(1)+":"+str;
        //创建Buffer
        ByteBuffer wrap = ByteBuffer.wrap(str.getBytes());
        //写入Channel
        socketChannel.write(wrap);
    }

    //接收服务器消息
    public void readServerMsg() throws IOException {
        try {
            int select = selector.select(1000);
            if (select > 0) {
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        SocketChannel channel = (SocketChannel) selectionKey.channel();
                        ByteBuffer allocate = ByteBuffer.allocate(1024);
                        int read = channel.read(allocate);
                        String msg = new String(allocate.array());
                        System.out.println(msg);
                    }
                }
                iterator.remove();
            }
        }catch (Exception e){
            System.out.println("没有可用的通道！！");
        }

    }

    public static void main(String[] args) throws IOException {
        NIOClinet nioClinet = new NIOClinet();
        //接收数据
        new Thread(){
            public  void run(){
                while(true){
                    try {
                        nioClinet.readServerMsg();
                        Thread.currentThread().sleep(1000);
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
        //发送数据
        Scanner scanner = new Scanner(System.in);
        
        while(scanner.hasNextLine()){
            String s = scanner.nextLine();
            nioClinet.wirteServerMsg(s);
        }
    }
}
