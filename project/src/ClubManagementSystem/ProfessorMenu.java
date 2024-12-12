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
        this.crudHandler = new CRUDHandlerImpl(sc); // CRUDHandler 초기화
    }

    public void showMenu() throws SQLException {
        while (true) {
            System.out.println("1. 동아리 2. 승인관리 3. 건의사항 4. 공지 5. 피드백 6. 프로필 설정 99. 로그아웃");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 99) {
                System.out.println("로그아웃되었습니다.");
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
                    System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }
    private void editProfile() {
        try {
            String checkQuery = "SELECT COUNT(*) FROM Professor WHERE ProfessorID = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);

            System.out.print("학번: ");
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
            System.out.println("DB 오류: " + e.getMessage());
        }
    }

    private void insertProfessorProfile(String ProfessorID) throws SQLException {
        String insertQuery = "INSERT INTO Professor (ProfessorID, Club) VALUES (?, ?)";
        PreparedStatement insertStmt = con.prepareStatement(insertQuery);

        System.out.print("지도 동아리: ");
        String Club = sc.nextLine();

        insertStmt.setString(1, ProfessorID);
        insertStmt.setString(2, Club);

        int rowsInserted = insertStmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("새로운 교수 정보가 추가되었습니다!");
            this.Club=Club; // 업데이트된 역할 저장
        } else {
            System.out.println("교수 정보 추가에 실패했습니다.");
        }
    }

    private void updateProfessorProfile(String ProfessorID) throws SQLException {
        String updateQuery = "UPDATE Professor SET Club = ? WHERE ProfessorID = ?";
        PreparedStatement updateStmt = con.prepareStatement(updateQuery);

        System.out.print("소속 동아리: ");
        String Club = sc.nextLine();

        updateStmt.setString(1, Club);
        updateStmt.setString(2, ProfessorID);

        int rowsUpdated = updateStmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("교수 정보가 업데이트되었습니다!");
            this.Club = Club; // 업데이트된 역할 저장
        } else {
            System.out.println("교수 정보 업데이트에 실패했습니다.");
        }
    }
    private void refreshMenu() throws SQLException {
        System.out.println("\n프로필 수정이 완료되었습니다. 새로 고침된 메뉴를 불러옵니다...\n");
        showMenu(); // 메뉴 재호출
    }
}
