//Program 2

import java.util.concurrent.TimeUnit;

public class UnblockIP {
    
    public static void main(String [] args){
    
    DBConnect1 connect = new DBConnect1();
    
    while (true){
    
        connect.releaseIP();
        
        try{

		TimeUnit.SECONDS.sleep(3);

	}catch(Exception ex){

	System.out.println(ex);

	}
    
    }
    
    }
    
}
