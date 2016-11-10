package edu.dbortnichuk.java.cert;

/**
 * Created by dbort on 04.11.2016.
 */
public class CH1 {

    public static void main(String[] args) {




    }

    public enum Season{
        WINTER(1, "it's cold"){
            @Override
            public void print() {
                System.out.println("brrr");
            }
            public void print1() {
                System.out.println("brrr1");
            }

        }, SUMMER(2, "time to swim"){
            @Override
            public void print() {
                System.out.println("aggghhh");
            }
        };

        private long id;
        private String description;

        public abstract void print();


        private Season(long id, String description){
            this.id = id;
            this.description = description;
        }
    }
}

class Cl1 {

    private String name = "name";

    public String getName() {
        return name;
    }

}

class Cl2 extends Cl1{

    @Override
    public String getName(){
        return "";
    }


    public static void main(String[] args) {
        Cl2 cl2 = new Cl2();
        System.out.println(cl2.getName());
    }

}


