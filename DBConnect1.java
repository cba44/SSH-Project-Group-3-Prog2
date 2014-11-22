//Program 2

package ssh2;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;

public class DBConnect1 {
    
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private String query;
    private int count = 0;
    
    private File file = new File("/home/chiran/Desktop/mylog.log");
    private FileWriter writer;
    private BufferedWriter bwriter;
    
    private final String pw = "mysql";
    
    public DBConnect1(){
        
        while (true) {
        
            try{

                Class.forName("com.mysql.jdbc.Driver");

                con = DriverManager.getConnection("jdbc:mysql://localhost:3306","root",pw);
                st = con.createStatement();

                query = "SHOW DATABASES LIKE 'sshblock'";
                rs = st.executeQuery(query);

                while (rs.next()){

                    count++;

                }

                if (count == 1){

                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sshblock","root",pw);
                    st = con.createStatement();

                }else{

                    query = "CREATE DATABASE IF NOT EXISTS sshblock";     //Creating DB if doesn't exist      
                    st.executeUpdate(query);

                    query = "USE sshblock";
                    st.executeUpdate(query);

                    //Creating table if doesn't exist

                    query = "CREATE TABLE IF NOT EXISTS blockedIP(\n" +
                            "    IPAddress VARCHAR (15) PRIMARY KEY,\n" +
                            "    BlockedDate DATE NOT NULL,\n" +
                            "    BlockedTime TIME NOT NULL,\n" +
                            "    ReleaseDate DATE NOT NULL,\n" +
                            "    ReleaseTime TIME NOT NULL\n" +
                            ")";

                    st.executeUpdate(query);

                    query = "SET global max_connections = 100000";            
                    st.executeUpdate(query);
                    
                    break;

                }

            }catch(Exception ex){   //waiting until apache web server and mysql server starts

                //System.out.println(ex);

            }
            
        }
        
    }
    
    private static java.sql.Date getCurrentJavaSqlDate() { //http://www.java2s.com/Code/JavaAPI/java.sql/PreparedStatementsetTimeintparameterIndexTimex.htm
        
        java.util.Date date = new java.util.Date();
        return new java.sql.Date(date.getTime());
        
    }

    private static java.sql.Time getCurrentJavaSqlTime() {   //http://www.java2s.com/Code/JavaAPI/java.sql/PreparedStatementsetTimeintparameterIndexTimex.htm
        
        java.util.Date date = new java.util.Date();
        return new java.sql.Time(date.getTime());
    
    }
    
    public void getData(){
        
        try{
            
            query = "SELECT * FROM blockedIP";
            rs = st.executeQuery(query);
            
            while (rs.next()){
            
               String IPAddress = rs.getString("IPAddress");
               String relD = rs.getString("ReleaseDate");
               String relT = rs.getString("ReleaseTime");
               
               System.out.println("IP "+IPAddress+" "+relD+" "+relT);
            
            }
            
            
        }catch(Exception ex){
            
            System.out.println(ex);
            
        }
        
    }
    
    public void releaseIP(){
        
        try{
            
            query = "SELECT IPAddress\n"
                    + "     FROM blockedIP\n"
                    + "     WHERE ReleaseTime < NOW() AND ReleaseDate < NOW()";
            rs = st.executeQuery(query);
            
            while (rs.next()){
                
               String IPAddress = rs.getString("IPAddress");
               
               java.sql.Time time = getCurrentJavaSqlTime();       //System.out.println(time);
               java.sql.Date date = getCurrentJavaSqlDate();       //System.out.println(date);
               
               String myStr = date + "\t" + time + "\tiptables -D INPUT -s " + IPAddress + " -j DROP";
               
               System.out.println(myStr);
               
               writer = new FileWriter(file.getAbsolutePath(),true);
               bwriter = new BufferedWriter(writer);
               bwriter.write(myStr+"\n");
               bwriter.close();
               
               query = "DELETE FROM blockedIP WHERE IPAddress = ?";
               
               PreparedStatement preparedStmt = con.prepareStatement(query);
               
               preparedStmt.setString (1, IPAddress);
               
               preparedStmt.executeUpdate();               
            
            }
            
        }catch (Exception ex){
            
            System.out.println(ex);
            
        }
        
    }
    
}
