package funix.asm.login_mygtel.control;

public enum LoginState {
    LOGIN_SUCCESS_AND_FIRST_LOGIN, // Đăng nhập thành công và đăng nhập lần đầu
    LOGIN_ID_RIGHT_PASS_FAIL, //Đăng nhập thất bại với ID đúng nhưng sai pass
    LOGIN_SUCCESS, // Đăng nhập thành công
    LOGIN_ID_AND_PASS_FAIL; // Đăng nhập thất bại với ID và PASS đều sai
    }
