import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;

public class Main {
	static Socket socket;
	static BufferedReader reader;
	static StringTokenizer tokenizer;
	static PrintWriter writer;

	static final boolean DEBUG = true;

	static String nextToken() throws IOException {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			String line = reader.readLine();
			if (DEBUG) {
				System.err.println("SERVER LINE: " + line);
			}
			tokenizer = new StringTokenizer(line);
		}
		String token = tokenizer.nextToken();
		return token;
	}

	static int nextInt() throws NumberFormatException, IOException {
		return Integer.parseInt(nextToken());
	}

	static double nextDouble() throws NumberFormatException, IOException {
		return Double.parseDouble(nextToken());
	}

	static void send(String message) {
		if (DEBUG) {
			System.err.println("ME: " + message);
		}
		writer.println(message);
		writer.flush();
	}

	static boolean readOK() throws IOException {
		String s = nextToken();
		if (s.equals("OK")) {
			return true;
		} else {
			tokenizer = null;
			return false;
		}
	}

	public static void doWait() throws IOException {
		send("WAIT");
		readOK();
		// nextToken();// WAITING, required in deadline24
		// nextDouble();// time, required in deadline24
		readOK();
	}

	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, -1, 0, 1 };

	public static void main(String[] args) throws InterruptedException {
		while (true) {
			try {
				String host = "tr.contest.pizza";
				int port = 10000;
				socket = new Socket(host, port);
				socket.setTcpNoDelay(true);
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream());
				tokenizer = null;
				nextToken();// LOGIN
				send("team1337");
				nextToken();// PASS
				send("Password1");
				readOK();
				while (true) {
					send("TURNS_LEFT");
					if (readOK()) {
						int turns = nextInt();
						System.err.println("Turn " + turns);
					}

					int dir = (int) (Math.random() * 4);
					send("MOVE " + dx[dir] + " " + dy[dir]);
					readOK();

					doWait();
				}
			} catch (Exception e) {
				System.err.println("DEAD: " + e.getMessage());
				Thread.sleep(1337);
			}
		}
	}
}
