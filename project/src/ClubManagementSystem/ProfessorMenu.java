package ClubManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class ProfessorMenu {
    private Connection con;
    private Scanner sc;
    private String Club;
    private CRUDHandler crudHandler;

    public ProfessorMenu(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
        this.crudHandler = new CRUDHandlerImpl(sc); // CRUDHandler �ʱ�ȭ
    }

    public void showMenu() throws SQLException {
        while (true) {
            System.out.println("1. ���Ƹ� 2. ���ΰ��� 3. ���ǻ��� 4. ���� 5. �ǵ�� 6. ������ ���� 99. �α׾ƿ�");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 99) {
                System.out.println("�α׾ƿ��Ǿ����ϴ�.");
                break;
            }

            switch (choice) {
                case 1:
                    HandleCRUDMenu clubMenu = new HandleCRUDMenu(crudHandler, sc);
                    clubMenu.handleMenu(con, "Club");
                    break;
                case 2:
                    HandleCRUDMenu approvalMenu = new HandleCRUDMenu(crudHandler, sc);
                    approvalMenu.handleMenu(con, "Approval");
                    break;
                case 3:
                    HandleCRUDMenu proposalMenu = new HandleCRUDMenu(crudHandler, sc);
                    proposalMenu.handleMenu(con, "Proposal");
                    break;
                case 4:
                    HandleCRUDMenu noticeMenu = new HandleCRUDMenu(crudHandler, sc);
                    noticeMenu.handleMenu(con, "Notice");
                    break;
                case 5:
                    HandleCRUDMenu feedbackMenu = new HandleCRUDMenu(crudHandler, sc);
                    feedbackMenu.handleMenu(con, "Feedback");
                    break;
                case 6:
                    editProfile();
                    break;
                default:
                    System.out.println("�ùٸ� ��ȣ�� �����ϼ���.");
            }
        }
    }
    private void editProfile() {
        try {
            String checkQuery = "SELECT COUNT(*) FROM Professor WHERE ProfessorID = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);

            System.out.print("�й�: ");
            String ProfessorID = sc.nextLine();
            checkStmt.setString(1, ProfessorID);

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            boolean exists = rs.getInt(1) > 0;

            if (!exists) {
                insertProfessorProfile(ProfessorID);
            } else {
                updateProfessorProfile(ProfessorID);
            }

            refreshMenu();
        } catch (SQLException e) {
            System.out.println("DB ����: " + e.getMessage());
        }
    }

    private void insertProfessorProfile(String ProfessorID) throws SQLException {
        String insertQuery = "INSERT INTO Professor (ProfessorID, Club) VALUES (?, ?)";
        PreparedStatement insertStmt = con.prepareStatement(insertQuery);

        System.out.print("���� ���Ƹ�: ");
        String Club = sc.nextLine();

        insertStmt.setString(1, ProfessorID);
        insertStmt.setString(2, Club);

        int rowsInserted = insertStmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("���ο� ���� ������ �߰��Ǿ����ϴ�!");
            this.Club=Club; // ������Ʈ�� ���� ����
        } else {
            System.out.println("���� ���� �߰��� �����߽��ϴ�.");
        }
    }

    private void updateProfessorProfile(String ProfessorID) throws SQLException {
        String updateQuery = "UPDATE Professor SET Club = ? WHERE ProfessorID = ?";
        PreparedStatement updateStmt = con.prepareStatement(updateQuery);

        System.out.print("�Ҽ� ���Ƹ�: ");
        String Club = sc.nextLine();

        updateStmt.setString(1, Club);
        updateStmt.setString(2, ProfessorID);

        int rowsUpdated = updateStmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("���� ������ ������Ʈ�Ǿ����ϴ�!");
            this.Club = Club; // ������Ʈ�� ���� ����
        } else {
            System.out.println("���� ���� ������Ʈ�� �����߽��ϴ�.");
        }
    }
    private void refreshMenu() throws SQLException {
        System.out.println("\n������ ������ �Ϸ�Ǿ����ϴ�. ���� ��ħ�� �޴��� �ҷ��ɴϴ�...\n");
        showMenu(); // �޴� ��ȣ��
    }
}
