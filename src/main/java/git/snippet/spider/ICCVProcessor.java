package git.snippet.spider;

import static git.snippet.spider.Conference.ICCV;

/**
 * http://www.informatik.uni-trier.de/~ley/db/conf/iccv/index.html
 *
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/19
 * @since
 */
public class ICCVProcessor implements Processor {
    // https://ieeexplore.ieee.org/document/9010912


    @Override
    public Conference type() {
        return ICCV;
    }


}
