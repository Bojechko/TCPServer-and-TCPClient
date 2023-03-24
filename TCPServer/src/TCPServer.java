import java.io.*;
import java.net.*;
import java.util.concurrent.ThreadLocalRandom;

public class TCPServer {

    public static void main(String[] args) {
        if (args.length < 1) return;

        int port = Integer.parseInt(args[0]);

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            System.out.println("Server is listening on port " + port);

            int randomNum = ThreadLocalRandom.current().nextInt(0, 10);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected");
                System.out.println(String.valueOf(randomNum));

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output, true);

                String[] words;

                do {
                    InputStream input = socket.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    words = reader.readLine().split(" ");

                    if (words[0].equals("GUESS")){
                        System.out.println(words[0] );
                        System.out.println(words[1] );
                        if (Integer.parseInt(words[1]) == randomNum) {
                            writer.println("Server: " + "EQUAL");

                            randomNum = ThreadLocalRandom.current().nextInt(0, 10);
                            System.out.println(String.valueOf(randomNum));
                        }

                        if (Integer.parseInt(words[1]) > randomNum) {
                            writer.println("Server: " + "MORE");
                        }

                        if (Integer.parseInt(words[1]) < randomNum) {
                            writer.println("Server: " + "LESS");
                        }
                    } else {
                        writer.println("Server: " + "wrong command");
                    }

                } while (!words[0].equals("bye"));

                socket.close();
            }

        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}