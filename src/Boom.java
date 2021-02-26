
//import static clinkEs.options;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import org.apache.commons.lang3.text.WordUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author user 1
 */
public class Boom extends Thread {

    private WebDriver driver;
    //static WebDriver bidDriver;
    private JTextArea console;
    private static Boolean exit;
    private RemoteWebDriver bidDriver;

    private Set<Cookie> cookies;

    public Boom(WebDriver driver, Set<Cookie> cookies, JTextArea console) {
        this.driver = driver;
        this.cookies = cookies;
        this.console = console;

        exit = false;
    }

    public static int availableThreads;

    @Override
    public void run() {
        try {
            availableThreads = Integer.parseInt(BaseClass.settings()[3]);
            int refreshRate = Integer.parseInt(BaseClass.settings()[0]);
            List<String> unwanteds = BaseClass.unwantedSubjects();
            WebDriverWait wait5 = new WebDriverWait(driver, 5);
            while (1 >= 0 && !exit) {
                //give browser time to load page
                driver.manage().timeouts().implicitlyWait(refreshRate, TimeUnit.SECONDS);
                
                //gets the td of order id
                List<WebElement> odas = driver.findElements(By.xpath("//tbody/tr[not(contains(@style,'display:none')) and not(contains(@style,'display: none'))]/td[@class='order_number ']"));

                console.append("available threads" + "[" + availableThreads + "]" + " orders in queue" + "[" + odas.size() + "]" + "\n");
                console.append("----------------------------------------------------------------" + "\n");
                for (WebElement oda : odas) {
                    if (odas.size() == 0) {
                        System.out.println("nina breallllllllllllllllllllllllllllllllllll");
                        break;
                    }
                    //////////////////////
                    WebElement odaDiscard = oda.findElement(By.xpath(".//following-sibling::td[@class='orderselect center']/a"));
                    WebElement odaId = oda.findElement(By.xpath(".//a"));
                    String[] subjectArray = oda.findElement(By.xpath("//following-sibling::td[@class='discipline']")).getText().split(":");
                    String subject;
                    try {
                        subject = subjectArray[0].trim();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        subject = "other";
                    }
//

                    if (unwanteds.contains(WordUtils.capitalizeFully(subject))) {
                        odaDiscard.click();
                        console.append(" [" + subject + "] is unwanted..discarded'\n");
                        console.append("----------------------------------------------------------------" + "\n");
                    } else {

                        if (availableThreads >= 1) {
                            console.append("Order id [" + odaId.getText() + "]\n");
                            console.append("Subject [" + subject + "]\n");
                            console.append("----------------------------------------------------------------" + "\n");

                            
                            if (!bot.idleWindows.isEmpty()) {

                                Random randomizer = new Random();
                                bidDriver = (RemoteWebDriver) bot.idleWindows.get(randomizer.nextInt(bot.idleWindows.size()));
                                bot.idleWindows.remove(bidDriver);

                            }

                            new BidThread("Bid", odaId.getAttribute("href"), bidDriver, console).start();
                            try {

                                odaDiscard.click();
                            } catch (Exception e) {
                            }

                            availableThreads--;
                            console.append("available threads" + "[" + availableThreads + "]" + " orders in queue" + "[" + odas.size() + "]" + "\n");
                            console.append("----------------------------------------------------------------" + "\n");
                        } else {
                            console.append("available threads" + "[" + availableThreads + "]" + " orders in queue" + "[" + odas.size() + "]" + "\n");
                            console.append("----------------------------------------------------------------" + "\n");
                            break;
                        }

                    }
//

                }
                driver.navigate().refresh();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Boom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String getTextNode(WebElement e) {
        String text = e.getText().trim();
        List<WebElement> children = e.findElements(By.xpath("./*"));
        for (WebElement child : children) {
            text = text.replaceFirst(child.getText(), "").trim();
        }
        return text;
    }

    // for stopping the thread 
    public static void exit() {
        exit = true;
    }

}
