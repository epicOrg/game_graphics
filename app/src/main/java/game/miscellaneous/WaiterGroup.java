package game.miscellaneous;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Class containing a list of {@link Waiter} and a {@link CountDownLatch} that identifies the condition when start the waiters.
 *
 * @author Torlaschi
 */
public class WaiterGroup {

    private CountDownLatch countDownLatch;

    private ArrayList<Waiter> waiters = new ArrayList<>();

    /**
     * Constructs an empty group of <code>Waiter</code> associated with the condition of the <code>CountDownLatch</code>.
     *
     * @param countDownLatch <code>CountDownLatch</code> that identifies the condition when start all the <code>Waiter</code>.
     */
    public WaiterGroup(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    /**
     * Adds a <code>Waiter</code> to the group.
     *
     * @param w Waiter to be added to the group.
     */
    public void addWaiter(Waiter w) {
        waiters.add(w);
    }

    /**
     * Start the waiting of all the <code>Waiter</code> for the condition.
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
