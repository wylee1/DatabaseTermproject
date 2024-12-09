package ClubManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class ProfessorMenu {
    private Connection con;
    private Scanner sc;
    private CRUDHandler crudHandler;

    public ProfessorMenu(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
        this.crudHandler = new CRUDHandlerImpl(sc); // CRUDHandler 초기화
    }

    public void showMenu() throws SQLException {
        while (true) {
            System.out.println("1. 동아리 2. 승인관리 3. 건의사항 4. 공지 5. 피드백 99. 로그아웃");
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
                default:
                    System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }

}
