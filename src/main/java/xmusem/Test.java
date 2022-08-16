package xmusem;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener;

import java.io.IOException;
import java.util.concurrent.*;

/**
 * @author shenxiangwei
 * @date 2022/7/25 上午10:38
 */
public class Test {
    private static WebClient buildWebClient() {
        WebClient wc = new WebClient(BrowserVersion.FIREFOX_ESR);
        //是否使用不安全的SSL
        wc.getOptions().setUseInsecureSSL(true);
        //启用JS解释器，默认为true
        wc.getOptions().setJavaScriptEnabled(true);
        //禁用CSS
        wc.getOptions().setCssEnabled(false);
        //js运行错误时，是否抛出异常
        wc.getOptions().setThrowExceptionOnScriptError(false);
        //状态码错误时，是否抛出异常
        wc.getOptions().setThrowExceptionOnFailingStatusCode(false);
        wc.getCookieManager().setCookiesEnabled(true);
        //是否允许使用ActiveX
        wc.getOptions().setActiveXNative(false);
        //等待js时间
        wc.waitForBackgroundJavaScript(1000);
        //设置Ajax异步处理控制器即启用Ajax支持
        wc.setAjaxController(new NicelyResynchronizingAjaxController());
        // 设置自定义 js 解析引擎
        //设置超时时间
        wc.getOptions().setTimeout(1000);
        wc.setCache(new Cache());
        //js错误不打印错误日志
        wc.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
        return wc;
    }

    private static final ThreadFactory NAMED_THREAD_FACTORY = new ThreadFactoryBuilder().build();

    public static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(10, 20,
            1000L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(5000), NAMED_THREAD_FACTORY, new ThreadPoolExecutor.AbortPolicy());


    public static void main(String[] args) {

        CountDownLatch countDownLatch = new CountDownLatch(1);

        THREAD_POOL.submit(()->{


            try {
                WebClient webClient = buildWebClient();
                HtmlPage page = webClient.getPage("https://xmuseum.org/xvirtual/kimlaughton/index.html");



            } catch (IOException e) {
                e.printStackTrace();
            }

            countDownLatch.countDown();
        });
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}
