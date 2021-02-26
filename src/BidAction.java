
//import static Boom.availableThreads;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import javax.swing.JTextArea;

public class BidAction {

    //public WebDriver driver;

    public void bid(String message, String bidURL, WebDriver driver,JTextArea console) throws InterruptedException {
        //recyclable exp wait
        driver.get(bidURL);
        WebDriverWait minuteWait = new WebDriverWait(driver, 60);
        WebDriverWait halfMinWait=new WebDriverWait(driver,30);
        WebDriverWait wait = new WebDriverWait(driver, 200);
        WebDriverWait wait5 = new WebDriverWait(driver, 5);
        
        WebElement amountInput;
        String recAmount;
        String orderId="";
        
        WebElement apply;

        //download files
        List<WebElement> files = driver.findElements(By.xpath("//dl/dt[contains(text(),'Uploaded additional materials')]/following-sibling::dd/div/a"));
        for (WebElement file : files) {
            file.click();
        }

        try {
            wait5.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_read_timeout_container"))).isDisplayed();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        try {
//            recAmount = sec5.until(ExpectedConditions.visibilityOfElementLocated(By.id("rec_amount"))).getText();
            amountInput=wait5.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_bid")));
            amountInput.sendKeys("0");
        } catch (Exception e) {
            System.out.println(e);
        }

        //click apply order to display mimimal amount error
        try{
            apply = wait.until(ExpectedConditions.elementToBeClickable(By.id("apply_order")));
            apply.submit();
            
        }catch(Exception e ){
            System.out.println(e);
        }
        
        //put minimal amount
        try{
        String minimalAmountString=wait5.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_bid-error"))).getText();
        String [] mimimalAmountArray=minimalAmountString.split(" ");
        String mimimalAmount=mimimalAmountArray[4].replace("$", "");
        System.out.println("minimal MOUNT"+mimimalAmountArray[4].replace("$", ""));
        amountInput=wait5.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_bid")));
        amountInput.sendKeys(mimimalAmount);
        }catch(Exception e){
        System.out.println(e);
        }
        
        
        //click apply order
        try{
            apply = wait.until(ExpectedConditions.elementToBeClickable(By.id("apply_order")));
            apply.submit();
            
        }catch(Exception e ){
            System.out.println(e);
        }
        
        try {
            orderId = driver.findElement(By.xpath("//div[@class=\"order-id grey data-hj-whitelist type_10\"]")).getText();
            console.append("[" + orderId + "]" + " successfully bid" + "\n");
            console.append("----------------------------------------------------------------" + "\n");
        } catch (Exception e){}
        
        //place message
       try{
           WebElement chatField = halfMinWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_body")));
           chatField.sendKeys(message);
           
           
       }catch(Exception e){
           System.out.println(e);
           console.append("["+orderId+"]"+" chat[0]"+ "\n");
           console.append("----------------------------------------------------------------"+ "\n");
       }
        
        try {
            Actions actions = new Actions(driver);
            actions.sendKeys(Keys.ENTER).perform();
        } catch (Exception e) {
        }
        
        bot.idleWindows.add(driver);
        Boom.availableThreads=Boom.availableThreads+1;

    }
}
