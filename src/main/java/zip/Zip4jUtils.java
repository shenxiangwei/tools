package zip;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.util.List;

/**
 * 带密码的解压文件
 * @author shenxiangwei
 * @date 2021/12/23 4:57 下午
 */
public class Zip4jUtils {
    /**
     * 解压文件 参考 https://github.com/srikanth-lingala/zip4j
     * @throws ZipException
     */
    public static void unzip() throws ZipException {
        ZipFile zipFile = new ZipFile("fileName", "password".toCharArray());
        List<FileHeader> fileHeaders = zipFile.getFileHeaders();
        zipFile.extractFile("fileHeader.getFileName()", "目标路路径");
    }
}
