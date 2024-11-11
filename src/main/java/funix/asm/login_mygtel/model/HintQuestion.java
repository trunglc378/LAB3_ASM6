package funix.asm.login_mygtel.model;

public class HintQuestion {
    private int questionId;
    private String question;

    public HintQuestion(int questionId, String question) {
        this.questionId = questionId;
        this.question = question;
    }

    public HintQuestion() {
    }

    public int getQuestionId() {
        return questionId;
    }

    public String getQuestion() {
        return question;
    }

}
