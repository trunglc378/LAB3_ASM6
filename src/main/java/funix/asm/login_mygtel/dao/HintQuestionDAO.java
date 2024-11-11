package funix.asm.login_mygtel.dao;

import funix.asm.login_mygtel.model.HintQuestion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HintQuestionDAO {
    private static Connection connection;


    public HintQuestionDAO(Connection connection) {
        HintQuestionDAO.connection = connection;
    }

    public HintQuestionDAO() {
    }

    /**
     * Phương thức lấy ra toàn bộ câu hỏi gợi ý
     *
     * @return Danh sách câu hỏi gợi ý
     * @throws SQLException Lỗi SQL
     */
    public List<HintQuestion> getAllHintQuestions() throws SQLException {
        String querry = "SELECT * FROM schooldb.hint_questions";
        List<HintQuestion> hintQuestionList = new ArrayList<>();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(querry);
        while (resultSet.next()) {
            int idQuestion = resultSet.getInt("question_id");
            String questionText = resultSet.getString("question_text");
            HintQuestion hintQuestion = new HintQuestion(idQuestion, questionText);
            hintQuestionList.add(hintQuestion);
        }
        return hintQuestionList;
    }

    /**
     * Phương thức lưu câu trả lời bí mật
     *
     * @param userId     ID truyền vào
     * @param questionId ID câu hỏi gợi ý
     * @param answer     Câu trả lời bí mật
     * @throws SQLException lỗi SQL
     */
    public void saveSercurityAnswer(String userId, int questionId, String answer) throws SQLException {
        String querry = "INSERT INTO schooldb.hint_answers(user_id, question_id, answer) VALUE (?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(querry);
        preparedStatement.setString(1, userId);
        preparedStatement.setInt(2, questionId);
        preparedStatement.setString(3, answer);
        preparedStatement.executeUpdate();
    }
}
