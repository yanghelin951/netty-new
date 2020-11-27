package cn.yhl.chat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class server {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private final int PORT = 6667;

    public server() {
        //初始化
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //监听
    public void listen() {
        while (true) {
            try {
                int count = selector.select(1000);
                //System.out.println("count:"+count);
                if (count > 0) {//有事件
                    //获取sleectionKey
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    //得到SelectionKey 遍历
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();
                        //注册事件
                        if (selectionKey.isAcceptable()) {
                            System.out.println("1111");
                            //创建SocketChannel
                            SocketChannel socketChannel = listenChannel.accept();
                            System.out.println(socketChannel.hashCode());
                            //设置无阻塞连接
                            socketChannel.configureBlocking(false);
                            //将通道注册在selector中
                            socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                            String msg =  + socketChannel.hashCode()+"以上线";
                            System.out.println(msg);
                            asd(socketChannel,msg);
                        }
                        //write  服务器向客户端发送消息
                        if (selectionKey.isWritable()) {

                        }
                        //read 客户端想服务器发送消息
                        if (selectionKey.isReadable()) {
                            //拿到客户端通道 接收消息
                            SocketChannel channel = null;
                            try {
                                channel = (SocketChannel) selectionKey.channel();
                                ByteBuffer allocate = ByteBuffer.allocate(1024);
                                int read = channel.read(allocate);
                                //根据read来判断用户是否断开连接
                                if(read>0){
                                    //读取到消息 并转发到其他客户端
                                    System.out.println(new String(allocate.array()));
                                    asd(channel,new String(allocate.array()));
                                }
                            } catch (Exception e) {
                                System.out.println(channel.getRemoteAddress()+"离线！！！！");
                                //取消注册 关闭通道
                                selectionKey.cancel();
                                channel.close();
                            }

                        }
                        //操作完后删除key 以防重复操作
                        iterator.remove();

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    //向客户端发消息
    public void asd(SocketChannel socketChannel, String msg) {
        Set<SelectionKey> keys = selector.keys();// selector.keys(); 拿到注册到selector中的所有组件
        Iterator<SelectionKey> iterator1 = keys.iterator();
        while (iterator1.hasNext()) {
            SelectionKey next = iterator1.next();
            try {
                if (next.channel() instanceof SocketChannel && !next.channel().equals(socketChannel)) {
                    SocketChannel channel = (SocketChannel) next.channel();
                    ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                    channel.write(wrap);
                }
            } catch (Exception e) {
                System.out.println("向客户端发消息失败");
            }
        }
    }

    public static void main(String[] args) {
        server server = new server();
        server.listen();

    }
}
