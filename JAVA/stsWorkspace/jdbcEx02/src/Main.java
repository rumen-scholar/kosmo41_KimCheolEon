import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Main {

	static {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		try {
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "scott", "tiger");

			Statement stmt = con.createStatement();

			// -----------------------------------------------------------

			//한번 생성후에 같음 이름으로 생성하려고 하면 당연히 안됨. 테이블 날려야함. or replace 쓰던가
			//append 특성상 공백까지 알아서 넣어줘야 하느니라.
			StringBuffer sb = new StringBuffer();
			sb.append("create table test1 (");
			sb.append("  id varchar(10), ");
			sb.append("  age number ) ");

			int updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("createCount : " + updateCount);

			// -----------------------------------------------------------

			sb.setLength(0);
			sb.append("insert into test1 ");
			sb.append("values ('홍길동', 10) ");

			updateCount = stmt.executeUpdate(sb.toString());
			System.out.println("createCount : " + updateCount);

			// -----------------------------------------------------------

			sb.setLength(0);
			sb.append("select * from test1 ");
			ResultSet rs = stmt.executeQuery(sb.toString());
			while (rs.next()) {
				System.out.print("id : " + rs.getString(1) + ", ");
				System.out.println("age : " + rs.getString("age"));
			}

			 //-----------------------------------------------------------
			
			 sb.setLength(0);
			 //쿼리에 실수로 괄호침
			 sb.append("update test1 ");
			 sb.append(" set id='전우치', ");
			 sb.append(" age=20");
			 sb.append(" where id='홍길동' ");
			 updateCount = stmt.executeUpdate(sb.toString());
			 System.out.println("updateCount : " + updateCount);
			
			 //-----------------------------------------------------------
			
			 sb.setLength(0);
			 sb.append("select * from test1");
			 rs = stmt.executeQuery(sb.toString());
			 while(rs.next()) {
			 System.out.print("id : " + rs.getString(1) + ", ");
			 System.out.println("age : " + rs.getString("age"));
			 }
			
			 //-----------------------------------------------------------
			
//			 sb.setLength(0);
//			 sb.append("delete from test1");
//			 rs = stmt.executeQuery(sb.toString());
//			 System.out.println("deleteCount : " + updateCount);
//			
//			 //-----------------------------------------------------------
//			
//			 sb.setLength(0);
//			 sb.append("drop table test1");
//			 rs = stmt.executeQuery(sb.toString());
//			 System.out.println("dropCount : " + updateCount);
			
			 //-----------------------------------------------------------

			rs.close();
			stmt.close();
			con.close();

		} catch (Exception e) {
			System.out.println("Connection Error");
			e.printStackTrace();
		}
	}

}
