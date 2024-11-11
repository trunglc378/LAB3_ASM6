package funix.asm.login_mygtel.dao;

import funix.asm.login_mygtel.control.AccountStatus;
import funix.asm.login_mygtel.control.LoginState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private static Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public UserDAO() {
    }

    /**
     * Kiểm tra ID và Password hợp lệ
     *
     * @param userID   Id truyền vào
     * @param password password truyền vòa
     * @return Trạng thái đăng nhập thành công, thành công lần đầu hay thất bại
     * @throws SQLException lỗi SQL
     */
    public LoginState validateLogin(String userID, String password) throws SQLException {
        String querry = "SELECT * FROM schooldb.user WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(querry);
        preparedStatement.setString(1, userID);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String dbPassword = resultSet.getString("password");
            if (dbPassword.equals(password)) {
                resetFailedLoginAttemp(userID);
                boolean isFirstLogin = resultSet.getBoolean("is_first_login");
                return isFirstLogin ? LoginState.LOGIN_SUCCESS_AND_FIRST_LOGIN : LoginState.LOGIN_SUCCESS;
            } else {
                // Password sai
                incrementFailedLoginAttemp(userID);
                return LoginState.LOGIN_ID_RIGHT_PASS_FAIL;
            }
        } else {
            // Không tìm thấy user ID
            return LoginState.LOGIN_ID_AND_PASS_FAIL;
        }
    }
//        if (resultSet.next()) {
//            String checkUserId = resultSet.getString("user_id");
//            String checkPassword = resultSet.getString("password");
//            if (checkUserId.equalsIgnoreCase(userID) && checkPassword.equalsIgnoreCase(password)) {
//                resetFailedLoginAttemp(userID);
//                boolean isFirstLogin = resultSet.getBoolean("is_first_login");
//                return isFirstLogin ? LoginState.LOGIN_SUCCESS_AND_FIRST_LOGIN : LoginState.LOGIN_SUCCESS;
//            } else if (checkUserId.equalsIgnoreCase(userID) && !checkPassword.equalsIgnoreCase(password)) {
//                incrementFailedLoginAttemp(userID);
//                return LoginState.LOGIN_ID_RIGHT_PASS_FAIL;
//            }
//        }
//        return LoginState.LOGIN_ID_AND_PASS_FAIL;


    /**
     * Lấy ra số lần đăng nhập sai trường hợp với userID đúng.
     *
     * @param userId id truyền vòa
     * @return số lần nhập sai, hoặc trả về 0 nếu không thấy userID
     * @throws SQLException lỗi SQL
     */
    public int getFailedLoginAttemps(String userId) throws SQLException {
        String querry = "SELECT failed_login_attemp FROM schooldb.user WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(querry);
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt("failed_login_attemp");
        }
        return 0;
    }

    /**
     * Kiểm tra trạng thái của tài khoản hiện tại.
     *
     * @param userId userID truyền vào
     * @return tài khoản đã bị khóa (true) hay chưa bị khóa (false)
     * @throws SQLException lỗi SQL
     */
    public AccountStatus getAccountStatus(String userId) throws SQLException {
        String querry = "SELECT is_locked FROM schooldb.user WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(querry);
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            boolean isAccountLocked = resultSet.getBoolean("is_locked");
            return isAccountLocked ? AccountStatus.ACCOUNT_LOCKED : AccountStatus.ACCOUNT_UNLOCK;
        }
        return AccountStatus.ACCOUNT_UNLOCK;
    }

    /**
     * Cập nhật trạng thái của tài khoản bị khóa.
     *
     * @param userId ID truyền vào
     * @throws SQLException lỗi SQL
     */
    public void updateAccountLocked(String userId, AccountStatus accountStatus) throws SQLException {
        String querry = "UPDATE schooldb.user SET is_locked = ? WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(querry);
        boolean isLocked = accountStatus == AccountStatus.ACCOUNT_LOCKED;
        preparedStatement.setBoolean(1, isLocked);
        preparedStatement.setString(2, userId);
        preparedStatement.executeUpdate();
    }

    /**
     * Tăng số lần đăng nhập sai lên 1, nếu số lần nhập sai thì cập nhật tài khoản bị "khóa".
     *
     * @param userId UserID truyền vào
     * @throws SQLException SQL lỗi
     */
    private void incrementFailedLoginAttemp(String userId) throws SQLException {
        String querry = "UPDATE schooldb.user SET failed_login_attemp = failed_login_attemp + 1 WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(querry);
        preparedStatement.setString(1, userId);
        preparedStatement.executeUpdate();
        int failedAttemp = getFailedLoginAttemps(userId);
        if (failedAttemp >= 3) {
            updateAccountLocked(userId, AccountStatus.ACCOUNT_LOCKED);
        }
    }

    /**
     * Đăng nhập đúng sẽ reset số lần đăng nhập sai
     *
     * @param userId ID truyền vào
     * @throws SQLException Lỗi SQL
     */
    private void resetFailedLoginAttemp(String userId) throws SQLException {
        String querry = "UPDATE schooldb.user SET failed_login_attemp = 0 WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(querry);
        preparedStatement.setString(1, userId);
        preparedStatement.executeUpdate();
    }

    /**
     * Kiểm tra password cũ và password trong Database có khớp với nhau không?
     * @param userId userID truyền vào
     * @param password mật kẩu truyền vào
     * @return khớp hoặc không?
     * @throws SQLException lỗi SQL
     */
    public boolean checkPassword(String userId, String password) throws SQLException {
        String querry = "SELECT password FROM schooldb.user WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(querry);
        preparedStatement.setString(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            String storedPassword = resultSet.getString("password");
            return storedPassword.equals(password);
        }
        return false;
    }

    /**
     * Cập nhật mật khẩu mới vào Cơ sở dữ liệu
     * @param userId ID tài khoản
     * @param newPassword mật khẩu mới
     * @throws SQLException lỗi SQL
     */
    public void  updateNewPassword(String userId, String newPassword) throws SQLException {
        String querrry = "UPDATE schooldb.user SET password = ?, is_first_login = 0 WHERE user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(querrry);
        preparedStatement.setString(1,newPassword);
        preparedStatement.setString(2, userId);
        preparedStatement.executeUpdate();
    }
}
