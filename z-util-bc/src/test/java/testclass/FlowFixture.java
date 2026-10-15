package testclass;

/**
 * 控制流夹具：if/else、while、do-while、for、switch 五种结构各一段，
 * 供 {@link com.zifang.util.bc.bytecode.decompile.flow} 的结构化测试使用。
 */
public class FlowFixture {

    private int counter;
    private String label = "";

    public int ifElse(int value) {
        if (value > 0) {
            return value * 2;
        } else {
            return -value;
        }
    }

    public int whileLoop(int limit) {
        int sum = 0;
        int i = 0;
        while (i < limit) {
            sum = sum + i;
            i = i + 1;
        }
        return sum;
    }

    public int doWhile(int limit) {
        int i = 0;
        do {
            i = i + 1;
        } while (i < limit);
        return i;
    }

    public void forLoop(int limit) {
        for (int i = 0; i < limit; i++) {
            this.counter = this.counter + i;
        }
    }

    public String tableSwitch(int value) {
        switch (value) {
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            default:
                return "many";
        }
    }

    public String lookupSwitch(int value) {
        switch (value) {
            case 100:
                return "hundred";
            case 1000:
                return "thousand";
            default:
                return "other";
        }
    }

    public int fieldAndCall(int value) {
        this.counter = value + 1;
        this.label = String.valueOf(this.counter);
        return this.counter;
    }
}
