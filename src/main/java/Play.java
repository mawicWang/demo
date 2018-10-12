import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;

public class Play {


    public static void main(String[] args) {
        Fruits fruits = new Fruits();
        CThread c = new CThread(fruits);
        c.start();

        PThread p1 = new PThread(1, fruits);
        PThread p2 = new PThread(2, fruits);
        PThread p3 = new PThread(3, fruits);
        PThread p4 = new PThread(4, fruits);
        PThread p5 = new PThread(5, fruits);
        PThread p6 = new PThread(6, fruits);
        PThread p7 = new PThread(7, fruits);
        PThread p8 = new PThread(8, fruits);
        PThread p9 = new PThread(9, fruits);

        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
        p6.start();
        p7.start();
        p8.start();
        p9.start();

        RTread r = new RTread(fruits, p1, p2, p3, p4, p5, p6, p7, p8, p9);
        r.start();

    }
}

class Pos {
    private int x;
    private int y;

    public Pos() {
        this.x = 0;
        this.y = 0;
    }

    public Pos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Pos{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pos pos = (Pos) o;
        return x == pos.x &&
                y == pos.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

class Fruits extends ConcurrentLinkedQueue<Pos> {

    private Set<Pos> all = new HashSet<>();

    public Pos get() {
        Pos p = super.poll();
        if (p != null) {
            synchronized (this) {
                all.remove(p);
            }
        }
        return p;
    }

    public void put(Pos p) {
        synchronized (this) {
            super.add(p);
            all.add(p);
        }
    }

    public Set<Pos> getAll() {
        return all;
    }
}

class RTread extends Thread {
    private PThread[] pThreads;
    private Fruits fruits;

    public RTread(Fruits fruits, PThread... pThreads) {
        this.fruits = fruits;
        this.pThreads = pThreads;
    }

    @Override
    public void run() {
        while (true) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 20; ++i) {
                for (int j = 0; j < 20; ++j) {
                    int id = isP(i, j);
                    if (id > 0) {
                        sb.append(" " + id);
                    } else {
                        if (isF(i, j)) {
                            sb.append(" o");
                        } else {
                            sb.append(" .");
                        }
                    }

                }
                sb.append("\n");
            }
            try {
                sb.append("\n");
                sb.append("\n");

                System.out.println(sb);

                sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int isP(int x, int y) {
        for (PThread pt : pThreads) {
            if (pt.getPos().getX() == x && pt.getPos().getY() == y) {
                return pt.getpId();
            }
        }
        return -1;
    }

    private boolean isF(int x, int y) {
        synchronized (fruits) {
            for (Pos p : fruits.getAll()) {
                if (p.getX() == x && p.getY() == y) {
                    return true;
                }
            }
        }
        return false;
    }
}

class CThread extends Thread {
    private Fruits fruits;

    public CThread(Fruits fruits) {
        this.fruits = fruits;
    }

    @Override
    public void run() {
        while (true) {
            Random rand = new Random();
            Pos p = new Pos(rand.nextInt(20), rand.nextInt(20));
//            System.out.println("new fruit : " + p);
            fruits.put(p);
            try {
                sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}

class PThread extends Thread {

    public static void main(String[] args) throws InterruptedException {
        PThread pThread = new PThread(100, null);
        pThread.go(new Pos(100, 109), 2000);
        pThread.go(new Pos(0, 0), 2000);

    }

    private Integer id;

    private Pos pos;

    private Fruits fruits;

    public Pos getPos() {
        return pos;
    }

    public Integer getpId() {
        return id;
    }

    /**
     * 0 - nothing
     * 1 - to fruit
     * 2 - getting fruit
     * 3 - back
     */
    private int state = 0;

    public PThread(Integer id, Fruits fruits) {
        this.id = id;
        this.pos = new Pos();
        this.fruits = fruits;
    }

    @Override
    public void run() {
        try {
            while (true) {
                int loopTime = 0;
                // 0
                Pos p = fruits.get();
                if (p == null) {
//                    System.out.println(id + " : no fruit now, wait 500ms");
                    loopTime += 500;
                    sleep(500);
                    continue;
                }
//                System.out.println(id + " : find fruit : " + p);
                int time = (int) Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY()) * 25;
//                System.out.println(id + " : goto fruit : " + p + ", will cost " + time + "ms");
                state = 1;
                loopTime += time;
                go(p, time);

                // 1
//                System.out.println(id + " : reach fruit, fetching");
                state = 2;
                pos = p;
                loopTime += 250;
                sleep(250);

                // 2
//                System.out.println(id + " : fetching complete, go back, will cost " + time + "ms");
                state = 3;
                loopTime += time;
                go(new Pos(), time);

                // 3
//                System.out.println(id + " : come back, a loop cost " + loopTime + "ms");
//                pos = new Pos();
                state = 0;

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void go(Pos p, int millis) throws InterruptedException {
        Pos sPos = this.pos;
        int gap = 25;
        int loop = millis / gap + 1;
        double dx = (p.getX() - sPos.getX() + 0.0) / loop;
        double dy = (p.getY() - sPos.getY() + 0.0) / loop;

//        System.out.println(sPos);
//        System.out.println(loop);
//        System.out.println(dx);
//        System.out.println(dy);
        for (int t = 1; t <= loop; ++t) {
            this.pos = new Pos((int) Math.floor(sPos.getX() + dx * t), (int) Math.floor(sPos.getY() + dy * t));
//            System.out.println(this.pos);
            if (t == loop) {
                sleep(millis - gap * (loop - 1));
            } else {
                sleep(gap);
            }
        }
    }
}
