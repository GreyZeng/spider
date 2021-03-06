package git.snippet.spider;

import org.jsoup.nodes.Document;

import static git.snippet.spider.Processor.TIME_OUT;
import static org.jsoup.Jsoup.connect;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/20
 * @since
 */
public class HttpUtil {
    public static Document confirmGet(String indexUrl) {
        while (true) {
            try {
                return connect(indexUrl).timeout(TIME_OUT).get();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("网络错误，重新再试一下 " + e.getMessage());
            }
        }
    }

    public static Document confirmPost(String indexUrl) {
        while (true) {
            try {
                return connect(indexUrl).timeout(TIME_OUT).post();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("网络错误，重新再试一下 " + e.getMessage());
            }
        }
    }
}
