
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
        // Initialize sockets for communication between server-client
        ServerSocket server;
        Socket sock;

        // Initialize buffers for communication between server-client
        BufferedReader in;
        PrintWriter out;

        // Initialize the AI player
        Computer computer = new Computer();

        // Initialize variables for game logic
        char prediction;  // AI's move prediction
        final int PATTERN_SIZE = 5; // longest pattern tracked by AI
        int info = 0; // initial amount of information AI has
        String p = " "; // initial pattern
        int difficulty; // define difficulty (beginner or veteran)
        File f = new File("Computer.dat"); // get file for computer

        try {
            // Setup connection between server and client
            server = new ServerSocket(1235);
            System.out.println("Waiting...");
            sock = server.accept();
            System.out.println("Connected");
            in = new BufferedReader(new InputStreamReader(
                    sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream());

            // Get the users difficulty choice and load if veteran
            difficulty = in.read();
            if (difficulty == '2') {
                load(computer, f);
            }

            // Continue to run game until client exits
            while (true) {
                // AI makes prediction using most recent user pattern
                prediction = computer.makePrediction(new Pattern(p));

                // prediction is sent to client
                out.println(prediction);
                out.flush();

                // user's move is read from client
                String move = in.readLine();

                // If AI doesn't have enough info to make a prediction, add
                // user's move to pattern
                if (info < PATTERN_SIZE - 2) {
                    p += move;
                    info++;
                } // Else, make a new pattern out of the user's most recent move
                // and the previous pattern
                else {
                    p = p.substring(0) + move;

                    // store recent pattern
                    computer.storePattern(new Pattern(p));
                    
                    // store all subpatterns
                    for (int i = 1; (PATTERN_SIZE - i) > 2; i++) {
                        computer.storePattern(
                                new Pattern(p.substring(0, PATTERN_SIZE - i)));
                    }
                    
                    // remove first move in pattern from the pattern
                    p = p.substring(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // save computer data
        save(computer, f);
    }
    
    /**
     * Saves the computer's data for use in veteran mode
     * @param computer      the computer being saved
     * @param f             the file being saved to
     */
    public static void save(Computer computer, File f) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(
                    new FileOutputStream(f));

            out.writeObject(computer);
            out.close();
            System.out.println("Computer saved.");

        } catch (IOException e) {
            System.out.println("Error processing file.");
        }
    }
    
    /**
     * Loads the computer's data from file for use in veteran mode
     * @param computer      the computer being loaded into
     * @param f             the file being loaded from
     */
    public static void load(Computer computer, File f) {
        if (f.exists()) {
            try {
                ObjectInputStream in = new ObjectInputStream(
                        new FileInputStream(f));

                computer = (Computer) in.readObject();
                in.close();
                System.out.println("Computer loaded.");
            } catch (IOException e) {
                System.out.println("Error processing file.");
            } catch (ClassNotFoundException c) {
                System.out.println("Class not found.");
            }
        }
    }
}
