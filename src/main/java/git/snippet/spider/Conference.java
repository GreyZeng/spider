package git.snippet.spider;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/19
 * @since
 */
public enum Conference {
    ICCV("http://www.informatik.uni-trier.de/~ley/db/conf/iccv/index.html", ICCVProcessor.class),
    ECCV("http://www.informatik.uni-trier.de/~ley/db/conf/eccv/index.html", ECCVProcessor.class),
    CVPR("http://www.informatik.uni-trier.de/~ley/db/conf/cvpr/index.html", CVPRProcessor.class);
    private String url;
    private Class<? extends Processor> processor;


    Conference(String url, Class<? extends Processor> processor) {
        this.url = url;
        this.processor = processor;
    }

    public Class<? extends Processor> getProcessor() {
        return processor;
    }

    public void setProcessor(Class<? extends Processor> processor) {
        this.processor = processor;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
