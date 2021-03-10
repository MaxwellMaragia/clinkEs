import java.awt.FontFormatException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

public class clinkEs {

    static ChromeOptions options = new ChromeOptions();
    
    public static void main(String[] args) throws InterruptedException, IOException, FontFormatException {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("excludeSwitches",Collections.singletonList("enable-automation"));  
//        options.addArguments("--headless");
        //fetch the refresh rate
        new Home().setVisible(true);
        
    }
}

class bot {
    //login
    public static int ordersFound;
    public static WebDriver driver;
    
    public static WebDriver bidDriver;
    public static String paro;
    public static Set<Cookie> cookies = null;
    public static Login login;
    public static List<WebDriver> idleWindows = new ArrayList<WebDriver>();
    public static List<WebDriver> allWindows = new ArrayList<WebDriver>();

    public static void login(String email, String password, String activationCode) throws InterruptedException, IOException {
        
        driver = new ChromeDriver(clinkEs.options);
        driver.get("https://www.essayshark.com");
        paro = driver.getWindowHandle();
        
       
        //recyclable exp wait
        WebDriverWait loginWait = new WebDriverWait(driver, 20);
        Thread.sleep(3000);

        WebElement myaccountBtn = loginWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_esauth_myaccount_login_link")));
        myaccountBtn.click();

        WebElement emailField = loginWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_esauth_login_field")));
        emailField.sendKeys(email);

        WebElement passwordField = loginWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("id_esauth_pwd_field")));
        passwordField.sendKeys(password);

        driver.findElement(By.id("id_esauth_login_button")).submit();
//        loginWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(),'Orders')]")));
//            driver.get("https://essayshark.com/writer/orders");
        if(driver.getCurrentUrl().equalsIgnoreCase("https://essayshark.com/writer/news/")){
         loginWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"latest-news-tab\"]/table/tbody/tr[1]/td[2]/a"))).click();
         driver.get("https://essayshark.com/writer/orders/");
         Thread.sleep(3000);
        }
        
        boolean login_success=false;
        
        try{
            login_success = loginWait.until(ExpectedConditions.urlMatches("https://essayshark.com/writer/orders/"));
        }catch(Exception e){
        login_success=false;
        }
        
        
        if(login_success){
            
            try {
                cookies = driver.manage().getCookies();
                Bid.cookies = cookies;
                Bid.driver = driver;
                URL url = new URL("https://es.clink.co.ke/setUser.php?act=" + activationCode + "&user=" + email);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                
                
                
                String line = "";
                InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder response = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                
                //adding square brackets to make a valid json array
                String datas = "[" + response.toString() + "]";
                JSONArray jsonArray = new JSONArray(datas);
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                
                int status = jsonObject1.getInt("status");
                System.out.print("Status = "+status);
                
                if(status==1){
                    
                    login.dispose();
                    System.out.print("Login done");
                    new Bid().setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(null, "Subscription not valid, please login with the correct account","ERROR",JOptionPane.ERROR_MESSAGE);
                    
                }
            } catch (MalformedURLException ex) {
                Logger.getLogger(bot.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(bot.class.getName()).log(Level.SEVERE, null, ex);
            }

          }else{
            login.dispose();
                    System.out.print("Login done");
                    new Bid().setVisible(true);
            JOptionPane.showMessageDialog(null, "Error launching,restart the bot and try again","ERROR",JOptionPane.ERROR_MESSAGE);
        }
            
        }
        
      

    //clear or discard orders
    public static void discardOrders() throws InterruptedException {

        WebDriverWait wf_discard = new WebDriverWait(driver, 10);
        Thread.sleep(3000);

        Select pagination = new Select(wf_discard.until(ExpectedConditions.visibilityOfElementLocated(By.id("table_orders_load_qty"))));
        pagination.selectByValue("200");

        ordersFound = orderCount();

        if (ordersFound / 200 == 0) {
            WebElement discardBtn = wf_discard.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"discard_all_visible\"]")));
            discardBtn.click();
            WebElement discardModalBtn = wf_discard.until(ExpectedConditions.visibilityOfElementLocated(By.className("ZebraDialog_Button_1")));
            discardModalBtn.click();
        } else if (ordersFound / 200 >= 1) {
            int times = (ordersFound / 200) + 1;
            System.out.println(ordersFound + " " + times);
            for (int i = 0; i <= times; i = i + 1) {
                WebElement discardBtn = wf_discard.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"discard_all_visible\"]")));
                discardBtn.click();

                WebElement discardModalBtn = wf_discard.until(ExpectedConditions.visibilityOfElementLocated(By.className("ZebraDialog_Button_1")));
                discardModalBtn.click();
                Thread.sleep(3000);
                
            }
        }
    }

    public static int orderCount() throws InterruptedException {

        WebDriverWait wf_discard = new WebDriverWait(driver, 10);
        Thread.sleep(3000);
        WebElement countOrders = wf_discard.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"available_tab\"]/a/label/span/span[2]")));
        ordersFound = Integer.parseInt(countOrders.getText());
        return ordersFound;
    }

    
    
    //this class gets the subject name of the parent class instead of all texts in the parent class
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
    
   

    //throw incoming orders to new tabs or windows(for now just open on same tab)
    //bidder method
    //put price
    //download files
    //check for pink banner
    //check if banner diassapears
    //check if bid button is clickable
    //write chat message
    //close tab or window

}



