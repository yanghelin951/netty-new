package cn.yhl.NIOBufferAndChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

//写入
public class NIOFileChannel03 {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\demo.txt");
        //创建一个输出流 ——>——>—>— Channel
        FileInputStream fileInputStream = new FileInputStream(file);
        //拿到Channel
        FileChannel channel = fileInputStream.getChannel();
        //创建缓存区
        ByteBuffer byteBuffer = ByteBuffer.allocate((int)file.length());
        //将读取到的数据存入缓存区
        channel.read(byteBuffer);
        //输出数据
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
}
