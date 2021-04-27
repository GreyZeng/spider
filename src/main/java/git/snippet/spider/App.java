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

import java.util.concurrent.CompletableFuture;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/19
 * @since
 */
public class App {


    public static void main(String[] args) {
        CompletableFuture<Void> p1 = CompletableFuture.runAsync(new Job(Conference.ICCV));
        CompletableFuture<Void> p2 = CompletableFuture.runAsync(new Job(Conference.CVPR));
        CompletableFuture<Void> p3 = CompletableFuture.runAsync(new Job(Conference.ECCV));
        CompletableFuture.allOf(p1, p2, p3).join();
    }

    private static class Job implements Runnable {
        private Conference conference;

        Job(Conference conference) {
            this.conference = conference;
        }

        @Override
        public void run() {
            try {
                handle(conference.getProcessor(), conference.getUrl());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void handle(Class<? extends Processor> clazz, String url) throws Exception {
        Processor processor = clazz.getDeclaredConstructor().newInstance();
        processor.save(processor.fetch(url));
    }
}
