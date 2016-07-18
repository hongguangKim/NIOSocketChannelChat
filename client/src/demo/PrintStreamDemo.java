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

		// System.out.println(ip);
		// String ipStart = "192.168.0.1";
		// String ipEnd = "192.168.1.255";
		// String[] is = ipStart.split("\\.");
		// String[] ie = ipEnd.split("\\.");
		// int[] ipsInt = new int[4];
		// int[] ipeInt = new int[4];
		// for (int i = 0; i < 4; i++) {
		// ipsInt[i] = Integer.parseInt(is[i]);
		// ipeInt[i] = Integer.parseInt(ie[i]);
		// }
		// byte[] buf = new byte[1024];
		// String chatKey = "SLEEKNETGEOCK4stsjeS";
		// Socket s = new Socket();
		// InetSocketAddress isa = new InetSocketAddress("192.168.137.1",
		// Integer.parseInt(port));
		// s.connect(isa, 5000);
		// DataInputStream dis;
		// if (s.isConnected()) {
		// DataOutputStream dos = new DataOutputStream(s.getOutputStream());
		// dis = new DataInputStream(s.getInputStream());
		// dos.writeUTF(chatKey + "online:" + "hongguang");
		// String reMsg;
		// while ((reMsg = dis.readUTF()) != null) {
		// System.out.println(reMsg);
		// }
		// }
		// for (int i = ipsInt[0]; i <= ipeInt[0]; i++) {
		// boolean flag = i < ipeInt[0];
		// int k = i != ipsInt[0] ? 0 : ipsInt[1];
		// for (; flag ? k < 256 : k <= ipeInt[1]; k++) {
		// boolean flag2 = flag || k < ipeInt[1];
		// int j = k != ipsInt[1] ? 0 : ipsInt[2];
		// for (; flag2 ? j < 256 : j <= ipeInt[2]; j++) {
		// boolean flag3 = flag2 || j < ipeInt[2];
		// int l = j != ipsInt[2] ? 0 : ipsInt[3];
		// for (; flag3 ? l < 256 : l <= ipeInt[3]; l++) {
		// String userip = i + "." + k + "." + j + "." + l;
		// if (InetAddress.getByName(userip).isReachable(100))
		// System.out.println(userip);
		// }
		// }
		// }
		// }
		// System.out.println("end");
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
