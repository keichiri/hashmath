import java.util.Random;

import com.swirlds.platform.SwirldMain;
import com.swirlds.platform.Platform;
import com.swirlds.platform.Console;
import com.swirlds.platform.SwirldState;


public class MathMain implements SwirldMain {
    private final int sleep = 50;
    private final int sync = 500;
    private Platform platform;
    private long selfId;
//    private Console console;
    private Random rand = new Random();


    @Override
    public void init(Platform platform, long id) {
        this.platform = platform;
        this.selfId = id;
//        this.console = platform.createConsole(true);
        platform.setAbout("Math calculator");
        platform.setSleepAfterSync(sync);
    }

    @Override
    public void preEvent() {}

    @Override
    public void run() {
//        this.console.out.println("Starting!");
        while (true) {
            MathState state = (MathState) platform
                    .getState();
            long result = state.getResult();
//            this.console.out.println("Result: " + result);

            try {
                Thread.sleep(sleep);
            } catch (Exception e) {

            }
            byte[] transaction = this.generateTransaction();
//            this.console.out.println("Sending transaction: " + new String(transaction));
            this.platform.createTransaction(transaction);
        }
    }

    private byte[] generateTransaction() {
        String tx = "";
        int num;
        switch (rand.nextInt(4)) {
            case 0:
                num = rand.nextInt(100);
                tx = "+" + num;
                break;
            case 1:
                num = rand.nextInt(100);
                tx = "-" + num;
                break;
            case 2:
                num = rand.nextInt(10) + 1;
                tx = "*" + num;
                break;
            case 3:
                num = rand.nextInt(10) + 1;
                tx = "/" + num;
                break;
        }
        try {
            return tx.getBytes("UTF-8");
        } catch(Exception e) {
            return null;
        }
    }

    @Override
    public SwirldState newState() {
        return new MathState(selfId);
    }
}