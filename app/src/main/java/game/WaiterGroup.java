package game;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Class containing a list of Waiter and a CountDownLatch that identifies the condition when start the waiters.
 *
 * @author Torlaschi
 */
public class WaiterGroup {

    private CountDownLatch countDownLatch;

    private ArrayList<Waiter> waiters = new ArrayList<>();

    /**
     * Constructs an empty group of waiters associated with the condition of the CountDownLatch.
     *
     * @param countDownLatch CountDownLatch that identifies the condition when start the waiters
     */
    public WaiterGroup(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    /**
     * Adds a Waiter to the group.
     *
     * @param w Waiter to be added to the group
     */
    public void addWaiter(Waiter w) {
        waiters.add(w);
    }

    /**
     * Start the waiting of the waiters for the condition.
     */
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
