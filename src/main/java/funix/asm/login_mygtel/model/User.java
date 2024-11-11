package funix.asm.login_mygtel.model;

public class User {
    private String userId;
    private String password;
    private String hintQuestion;
    private String hintAnswer;
    private boolean firstLogin;
    private boolean statusAccount;
    private int failedLoginAttemp;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHintQuestion() {
        return hintQuestion;
    }

    public void setHintQuestion(String hintQuestion) {
        this.hintQuestion = hintQuestion;
    }

    public String getHintAnswer() {
        return hintAnswer;
    }

    public void setHintAnswer(String hintAnswer) {
        this.hintAnswer = hintAnswer;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public boolean isStatusAccount() {
        return statusAccount;
    }

    public void setStatusAccount(boolean statusAccount) {
        this.statusAccount = statusAccount;
    }

    public int getFailedLoginAttemp() {
        return failedLoginAttemp;
    }

    public void setFailedLoginAttemp(int failedLoginAttemp) {
        this.failedLoginAttemp = failedLoginAttemp;
    }
}
