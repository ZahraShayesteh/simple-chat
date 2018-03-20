package HW11_simpleChat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	static String line1 = "";
	static String line2 = "";

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		ServerSocket server = new ServerSocket(10010);
		Socket client1 = server.accept();
		Socket client2 = server.accept();
		DataInputStream is1 = new DataInputStream(client1.getInputStream());
		DataOutputStream os1 = new DataOutputStream(client1.getOutputStream());
		DataInputStream is2 = new DataInputStream(client2.getInputStream());
		DataOutputStream os2 = new DataOutputStream(client2.getOutputStream());

		class readFromSocketClient1Thread extends Thread {
			public void run() {
				while (!line1.equals("q") & !line2.equals("q")) {
					try {
						line1 = is1.readLine();
						try {
							if (line1.equals("q") || line2.equals("q")) {
								os2.writeBytes(line1 + "\n");
								System.exit(0);
							}
							os2.writeBytes(line1 + "\n");
						} catch (IOException e) {
						}
					} catch (IOException e) {
					}
				}
			}
		}

		class readFromSocketClient2Thread extends Thread {
			public void run() {
				while (!line1.equals("q") & !line2.equals("q")) {
					try {
						line2 = is2.readLine();
						try {
							if (line1.equals("q") || line2.equals("q")) {
								os1.writeBytes(line2 + "\n");
								System.exit(0);
							}
							os1.writeBytes(line2 + "\n");
						} catch (IOException e) {
						}
					} catch (IOException e) {
					}
				}
			}
		}
		readFromSocketClient1Thread tread1 = new readFromSocketClient1Thread();
		readFromSocketClient2Thread tread2 = new readFromSocketClient2Thread();
		tread1.start();
		tread2.start();
		if (line1.equals("q") || line2.equals("q")) {
			server.close();
			client1.close();
			client2.close();
		}
	}
}