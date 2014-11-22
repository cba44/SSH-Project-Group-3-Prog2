//Program 2

package ssh2;

import java.util.concurrent.TimeUnit;
import org.apache.commons.daemon.*;

public class UnblockIP implements Daemon{
    
    public static void main(String [] args){       
    
        DBConnect1 connect = new DBConnect1();

        while (true){

            try{

                connect.releaseIP();

                try{

                        TimeUnit.SECONDS.sleep(3);

                }catch(Exception ex){

                System.out.println(ex);

                }

            }catch (Exception ex){

                System.out.println(ex);

            }

        }
    
    }
    
    @Override
    public void init(DaemonContext dc) throws DaemonInitException, Exception {
        System.out.println("initializing ...");
    }
    
    @Override
    public void start() throws Exception {
        System.out.println("starting ...");
        main(null);     //Running the main method
    }

    @Override
    public void stop() throws Exception {
        System.out.println("stopping ...");
    }

    @Override
    public void destroy() {
        System.out.println("done.");
    }
    
}
