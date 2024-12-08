import java.sql.*;

public class CREATE {
    public static void main(String[] args) {
        try {
            // 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 데이터베이스 연결
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://192.168.56.101:4567/ClubManagementSystem",
                "wylee",
                "madiq8047!@"
            );

            // SQL 실행을 위한 Statement 생성
            Statement stmt = con.createStatement();

            // 데이터베이스 및 테이블 생성
            String createUserTable = "CREATE TABLE IF NOT EXISTS User (" +
                                    "UserID INT AUTO_INCREMENT PRIMARY KEY, "+
                                    "Username VARCHAR(50) NOT NULL, "+
                                    "Phone VARCHAR(20) NOT NULL, "+
                                    "ID VARCHAR(50) NOT NULL, "+
                                    "passwd VARCHAR(50) NOT NULL, "+
                                    "address VARCHAR(50) NOT NULL);";

            String createProfessorTable = "CREATE TABLE IF NOT EXISTS Professor (" +
                                        "professorID INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "name VARCHAR(50) NOT NULL, " +
                                        "clubName VARCHAR(50) NOT NULL, " +
                                        "FOREIGN KEY (professorID) REFERENCES User(UserID) ON DELETE CASCADE ON UPDATE CASCADE);";

            String createStudentTable = "CREATE TABLE IF NOT EXISTS Student (" +
                                        "studentID INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "name VARCHAR(50) NOT NULL, " +
                                        "clubName VARCHAR(50) NOT NULL, " +
                                        "grade INT NOT NULL, " +
                                        "position VARCHAR(50) NOT NULL, " +
                                        "FOREIGN KEY (studentID) REFERENCES User(UserID) ON DELETE CASCADE ON UPDATE CASCADE);";

            String createClubTable = "CREATE TABLE IF NOT EXISTS Club (" +
                                    "clubName VARCHAR(50) PRIMARY KEY, " +
                                    "location VARCHAR(50) NOT NULL, " +
                                    "equipment TEXT NOT NULL, " +
                                    "ProfessorID INT NOT NULL, " +
                                    "FOREIGN KEY (ProfessorID) REFERENCES Professor(professorID) ON DELETE CASCADE ON UPDATE CASCADE);";

            String createActivityTable = "CREATE TABLE IF NOT EXISTS Activity (" +
                                        "activityName VARCHAR(50) PRIMARY KEY, " +
                                        "clubName VARCHAR(50) NOT NULL, " +
                                        "FOREIGN KEY (clubName) REFERENCES Club(clubName) ON DELETE CASCADE ON UPDATE CASCADE);";
            
            String createActivityDetailTable = "CREATE TABLE IF NOT EXISTS Activity (" +
                                        "activityName VARCHAR(50) PRIMARY KEY, " +
                                        "place VARCHAR(50) NOT NULL, " +
                                        "date DATE NOT NULL, " +
                                        "content TEXT NOT NULL, " +
                                        "FOREIGN KEY (activityName) REFERENCES Activity(activityName) ON DELETE CASCADE ON UPDATE CASCADE);";

            String createApprovalTable = "CREATE TABLE IF NOT EXISTS Approval (" +
                                        "approvalID INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "clubName VARCHAR(50) NOT NULL, " +
                                        "status VARCHAR(20) NOT NULL, " +
                                        "ActivityName VARCHAR(50) NOT NULL, " +
                                        "FOREIGN KEY (ActivityName) REFERENCES Activity(activityNAME) ON DELETE CASCADE ON UPDATE CASCADE, "+
                                        "FOREIGN KEY (clubName) REFERENCES Club(clubName) ON DELETE CASCADE ON UPDATE CASCADE);";

            String createPostTable = "CREATE TABLE IF NOT EXISTS Post (" +
                                    "postID INT AUTO_INCREMENT PRIMARY KEY, " +
                                    "clubName VARCHAR(50) NOT NULL, " +
                                    "title VARCHAR(100) NOT NULL, " +
                                    "content TEXT NOT NULL, " +
                                    "FOREIGN KEY (clubName) REFERENCES Club(clubName) ON DELETE CASCADE ON UPDATE CASCADE); ";

            String createFeedbackTable = "CREATE TABLE IF NOT EXISTS Feedback (" +
                                        "feedbackID INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "ActivityName VARCHAR(50) NOT NULL, " +
                                        "content TEXT NOT NULL, " +
                                        "FOREIGN KEY (ActivityName) REFERENCES Activity(ActivityName) ON DELETE CASCADE ON UPDATE CASCADE);";

            String createProposalTable = "CREATE TABLE IF NOT EXISTS Proposal (" +
                                        "proposalID INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "clubName VARCHAR(50) NOT NULL, " +
                                        "title VARCHAR(100) NOT NULL, " +
                                        "content TEXT NOT NULL, " +
                                        "date DATE NOT NULL, " +
                                        "FOREIGN KEY (clubName) REFERENCES Club(clubName) ON DELETE CASCADE ON UPDATE CASCADE);";

            String createNoticeTable = "CREATE TABLE IF NOT EXISTS Notice (" +
                                        "noticeID INT AUTO_INCREMENT PRIMARY KEY, " +
                                        "clubName VARCHAR(50) NOT NULL, " +
                                        "title VARCHAR(100) NOT NULL, " +
                                        "content TEXT NOT NULL, " +
                                        "date DATE NOT NULL, " +
                                        "FOREIGN KEY (clubName) REFERENCES Club(clubName) ON DELETE CASCADE ON UPDATE CASCADE);";

            // 테이블 생성 실행
            stmt.executeUpdate(createUserTable);
            stmt.executeUpdate(createProfessorTable);
            stmt.executeUpdate(createStudentTable);
            stmt.executeUpdate(createClubTable);
            stmt.executeUpdate(createActivityTable);
            stmt.executeUpdate(createActivityDetailTable);
            stmt.executeUpdate(createApprovalTable);
            stmt.executeUpdate(createPostTable);
            stmt.executeUpdate(createFeedbackTable);
            stmt.executeUpdate(createProposalTable);
            stmt.executeUpdate(createNoticeTable);

            System.out.println("Tables created successfully!");

            // 연결 종료
            con.close();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
