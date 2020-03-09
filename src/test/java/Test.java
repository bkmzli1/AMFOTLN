import ru.bkmz.Main;

import java.io.IOException;

public class Test {
    @org.junit.Test
    public void test(){
        try {
            Main.main(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
