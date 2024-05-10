package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class data {
  public static void main(String[] args) {
    Connection conn = null;
    Statement stmt = null;

    try {
      // JDBC 드라이버 로드
      Class.forName("com.mysql.cj.jdbc.Driver");

      // 데이터베이스 연결
      conn = DriverManager.getConnection(
        "jdbc:mysql://localhost:3306/kingjinpan?serverTimezone=UTC", "root", "3192");

      // 스테이트먼트 객체 생성
      stmt = conn.createStatement();

      // 기존 테이블이 있다면 삭제
      stmt.execute("DROP TABLE IF EXISTS test_table");

      // 새 테이블 생성
      stmt.execute("CREATE TABLE test_table (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(255) NOT NULL)");

      System.out.println("Table created successfully!");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Failed to create the table.");
    } finally {
      // 자원 해제
      try {
        if (stmt != null) stmt.close();
        if (conn != null) conn.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
