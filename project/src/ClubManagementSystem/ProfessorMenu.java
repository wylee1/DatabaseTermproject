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
        this.crudHandler = new CRUDHandlerImpl(sc); // CRUDHandler �ʱ�ȭ
    }

    public void showMenu() throws SQLException {
        while (true) {
            System.out.println("1. ���Ƹ� 2. ���ΰ��� 3. ���ǻ��� 4. ���� 5. �ǵ�� 99. �α׾ƿ�");
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
                default:
                    System.out.println("�ùٸ� ��ȣ�� �����ϼ���.");
            }
        }
    }

}
