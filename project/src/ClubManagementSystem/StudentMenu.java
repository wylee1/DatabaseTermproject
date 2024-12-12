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

        // clubRole 초기화
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
                this.clubRole = ""; // 기본값 설정
                System.out.println("ClubRole 정보를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("DB 오류: " + e.getMessage());
        }
    }

    public void showMenu() throws SQLException {
        while (true) {
            if ("president".equalsIgnoreCase(clubRole)) {
                System.out.println("1. 동아리 2. 게시글 3. 활동 4. 승인관리 5. 건의사항 6. 공지 7. 피드백 8. 프로필 수정 99. 로그아웃");
            } else {
                System.out.println("1. 게시글 2. 건의사항 3. 공지 4. 프로필 수정 99. 로그아웃");
            }

            int choice = sc.nextInt();
            sc.nextLine(); // 개행 문자 처리

            if (choice == 99) {
                System.out.println("로그아웃되었습니다.");
                break;
            }

            switch (choice) {
                case 1: // 동아리
                    if ("president".equalsIgnoreCase(clubRole)) {
                        HandleCRUDMenu clubMenu = new HandleCRUDMenu(crudHandler, sc);
                        clubMenu.handleMenu(con, "Club"); // 'Club' 테이블 관리
                    } else{
                        HandleCRUDMenu postMenu = new HandleCRUDMenu(crudHandler, sc);
                        postMenu.handleMenu(con, "Post");
                    }
                    break;
                case 2: // 게시글
                    if ("president".equalsIgnoreCase(clubRole)) {
                    HandleCRUDMenu postMenu = new HandleCRUDMenu(crudHandler, sc);
                    postMenu.handleMenu(con, "Post"); // 'Post' 테이블 관리
                    } else{
                        HandleCRUDMenu proposalMenu = new HandleCRUDMenu(crudHandler, sc);
                        proposalMenu.handleMenu(con, "Proposal");
                    }
                    break;
                case 3: // 활동
                    if ("president".equalsIgnoreCase(clubRole)) {
                        HandleCRUDMenu activityMenu = new HandleCRUDMenu(crudHandler, sc);
                        activityMenu.handleMenu(con, "Activity"); // 'Activity' 테이블 관리
                    } else{
                        handleReadOnlyMenu("Notice");
                    }
                    break;
                case 4: // 승인관리
                    if ("president".equalsIgnoreCase(clubRole)) {
                        handleApprovalMenu();
                    } else {
                        editProfile();
                    }
                    break;
                case 5: // 건의사항
                    if ("president".equalsIgnoreCase(clubRole)) {
                        HandleCRUDMenu proposalMenu = new HandleCRUDMenu(crudHandler, sc);
                        proposalMenu.handleMenu(con, "Proposal"); // 'Proposal' 테이블 관리
                    } else {
                        System.out.println("올바른 번호를 선택하세요.");
                    }
                    break;
                case 6: // 공지
                    if ("president".equalsIgnoreCase(clubRole)) {
                        handleReadOnlyMenu("Notice"); // 'Notice' 테이블 관리
                    } else {
                        System.out.println("올바른 번호를 선택하세요.");
                    }
                    break;
                case 7: // 피드백
                    if ("president".equalsIgnoreCase(clubRole)) {
                        handleReadOnlyMenu("Feedback");
                        System.out.println("올바른 번호를 선택하세요.");
                    }
                    break;
                case 8:
                    if ("president".equalsIgnoreCase(clubRole)) {
                        editProfile();
                    } else {
                        System.out.println("올바른 번호를 선택하세요.");
                    }
                    break;
                default:
                    System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }

    private void editProfile() {
        try {
            String checkQuery = "SELECT COUNT(*) FROM Student WHERE StudentID = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkQuery);

            System.out.print("학번: ");
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
            System.out.println("DB 오류: " + e.getMessage());
        }
    }

    private void insertStudentProfile(String studentID) throws SQLException {
        String insertQuery = "INSERT INTO Student (StudentID, Club, Grade, ClubRole) VALUES (?, ?, ?, ?)";
        PreparedStatement insertStmt = con.prepareStatement(insertQuery);

        System.out.print("소속 동아리: ");
        String club = sc.nextLine();
        System.out.print("학년: ");
        String grade = sc.nextLine();
        System.out.print("직급 (예: president, member): ");
        String clubRole = sc.nextLine();

        insertStmt.setString(1, studentID);
        insertStmt.setString(2, club);
        insertStmt.setString(3, grade);
        insertStmt.setString(4, clubRole);

        int rowsInserted = insertStmt.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("새로운 학생 정보가 추가되었습니다!");
            this.clubRole = clubRole; // 업데이트된 역할 저장
            this.club = club;
            this.grade = grade;
            this.studentID = studentID;
        } else {
            System.out.println("학생 정보 추가에 실패했습니다.");
        }
    }

    private void updateStudentProfile(String studentID) throws SQLException {
        String updateQuery = "UPDATE Student SET Club = ?, Grade = ?, ClubRole = ? WHERE StudentID = ?";
        PreparedStatement updateStmt = con.prepareStatement(updateQuery);

        System.out.print("소속 동아리: ");
        String club = sc.nextLine();
        System.out.print("학년: ");
        String grade = sc.nextLine();
        System.out.print("직급 (예: president, member): ");
        String clubRole = sc.nextLine();

        updateStmt.setString(1, club);
        updateStmt.setString(2, grade);
        updateStmt.setString(3, clubRole);
        updateStmt.setString(4, studentID);

        int rowsUpdated = updateStmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("학생 정보가 업데이트되었습니다!");
            this.clubRole = clubRole; // 업데이트된 역할 저장
            this.club = club;
            this.grade = grade;
            this.studentID = studentID;
        } else {
            System.out.println("학생 정보 업데이트에 실패했습니다.");
        }
    }

    private void refreshMenu() throws SQLException {
        System.out.println("\n프로필 수정이 완료되었습니다. 새로 고침된 메뉴를 불러옵니다...\n");
        showMenu(); // 메뉴 재호출
    }
    private void handleApprovalMenu() throws SQLException {
        while (true) {
            System.out.println("1. 조회(Read) 2. 입력(Insert) 99. 이전 메뉴");
            int choice = sc.nextInt();
            sc.nextLine();
    
            if (choice == 99) {
                return; // 이전 메뉴로 돌아가기
            }
    
            switch (choice) {
                case 1:
                    crudHandler.handleRead(con, "Approval"); // 승인관리 조회
                    break;
                case 2:
                    crudHandler.handleInsert(con, "Approval"); // 승인관리 입력
                    break;
                default:
                    System.out.println("올바른 번호를 선택하세요.");
            }
        }
    }
    
    private void handleReadOnlyMenu(String tableName) throws SQLException {
        System.out.println("조회 가능한 항목입니다.");
        crudHandler.handleRead(con, tableName); // 조회만 허용
    }
}
