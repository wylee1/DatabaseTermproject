import java.sql.*;

public class project1 {
    public static void main(String args[]) {
        try {
            // ����̹� �ε� (���� ����)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // �����ͺ��̽� ����
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://192.168.56.101:4567/ClubManagementSystem",
                "wylee",
                "madiq8047!@"
            );

            // SQL ����
            Statement stmt = con.createStatement();
            stmt.executeUpdate("CREATE DATABASE ClubManagementSystem");

            System.out.println("Database 'ClubManagementSystem' created successfully!");

            // ���� ����
            con.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}