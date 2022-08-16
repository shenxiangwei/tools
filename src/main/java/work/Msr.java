package work;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author shenxiangwei
 * @date 2022/7/18 上午10:05
 */
public class Msr {

    private static final String sql = "insert into linked_system_config(%s) values(%s)";

    private static Map<String,String> s2b = new HashMap<>();
    private static Map<String,String> b2s = new HashMap<>();

    public static void main(String[] args) {
        String s = FileUtil.readString("/Users/sxw/File/s360/FCS工单/link_system.json", StandardCharsets.UTF_8);

        String propertyJsonStr = FileUtil.readString("/Users/sxw/File/s360/FCS工单/property.json", StandardCharsets.UTF_8);


        JSONArray system = JSON.parseArray(s);
        JSONArray propertyJson = JSON.parseArray(propertyJsonStr);

        for (int i = 0; i < propertyJson.size(); i++) {
            String s360 = propertyJson.getJSONObject(i).getString("s360_property_code");
            String b360 = propertyJson.getJSONObject(i).getString("book360_property_code");
            s2b.put(s360,b360);
            b2s.put(b360,s360);
        }

        for (int i = 0; i < system.size(); i++) {
//            System.out.println(jsonToSql(system.getJSONObject(i)) + ";");
            jsonToSql(system.getJSONObject(i));
        }

    }

    private static String jsonToSql(JSONObject jsonObject){
        StringBuilder key = new StringBuilder();
        StringBuilder value = new StringBuilder();

        jsonObject.keySet().forEach(k -> {
            if(!k.equals("id") && !k.equals("create_time") && !k.equals("update_time")){
                key.append("`").append(k).append("`").append(",");
                if(k.equals("property_code")){
                    if(s2b.containsKey(jsonObject.getString(k))){
                        value.append("'").append(jsonObject.getString(k)).append("'").append(",");
//                        System.out.println("---------------使用的是s360 [" + jsonObject.getString(k) + "] code---------------");
                    }else if(b2s.containsKey(jsonObject.getString(k))){
                        value.append("'").append(b2s.get(jsonObject.getString(k))).append("'").append(",");
                        System.out.println("---------------使用的是b360 [" + jsonObject.getString(k) + "] code---------------");
                    }else {
                        System.out.println("---------------[" + jsonObject.getString(k) + "] 不存在---------------");
                    }
                }else {
                    key.append("`").append(k).append("`").append(",");
                }




//                if(k.equals("pass")){
//                    value.append("'")
//                            .append(EncryptionUtils.decryption(jsonObject.getString(k)))
//                            .append("',");
//                }else {
//                    value.append("'").append(jsonObject.getString(k)).append("'").append(",");
//                }


            }
        });

        return String.format(sql,key.toString().substring(0,key.toString().length() -1),
                value.toString().substring(0,value.toString().length() -1));
    }
}
