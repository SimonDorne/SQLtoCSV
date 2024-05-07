//					java -cp /usr/share/java/mariadb-java-client-2.7.6.jar toCSV.java
//					to compile, add root passwort after toCSV.java or enter passwort when asked

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class ToCSV {

    private static final String SELECT_QUERY = "SELECT * FROM home LEFT JOIN person ON home.IDmeter = person.IDmeter WHERE home.collected = 0;";
   

    public static void main(String[] args) {
    	String pw="";
    	String CSV_FILE = "output.csv";
    	if (args.length > 0) {
    		System.out.print(""+args.length);
            pw=pw+args[0];
            
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Gib das Passwort ein!: ");
        	pw =pw+scanner.nextLine();
        }
    	try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1?user=root&password="+pw);
    	    //Connection
            PreparedStatement selectStatement = connection.prepareStatement(SELECT_QUERY);
            ResultSet resultSet = selectStatement.executeQuery();
            //Writer
            BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) 
     		{

            	// CSV header
            	writer.write("ID Person;Zählernummer;Nachname;Vorname;Telefon;Straße;Hausnummer;Erledigt\n");

           		// Process SQL query results
            	while (resultSet.next()) {
                	int idperson = resultSet.getInt("IDperson");
                	String idmeter = resultSet.getString("IDmeter");
                	String name = resultSet.getString("name");
                	String firstname = resultSet.getString("firstname");
                	int phonenumber = resultSet.getInt("phonenumber");
                	String street = resultSet.getString("street");
                	int streetnumber=resultSet.getInt("streetnumber");
                	int collected = resultSet.getInt("Collected");

                	// Write data to CSV
                	writer.write(idperson+";"+idmeter+ ";" + name + ";" + firstname + ";" + phonenumber + ";" + street + ";" +streetnumber+";" + collected + "\n");
            	}

            	System.out.println("Daten erfolgreich in CSV-Datei kopiert");

        	} catch (SQLException | IOException e) {
            	e.printStackTrace();
            	System.err.println("Fehler: " + e.getMessage());
        	}
    		
      
    }
}



