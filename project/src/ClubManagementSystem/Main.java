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
                sc.nextLine(); // 버퍼 비우기

                if (choice == 1) {
                    login(sc);
                } else if (choice == 2) {
                    signUp(sc);
                } else if (choice == 99) {
                    System.out.println("프로그램 종료.");
                    break;
                } else {
                    System.out.println("올바른 번호를 선택하세요.");
                }
            }
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
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
                    System.out.println("로그인 성공! " + rs.getString("Username") + "님 환영합니다.");
                    String role = rs.getString("Role");
                    if ("professor".equalsIgnoreCase(role)) {
                        ProfessorMenu professorMenu = new ProfessorMenu(con, sc);
                        professorMenu.showMenu();
                    } else if ("student".equalsIgnoreCase(role)) {
                        String userId = rs.getString("UserID");
                        StudentMenu studentMenu = new StudentMenu(con, sc, userId);
                        studentMenu.showMenu(); // 메뉴로 이동
                    }
                } else {
                    System.out.println("로그인 실패. ID 또는 비밀번호를 확인하세요.");
                }
            }
        } catch (SQLException e) {
            System.out.println("DB 오류: " + e.getMessage());
        }
    }
    

    private static void signUp(Scanner sc) {
        try (Connection con = connectDB();
            Statement stmt = con.createStatement()) {
            System.out.print("학번: ");
            String num = sc.nextLine();
            System.out.print("이름: ");
            String name = sc.nextLine();
            System.out.print("전화번호: ");
            String phone = sc.nextLine();
            System.out.print("Password: ");
            String passwd = sc.nextLine();
            System.out.print("직급 (student/professor): ");
            String role = sc.nextLine();
            System.out.print("주소: ");
            String address = sc.nextLine();

            String query = "INSERT INTO User (UserID, Username, Phone, passwd, Role, address) " +
                        "VALUES ('"+ num + "', '" + name + "', '" + phone + "', '" + passwd + "', '" + role + "', '" + address + "')";
            stmt.executeUpdate(query);
            System.out.println("회원가입 성공!");
        } catch (SQLException e) {
            System.out.println("DB 오류: " + e.getMessage());
        }
    }


    
}
