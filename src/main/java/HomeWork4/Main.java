package HomeWork4;

public class Main {
    private static final Object monitor = new Object();
    static int repeatCount = 5;
    static volatile char buffChar = 'A';

    public static void main(String[] args) {

        new OrdinalPrinting('A', 'B').start();
        new OrdinalPrinting('B', 'C').start();
        new OrdinalPrinting('C', 'A').start();

    }


    private static class OrdinalPrinting extends Thread {
        private char prevousСhar;
        private char nextСhar;

        public OrdinalPrinting(char prevousСhar, char nextСhar) {
            this.prevousСhar = prevousСhar;
            this.nextСhar = nextСhar;
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < repeatCount; i++) {
                    synchronized (monitor) {
                        while (buffChar != prevousСhar) {
                            monitor.wait();
                        }
                        System.out.print(prevousСhar);
                        buffChar = nextСhar;
                        monitor.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
