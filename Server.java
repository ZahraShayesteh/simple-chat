package fgh;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {
	static String line1 = "";//input stream of client1
	static String line2 = "";//input stream of client2

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		ServerSocket server = new ServerSocket(10010);
		Socket client1 = server.accept();
		Socket client2 = server.accept();
		DataInputStream is1 = new DataInputStream(client1.getInputStream());
		DataOutputStream os1 = new DataOutputStream(client1.getOutputStream());
		DataInputStream is2 = new DataInputStream(client2.getInputStream());
		DataOutputStream os2 = new DataOutputStream(client2.getOutputStream());

		class readFromSocketClient1Thread extends Thread {//observing client1
			public void run() {
				while (!line1.equals("q") & !line2.equals("q")) {
					try {
						line1 = is1.readLine();//reading input stream of socket client 1
						if (line1.equals("q") || line2.equals("q")) {
							os2.writeBytes(line1 + "\n");//writing line1 on socket client2
							System.exit(0);//chat will terminate
						}
						os2.writeBytes(line1 + "\n");//writing line1 on socket client2
					} catch (IOException e) {
					}
				}
			}
		}

		class readFromSocketClient2Thread extends Thread {//observing client2
			public void run() {
				while (!line1.equals("q") & !line2.equals("q")) {
					try {
						line2 = is2.readLine();//reading input stream of socket client 2
						if (line1.equals("q") || line2.equals("q")) {
							os1.writeBytes(line2 + "\n");//writing line2 on socket client1
							System.exit(0);//chat will terminate
						}
						os1.writeBytes(line2 + "\n");//writing line2 on socket client1
					} catch (IOException e) {
					}
				}
			}
		}
		readFromSocketClient1Thread tread1 = new readFromSocketClient1Thread();
		readFromSocketClient2Thread tread2 = new readFromSocketClient2Thread();
		tread1.start();//starting the thread of observing client1
		tread2.start();//starting the thread of observing client2
		if (line1.equals("q") || line2.equals("q")) {//if input streams of sockets are "q" socket and chat will terminated
			server.close();
			client1.close();
			client2.close();
		}
	}
}
