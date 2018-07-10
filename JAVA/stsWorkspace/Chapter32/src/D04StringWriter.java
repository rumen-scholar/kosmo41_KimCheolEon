//Buffered Read, Write

import java.io.*;

public class D04StringWriter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String ks = "공부에 있어서 돈이 꼭 필요한 것은 아니다.";
		String es = "Life is long if you know ho to use it.";
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("String.txt"))) {
			bw.write(ks, 0, ks.length());
			bw.newLine();	// 운영체제 별 줄 바꿈의 표시방법이 다르다고 함
			bw.write(es, 0, es.length());
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
