import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	public static void main(String[] args) {
		ServerSocket server;
		Socket sock;
		BufferedReader in;
		PrintWriter out;
		Computer computer = new Computer();
		char prediction;
		final int PATTERN_SIZE = 5;
		int info = 0;
		int i = 1;
		String p = " ";
		int difficulty;
		File f = new File("Computer.dat");
		try {
			server = new ServerSocket(1235);
			System.out.println("Waiting...");
			sock = server.accept();
			System.out.println("Connected");
			in = new BufferedReader(new InputStreamReader(
			 sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream());
			difficulty = in.read();
			if ( difficulty == '2' ) {
				load(computer, f);
			}
			while (true) {
				prediction = computer.makePrediction( new Pattern( p ) );
				out.println(prediction);
				out.flush();
				String move = in.readLine();
				if ( info < PATTERN_SIZE - 2 ) {
					p += move;
					info++;
				}
				else {
					p = p.substring(0) + move;
					computer.storePattern( new Pattern( p ) );
					while ( ( PATTERN_SIZE - i ) > 2 ) {
						computer.storePattern( 
						 new Pattern( p.substring( 0, PATTERN_SIZE - i ) ) );
						i++;
					}
					i = 1;
					p = p.substring(1);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		save(computer,f);
	}
	
	public static void save( Computer computer, File f ) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
			 new FileOutputStream ( f ));

	        out.writeObject( computer );
	        out.close();
	        System.out.println("Computer saved.");
	        
	    } catch ( IOException e ) {
	    	System.out.println("Error processing file.");
	    }
	}
	
	public static void load ( Computer computer, File f ) {
		if ( f.exists() ) {
			try {
				ObjectInputStream in = new ObjectInputStream(
                new FileInputStream( f ));

                computer = ( Computer ) in.readObject();
                in.close();
                System.out.println("Computer loaded.");
            } catch ( IOException e) {
                System.out.println("Error processing file.");
            } catch ( ClassNotFoundException c ) {
                System.out.println("Class not found.");
            }
		}
	}
}
