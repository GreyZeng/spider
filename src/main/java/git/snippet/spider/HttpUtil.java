package git.snippet.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import static git.snippet.spider.Processor.TIME_OUT;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/20
 * @since
 */
public class HttpUtil {
    public static Document confirmGet(String indexUrl) {
        while (true) {
            try {
                Document document = Jsoup.connect(indexUrl).timeout(TIME_OUT).get();
                return document;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("网络错误，重新再试一下 " + e.getMessage());
            }
        }
    }

    public static Document confirmPost(String indexUrl) {
        while (true) {
            try {
                return Jsoup.connect(indexUrl).timeout(TIME_OUT).post();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("网络错误，重新再试一下 " + e.getMessage());
            }
        }
    }
}
