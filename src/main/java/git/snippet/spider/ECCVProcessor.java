package git.snippet.spider;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static git.snippet.spider.Conference.ECCV;

/**
 * @author <a href="mailto:410486047@qq.com">Grey</a>
 * @date 2021/3/19
 * @since
 */
public class ECCVProcessor implements Processor {

    // String url = "https://link.springer.com/chapter/10.1007%2F978-3-030-58452-8_1";
    @Override
    public String metaInfo(String url) throws Exception {
        Document document = HttpUtil.confirmPost(url);
        String absPar1 = getAbstract(document);
        Map<String, Object> map = new HashMap<>();
        map.put("摘要", absPar1);
        map.put("发布时间", getPublishDate(document));
        map.put("会议和年份", yearAndConf(document));
        map.put("论文名称", getTitle(document));
        List<String> kwList = getKws(document);
        map.put("关键词", kwList);
        map.put("原文链接", getLink(document));
        return JSONUtil.parse(map).toStringPretty();
    }

    public String getAbstract(Document document) {
        Element par1 = document.getElementById("Par1");
        if (par1 != null && StrUtil.isNotBlank(par1.text())) {
            return par1.text();
        }

        Elements para = document.getElementsByClass("Para");
        if (para == null || para.size() == 0) {
            return "";
        }
        return para.get(0).text();
    }

    public String getPublishDate(Document document) {
        Element select = document.selectFirst("#main-content > div > div > article > div > div.ArticleHeader.main-context > div.main-context__container > div:nth-child(1) > div > span.article-dates__first-online > time");
        if (select != null) {
            return select.text();
        }
        return "";
    }

    public String yearAndConf(Document document) {
        Element select = document.selectFirst("#enumeration > p > span:nth-child(1)");
        if (select != null) {
            return select.text();
        }
        return "";
    }

    public String getTitle(Document document) {
        Element select = document.selectFirst("#main-content > div > div > article > div > div.ArticleHeader.main-context > div.MainTitleSection > h1");
        if (select != null) {
            return select.text();
        }
        return "";
    }

    public List<String> getKws(Document document) {
        Elements kws = document.getElementsByClass("Keyword");
        if (kws == null || kws.size() == 0) {
            return new ArrayList<>();
        }
        List<String> kwList = new ArrayList<>();
        for (Element kw : kws) {
            kwList.add(kw.text());
        }
        return kwList;
    }

    public String getLink(Document document) {
        Element select = document.selectFirst("#doi-url");
        if (select != null) {
            return select.text();
        }
        return "";
    }

    @Override
    public Conference type() {
        return ECCV;
    }

    @Override
    public String title(String content) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(content);
            String title = jsonNode.get("论文名称").asText();
            String year = jsonNode.get("会议和年份").asText().split(" ")[1];
            String validTitle = Processor.fix(year + "_" + title);
            System.out.printf("获取 %s  标题\n %s  \n", content, validTitle);
            return validTitle;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("转换错误 \n" + content);
            return String.valueOf(System.currentTimeMillis());
        }

    }
}
