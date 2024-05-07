//										java -cp /usr/share/java/mariadb-java-client-2.7.6.jar toSQL.java 
// 										to compile, add root passwort after toSQL.java or enter passwort when asked
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.Scanner;

public class toSQL {
    //Method for reading a Random line of files (firstname, name, adress) for creating sql data
    public static String readRandomLineFromFile(String filePath, int min, int max, Random random) {
        int randomNumber = random.nextInt(max - min + 1) + min;
        //if reader works, go through file and select the random line, then return this line
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                if (lineNumber == randomNumber) {
                    return line;
                }
                lineNumber++;
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Fehler beim Lesen der Datei: " + e.getMessage());
            
        }
        return "";
    }

    //method for building a String with the correct SQL Insert Into statement, this String will be returned
    public static String buildInsertStatement(String tableName, int id, int idmeter,String name, String firstname, int phone)
    {
        return String.format(
		"INSERT INTO %s VALUES('%d', '%d', '%s', '%s','%d');",
		tableName, id, idmeter, name,firstname, phone);
    }

    public static void main(String[] args) {
        //Initiating Random generator
        Random random = new Random(System.currentTimeMillis());
        String pw="";
    	
    	if (args.length > 0) {
    		System.out.print(""+args.length);
            pw=pw+args[0];
            
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Gib das Passwort ein: ");
        	pw =pw+scanner.nextLine();
        }
        //Initiating connection, which is used for linking this programm with the SQL Database    
        Connection connection;
        
        //try to open the Database
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1?user=root&password="+pw);
            Statement statement = connection.createStatement();
            
            //Creating 10.000 Datasets while using randomlines as content
            for(int id=10000;id<10002;id++){
            
            
                //Creating content of one row, ordered by the SQL-Columns
                int idmeter = random.nextInt(9999 - 1 + 1) + 1;    
            	String firstname=readRandomLineFromFile("vornamen.txt", 1, 1000,random);
            	String name=readRandomLineFromFile("nachnamen.txt", 1, 1000,random);
                int phone = random.nextInt(900000 - 100000 + 1) + 100000;
                String street=readRandomLineFromFile("strasse.txt", 1, 198,random);
                int streetnumber = random.nextInt(20 - 1 + 1) + 0;     
            	int collected = random.nextInt(2);
            	
            	//Creating the finished SQL command, using the buildInsertStatement method
            	String sqlExec = buildInsertStatement("person", id,idmeter,name,firstname, phone);
            	System.out.println(sqlExec);

                //using statement to execute the SQL command, the return Value gives the number of changed rows, which should always be 1
       	    	int rowsAffected = statement.executeUpdate(sqlExec);
            	System.out.println("Anzahl der betroffenen Zeilen: " + rowsAffected);
   		    }
   		    connection.close();
   		    statement.close();
        } 
        catch (SQLException e) {
            System.err.println("Fehler beim Ausführen des SQL-Befehls: " + e.getMessage());
        }
        
       
       
        
 
    }  
}

