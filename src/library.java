import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class library
{
	public Connection con;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs;
	Scanner sc = new Scanner(System.in);
	
	public library() {
		// Oracle 연결
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			String id = "homework";
			String pass = "1234";

			con = DriverManager.getConnection(url,id,pass);
			System.out.println("Oracle 연결성공!");
			System.out.println();
		}
		catch(Exception e) {
			System.out.println("Oracle 연결시 예외발생");
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		library book = new library();
		book.doRun();
	}
	
	// 메뉴 선택
	public void showMenu()
	{
		System.out.println("┌────<메뉴 선택>────┐");
		System.out.println("│1.   회원 관리     │");
		System.out.println("│2.   도서 관리     │");
		System.out.println("│3.   도서 대여     │");
		System.out.println("│4.  프로그램 종료  │");
		System.out.println("└───────────────────┘");
		System.out.print("메뉴를 선택해 주세요 : ");
	}
	
	public void doRun() {
		while(true) {
			showMenu();
			int choice = sc.nextInt();
			sc.nextLine();
			switch(choice) {
			case 1:
				MemberRun();
				break;
			case 2:
				BookRun();
				break;
			case 3:
				System.out.println("시스템 작업 중입니다. 다른 메뉴를 선택해 주세요.");
				break;
			case 4:
				System.out.println("도서 프로그램을 종료합니다. 감사합니다:) ");
				return;
			default:
				System.out.println("잘못 입력하셨습니다.");
				break;	
			}
		}
	}
	
	
	// 회원 메뉴 선택
	public void MemberMenu()
	{
		System.out.println("");
		System.out.println("┌────[메뉴 선택]────┐");
		System.out.println("│1.   회원 등록     │");
		System.out.println("│2.   회원 조회     │");
		System.out.println("│3. 모든 회원 조회  │");	
		System.out.println("│4.   회원 탈퇴     │");
		System.out.println("│5.      종료       │");
		System.out.println("└───────────────────┘");
		System.out.print("메뉴를 선택해 주세요 : ");
	}
	
	// 회원 프로그램 실행
	public void MemberRun() {
		while(true) {
			MemberMenu();
			int choice = sc.nextInt();
			sc.nextLine();
			switch(choice) {
			case 1:
				addMember();
				break;
			case 2:
				searchMember();
				break;
			case 3:
				allMember();
				break;
			case 4:
				delMember();
				break;
			case 5:
				System.out.println("회원 프로그램을 종료합니다.");
				System.out.println();
				return;
			default:
				System.out.println("잘못 입력하셨습니다.");
				break;	
			}
		}
	}
	
	// 1-1. 회원 등록
	public void addMember() {
		System.out.print("등록할 회원의 아이디를 입력해 주세요 : ");
		String id = sc.nextLine();
			try{	
			String sql = "select * from memberDB where id = ?";
			psmt = con.prepareStatement(sql);
			psmt.setString(1, id);
			rs = psmt.executeQuery();
			int nResult = 0;
			while(rs.next()) {
				nResult++;
				rs.getString("id");
				System.out.println();
				System.out.println(rs.getString("id")+"는 이미 사용중인 ID입니다.");
			}
			if (nResult == 0)
			{
				System.out.println();
				System.out.println(id+"는 사용 가능한 ID입니다.");
				System.out.println();
				System.out.print("등록할 회원의 이름을 입력해 주세요 : ");
				String name = sc.nextLine();
				System.out.print("등록할 회원의 블랙리스트 여부를 입력해 주세요.");
				System.out.println("(블랙리스트가 아닐 경우 엔터키를 눌러주세요)");
				String blackuser = sc.nextLine();
				try
				{
					String sql2 = "INSERT INTO MEMBERDB VALUES(seq_member_num.nextval, ?, ?, ?)";
					psmt = con.prepareStatement(sql2);
					
					psmt.setString(1, id);
					psmt.setString(2, name);
					psmt.setString(3, blackuser);
					
					int updateCount = psmt.executeUpdate();
				if(updateCount == 1) {
					System.out.println(name + "님의 회원가입이 정상 처리 되었습니다.");
					System.out.println("환영합니다! 가입을 축하드립니다:)");
					System.out.println();
				}else {
					System.out.println("데이터 입력에 실패했습니다.(#가입오류)");
				}
				//System.out.println("insertCount : " + updateCount);
			
				}catch(Exception e)
				{
					e.printStackTrace();
					System.out.println("데이터 입력에 실패했습니다.(#데이터예외)");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("입력에 실패했습니다.(#3)");
		}
		
	}
	
	// 1-2. 회원 조회
	public void searchMember() {
		System.out.print("조회할 회원의 이름을 입력해 주세요 : ");
		String name = sc.nextLine();
		try{	
			String sql = "select * from memberDB where name = ?";
			
			psmt = con.prepareStatement(sql);
			psmt.setString(1, name);
			rs = psmt.executeQuery();
			
			int nResult = 0;
			while(rs.next()) {
				nResult++;
				rs.getString("id");
				System.out.println(rs.getString("name")+"님의 정보를 조회합니다.");
				System.out.println("------------------------------");
				System.out.println("회  원  번  호 : " + rs.getInt("membernum"));
				System.out.println("아    이    디 : " + rs.getString("id"));
				System.out.println("이          름 : " + rs.getString("name"));
				System.out.println("블랙리스트 여부: " + rs.getString("blackuser"));				
				System.out.println("------------------------------");
			}
			if (nResult == 0)
			{
				System.out.println("이름이 잘못 입력되었거나 등록되어 있지 않은 회원입니다.");
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("데이터 입력에 실패했습니다.(#3)");
		}
	}
	
	// 1-3. 전체 회원 조회
	public void allMember() {
		try{	
			String sql = "select * from memberDB order by membernum";
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery(sql);
		
			while(rs.next()) {
				System.out.println("------------------------------");
				System.out.println("회  원  번  호 : " + rs.getInt("membernum"));
				System.out.println("아    이    디 : " + rs.getString("id"));
				System.out.println("이          름 : " + rs.getString("name"));
				System.out.println("블랙리스트 여부: " + rs.getString("blackuser"));				
				System.out.println("------------------------------");
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 1-4. 회원 탈퇴
	public void delMember() {
		System.out.print("탈퇴할 회원의 이름을 입력해 주세요 : ");
		String name = sc.nextLine();
		try{	
			String sql = "delete from memberDB where name = ?";
			
			psmt = con.prepareStatement(sql);
			psmt.setString(1, name);
			psmt.executeUpdate();
			
			System.out.println(name + "(이)가 삭제되었습니다.");
			System.out.println("이용해 주셔서 감사합니다:)");
			
		}catch(Exception e) {
			System.out.println("데이터베이스 삭제 에러입니다.");
		}
	}
		
	
	// 책 관리 메뉴 선택
	public void BookMenu()
	{
		System.out.println("");
		System.out.println("┌────<메뉴 선택>────┐");
		System.out.println("│1.    책 등록      │");
		System.out.println("│2.   도서 조회     │");
		System.out.println("│3.전체 리스트 조회 │");
		System.out.println("│4. 낡은 책 버리기  │");
		System.out.println("│5.  프로그램 종료  │");
		System.out.println("└───────────────────┘");
		System.out.print("메뉴를 선택해 주세요 : ");
	}
	
	// 책 프로그램 실행
	public void BookRun() {
		while(true) {
			BookMenu();
			int choice = sc.nextInt();
			sc.nextLine();
			switch(choice) {
			case 1:
				addBook();
				break;
			case 2:
				searchBook();
				break;
			case 3:
				allBook();
				break;
			case 4:
				delBook();
				break;
			case 5:
				System.out.println("도서 관리 프로그램을 종료합니다.");
				System.out.println();
				return;
			default:
				System.out.println("잘못 입력하셨습니다.");
				break;	
			}
		}
	}
	
	// 2-1. 책 등록
	public void addBook() {
		try
		{
			System.out.print("등록할 책 제목을 입력해 주세요 : ");
			String bookname = sc.nextLine();
			System.out.print("등록할 책의 권 수를 입력해 주세요 : ");
			int howmany = sc.nextInt();
			
			String sql = "INSERT INTO BOOKDB VALUES(seq_book_num.nextval, ?, ?)";
			psmt = con.prepareStatement(sql);
			
			psmt.setString(1, bookname);
			psmt.setInt(2, howmany);
			
			psmt.executeUpdate();
			System.out.println();
			System.out.println(bookname + "(이)가 " + howmany +"권 추가되었습니다.");
			
		}
		catch(Exception e) {
			e.printStackTrace();
			System.out.println("데이터 입력에 실패했습니다.(#데이터예외)");
		}
	}
	
	// 2-2. 책 조회
	public void searchBook() {
		System.out.print("조회할 책 제목을 입력해 주세요 : ");
		String bookname = sc.nextLine();
		try{	
			String sql = "select * from bookDB where bookname = ?";
			
			psmt = con.prepareStatement(sql);
			psmt.setString(1, bookname);
			rs = psmt.executeQuery();
			
			while(rs.next()) {
				System.out.println("------------------------------");
				System.out.println("책번호 : " + rs.getString("booknum"));
				System.out.println("제  목 : " + rs.getString("bookname"));
				System.out.println("수  량 : " + rs.getInt("howmany"));
				System.out.println("------------------------------");
			}
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("데이터 입력에 실패했습니다.(#3)");
		}
	}
	
	// 2-3. 전체 리스트 조회
	public void allBook() {
		try{	
			String sql = "select * from bookDB order by booknum";
			psmt = con.prepareStatement(sql);
			rs = psmt.executeQuery(sql);
		
			while(rs.next()) {
				System.out.println("------------------------------");
				System.out.println("책번호 : "+rs.getString("booknum"));
				System.out.println("제  목 : "+rs.getString("bookname"));
				System.out.println("수  량 : " + rs.getInt("howmany"));
				System.out.println("------------------------------");
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// 2-4. 낡은 책 버리기
	public void delBook() {
		System.out.print("삭제할 책 제목을 입력해 주세요 : ");
		String bookname = sc.nextLine();
		try{	
			String sql = "delete from bookDB where bookname = ?";
			
			psmt = con.prepareStatement(sql);
			psmt.setString(1, bookname);
			psmt.executeUpdate();
			
			System.out.println();
			System.out.println(bookname + "(이)가 삭제되었습니다.");
			
		}catch(Exception e) {
			System.out.println("데이터베이스 삭제 에러입니다.");
		}
	}
}