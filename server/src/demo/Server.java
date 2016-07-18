package demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

	public static List<Socket> clients = new ArrayList<Socket>();

	public static void main(String[] args) {
		int port = 1025;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				Socket client = serverSocket.accept();
				clients.add(client);
				new Thread(new Client(client)).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static class Client implements Runnable {
		Socket client;
		BufferedReader br;

		public Client(Socket client) throws IOException {
			this.client = client;
			br = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
		}

		public String read() {
			try {
				return br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				clients.remove(client);
			}
			return null;

		}

		public void run() {
			// TODO Auto-generated method stub
			try {

				String msg = null;
				while ((msg = read()) != null) {
					for (Socket s : clients) {
						PrintStream pw = new PrintStream(s.getOutputStream());
						pw.println(msg);
						System.out.println(msg);
					}
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}

		}
	}

}
