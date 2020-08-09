/**
 * Author: Tang Yuqian
 * Date: 2020/8/3
 */
package com.concurrency.in.practice.cancellation_shutdown;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class GoogleWebCrawler extends WebCrawler {
    public GoogleWebCrawler(URL startUrl) {
        super(startUrl);
    }

    @Override
    protected List<URL> processPage(URL url) {
        System.out.println("下载当前页面：" + url);
        String linkUrl = url + "children.html";
        System.out.println("得到连接页面:" + linkUrl);

        try {
            return Arrays.asList(new URL(linkUrl));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws MalformedURLException, InterruptedException {

        WebCrawler webCrawler = new GoogleWebCrawler(new URL("google.com"));
        webCrawler.start();

        Thread.sleep(1000);

        webCrawler.stop();

        Thread.sleep(1000);

        webCrawler.start();
    }
}
