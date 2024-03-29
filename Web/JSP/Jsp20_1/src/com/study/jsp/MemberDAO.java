package com.study.jsp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class MemberDAO {
	/*
	 * private Connection con; private PreparedStatement pstmt;
	 */

	/*private String driver = "oracle.jdbc.driver.OracleDriver";*/
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String uid = "scott";
	private String upw = "tiger";

	public MemberDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<MemberDTO> memberSelect() {

		ArrayList<MemberDTO> dtos = new ArrayList<MemberDTO>();

		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			con = DriverManager.getConnection(url, uid, upw);
			stmt = con.createStatement();
			rs = stmt.executeQuery("select * from member");

			while (rs.next()) {
				String id = rs.getString("id");
				String pw = rs.getString("pw");
				String name = rs.getString("name");
				String phone = rs.getString("phone");
				String gender = rs.getString("gender");

				MemberDTO dto = new MemberDTO(id, pw, name, phone, gender);

				dtos.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) rs.close();
				if(stmt != null) stmt.close();
				if(con != null) con.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		return dtos;
	}
}
