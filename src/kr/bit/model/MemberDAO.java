package kr.bit.model;

// JDBC -> myBatis, JPA
import java.sql.*;
import java.util.ArrayList;
public class MemberDAO {
	private Connection conn; // 데이터베이스 연결객체
	private PreparedStatement ps; // SQL 전송 객체
	private ResultSet rs; // DB의 데이터를 가지고 와서 저장할 수 있는 객체
	
	// 데이터베이스 연결객체 생성 - MySQL
	public void getConnect() {
		// 데이터베이스접속URL -> 벤더마다 접속URL은 다르다.
		String URL = "jdbc:mysql://localhost:3306/test?characterEncoding=UTF-8&serverTimeZone=UTC";
		String user = "root";
		String password = "root";
		
		// MySQL Driver Loading
		try {
			// 정적로딩: MySQL -> Oracle 변경 시 클래스 부분을 전부 변경해줘야함
			//DriverManager driver = new com.mysql.jdbc.Driver();
			//conn = driver.getConnection(URL, user, password);
			
			// 동적로딩(실행시점에서 객체를 생성하는 방법) <-> 정적로딩(컴파일 시점에서 객체를 만듦)
			Class.forName("com.mysql.jdbc.Driver"); // lib에서 com.mysql.jdbc.Driver 클래스를 찾아 메모리에 Loading
			
			// DriverManager: Driver 관리하는 클래스
			conn = DriverManager.getConnection(URL, user, password); // MySQL 접속시도 -> 접속 성공시 연결정보를 넘겨준다.
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 회원저장 동작
	public int memberInsert(MemberVO vo) {
		//                                                            ?: parameter 1  2  3  4  5  6
		String SQL = "insert into member(id, pass, name, age, email, phone) values(?, ?, ?, ?, ?, ?)";
		getConnect();
		int cnt = 0;
		
		// SQL 문장을 전송하는 객체 생성 - PreparedStatement
		try {
			ps = conn.prepareStatement(SQL); // 미리 컴파일을 시킨다(preCompile) -> 속도향상, 오타체크
			ps.setString(1, vo.getId());
			ps.setString(2, vo.getPass());
			ps.setString(3, vo.getName());
			ps.setInt(4, vo.getAge());
			ps.setString(5, vo.getEmail());
			ps.setString(6, vo.getPhone());
			
			cnt = ps.executeUpdate(); // DB 전송(실행) -> 완성된 SQL문을 실행, return: 성공한 row 수, insert 시 table이 update되기 때문
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return cnt; // 성공: 1 or 실패: 0
	}
	
	// 회원(VO) 전체 List(ArrayList) 가져오기
	public ArrayList<MemberVO> memberList() {
		String SQL = "select * from member";
		getConnect();
		ArrayList<MemberVO> list = new ArrayList<>();
		
		try {
			ps = conn.prepareStatement(SQL); // ?(parameter)이 없어도 먼저 SQL 컴파일해도 상관없다.
			// rs -> 커서
			rs = ps.executeQuery(); // table의 변화가 없으며, select의 결과값만 가지고 오기 때문에
			
			while(rs.next()) {
				int num = rs.getInt("num");
				String id = rs.getString("id");
				String pass = rs.getString("pass");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String email = rs.getString("email");
				String phone = rs.getString("phone");
				
				// VO 묶고 -> 담기(ArrayList)
				MemberVO vo = new MemberVO(num, id, pass, name, age, email, phone);
				list.add(vo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return list;
	}
	
	// 회원정보 삭제
	public int memberDelete(int num) {
		String SQL = "delete from member where num=?";
		getConnect();
		int cnt = -1;
		
		try {
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, num);
			cnt = ps.executeUpdate(); // DB 실행 -> TABLE 변동
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return cnt;
	}
	
	// 회원정보 상세보기
	public MemberVO memberContent(int num) {
		String SQL = "select * from member where num=?";
		getConnect();
		MemberVO vo = null;
		try {
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, num);
			rs = ps.executeQuery();
			if (rs.next()) {
				// 회원 1명의 정보를 가져와서 MemberVO 객체에 묶기
				num = rs.getInt("num");
				String id = rs.getString("id");
				String pass = rs.getString("pass");
				String name = rs.getString("name");
				int age = rs.getInt("age");
				String email = rs.getString("email");
				String phone = rs.getString("phone");
				vo = new MemberVO(num, id, pass, name, age, email, phone);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return vo;
	}
	
	// 회원정보 수정
	public int memberUpdate(MemberVO vo) {
		String SQL = "update member set age=?, email=?, phone=? where num=?";
		getConnect();
		int cnt = -1;
		
		try {
			ps = conn.prepareStatement(SQL);
			ps.setInt(1, vo.getAge());
			ps.setString(2, vo.getEmail());
			ps.setString(3, vo.getPhone());
			ps.setInt(4, vo.getNum());
			cnt=ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbClose();
		}
		return cnt;
	}
	
	// 데이터베이스 연결 끊기 - 메서드 호출 시 DB 연결하고 메서드가 수행완료되면 DB 연결 끊어주기
	public void dbClose() {
		try {
			if (rs != null) rs.close();
			if (ps != null) ps.close();
			if (conn != null) conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}