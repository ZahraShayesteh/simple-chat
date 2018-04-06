package chat;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import com.sun.xml.internal.ws.assembler.jaxws.TerminalTubeFactory;

public class server {
	static ArrayList<String> onlineFreeClients = new ArrayList<String>();// list
																			// of
																			// online
																			// free
																			// users
	static String[] usernames = { "zahra", "roghayeh", "fatemeh", "mahya", "atefeh", "maryam", "parisa" };// list
																											// of
																											// usernames
	static String[] passwords = { "zahra", "roghayeh", "fatemeh", "mahya", "atefeh", "maryam", "parisa" };// list
																											// of
																											// passwords
	static HashMap<String, Boolean> availableFlags = new HashMap<String, Boolean>();// list
																					// of
																					// the
																					// usernames
																					// that
																					// are
																					// not
																					// busy
																					// in
																					// a
																					// chat
	static HashMap<String, DataInputStream> map_in = new HashMap<String, DataInputStream>();// list
																							// of
																							// users
																							// with
																							// their
																							// input
																							// streams
	static HashMap<String, DataOutputStream> map_out = new HashMap<String, DataOutputStream>();// list
																								// of
																								// users
																								// with
																								// their
																								// output
																								// streams

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		ServerSocket server = new ServerSocket(8090);
		while (true) {// accepting all sockets who want to connect to server and
						// create a thread for them
			Socket client = server.accept();// accept a socket
			DataInputStream is = new DataInputStream(client.getInputStream());
			DataOutputStream os = new DataOutputStream(client.getOutputStream());
			class ReadSocketClientThread extends Thread {// thread of each
															// socket which
															// listen always to
															// it
				int i;
				String[] splittedInput;// input[]
				int loginFlag = -1;// if socket is login this will change

				public void run() {
					try {
						loop1: while (loginFlag == -1) {
							splittedInput = is.readLine().split(" ");// reading
																		// the
																		// input
							if (splittedInput[0].equals("LOGIN")) {// if input
																	// contains
																	// LOGIN
								for (i = 0; i < usernames.length; i++)
									if (splittedInput[1].equals(usernames[i]))// if
																				// a
																				// username
																				// come
																				// after
																				// LOGIN
										if (splittedInput[1].equals(passwords[i])) {// if
																					// the
																					// username's
																					// password
																					// come
																					// after
																					// username
											onlineFreeClients.add(usernames[i]);// adding
																				// the
																				// username
																				// to
																				// online
																				// free
																				// clients
											map_in.put(usernames[i], is);// adding
																			// its
																			// is
																			// to
																			// is
																			// list
											map_out.put(usernames[i], os);// adding
																			// its
																			// os
																			// to
																			// os
																			// list
											availableFlags.put(usernames[i], true);// available
																					// flag
																					// of
																					// it
																					// will
																					// set
																					// to
																					// true
											while (true) {// listening to a
															// client after
															// login of it
												if (availableFlags.get(usernames[i]) == true) {// if
																								// user
																								// is
																								// not
																								// busy
																								// in
																								// a
																								// chat
													splittedInput = map_in.get(usernames[i]).readLine().split(" ");// reading
																													// the
																													// is
													if (splittedInput[0].equals("LIST")) {// if
																							// input
																							// is
																							// the
																							// list
																							// order
														String list = onlineFreeClients.size() + " ";
														for (int j = 0; j < onlineFreeClients.size(); j++)
															list = list + onlineFreeClients.get(j) + " ";
														map_out.get(usernames[i]).writeBytes(list + "\n");
													} else if (splittedInput[0].equals("CLOSE")) {// if
																									// the
																									// input
																									// is
																									// the
																									// close
																									// order
														onlineFreeClients.remove(usernames[i]);
														map_in.remove(usernames[i]);
														map_out.remove(usernames[i]);
														availableFlags.remove(usernames[i]);
														client.close();// closing
																		// the
																		// socket
														break loop1;// go to the
																	// end of
																	// the
																	// client's
																	// thread
													} else if (splittedInput[0].equals("CONNECT")) {// if
																									// the
																									// input
																									// is
																									// the
																									// connect
																									// order
														for (int j = 0; j < onlineFreeClients.size(); j++)
															if (splittedInput[1].equals(onlineFreeClients.get(j))) {
																onlineFreeClients.remove(usernames[i]);// source
																// client//removing
																// current user
																// from online
																// list
																onlineFreeClients.remove(splittedInput[1]);// destination
																// client//removing
																// target user
																// from online
																// list
																availableFlags.put(usernames[i], false);
																availableFlags.put(splittedInput[1], false);
																// connect(i,
																// splittedInput[1]);
																// while(availableFlags.get(usernames[i]).equals(false))
																// continue;
																class readFromSocket1stClientThread extends Thread {// writing
																													// is1
																													// on
																													// os2
																	String line1 = "";

																	public void run() {
																		while (!line1.equals("CLOSE")) {
																			try {

																				line1 = map_in.get(usernames[i])
																						.readLine();// reading
																									// is1
																				if (line1.equals("CLOSE")) {
																					map_out.get(splittedInput[1])
																							.writeBytes(line1 + "\n");// writing
																														// is1
																														// on
																														// os2
																					availableFlags.put(usernames[i],
																							true);
																					availableFlags.put(splittedInput[1],
																							true);
																				}
																				map_out.get(splittedInput[1])
																						.writeBytes(line1 + "\n");// writing
																													// is1
																													// on
																													// os2
																			} catch (IOException e) {
																			}
																		}
																	}
																}
																class readFromSocket2ndClientThread extends Thread {// writing
																													// is2
																													// on
																													// os1
																	String line2 = "";

																	public void run() {
																		while (!line2.equals("CLOSE")) {
																			try {

																				line2 = map_in.get(splittedInput[1])
																						.readLine();// reading
																									// is2
																				if (line2.equals("CLOSE")) {
																					map_out.get(usernames[i])
																							.writeBytes(line2 + "\n");// writing
																														// is2
																														// on
																														// os1
																					availableFlags.put(splittedInput[1],
																							true);
																					availableFlags.put(usernames[i],
																							true);
																				}
																				map_out.get(usernames[i])
																						.writeBytes(line2 + "\n");// writing
																													// is2
																													// on
																													// os1
																			} catch (IOException e) {
																			}
																		}

																	}
																}
																readFromSocket1stClientThread tread1 = new readFromSocket1stClientThread();
																readFromSocket2ndClientThread tread2 = new readFromSocket2ndClientThread();
																tread2.start();
																tread1.start();
																boolean flag = true;
																while (flag) {
																	if (availableFlags.get(usernames[i]).equals(true)
																			&& availableFlags.get(splittedInput[1])
																					.equals(true)) {
																		onlineFreeClients.add(splittedInput[1]);
																		onlineFreeClients.add(usernames[i]);
																		break;
																	} // while
																		// the
																		// clients
																		// are
																		// not
																		// free
																		// this
																		// loop
																		// will
																} // continue
																tread1.stop();// if
																				// client
																				// become
																				// free
																				// its
																				// chat
																				// thread
																				// will
																				// terminate
																tread2.stop();// "
																break;
															}
													} else
														continue;
												}
											}
										}
							} else if (splittedInput[0].equals("CLOSE")) {// if
																			// client
																			// order
																			// close
																			// before
																			// login
								client.close();
							} else// if the input of client is sth different of
									// login or close
								loginFlag = -1;// client haven't been login yet
						}
					} catch (IOException e) {
					}
				}
			}
			ReadSocketClientThread t1 = new ReadSocketClientThread();
			t1.start();// staring the thread of each client
		}
	}

	public static void connect(int i, String splittedInput1) {// connect order
		class readFromSocket1stClientThread extends Thread {// writing is1 on
															// os2
			String line1 = "";

			public void run() {
				while (!line1.equals("CLOSE")) {
					try {

						line1 = map_in.get(usernames[i]).readLine();// is1
						if (line1.equals("CLOSE")) {
							map_out.get(splittedInput1).writeBytes(line1 + "\n");// os2
							onlineFreeClients.add(usernames[i]);
							availableFlags.put(usernames[i], true);
							// stop();
						}
						map_out.get(splittedInput1).writeBytes(line1 + "\n");// os2
					} catch (IOException e) {
					}
				}
			}
		}
		class readFromSocket2ndClientThread extends Thread {// writing is2 on
															// os1
			String line2 = "";

			public void run() {
				while (!line2.equals("CLOSE")) {
					try {

						line2 = map_in.get(splittedInput1).readLine();// is2
						if (line2.equals("CLOSE")) {
							map_out.get(usernames[i]).writeBytes(line2 + "\n");// os1
							onlineFreeClients.add(splittedInput1);
							availableFlags.put(splittedInput1, true);
							// stop();
						}
						map_out.get(usernames[i]).writeBytes(line2 + "\n");// os1
					} catch (IOException e) {
					}
				}

			}
		}
		readFromSocket1stClientThread tread1 = new readFromSocket1stClientThread();
		readFromSocket2ndClientThread tread2 = new readFromSocket2ndClientThread();
		tread2.start();
		tread1.start();
	}
}
