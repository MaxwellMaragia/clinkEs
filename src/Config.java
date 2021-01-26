import static com.sun.org.apache.xalan.internal.utils.SecuritySupport.getResourceAsStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.text.WordUtils;


/**
 *
 * @author alvo
 */
public class Config {
    
    
    public static String readFile(String filePath){
     InputStream in = getResourceAsStream(filePath);
     Scanner scan=null;
     scan=new Scanner(in); //config file is not there
        
     String fileString=scan.nextLine();
    
            
        return fileString;
       
    }
    
    public static void writeFile(String filePath,String newConfig){
        try { 
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write(newConfig);
            myWriter.close();
        } catch (IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    public static boolean isSubjectUnwanted(String subject){
        boolean isUnwanted;
        String filter=Config.readFile("sare_hizo.txt");
        String [] filterArray=filter.split(",");
     
        int i=0;
        while(i<filterArray.length){
            filterArray[i]=WordUtils.capitalizeFully(filterArray[i]);
            i++;
     }
     
     List<String> list = Arrays.asList(filterArray);
     
     isUnwanted = list.contains(subject);
     return isUnwanted;
    }
    
   
}
