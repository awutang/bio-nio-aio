import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

/**
 * Author: Tang Yuqian
 * Date: 2021/2/10
 */

public class ByteOrderTest {

    public static void main(String[] args) {
        // 大端顺序
        // ByteBuffer byteBuffer = ByteBuffer.allocate(7).order(ByteOrder.BIG_ENDIAN);
        // 小端顺序
        ByteBuffer byteBuffer = ByteBuffer.allocate(7).order(ByteOrder.LITTLE_ENDIAN);
        CharBuffer charBuffer = byteBuffer.asCharBuffer();
        byteBuffer.put(0, (byte) 'A');
        byteBuffer.put(1, (byte) 'A');
        byteBuffer.put(2, (byte) 0);
        byteBuffer.put(3, (byte) 'i');
        byteBuffer.put(4, (byte) 0);
        byteBuffer.put(5, (byte) '!');
        byteBuffer.put(6, (byte) 0);
        /**大端顺序（从左往右存、从右往左读）：65 65 0 105 0 33 0 -> 01000001 01000001 00000000 01101001 00000000 00100001 00000000
         *          䅁(01000001 01000001->\u4141)i(00000000 01101001->\u0069)!(00000000 00100001->\u0021)
         * 小端顺序(从右往左存、从右往左读)：10000010 10000010 00000000 10010110 00000000 10000100 00000000
         *          䅁(10000010 10000010->\u4141)椀(00000000 10010110->\u6900)℀(00000000 10000100->\u2100)
         * 因为存储和读取的顺序一致，所以字节顺序对于一个字节的内部比特位顺序并没有影响;但字节顺序会影响字节间的顺序，会对视图映射等操作后读取的结果带来影响
         * */

        System.out.println(charBuffer);
    }
}
