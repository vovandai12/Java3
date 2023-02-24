package helper;

import model.User;

/**
 *
 * @author ACER
 */
public class ShareHelper {

    /**
     * Đối tượng này chứa thông tin người sử dụng sau khi đăng nhnập
     */
    public static User USER = null;

    /**
     * Xóa thông tin của người sử dụng khi có yêu cầu đăng xuất
     *
     * @return
     */
    public static boolean logoff() {
        ShareHelper.USER = null;
        return true;
    }

    /**
     * Kiểm tra xem đăng nhập hay chưa
     *
     * @return đăng nhập hay chưa
     */
    public static boolean authenticated() {
        return ShareHelper.USER != null;
    }
}
