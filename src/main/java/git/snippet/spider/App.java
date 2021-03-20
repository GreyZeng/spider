package git.snippet.spider;
/**
 * ECCV：
 * http://www.informatik.uni-trier.de/~ley/db/conf/eccv/index.html
 * <p>
 * <p>
 * ICCV：
 * http://www.informatik.uni-trier.de/~ley/db/conf/iccv/index.html
 * <p>
 * <p>
 * CVPR：
 * http://www.informatik.uni-trier.de/~ley/db/conf/cvpr/index.html
 */

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static git.snippet.spider.Conference.*;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/19
 * @since
 */
public class App {
    private static final Map<Conference, String> MAP = new HashMap<>();

    static {
        MAP.put(ECCV, "http://www.informatik.uni-trier.de/~ley/db/conf/eccv/index.html");
        MAP.put(ICCV, "http://www.informatik.uni-trier.de/~ley/db/conf/iccv/index.html");
        MAP.put(CVPR, "http://www.informatik.uni-trier.de/~ley/db/conf/cvpr/index.html");
    }

    public static void main(String[] args) throws Exception {
        Set<Map.Entry<Conference, String>> entries = MAP.entrySet();
        for (Map.Entry<Conference, String> entry : entries) {
            Conference key = entry.getKey();
            String url = entry.getValue();
            handle(key, url);
        }
    }

    private static void handle(Conference key, String url) throws Exception {
        Processor processor = null;
        switch (key) {
            case ECCV:
                processor = new ECCVProcessor();
                break;
            case ICCV:
                processor = new ICCVProcessor();
                break;
            case CVPR:
                processor = new CVPRProcessor();
                break;
            default:
                System.out.println("没有找到合适的爬虫");
                break;
        }

        processor.save(processor.fetch(url));
    }
}
