public class Test {
    @org.junit.Test
    public void test(){
        System.out.println(123);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
