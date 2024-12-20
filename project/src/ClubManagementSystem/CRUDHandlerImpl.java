package ClubManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class CRUDHandlerImpl implements CRUDHandler {
    private Scanner sc;

    public CRUDHandlerImpl(Scanner sc) {
        this.sc = sc;
    }

    @Override
    public void handleRead(Connection con, String menuName) throws SQLException {
        String query = "SELECT * FROM " + menuName;
        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            System.out.println(menuName + " 조회 결과:");
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + rs.getString(i) + " ");
                }
                System.out.println();
            }
        }
    }

    @Override
    public void handleInsert(Connection con, String menuName) throws SQLException {
        System.out.println(menuName + " 데이터를 추가합니다.");
        System.out.println("추가할 내용을 입력하세요 (필드별로 쉼표로 구분):");
        String input = sc.nextLine();
        String[] values = input.split(",");

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ");
        queryBuilder.append(menuName).append(" VALUES (");
        for (int i = 0; i < values.length; i++) {
            queryBuilder.append("?");
            if (i < values.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(")");

        try (PreparedStatement pstmt = con.prepareStatement(queryBuilder.toString())) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, values[i].trim());
            }
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println(menuName + " 데이터가 추가되었습니다.");
            }
        }
    }

    @Override
public void handleUpdate(Connection con, String menuName) throws SQLException {
    System.out.println(menuName + " 데이터를 수정합니다.");
    System.out.print("수정할 기준 컬럼 이름 (예: UserID): ");
    String keyColumn = sc.nextLine(); // 기준 열 이름 입력
    System.out.print("수정할 기준 값: ");
    String keyValue = sc.nextLine(); // 기준 값 입력
    System.out.print("수정할 내용을 입력하세요 (필드명=값 형식으로 쉼표로 구분): ");
    String input = sc.nextLine();

    // 필드명=값 형식을 쉼표로 구분하여 처리
    String[] updates = input.split(",");
    StringBuilder queryBuilder = new StringBuilder("UPDATE ");
    queryBuilder.append(menuName).append(" SET ");
    for (int i = 0; i < updates.length; i++) {
        queryBuilder.append(updates[i].trim());
        if (i < updates.length - 1) {
            queryBuilder.append(", ");
        }
    }
    queryBuilder.append(" WHERE ").append(keyColumn).append(" = ?");

    try (PreparedStatement pstmt = con.prepareStatement(queryBuilder.toString())) {
        pstmt.setString(1, keyValue); // 기준 값 설정
        int rows = pstmt.executeUpdate();
        if (rows > 0) {
            System.out.println(menuName + " 데이터가 수정되었습니다.");
        } else {
            System.out.println(menuName + " 데이터 수정에 실패했습니다.");
        }
    }
}

    @Override
    public void handleDelete(Connection con, String menuName) throws SQLException {
        System.out.println(menuName + " 데이터를 삭제합니다.");
        System.out.print("삭제할 기준 컬럼 이름 (예: UserID): ");
        String keyColumn = sc.nextLine();
        System.out.print("삭제할 값: ");
        String keyValue = sc.nextLine();

        String query = "DELETE FROM " + menuName + " WHERE " + keyColumn + " = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, keyValue);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println(menuName + " 데이터가 삭제되었습니다.");
            }
        }
    }
}
