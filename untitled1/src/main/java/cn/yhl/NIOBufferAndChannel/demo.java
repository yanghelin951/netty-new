package cn.yhl.NIOBufferAndChannel;

import java.nio.ByteBuffer;
/*
* Buffer 存放数据测试
* */
public class demo {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(66);
        byteBuffer.putInt(1);//int
        byteBuffer.putLong(9);//Long
        byteBuffer.putChar('五');//char

        //取数据
        byteBuffer.flip();
        //按照FIFI先进先出
        //先取int  -  Long    - char
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
    }
}
