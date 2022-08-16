package xmusem;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 统计discord消息
 * @author shenxiangwei
 * @date 2022/8/16 上午10:46
 */
public class Discord {

    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.51 Safari/537.36";

    private static final OkHttpClient CLIENT = new OkHttpClient();
    public static final Map<String,String> HEADERS = new HashMap<>();

    public static final String format = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX";
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);

    public static final String format2 = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern(format2);
    public static void main(String[] args) throws IOException, InterruptedException {
        String authorization = "OTgzOTQ3MTI1MzE2NTUwNjg2.GAGDKQ.hM_zpbc36IgrDAvSSSw9ijNxS9RCGnFNEXP0iw";
        HEADERS.put("authorization",authorization);
        String date = "2022-07-27";
        String channelId = "1001781210466496522";

        LinkedList<BotMessage> result = new LinkedList<>();

        getMessageWithDate(result,date,channelId,null);

        System.out.println(date + " 总数据 : " + JSON.toJSONString(result));

        parseStatistics(result,channelId);

    }

    private static void parseStatistics(LinkedList<BotMessage> result,String channelId){
        Map<String, List<BotMessage>> collect = result.stream()
                .collect(Collectors.groupingBy(BotMessage::getAuthorName));

        Map<String,MessageExcel> nameExcel = new HashMap<>();
        for (String name : collect.keySet()){
            if(!"xvirtual".equals(name)) {
                MessageExcel messageExcel;
                if (nameExcel.get(name) == null) {
                    messageExcel = new MessageExcel();
                    nameExcel.put(name,messageExcel);
                } else {
                    messageExcel = nameExcel.get(name);
                }
                messageExcel.setUserName(name);
                parseChatExcel(collect.get(name),messageExcel);
            }
        }

        parseXVirtualExcel(collect.get("xvirtual"),nameExcel);


        // 写法2
        String fileName = "/Users/sxw/File/xmusem/discord_report/"+channelId+".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        // 如果这里想使用03 则 传入excelType参数即可
        EasyExcel.write(fileName, MessageExcel.class).sheet("模板").doWrite(nameExcel.values());

    }

    private static void parseChatExcel(List<BotMessage> messages,MessageExcel messageExcel){
        StringBuilder stringBuilder = new StringBuilder();
        for (BotMessage botMessage : messages){
            stringBuilder.append(botMessage.getTimestamp())
                    .append(" ")
                    .append(botMessage.getContent())
                    .append("\n");
        }
        messageExcel.setChatContent(stringBuilder.toString());
    }

    private static void parseXVirtualExcel(List<BotMessage> messages,Map<String,MessageExcel> nameExcel){
        for (BotMessage message : messages){
            String join = "\\*\\*(.+?)\\*\\* joined the Hubs room";
            String left = "\\*\\*(.+?)\\*\\* left the Hubs room";
            if(StringUtils.isNotBlank(getByRegex(join,message.getContent()))){
                String name = getByRegex(join, message.getContent());
                handleNameExcel(name,nameExcel);
                MessageExcel messageExcel = nameExcel.get(name);
                messageExcel.setEnterTime(message.getTimestamp());
            }else if(StringUtils.isNotBlank(getByRegex(left,message.getContent()))){
                String name = getByRegex(left, message.getContent());
                handleNameExcel(name,nameExcel);
                MessageExcel messageExcel = nameExcel.get(name);
                messageExcel.setLeftTime(message.getTimestamp());
            }
        }
    }

    private static void handleNameExcel(String name,Map<String,MessageExcel> nameExcel){
        if(nameExcel.get(name) == null){
            MessageExcel messageExcel = new MessageExcel();
            messageExcel.setUserName(name);
            nameExcel.put(name,messageExcel);
        }
    }

    /**
     * 从字符串中获取特定匹配信息
     * @param value
     * @return
     */
    private static String getByRegex(String regex,String value){
        String result = "";
        Matcher matcher = Pattern.compile(regex).matcher(value);
        if(matcher.find()){
            result = matcher.group(1);
        }
        return result;
    }


    private static void getMessageWithDate(LinkedList<BotMessage> resultList, String date,String channelId,String beforeId) throws IOException, InterruptedException {
        TimeUnit.MILLISECONDS.sleep(500);
        DateTime beforeDate = DateUtil.parseDate(date);
        String channelMessage = getChannelMessage(channelId, 100, beforeId);
        List<BotMessage> messages = parseMessage(channelMessage);
        if(CollectionUtils.isEmpty(messages)){
            return;
        }
        resultList.addAll(resultList.size(),messages);
        if(beforeDate.isBefore(resultList.get(resultList.size() - 1).getDate())) {
            getMessageWithDate(resultList,date,channelId,resultList.get(resultList.size() - 1).getId());
        }
    }


    private static List<BotMessage> parseMessage(String message){
        List<BotMessage> messages = JSONArray.parseArray(message,BotMessage.class);
        for(BotMessage value : messages){
            value.setTimestamp(LocalDateTime.parse(value.getTimestamp()).format(dateTimeFormatter2));
            value.setDate(DateUtil.parse(value.getTimestamp()).toJdkDate());
            value.setAuthorName(value.getAuthor().getUsername());
            value.setAuthor(null);
        }
        System.out.println(JSON.toJSONString(messages));
        return messages;
    }

    private static String getChannelMessage(String channelId,Integer limit,String before) throws IOException {
        if(null == limit){
            limit = 500;
        }
        String url;
        if(StringUtils.isNotBlank(before)){
            url = "https://discord.com/api/v9/channels/%s/messages?before=%s&limit=%s";
            return get(String.format(url, channelId, before, limit), HEADERS);
        }else {
            url = "https://discord.com/api/v9/channels/%s/messages?limit=%s";
            return get(String.format(url, channelId, limit), HEADERS);
        }
    }

    @Data
    public static class BotMessage{
        private String id;
        private String content;
        private String timestamp;
        private Date date;
        private String authorName;
        private Author author;
    }

    @Data
    public static class Author{
        private String username;
    }


    public static String get(String url, Map<String,String> headers) throws IOException {

        Request.Builder builder = new Request.Builder();
        if(ObjectUtil.isNotEmpty(headers)){
            builder.headers(Headers.of(headers));
        }
        Request request = builder
                .header("user-agent", USER_AGENT)
                .url(url)
                .get()
                .build();
        try (Response response = CLIENT.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    @Data
    public static class MessageExcel{
        @ExcelProperty("用户")
        private String userName;
        @ExcelProperty("进入时间")
        private String enterTime;
        @ExcelProperty("离开时间")
        private String leftTime;
        @ExcelProperty("停留时长")
        private String stay;
        @ExcelProperty("聊天内容")
        private String chatContent;
    }
}
