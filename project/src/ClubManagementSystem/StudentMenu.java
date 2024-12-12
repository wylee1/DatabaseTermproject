package ClubManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class StudentMenu {
    private Connection con;
    private Scanner sc;
    private String clubRole;
    private String userId;
    private String grade;
    private String studentID;
    private String club;
    private CRUDHandler crudHandler;

    public StudentMenu(Connection con, Scanner sc, String userId) {
        this.con = con;
        this.sc = sc;
        this.userId = userId;
        this.crudHandler = new CRUDHandlerImpl(sc);

        // clubRole �ʱ�ȭ
        initializeClubRole();
    }

    private void initializeClubRole() {
        try {
            String query = "SELECT ClubRole FROM Student WHERE StudentID = ?";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.clubRole = rs.getString("ClubRole");
            } else {
                this.clubRole = ""; // �⺻�� ����
                System.out.println("ClubRole ������ ã�� �� �����ϴ�.");
            }
        } catch (SQLException e) {
            System.out.println("DB ����: " + e.getMessage());
        }
    }

    public void showMenu() throws SQLException {
        while (true) {
            if ("president".equalsIgnoreCase(clubRole)) {
                System.out.println("1. ���Ƹ� 2. �Խñ� 3. Ȱ�� 4. ���ΰ��� 5. ���ǻ��� 6. ���� 7. �ǵ�� 8. ������ ���� 99. �α׾ƿ�");
            } else {
                System.out.println("1. �Խñ� 2. ���ǻ��� 3. ���� 4. ������ ���� 99. �α׾ƿ�");
            }

            int choice = sc.nextInt();
            sc.nextLine(); // ���� ���� ó��

            if (choice == 99) {
                System.out.println("�α׾ƿ��Ǿ����ϴ�.");
                break;
            }

            switch (choice) {
                case 1: // ���Ƹ�
                    if ("president".equalsIgnoreCase(clubRole)) {
                        HandleCRUDMenu clubMenu = new HandleCRUDMenu(crudHandler, sc);
                        clubMenu.handleMenu(con, "Club"); // 'Club' ���̺� ����
                    } else{
                        HandleCRUDMenu postMenu = new HandleCRUDMenu(crudHandler, sc);
                        postMenu.handleMenu(con, "Post");
                    }
                    break;
                case 2: // �Խñ�
                    if ("president".equalsIgnoreCase(clubRole)) {
                    HandleCRUDMenu postMenu = new HandleCRUDMenu(crudHandler, sc);
                    postMenu.handleMenu(con, "Post"); // 'Post' ���̺� ����
                    } else{
                        HandleCRUDMenu proposalMenu = new HandleCRUDMenu(crudHandler, sc);
                        proposalMenu.handleMenu(con, "Proposal");
                    }
                    break;
                case 3: // Ȱ��
                    if ("president".equalsIgnoreCase(clubRole)) {
                        HandleCRUDMenu activityMenu = new HandleCRUDMenu(crudHandler, sc);
                        activityMenu.handleMenu(con, "Activity"); // 'Activity' ���̺� ����
                    } else{
                        handleReadOnlyMenu("Notice");
                    }
                    break;
                case 4: // ���ΰ���
                    if ("president".equalsIgnoreCase(clubRole)) {
                        handleApprovalMenu();
                    } else {
                        editProfile();
                    }
                    break;
                case 5: // ���ǻ���
                    if ("president".equalsIgnoreCase(clubRole)) {
                        HandleCRUDMenu proposalMenu = new HandleCRUDMenu(crudHandler, sc);
                        proposalMenu.handleMenu(con, "Proposal"); // 'Proposal' ���̺� ����
                    } else {
                        System.out.println("�ùٸ� ��ȣ�� �����ϼ���.");
                    }
                    break;
                case 6: // ����
                    if ("president".equalsIgnoreCase(clubRole)) {
                        handleReadOnlyMenu("Notice"); // 'Notice' ���̺� ����
                    } else {
                        System.out.println("�ùٸ� ��ȣ�� �����ϼ���.");
                    }
                    break;
                case 7: // �ǵ��
                    if ("president".equalsIgnoreCase(clubRole)) {
                        handleReadOnlyMenu("Feedback");
                        System.out.println("�ùٸ� ��ȣ�� �����ϼ���.");
                    }
                    break;
                case 8:
                    if ("president".equalsIgnoreCase(clubRole)) {
                        editProfile();
                    } else {
                        System.out.println("�ùٸ� ��ȣ�� �����ϼ���.");
                    }
                    break;
                default:
                    System.out.println("�ùٸ� ��ȣ�� �����ϼ���.");
            }
        }
    }

    private void editProfile() {
        try {
            String checkQuery = "SELECT COUNT(*) FROM Student WHERE StudentID = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);

            System.out.print("�й�: ");
            String studentID = sc.nextLine();
            checkStmt.setString(1, studentID);

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;

            if (!exists) {
                insertStudentProfile(studentID);
            } else {
                updateStudentProfile(studentID);
            }

            refreshMenu();
        } catch (SQLException e) {
            System.out.println("DB ����: " + e.getMessage());
        }
    }

    private void insertStudentProfile(String studentID) throws SQLException {
        String insertQuery = "INSERT INTO Student (StudentID, Club, Grade, ClubRole) VALUES (?, ?, ?, ?)";
        PreparedStatement insertStmt = con.prepareStatement(insertQuery);

        System.out.print("�Ҽ� ���Ƹ�: ");
        String club = sc.nextLine();
        System.out.print("�г�: ");
        String grade = sc.nextLine();
        System.out.print("���� (��: president, member): ");
        String clubRole = sc.nextLine();

        insertStmt.setString(1, studentID);
        insertStmt.setString(2, club);
        insertStmt.setString(3, grade);
        insertStmt.setString(4, clubRole);

        int rowsInserted = insertStmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("���ο� �л� ������ �߰��Ǿ����ϴ�!");
            this.clubRole = clubRole; // ������Ʈ�� ���� ����
            this.club = club;
            this.grade = grade;
            this.studentID = studentID;
        } else {
            System.out.println("�л� ���� �߰��� �����߽��ϴ�.");
        }
    }

    private void updateStudentProfile(String studentID) throws SQLException {
        String updateQuery = "UPDATE Student SET Club = ?, Grade = ?, ClubRole = ? WHERE StudentID = ?";
        PreparedStatement updateStmt = con.prepareStatement(updateQuery);

        System.out.print("�Ҽ� ���Ƹ�: ");
        String club = sc.nextLine();
        System.out.print("�г�: ");
        String grade = sc.nextLine();
        System.out.print("���� (��: president, member): ");
        String clubRole = sc.nextLine();

        updateStmt.setString(1, club);
        updateStmt.setString(2, grade);
        updateStmt.setString(3, clubRole);
        updateStmt.setString(4, studentID);

        int rowsUpdated = updateStmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("�л� ������ ������Ʈ�Ǿ����ϴ�!");
            this.clubRole = clubRole; // ������Ʈ�� ���� ����
            this.club = club;
            this.grade = grade;
            this.studentID = studentID;
        } else {
            System.out.println("�л� ���� ������Ʈ�� �����߽��ϴ�.");
        }
    }

    private void refreshMenu() throws SQLException {
        System.out.println("\n������ ������ �Ϸ�Ǿ����ϴ�. ���� ��ħ�� �޴��� �ҷ��ɴϴ�...\n");
        showMenu(); // �޴� ��ȣ��
    }
    private void handleApprovalMenu() throws SQLException {
        while (true) {
            System.out.println("1. ��ȸ(Read) 2. �Է�(Insert) 99. ���� �޴�");
            int choice = sc.nextInt();
            sc.nextLine();
    
            if (choice == 99) {
                return; // ���� �޴��� ���ư���
            }
    
            switch (choice) {
                case 1:
                    crudHandler.handleRead(con, "Approval"); // ���ΰ��� ��ȸ
                    break;
                case 2:
                    crudHandler.handleInsert(con, "Approval"); // ���ΰ��� �Է�
                    break;
                default:
                    System.out.println("�ùٸ� ��ȣ�� �����ϼ���.");
            }
        }
    }
    
    private void handleReadOnlyMenu(String tableName) throws SQLException {
        System.out.println("��ȸ ������ �׸��Դϴ�.");
        crudHandler.handleRead(con, tableName); // ��ȸ�� ���
    }
}
