package sample;

public class test {
    public String name;
    public static void main(String[] args) {
        //testing();
        testtest a =new testtest();
        a.name="wa";
    }


    public static void testing(){
        System.out.println("static class");
        //testing2();
    }

    public void testing2(){
        System.out.println("instance");
        testing();
    }
}
