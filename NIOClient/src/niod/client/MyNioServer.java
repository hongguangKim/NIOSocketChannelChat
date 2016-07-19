package niod.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class MyNioServer {

	protected Selector selector;
	static int BufferSize = 2 * 1024;
	protected ByteBuffer clientBuffer = ByteBuffer.allocate(BufferSize);
	ServerSocketChannel server;

	public MyNioServer(int port) throws IOException {
		selector = this.getSelector(port);

	}

	// ��ȡSelector
	protected Selector getSelector(int port) throws IOException {
		Selector sel = Selector.open();
		server = ServerSocketChannel.open();
		server.socket().bind(new InetSocketAddress("localhost", port));
		server.configureBlocking(false);
		server.register(sel, SelectionKey.OP_ACCEPT);
		return sel;
	}

	// �����˿�
	public void listen() {
		try {
			/*
			 * ���ǵ��� Selector �� select() ���������������������ֱ��������һ����ע����¼�������
			 * ��һ�����߸�����¼�����ʱ�� select() �������������������¼����������÷�����������ִ�С�
			 */
			while (selector.select() > 0) {
					Iterator iter = selector.selectedKeys().iterator();
					while (iter.hasNext()) {
						/*
						 * �ڴ��� SelectionKey ֮�����Ǳ������Ƚ��������SelectionKey ��ѡ���ļ�������ɾ����
						 * �������û��ɾ��������ļ�����ô����Ȼ��������������һ������ļ����֣���ᵼ�����ǳ����ٴδ�������
						 * ���ǵ��õ������� remove() ������ɾ��������� SelectionKey��iter.remove();
						 */
						SelectionKey key = (SelectionKey) iter.next();
						iter.remove();
						process(key);
					}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// �����¼�
	protected void process(SelectionKey key) throws IOException {
		if (key.isAcceptable()) { // ��������
			SocketChannel channel = server.accept();
			// ���÷�����ģʽ
			channel.configureBlocking(false);
			//ע�������
			channel.register(selector, SelectionKey.OP_READ);
		} else if (key.isReadable()) { // ����Ϣ
			SocketChannel channel = (SocketChannel) key.channel();
			if (channel.read(clientBuffer) > 0) {
				// ����˻�����������������Ϊ��ǰλ�ã�Ȼ�󽫵�ǰλ������Ϊ0
				clientBuffer.flip();
				byte[] data = clientBuffer.array();
				System.out.println(new String(data).trim());
				clientBuffer.clear();
			}
			//ע��д����
			SelectionKey sKey = channel.register(selector,SelectionKey.OP_WRITE);
			//����д��Ӧ�ĸ���(ֵ)
			sKey.attach("hello! i am server!");
		} else if (key.isWritable()) { // д��Ϣ
			SocketChannel channel = (SocketChannel) key.channel();
			String name = (String) key.attachment();
			clientBuffer.put(name.getBytes());
			clientBuffer.flip();
			channel.write(clientBuffer);
			clientBuffer.clear();
			// ����close������ѭ��
			channel.close();
		}

	}

	public static void main(String[] args) {
		int port = 8888;
		try {
			MyNioServer server = new MyNioServer(port);
			server.listen();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
