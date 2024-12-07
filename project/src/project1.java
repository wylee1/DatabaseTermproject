import java.sql.*;

public class project1 {
    public static void main(String args[]) {
        try {
            // 드라이버 로드 (선택 사항)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 데이터베이스 연결
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://192.168.56.101:4567/ClubManagementSystem",
                "wylee",
                "madiq8047!@"
            );

            // SQL 실행
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE DATABASE ClubManagementSystem");

            System.out.println("Database 'ClubManagementSystem' created successfully!");

            // 연결 종료
            con.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}