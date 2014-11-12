//Program 2

import java.sql.*;

public class DBConnect1 {
    
    private Connection con;
    private Statement st;
    private ResultSet rs;
    private String query;
    
    private final String pw = "mysql";
    
    public DBConnect1(){
        
        try{
            
            Class.forName("com.mysql.jdbc.Driver");
            
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/sshblock","root",pw);
            st = con.createStatement();
            
        }catch(Exception ex){
            
            System.out.println(ex);
            
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
               
               System.out.println(date + "\t" + time + "\tiptables -D INPUT -s " + IPAddress + " -j DROP");
               
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
