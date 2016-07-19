package nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class MyNioClient {

	static class Message implements Runnable {
		String msg = "";

		public Message(String msg) {
			this.msg = msg;
		}

		public void run() {
			try {
				// 打开Socket通道
				DatagramChannel channel = DatagramChannel.open();
				channel.socket().bind(new InetSocketAddress(8888));
				// 分配内存
				ByteBuffer buffer = ByteBuffer.allocate(8 * 1024);
				buffer.clear();
				buffer.put(msg.getBytes());
				buffer.flip();
				channel.send(buffer, new InetSocketAddress("localhost",8888));
				while (true) {
					buffer.clear();
					channel.receive(buffer);
					buffer.flip();
					System.out.println("Time request from "+ new String(buffer.array()));
					
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException {
		new Thread(new Message("hello! i am client!")).start();
	}
}
