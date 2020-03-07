public class Government {
    private int treasury = 5;

    private void spend() {
        treasury -= 1;
    }

    private void tax() {
        treasury += 1;
    }

    public void report() {
        System.out.println(treasury);
    }

    public static Government greaterTreasury(Government a, Government b) {
        if (a.treasury > b.treasury) {
            return a;
        }
        return b;
    }

    public static class Peasant {
        public void doStuff() {
            System.out.println("hello");
        }
    }

    public class King {
        public void doStuff() {
            spend();
        }
    }

    public class Mayor {
        public void doStuff() {
            tax();
        }
    }

    public class Accountant {
        public void doStuff() {
            report();
        }
    }

    public class Thief {
        public void doStuff() {
            treasury = 0;
        }
    }

    public class Explorer {
        public void doStuff(Government a, Government b) {
            Government favorite = Government.greaterTreasury(a, b);
            System.out.println("The best government has treasury " + favorite.treasury);
        }
    }
}