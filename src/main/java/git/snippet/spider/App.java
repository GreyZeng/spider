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

import static git.snippet.spider.Conference.values;
import static java.util.Arrays.stream;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/19
 * @since
 */
public class App {


    public static void main(String[] args) {
        stream(values()).forEach(conference -> {
            Class<? extends Processor> clazz = conference.getProcessor();
            String url = conference.getUrl();
            try {
                handle(clazz, url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void handle(Class<? extends Processor> clazz, String url) throws Exception {
        Processor processor = clazz.getDeclaredConstructor().newInstance();
        processor.save(processor.fetch(url));
    }
}
