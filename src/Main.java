import java.util.concurrent.CountDownLatch;

class SumThread extends Thread {
    public long partSum = 0;
    int[] mas;
    int begin, end;
    CountDownLatch synch;

    SumThread(int[] curr_mas, int curr_begin, int curr_end, CountDownLatch s) {
        mas = curr_mas;
        begin = curr_begin;
        end = curr_end;
        synch = s;
    }

    public void run() {
        for (int i = begin; i <= end; i++)
            partSum = partSum + mas[i];
        synch.countDown();
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int size = 1000000;
        int[] mas = new int[size];
        long SumMas = 0;
        for (int i = 0; i < size; i++)
            mas[i] = i;
        for (int i = 0; i < size; i++)
            SumMas = SumMas + mas[i];
        System.out.println("Сума в однопоточному режимі:");
        System.out.println(SumMas);
//
        int NumThread = 10;
        int[] beginMas = new int[NumThread];
        int[] endMas = new int[NumThread];
        for (int i = 0; i < NumThread; i++)
            beginMas[i] = size / NumThread * i;
        for (int i = 0; i < NumThread - 1; i++)
            endMas[i] = beginMas[i + 1] - 1;

        endMas[NumThread - 1] = size - 1;


        long sumMas2 = 0;
        CountDownLatch synch = new CountDownLatch(NumThread);
        SumThread[] threads = new SumThread[NumThread];
        for (int i = 0; i < NumThread; i++) {
            threads[i] = new SumThread(mas, beginMas[i], endMas[i], synch);
            threads[i].start();
        }

        synch.await();

        for (int i = 0; i < NumThread; i++) {
            sumMas2 = sumMas2 + threads[i].partSum;
        }
        System.out.println("Сума в багатопоточному режимі:");
        System.out.println(sumMas2);
    }
}