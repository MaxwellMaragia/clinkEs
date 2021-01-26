import javax.swing.JTextArea;
import org.openqa.selenium.WebDriver;

public class BidThread extends Thread {

    BidAction bidAction;
    private String url;
    private WebDriver driver;
    public JTextArea console;

    public BidThread(String threadName, String url, WebDriver driver,JTextArea console) {
        super(threadName);
        bidAction = new BidAction();
        this.url = url;
        this.driver = driver;
        this.console=console;
    }

    @Override
    public void run() {
        System.out.println("Bid started" + Thread.currentThread().getName());
        try {
            Thread.sleep(1000);
            bidAction.bid(BaseClass.settings()[1], url,driver,console);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println("thread - ended " + Thread.currentThread().getName());
    }

}
