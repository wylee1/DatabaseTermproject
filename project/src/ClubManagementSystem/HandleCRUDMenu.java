package ClubManagementSystem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class HandleCRUDMenu {
    private CRUDHandler crudHandler;
    private Scanner sc;

    public HandleCRUDMenu(CRUDHandler crudHandler, Scanner sc) {
        this.crudHandler = crudHandler;
        this.sc = sc;
    }

    public void handleMenu(Connection con, String tableName) throws SQLException {
        while (true) {
            System.out.println("1. ��ȸ(Read) 2. �Է�(Insert) 3. ����(Update) 4. ����(Delete) 99. ���� �޴�");
            int action = sc.nextInt();
            sc.nextLine(); // ���� ���� ó��

            switch (action) {
                case 1:
                    // Read �۾�
                    crudHandler.handleRead(con, tableName);
                    break;
                case 2:
                    // Insert �۾�
                    crudHandler.handleInsert(con, tableName);
                    break;
                case 3:
                    // Update �۾�
                    crudHandler.handleUpdate(con, tableName);
                    break;
                case 4:
                    // Delete �۾�
                    crudHandler.handleDelete(con, tableName);
                    break;
                case 99:
                    // ���� �޴��� ���ư���
                    return;
                default:
                    System.out.println("�ùٸ� ��ȣ�� �����ϼ���.");
            }
        }
    }
}
