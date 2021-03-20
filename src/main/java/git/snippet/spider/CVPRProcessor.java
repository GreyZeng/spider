package git.snippet.spider;


import static git.snippet.spider.Conference.CVPR;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/19
 * @since
 */
public class CVPRProcessor implements Processor {


    // String url = "https://ieeexplore.ieee.org/document/8237272"


    @Override
    public Conference type() {
        return CVPR;
    }


}
