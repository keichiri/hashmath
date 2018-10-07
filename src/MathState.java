import java.time.Instant;
import java.io.IOException;

import com.swirlds.platform.SwirldState;
import com.swirlds.platform.AddressBook;
import com.swirlds.platform.Console;
import com.swirlds.platform.Platform;
import com.swirlds.platform.Address;
import com.swirlds.platform.FCDataInputStream;
import com.swirlds.platform.FCDataOutputStream;
import com.swirlds.platform.FastCopyable;


public class MathState implements SwirldState {
    private AddressBook addressBook;
    private long result;
    private  long selfId;
    private Console console;

    public MathState (long selfId) {
        this.selfId = selfId;
    }

    @Override
    public synchronized void init(Platform platform, AddressBook addressBook) {
        this.addressBook = addressBook;
        this.result = 100;
        this.console = platform.createConsole(true);
        this.console.out.println("YESSS!");
    }

    @Override
    public synchronized AddressBook getAddressBookCopy() {
        return this.addressBook.copy();
    }

    @Override
    public synchronized void copyFrom(SwirldState state) {
        this.addressBook = ((MathState) state).addressBook;
    }

    @Override
    public synchronized void handleTransaction(long id, boolean isConsensus, Instant timestamp, byte[] trans, Address address) {
        String transactionData = new String(trans);
        long num = Integer.parseInt(transactionData.substring(1, transactionData.length()));
        long currentRes = result;

        if (isConsensus) {

            try {
                switch (transactionData.charAt(0)) {
                    case '+':
                        this.result += num;
                        break;
                    case '-':
                        this.result -= num;
                        break;
                    case '*':
                        this.result *= num;
                        break;
                    case '/':
                        this.result /= num;
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e );
            }

            System.out.println("ID: " + selfId + ". Sender:" + id + ". Calculation: " + currentRes + " " + new String(trans) + " = " + this.result);
        }

    }

    @Override
    public void noMoreTransactions() {}

    @Override
    public synchronized FastCopyable copy() {
        MathState copy = new MathState(selfId);
        copy.copyFrom(this);
        return copy;
    }

    @Override
    public synchronized void copyTo(FCDataOutputStream outStream)
            throws IOException {
        addressBook.copyTo(outStream);
    }

    @Override
    public synchronized void copyFrom(FCDataInputStream inStream)
            throws IOException {
        addressBook.copyFrom(inStream);
    }

    public long getResult() {
        return this.result;
    }
}