package excel2sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author shenxiangwei
 * @date 2022/11/9 上午10:04
 */
public class PropertyMobileTypeSql {
    static String sql = "INSERT INTO `job_property_mobile_type`(`property_code`, `mobile_type_code`, `sequence`) " +
            "VALUES ('%s', '%s', %s);";

    static String[] propertyCodes = {"THHK","KHHK","KSL","ESL","FIJ","GSH","THOG","SLP","THPH","THS","RRR","RSR","SEN","SLBK","SLBO","SLCB","SLCM","SLFM","SLHT","SLJ","SLKL","SLMC","SLSN","SUR","TAH"};

    static String sequence = "PILLOWS\t1\n" +
            "SLIPPERS\t2\n" +
            "TOWELS\t3\n" +
            "TOILETRIES\t4\n" +
            "WATER&ICE\t5\n" +
            "TABLEWARE\t6\n" +
            "BAGGAGE_ASSISTANCE\t7\n" +
            "LAUNDRY\t8\n" +
            "ROOM_MAKE-UP\t9\n" +
            "Baby_Products\t10";

    public static void main(String[] args) {
        List<String> types = new ArrayList<>();
        String[] split = sequence.split("\n");

        for (String prop : propertyCodes){
            for(String type : split) {
                String[] split1 = type.split("\t");
                System.out.println(String.format(sql, prop, split1[0], split1[1]));
            }
        }
    }
}

