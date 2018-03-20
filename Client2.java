package HW11_simpleChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client2 {
	static String in_con = "";
	static String line = "";

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		Socket client = new Socket("127.0.0.1", 10010);
		DataInputStream is = new DataInputStream(client.getInputStream());
		DataOutputStream os = new DataOutputStream(client.getOutputStream());
		class writeToSocketThread extends Thread {
			public void run() {
				while (!in_con.equals("q") & !line.equals("q")) {
					in_con = input.nextLine();
					try {
						if (line.equals("q") || in_con.equals("q")) {
							os.writeBytes(in_con + "\n");
							System.exit(0);
						}
						os.writeBytes(in_con + "\n");
					} catch (IOException e) {
					}
				}
			}
		}

		class readFromSocketThread extends Thread {
			public void run() {
				while (!line.equals("q") & !in_con.equals("q")) {
					try {
						line = is.readLine();
						if (line.equals("q") || in_con.equals("q"))
							System.exit(0);
						System.out.println(line);
					} catch (IOException e) {
					}
				}
			}
		}
		readFromSocketThread tread = new readFromSocketThread();
		writeToSocketThread twrite = new writeToSocketThread();
		tread.start();
		twrite.start();
		if (line.equals("q") || in_con.equals("q")) {
			client.close();
			input.close();
		}
	}
}
