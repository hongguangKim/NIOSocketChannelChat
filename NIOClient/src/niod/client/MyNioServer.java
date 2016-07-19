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

	// 获取Selector
	protected Selector getSelector(int port) throws IOException {
		Selector sel = Selector.open();
		server = ServerSocketChannel.open();
		server.socket().bind(new InetSocketAddress("localhost", port));
		server.configureBlocking(false);
		server.register(sel, SelectionKey.OP_ACCEPT);
		return sel;
	}

	// 监听端口
	public void listen() {
		try {
			/*
			 * 我们调用 Selector 的 select() 方法。这个方法会阻塞，直到至少有一个已注册的事件发生。
			 * 当一个或者更多的事件发生时， select() 方法将返回所发生的事件的数量。该方法必须首先执行。
			 */
			while (selector.select() > 0) {
					Iterator iter = selector.selectedKeys().iterator();
					while (iter.hasNext()) {
						/*
						 * 在处理 SelectionKey 之后，我们必须首先将处理过的SelectionKey 从选定的键集合中删除。
						 * 如果我们没有删除处理过的键，那么它仍然会在主集合中以一个激活的键出现，这会导致我们尝试再次处理它。
						 * 我们调用迭代器的 remove() 方法来删除处理过的 SelectionKey：iter.remove();
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

	// 处理事件
	protected void process(SelectionKey key) throws IOException {
		if (key.isAcceptable()) { // 接收请求
			SocketChannel channel = server.accept();
			// 设置非阻塞模式
			channel.configureBlocking(false);
			//注册读动作
			channel.register(selector, SelectionKey.OP_READ);
		} else if (key.isReadable()) { // 读信息
			SocketChannel channel = (SocketChannel) key.channel();
			if (channel.read(clientBuffer) > 0) {
				// 重设此缓冲区，将限制设置为当前位置，然后将当前位置设置为0
				clientBuffer.flip();
				byte[] data = clientBuffer.array();
				System.out.println(new String(data).trim());
				clientBuffer.clear();
			}
			//注册写动作
			SelectionKey sKey = channel.register(selector,SelectionKey.OP_WRITE);
			//传递写对应的附件(值)
			sKey.attach("hello! i am server!");
		} else if (key.isWritable()) { // 写信息
			SocketChannel channel = (SocketChannel) key.channel();
			String name = (String) key.attachment();
			clientBuffer.put(name.getBytes());
			clientBuffer.flip();
			channel.write(clientBuffer);
			clientBuffer.clear();
			// 不加close会无限循环
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
