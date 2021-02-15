import static com.sun.xml.internal.fastinfoset.util.ValueArray.MAXIMUM_CAPACITY;

/**
 * Author: Tang Yuqian
 * Date: 2021/2/3
 */

public class TableSizeForTest {


    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1; //
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static void main(String[] args) {
        // tableSizeFor(54);

        System.out.println(new String("aadfsgafsgadghiuhihpuhpoihinjasdnvoiajsovijagsognvaovnaosunbvoaurnvoabvouahfkjbn zkjxnvjdfnvoajnvoafnbvoanfbovanbovaobinvaobnaoejn").getBytes().length);
    }
}
