
import com.formdev.flatlaf.json.ParseException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import org.apache.commons.lang3.text.WordUtils;
import org.openqa.selenium.WebDriver;


public class BaseClass {
    
    public static Properties pro;
    
    public static String version() throws FileNotFoundException, IOException{
        pro = new Properties();
        FileInputStream fip = new FileInputStream("objects.properties");
        pro.load(fip);
       
        return pro.getProperty("VERSION");
        
    }
    
    public static String title() throws FileNotFoundException, IOException{
        pro = new Properties();
        FileInputStream fip = new FileInputStream("objects.properties");
        pro.load(fip);
        
        return pro.getProperty("TITLE");
    }
    
    public static String[] settings() throws FileNotFoundException{
         String settings = readFile("settings.txt");
         String[] settings_value;
         settings_value = settings.split(",,,");
         return settings_value;
         
    }
    
    public static boolean isValid(String email) 
    { 
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                            "[a-zA-Z0-9_+&*-]+)*@" + 
                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                            "A-Z]{2,7}$"; 
                              
        Pattern pat = Pattern.compile(emailRegex); 
        if (email == null) 
            return false; 
        return pat.matcher(email).matches(); 
    } 
    
    public static String readFile(String filePath) throws FileNotFoundException{
      pro = new Properties();
        FileInputStream fip = new FileInputStream(filePath);
       
        Scanner scan=null;
        scan=new Scanner(fip); //config file is not there

        String fileString=scan.nextLine();
    
            
        return fileString;
       
    }
    
    public static void writeFile(String filePath,String newConfig){
       
            FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(filePath);
            myWriter.write(newConfig);
            myWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(BaseClass.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                myWriter.close();
            } catch (IOException ex) {
                Logger.getLogger(BaseClass.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
       
    }
    public static  List<String> unwantedSubjects() throws FileNotFoundException{
        String filter=readFile("sare_hizo.txt");
        String [] filterArray=filter.split(",");
     
        int i=0;
        while(i<filterArray.length){
            filterArray[i]=WordUtils.capitalizeFully(filterArray[i]);
            i++;
     }
     
     List<String> list = Arrays.asList(filterArray);
     
//     isUnwanted = list.contains(subject);
     return list;
    }
    
    public static int activationStatus(String endDay) throws java.text.ParseException{
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String dateToday = df.format(new Date());
        
	 String dateBeforeString = dateToday;
	 String dateAfterString = endDay;
         float daysBetween=0;
         int daysBetweenInt=0;
	 try {
	       Date dateBefore = df.parse(dateBeforeString);
	       Date dateAfter = df.parse(dateAfterString);
	       long difference = dateAfter.getTime() - dateBefore.getTime();
	       daysBetween = (difference / (1000*60*60*24));
               daysBetweenInt=Math.round(daysBetween);
               
	 } catch (ParseException e) {
	       //e.printStackTrace();
	 }

        return daysBetweenInt;
    } 
    
     public static void stopBid(WebDriver driver){
            driver.close();
    }
}
