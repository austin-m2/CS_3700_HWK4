package CS_3700;

import java.util.concurrent.locks.ReentrantLock;

public class Unstructured {
    public static void main(String[] args) {
        ReentrantLock forks[] = new ReentrantLock[5];
        for (int i = 0; i < forks.length; i++) {
            forks[i] = new ReentrantLock();
        }

        Philosopher1 A = new Philosopher1(forks, 'A');
        Philosopher1 B = new Philosopher1(forks, 'B');
        Philosopher1 C = new Philosopher1(forks, 'C');
        Philosopher1 D = new Philosopher1(forks, 'D');
        Philosopher1 E = new Philosopher1(forks, 'E');

        A.start();
        B.start();
        C.start();
        D.start();
        E.start();
    }
}

class Philosopher1 extends Thread {
    ReentrantLock forks[];
    ReentrantLock leftFork;
    ReentrantLock rightFork;
    char name;

    Philosopher1(ReentrantLock forks[], char name) {
        this.forks = forks;
        this.name = name;
        switch (name) {
            case 'A':
                leftFork = forks[0];
                rightFork = forks[1];
                break;
            case 'B':
                leftFork = forks[1];
                rightFork = forks[2];
                break;
            case 'C':
                leftFork = forks[2];
                rightFork = forks[3];
                break;
            case 'D':
                leftFork = forks[3];
                rightFork = forks[4];
                break;
            case 'E':
                //Order is NOT flipped for this philosopher
                leftFork = forks[4];
                rightFork = forks[0];
                break;
            default:
                System.out.println("Invalid philosopher name!");
                System.exit(1);
        }
    }

    @Override
    public void run() {
        int time;
        while (true) {
            try {
                System.out.println("Philosopher " + name + ": attempt to acquire fork to left");
                if (leftFork.tryLock()) {
                    System.out.println("Philosopher " + name + ": acquired left fork");
                    System.out.println("Philosopher " + name + ": attempt to acquire fork to right");
                    if (rightFork.tryLock()) {
                        System.out.println("Philosopher " + name + ": acquired right fork");
                        time = (int) (Math.random() * 10 + 1);
                        System.out.println("Philosopher " + name + ": eating for " + time + " seconds");
                        sleep(time * 1000);
                        System.out.println("Philosopher " + name + ": finished eating! dropping both forks");
                        rightFork.unlock();
                        leftFork.unlock();
                    } else {
                        System.out.println("Philosopher " + name + ": right fork is in use! Dropping left fork");
                        leftFork.unlock();
                    }
                } else {
                    System.out.println("Philosopher " + name + ": left fork is in use! ");
                }

                time = (int) (Math.random() * 10 + 1);
                System.out.println("Philosopher " + name + ": thinking for " + time + " seconds");
                sleep(time * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}