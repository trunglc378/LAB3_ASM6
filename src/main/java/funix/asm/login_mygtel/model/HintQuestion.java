package funix.asm.login_mygtel.model;

public class HintQuestion {
    private final int questionId;
    private final String question;

    public HintQuestion(int questionId, String question) {
        this.questionId = questionId;
        this.question = question;
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }

}
