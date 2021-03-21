package git.snippet.spider;

import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static git.snippet.spider.HttpUtil.confirmGet;
import static java.lang.String.valueOf;

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
public interface Processor {
    Pattern PATTERN = Pattern.compile("xplGlobal\\.document\\.metadata=(.*+)");
    // Pattern PATTERN = Pattern.compile("xplGlobal.document.metadata=(.*?);");
    int TIME_OUT = 2400 * 1000;

    /**
     * 获取某个论文的MetaInfo
     * 适合抓取CVPR和ICCV的论文
     *
     * @param url
     * @return
     */
    default String metaInfo(String url) throws Exception {
        Document document = confirmGet(url);
        String html = document.html();
        Matcher m = PATTERN.matcher(html);
        if (!m.find()) {
            System.err.printf("没有找到合适的信息 %s", url);
            return "";
        }
        // System.out.println(targetTxt);

        return m.group(1);
    }

    /**
     * 标注论文属于哪个会议
     *
     * @return
     */
    Conference type();

    /**
     * 保存论文信息到JSON文件，固定逻辑
     *
     * @param link
     * @throws Exception
     */
    default void save(String link) throws Exception {
        String content = metaInfo(link);
        if (StrUtil.isBlank(content)) {
            System.out.println("没有爬取到内容" + link);
            return;
        }
        File subFolder = new File("download", valueOf(type()));
        if (!subFolder.exists()) {
            subFolder.mkdir();
        }
        File file1 = new File(subFolder.getAbsolutePath(), title(content) + ".json");
        if (!file1.exists()) {
            FileWriter writer = new FileWriter(file1.getAbsolutePath());
            writer.write(content);
        } else {
            System.out.println("这个文件已经存在：" + file1.getAbsolutePath());
        }
    }

    /**
     * 获取论文标题
     *
     * @param content
     * @return
     */
    default String title(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(content);
            String title = jsonNode.get("title").asText();
            String year = jsonNode.get("publicationYear").asText();
            String validTitle = fix(year + "_" + title);
            System.out.printf("获取 %s  标题\n %s  \n", content, validTitle);
            return validTitle;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("转换错误 \n" + content);
            return String.valueOf(System.currentTimeMillis());
        }
    }

    default void save(List<String> links) throws Exception {
        int size = links.size();
        int index = 0;
        for (String link : links) {
            System.out.println("开始处理 (" + size + "/" + index + ") link " + link);
            index++;
            save(link);
        }
    }

    /**
     * 通过索引页面爬取所有链接
     *
     * @param indexUrl
     * @return
     */
    default List<String> fetch(String indexUrl) {
        Document document = confirmGet(indexUrl);
        Elements selects = document.getElementsByClass("toc-link");
        List<String> urlByYears = new ArrayList<>();
        for (Element element : selects) {
            String href = element.attr("href");
            System.out.println("每年的链接 " + href);
            urlByYears.add(href);
        }
        List<String> links = new ArrayList<>();
        for (String oneYearLink : urlByYears) {
            document = confirmGet(oneYearLink);
            Elements elementsByClass = document.getElementsByClass("entry inproceedings");
            for (Element element : elementsByClass) {
                if (element.getElementsByClass("drop-down") == null || element.getElementsByClass("drop-down").size() == 0) {
                    continue;
                }
                Element select = element.getElementsByClass("drop-down").get(0);
                if (select.getElementsByClass("head") == null || select.getElementsByClass("head").size() == 0) {
                    continue;
                }
                Element element1 = select.getElementsByClass("head").get(0);
                if (element1.select("a") == null || element1.select("a").size() == 0) {
                    break;
                }
                String link = element1.select("a").get(0).attr("href");
                if (!link.contains("https://doi.org")) {
                    if (select.getElementsByClass("ee") == null || select.getElementsByClass("ee").size() <= 1) {
                        continue;
                    }

                    Element element2 = select.getElementsByClass("ee").get(1);
                    link = element2.select("a").get(0).attr("href");
                }
                System.out.println("每个文章的链接 " + link);
                links.add(link);
            }
        }
        return links;
    }


    Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");

    // 解决windows下文件命名的特殊字符
    static String fix(String str) {
        return str == null ? null : FilePattern.matcher(str).replaceAll("");
    }


}
