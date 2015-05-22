package util;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.widget.Toast;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duan on 2015/2/6.
 */
public class ValidateUtil {
    private static final Logger logger = Logger.getLogger(String.valueOf(ValidateUtil.class));

    /**
     * 鏍￠獙鏄惁鏄墜鏈哄彿
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        logger.info(m.matches() + "---");

        return m.matches();
    }


    /**
     * 鏍￠獙鏄惁鏄偖绠卞湴鍧�
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        logger.info(m.matches() + "---");

        return m.matches();
    }

    /**
     * 鏍￠獙鏄惁鏄疷RL鍦板潃
     *
     * @param urlStr
     * @return
     */
    public static boolean isUrl(String urlStr) {
        String str = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(urlStr);
        logger.info(m.matches() + "---");

        return m.matches();
    }


    /**
     * 妫�鏌ユ槸鍚︽湁缃戠粶杩炴帴
     *
     * @param context
     */
    public static void checkNetWork(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        if (!wifi && !internet) {
            Toast.makeText(context.getApplicationContext(), "亲， 检测到网络有问题，请设置！", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context.getApplicationContext(), "网络连接正常！", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 妫�鏌ユ槸鍚ifi缃戣矾
     *
     * @param context
     * @return true/false
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();

        if (wifi) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 妫�鏌ユ槸鍚︽墜鏈烘祦閲忕綉缁�
     *
     * @param context
     * @return true/false
     */
    public static boolean isInternet(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        if (internet) {
            return true;
        } else {
            return false;
        }
    }
}
