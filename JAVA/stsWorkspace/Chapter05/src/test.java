import java.util.*;


public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Random randromV1 = new Random();
				
		System.out.println(randromV1.nextBoolean());
		System.out.println(randromV1.nextFloat());
		System.out.println(randromV1.nextInt());
		System.out.println(randromV1.nextInt(100));
		
		double randomV2 = Math.random();
		System.out.println("1 : " + randomV2);
		int intVal = (int)(randomV2 * 100);
		System.out.println("2 : " + intVal);
	}

}
