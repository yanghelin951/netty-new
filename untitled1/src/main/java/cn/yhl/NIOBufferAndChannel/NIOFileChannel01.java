package cn.yhl.NIOBufferAndChannel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
//写入
public class NIOFileChannel01 {
    public static void main(String[] args) throws IOException {
        String str = "hello 111world";
        //创建一个输出流 ——>——>—>— Channel
        FileOutputStream fileOutputStream = new FileOutputStream("D:\\demo.txt");
        //通过输出流得到FileChnnel
        FileChannel channel = fileOutputStream.getChannel();
        //创建一个缓冲区
        ByteBuffer allocate = ByteBuffer.allocate(1024);
        //将Str放入buf
        allocate.put(str.getBytes());
        //对buf进行反转 开始写了
        allocate.flip();
        //将buf的数据写入通道Channel
        channel.write(allocate);
        //关闭流
        fileOutputStream.close();
    }
}
