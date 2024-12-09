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
            System.out.println(menuName + " ��ȸ ���:");
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
        System.out.println(menuName + " �����͸� �߰��մϴ�.");
        System.out.println("�߰��� ������ �Է��ϼ��� (�ʵ庰�� ��ǥ�� ����):");
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
                System.out.println(menuName + " �����Ͱ� �߰��Ǿ����ϴ�.");
            }
        }
    }

    @Override
    public void handleUpdate(Connection con, String menuName) throws SQLException {
        System.out.println(menuName + " �����͸� �����մϴ�.");
        System.out.print("������ ID: ");
        String id = sc.nextLine();
        System.out.print("������ ������ �Է��ϼ��� (�ʵ��=�� �������� ��ǥ�� ����): ");
        String input = sc.nextLine();

        StringBuilder queryBuilder = new StringBuilder("UPDATE ");
        queryBuilder.append(menuName).append(" SET ");
        String[] updates = input.split(",");
        for (int i = 0; i < updates.length; i++) {
            queryBuilder.append(updates[i].trim());
            if (i < updates.length - 1) {
                queryBuilder.append(", ");
            }
        }
        queryBuilder.append(" WHERE id = ?");

        try (PreparedStatement pstmt = con.prepareStatement(queryBuilder.toString())) {
            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println(menuName + " �����Ͱ� �����Ǿ����ϴ�.");
            }
        }
    }

    @Override
    public void handleDelete(Connection con, String menuName) throws SQLException {
        System.out.println(menuName + " �����͸� �����մϴ�.");
        System.out.print("������ ID: ");
        String id = sc.nextLine();

        String query = "DELETE FROM " + menuName + " WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println(menuName + " �����Ͱ� �����Ǿ����ϴ�.");
            }
        }
    }
}
