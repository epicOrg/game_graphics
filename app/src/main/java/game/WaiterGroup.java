package game;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Andrea on 04/05/2015.
 */
public class WaiterGroup {

    private CountDownLatch countDownLatch;

    private ArrayList<Waiter> waiters = new ArrayList<>();

    public WaiterGroup(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public void addWaiter(Waiter w) {
        waiters.add(w);
    }

    public void startWaiting() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                for (Waiter w : waiters) {
                    w.unleash();
                }
            }
        }).start();
    }

}
