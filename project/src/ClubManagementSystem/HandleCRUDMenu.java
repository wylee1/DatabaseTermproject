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
            System.out.println("1. 조회(Read) 2. 입력(Insert) 3. 수정(Update) 4. 삭제(Delete) 99. 이전 메뉴");
            int action = sc.nextInt();
            sc.nextLine(); // 개행 문자 처리

            switch (action) {
                case 1:
                    // Read 작업
                    crudHandler.handleRead(con, tableName);
                    break;
                case 2:
                    // Insert 작업
                    crudHandler.handleInsert(con, tableName);
                    break;
                case 3:
                    // Update 작업
                    crudHandler.handleUpdate(con, tableName);
                    break;
                case 4:
                    // Delete 작업
                    crudHandler.handleDelete(con, tableName);
                    break;
                case 99:
                    // 이전 메뉴로 돌아가기
                    return;
                default:
                    System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }
}
