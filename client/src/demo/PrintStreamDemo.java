package demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class PrintStreamDemo {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String ip = InetAddress.getLocalHost().getHostAddress();
		int port = 1025;
		Socket socket = new Socket(ip, port);
		new Thread(new ClientReceiver(socket)).start();

		PrintStream pw = new PrintStream(socket.getOutputStream());
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String msg = null;
		while ((msg = br.readLine()) != null) {
			pw.println(msg);
		}
	}

	public static class ClientReceiver implements Runnable {
		Socket client;
		BufferedReader br;

		public ClientReceiver(Socket client) throws IOException {
			this.client = client;
			br = new BufferedReader(new InputStreamReader(
					client.getInputStream()));
		}

		public void run() {
			// TODO Auto-generated method stub
			try {
				String msg = null;
				while ((msg = br.readLine()) != null) {
					System.out.println(msg);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
