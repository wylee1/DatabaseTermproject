package ClubManagementSystem;
import java.sql.*;
import java.util.Scanner;

public class Main {
    private static Connection connectDB() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://192.168.56.101:4567/ClubManagementSystem",
                "wylee", "madiq8047!@");
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.println("1. login 2. sign_up 99. exit");
                int choice = sc.nextInt();
                sc.nextLine(); // ���� ����

                if (choice == 1) {
                    login(sc);
                } else if (choice == 2) {
                    signUp(sc);
                } else if (choice == 99) {
                    System.out.println("���α׷� ����.");
                    break;
                } else {
                    System.out.println("�ùٸ� ��ȣ�� �����ϼ���.");
                }
            }
        } catch (Exception e) {
            System.out.println("���� �߻�: " + e.getMessage());
        }
    }

    private static void login(Scanner sc) {
        try (Connection con = connectDB()) {
            System.out.print("UserID: ");
            String id = sc.nextLine();
            System.out.print("Password: ");
            String passwd = sc.nextLine();
    
            String query = "SELECT * FROM User WHERE UserID = ? AND passwd = ?";
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, id);
                pstmt.setString(2, passwd);
                ResultSet rs = pstmt.executeQuery();
    
                if (rs.next()) {
                    System.out.println("�α��� ����! " + rs.getString("Username") + "�� ȯ���մϴ�.");
                    String role = rs.getString("Role");
                    if ("professor".equalsIgnoreCase(role)) {
                        ProfessorMenu professorMenu = new ProfessorMenu(con, sc);
                        professorMenu.showMenu();
                    } else if ("student".equalsIgnoreCase(role)) {
                        String userId = rs.getString("UserID");
                        StudentMenu studentMenu = new StudentMenu(con, sc, userId);
                        studentMenu.showMenu(); // �޴��� �̵�
                    }
                } else {
                    System.out.println("�α��� ����. ID �Ǵ� ��й�ȣ�� Ȯ���ϼ���.");
                }
            }
        } catch (SQLException e) {
            System.out.println("DB ����: " + e.getMessage());
        }
    }
    

    private static void signUp(Scanner sc) {
        try (Connection con = connectDB();
            Statement stmt = con.createStatement()) {
            System.out.print("�й�: ");
            String num = sc.nextLine();
            System.out.print("�̸�: ");
            String name = sc.nextLine();
            System.out.print("��ȭ��ȣ: ");
            String phone = sc.nextLine();
            System.out.print("Password: ");
            String passwd = sc.nextLine();
            System.out.print("���� (student/professor): ");
            String role = sc.nextLine();
            System.out.print("�ּ�: ");
            String address = sc.nextLine();

            String query = "INSERT INTO User (UserID, Username, Phone, passwd, Role, address) " +
                        "VALUES ('"+ num + "', '" + name + "', '" + phone + "', '" + passwd + "', '" + role + "', '" + address + "')";
            stmt.executeUpdate(query);
            System.out.println("ȸ������ ����!");
        } catch (SQLException e) {
            System.out.println("DB ����: " + e.getMessage());
        }
    }


    
}
