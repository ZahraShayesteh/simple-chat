package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class client {
	static String in_con = "";// input from console
	static String line = "";// input stream

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		Scanner input = new Scanner(System.in);
		Socket client = new Socket("127.0.0.1", 8090);
		DataInputStream is = new DataInputStream(client.getInputStream());
		DataOutputStream os = new DataOutputStream(client.getOutputStream());
		class writeToSocketThread extends Thread { // writing from console on
													// socket
			public void run() {
				while (!in_con.equals("CLOSE") & !line.equals("CLOSE")) {// till
																			// "Close"
																			// is
																			// not
																			// entered
																			// or
																			// received
																			// from
																			// socket
																			// continue
																			// the
																			// loop
					in_con = input.nextLine();// getting the input from console
					try {
						if (line.equals("CLOSE") || in_con.equals("CLOSE")) {
							os.writeBytes(in_con + "\n");
							continue;
						}
						os.writeBytes(in_con + "\n");
					} catch (IOException e) {
						System.out.println("error cw");
					}
				}
			}
		}

		class readFromSocketThread extends Thread {// reading from socket and
													// writing it on the console
			public void run() {
				while (!line.equals("CLOSE") & !in_con.equals("CLOSE")) {// till
																			// "Close"
																			// is
																			// not
																			// entered
																			// or
																			// received
																			// from
																			// socket
																			// continue
																			// the
																			// loop
					try {
						line = is.readLine();// reading the input stream from
												// socket
						if (line.equals("CLOSE") || in_con.equals("CLOSE")) {
							continue;
						}
						System.out.println(line);
					} catch (IOException e) {
						System.out.println("error cr");
					}
				}
			}
		}
		readFromSocketThread tread = new readFromSocketThread();
		writeToSocketThread twrite = new writeToSocketThread();
		tread.start();// starting the read thread
		twrite.start();// starting the write thread
	}
}
