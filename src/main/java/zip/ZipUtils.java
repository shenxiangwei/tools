package zip;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author shenxiangwei
 * @date 2021/12/23 10:55 上午
 */
public class ZipUtils {
    /**
     * 解压账单压缩包特定账单文件(文件名包含明细的Excel)
     *
     * @param zipFilePath
     */
    private static void alipayZipToExcel(String zipFilePath, String resultFilePath) throws IOException {
        File zipFile = new File(zipFilePath);
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (!entry.getName().contains("汇总")) {
                //写入文件
                byte[] bytes = IOUtils.toByteArray(zip.getInputStream(entry));
                IOUtils.write(bytes, new FileOutputStream(resultFilePath));
            }
        }
    }


}
