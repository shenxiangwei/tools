package excel2sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 生成 job_config表,job_upgrade_strategy表,skill_group表SQL
 * todo
 * @author shenxiangwei
 * @date 2022/11/8 下午6:37
 */
public class S360SkillGroupJobConfigSql {
    public static long generate() {
        return System.currentTimeMillis() / 1000;
    }
    public static void main(String[] args) {
        String skillSql = "INSERT INTO `skill_group`(`property_code`, `group_code`, `group_name`, `group_type`, `is_del`, `apply_to`) VALUES ('%s', '%s' ,'%s', 'NON_OPERATOR', 0, 1);";
        String jobConfigSql = "INSERT INTO `job_config`(`job_config_id`, `property_code`, `service_code`, `service_type`, `service_item`, `language`, `heat`, `skill_group`, `notify_channel`) VALUES ('%s', '%s', '%s', 'GUEST_REQUEST', '%s', 'EN', 0, '%s', 'AppPush');";
        String upgradeSql = "INSERT INTO `job_upgrade_strategy`(`job_config_id`, `upgrade_time`, `upgrade_num`, `notify_channel`, `skill_group_code`) VALUES ('%s', 30, 1, 'Message', '%s');";
        String skillHouse = "MSR Housekeeping";
        String skillUpgrade = "MSR Service";
        String[] skills = {skillHouse,skillUpgrade};
        String[] propertyCodes = {"THHK","KHHK","KSL","ESL","FIJ","GSH","THOG","SLP","THPH","THS","RRR","RSR","SEN","SLBK","SLBO","SLCB","SLCM","SLFM","SLHT","SLJ","SLKL","SLMC","SLSN","SUR","TAH"};

        System.out.println("------------------skillSql");
        long start = generate();
        Map<String,String> propSkillNameIdMap = new HashMap<>();
        for (String property : propertyCodes) {
            for (String skill : skills){
//                sleep();
                start += 2;
                String skillId = String.valueOf(start);
                String v = String.format(skillSql,property,skillId,skill);
                propSkillNameIdMap.put(property+skill,skillId);
                //skill sql
                System.out.println(v);
            }
        }
        System.out.println("------------------jobConfigSql");
        List<String> configIds = new ArrayList<>();
        Map<String, String> serviceCodeNameMap = getServiceCodeNameMap();
        Map<String, String> configIdUpgradeMap = getServiceCodeNameMap();
        for (String property : propertyCodes) {
            for(String serviceCode : serviceCodeNameMap.keySet()){
//                sleep();
                String configId = String.valueOf(start);
                configIds.add(configId);
                configIdUpgradeMap.put(configId,propSkillNameIdMap.get(property + skillUpgrade));
                System.out.println(String.format(jobConfigSql, configId, property, serviceCode, serviceCodeNameMap.get(serviceCode), propSkillNameIdMap.get(property + skillHouse)));
            }
        }

        System.out.println("------------------upgradeSql");
        for (String configId : configIds){
            System.out.println(String.format(upgradeSql,configId,configIdUpgradeMap.get(configId)));
        }

    }

    private static void sleep(){
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    static String serviceCodeName = "Chopsticks\t5027\n" +
            "Bowl\t5008\n" +
            "Plate\t5005\n" +
            "Baggage Assistance\t4006\n" +
            "Bath Mat\t1138\n" +
            "Room Make-up\t1125\n" +
            "Ice Bucket\t1123\n" +
            "Glass\t1113\n" +
            "Laundry\t1103\n" +
            "Foam Pillow\t1101\n" +
            "Buckwheat Pillow\t1100\n" +
            "Contour Pillow\t1097\n" +
            "Feather Pillow - Firm\t1096\n" +
            "Face Towel\t1086\n" +
            "Hand Towel\t1085\n" +
            "Bath Towel\t1084\n" +
            "Comb\t1046\n" +
            "Box Tissue\t1031\n" +
            "Bottled Water (extra)\t1030\n" +
            "Slippers - Kids\t1023\n" +
            "Slippers - Large\t1022\n" +
            "Slippers - Medium\t1021\n" +
            "Shower Cap\t1017\n" +
            "Cotton Buds\t1016\n" +
            "Shaving Kit\t1014\n" +
            "Toothbrush\t1013\n" +
            "Body Lotion\t1009\n" +
            "Shower Gel\t1008\n" +
            "Conditioner\t1007\n" +
            "Shampoo\t1006\n" +
            "Foam Pillow (for side sleeping)\t1170\n" +
            "Lavender Pillow\t1173\n" +
            "Cassia Seed Pillow\t1172\n" +
            "Knife, Fork & Spoon Set\t5060\n" +
            "Feather Pillow - Soft\t23080\n" +
            "Slippers - Extra Large\t23081\n" +
            "Baby Bottle Steriliser\t23082\n" +
            "Baby Bottle Warmer\t23083\n" +
            "Baby Highchair\t23084\n" +
            "Baby Playpen\t23085";

    private static Map<String,String> getServiceCodeNameMap (){
        Map<String,String> serviceCodeNameMap = new HashMap<>();
        String[] split = serviceCodeName.split("\n");
        for (String s : split){
            String[] split1 = s.split("\t");
            serviceCodeNameMap.put(split1[1],split1[0]);
        }
//        if(serviceCodeNameMap.size() != 40){
//            throw new RuntimeException("异常");
//        }
        return serviceCodeNameMap;
    }


}
