package regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则工具类
 * @author shenxiangwei
 * @date 2021/12/23 10:37 上午
 */
public class RegexUtils {

    /**
     * 判断字符串是不是数字
     * @param str 入参字符串
     * @return 结果
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 把字符串里匹配到的数字 替换成 **匹配到的数字**,然后返回整个字符串
     * 例如 : 把  "hello888world" 替换成 "helloisInput(888)world"
     * 如果是简单的替换可以使用  str.replaceAll(对应的正则,要替换成的字符串)
     * @param value 入参
     * @return 出参
     */
    public static String buildResult(String value){
        Pattern p = Pattern.compile("\\d+");
        // 获取 matcher 对象
        Matcher m = p.matcher(value);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            m.appendReplacement(sb,"isInput("+m.group()+");");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) {
        String regex = "[^0-9]+";
        if("sse33ss".matches(regex)){
            System.out.println(111);
        }
    }


}
