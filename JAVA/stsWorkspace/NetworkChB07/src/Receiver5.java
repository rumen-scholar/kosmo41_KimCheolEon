import java.io.*;
import java.net.*;

//서버로 메시지를 전송하는 클래스
public class Receiver5 extends Thread {

	Socket socket;
	BufferedReader in = null;

	// Socket을 매개변수로 받는 생성자
	public Receiver5(Socket socket) {
		this.socket = socket;

		try {
			in = new BufferedReader(
					new InputStreamReader(
							this.socket.getInputStream()));
			
		} catch (Exception e) {
			System.out.println("예외1[Receiver3 Class] : " + e);
		}
	}

	// run() 메소드 재정의 - 오버라이드
	@Override
	public void run() {
		while (in != null) {
			try {
				System.out.println("Thread Receive : " + in.readLine());

			} catch (java.net.SocketException ne) {
				break;
			} catch (Exception e) {
				System.out.println("예외2[Receiver3 run()] : " + e);
			}
		}

		try {
			in.close();
		} catch (Exception e) {
			System.out.println("예외3[run() close] : " + e);
		}
	}
}
