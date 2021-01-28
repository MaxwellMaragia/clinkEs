
//import static clinkEs.options;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user 1
 */
public class Boom extends Thread{
    
    private WebDriver driver;
    static WebDriver bidDriver;
    private JTextArea console;
    private static Boolean exit;
      
    private Set<Cookie> cookies;    
    
    public Boom(WebDriver driver, Set<Cookie> cookies,JTextArea console){
        this.driver = driver;
        this.cookies = cookies;
        this.console = console;
        exit = false; 
    }
    
    
    public static int availableThreads;
    @Override
    public void run(){
        try {
            availableThreads=Integer.parseInt(BaseClass.settings()[3]);
            int refreshRate = Integer.parseInt(BaseClass.settings()[0]);
            List<String>unwanteds=BaseClass.unwantedSubjects();
            int i = 0;
            
            while (i >= 0 && !exit) {
                try {
                    driver.navigate().refresh();
                    Thread.sleep(refreshRate * 1000);
                    //System.out.println(bot.orderCount());
                    WebElement returnType_table = driver.findElement(By.id("available_orders_list_container"));
                    List<WebElement> orders = returnType_table.findElements(By.xpath("//tr[not(contains(@style,'display:none'))]/td[@class='topictitle' or @class='bold' or @class='topictitle bold']/a"));
                    List<WebElement>orderIds=returnType_table.findElements(By.xpath("//tr[not(contains(@style,'display:none'))]/td[@class='topictitle' or @class='bold' or @class='order_number ']/a"));
                    List<WebElement> subjects=returnType_table.findElements(By.xpath("//tr[not(contains(@style,'display:none'))]/td[@class='topictitle' or @class='bold' or @class='topictitle bold']/a/parent::td/parent::tr/td[@class='discipline']"));
                    List<WebElement> discards = returnType_table.findElements(By.xpath("//tr[not(contains(@style,'display:none'))]/td[@class='topictitle' or @class='bold' or @class='topictitle bold']/a/../../td/a[@class='order_container_discard ']"));
                    console.append("available threads" +"["+ availableThreads +"]"+" orders in queue"+"["+ orders.size() +"]"+ "\n");
                    console.append("----------------------------------------------------------------"+ "\n");
                    for (int j = 0; j < orders.size(); j++) {
                        
                        String[] subjectArray = subjects.get(j).getText().split(":");
                        String subject;
                        try {
                            subject = subjectArray[0].trim();
                        } catch (ArrayIndexOutOfBoundsException e) {
                            subject = "other";
                        }
//

if(unwanteds.contains(WordUtils.capitalizeFully(subject))){
    discards.get(j).click();
    console.append(" ["+subject+"] is unwanted..discarded'\n");
    console.append("----------------------------------------------------------------"+ "\n");
}else{
    
    if (availableThreads >= 1) {
        console.append("Bidding [" + orders.get(j).getText() + "]\n");
        console.append("Order id [" + orderIds.get(j).getText() + "]\n");
        console.append("Subject [" + subject + "]\n");
        console.append("----------------------------------------------------------------"+ "\n");
        
        
        ChromeOptions options = new ChromeOptions();
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
//        options.addArguments("--headless");
        bidDriver = new ChromeDriver(options);
        
        
        bidDriver.get("https://essayshark.com/writer/orders/");
        for (Cookie cookie : cookies) {
            bidDriver.manage().addCookie(cookie);
        }
        new BidThread("Bid",orders.get(j).getAttribute("href"), bidDriver, console).start();
        try{
        
        discards.get(j).click();
        }catch(Exception e){}
        
        
        availableThreads--;
        console.append("available threads" +"["+ availableThreads +"]"+" orders in queue"+"["+ orders.size() +"]"+ "\n");
        console.append("----------------------------------------------------------------"+ "\n");
    }else{
        console.append("available threads" +"["+ availableThreads +"]"+" orders in queue"+"["+ orders.size() +"]"+ "\n");
        console.append("----------------------------------------------------------------"+ "\n");
        break;}
    
}
//

                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(Boom.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Boom.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String getTextNode(WebElement e)
    {
        String text = e.getText().trim();
        List<WebElement> children = e.findElements(By.xpath("./*"));
        for (WebElement child : children)
        {
            text = text.replaceFirst(child.getText(), "").trim();
        }
        return text;
    }
    
     // for stopping the thread 
    
    public static void exit() 
    { 
         exit = true; 
    } 
    
}


