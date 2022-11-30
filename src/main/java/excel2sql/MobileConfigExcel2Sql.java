package excel2sql;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * location 映射关系
 * @author shenxiangwei
 * @date 2022/2/24 3:20 下午
 */
public class MobileConfigExcel2Sql {


    public static void main(String[] args) throws IOException {


        String template = "INSERT INTO `job_mobile_config`(`property_code`, `service_code`, `mobile_type_code`, `available_before_arrival`, `max_quantity`, `fcs_service_code`, `sequence`, `active`) " +
                "VALUES ('%s', '%s', '%s', 0, %s, '%s',%s, 0);";

        List<String> s360Code = Arrays.asList("THHK","KHHK","KSL","ESL","FIJ","GSH","THOG","SLP","THPH","THS","RRR","RSR","SEN","SLBK","SLBO","SLCB","SLCM","SLFM","SLHT","SLJ","SLKL","SLMC","SLSN","SUR","TAH");

        List<String> results = new ArrayList<>();
        final List<ExcelPo> excelPos = new ArrayList<>();
        String path = "/Users/sxw/File/s360/酒店初始化文件/job_mobile_config_模板（11.14，开发版）.xlsx";

        readExcel(path,excelPos);


        for(ExcelPo excelPo : excelPos){
            if(!s360Code.contains(excelPo.getProperty())){
                System.out.println(excelPo.getProperty());
            }
            String sql = String.format(template,excelPo.getProperty(),excelPo.getService_code(),excelPo.getMobile_type_code(),
                    excelPo.getMax_quantity(),excelPo.getFcs_service_code(),excelPo.getSequence());
//            System.out.println(sql);

        }


//        IOUtils.write(stringBuilder.toString(),new FileOutputStream("infra_report_cate.sql"),"utf-8");


    }

    private static void readExcel(String path,final List<ExcelPo> excelPos){
        EasyExcel.read(path, ExcelPo.class, new AnalysisEventListener<ExcelPo>() {
            @Override
            public void invoke(ExcelPo excelPo, AnalysisContext analysisContext) {
                excelPos.add(excelPo);
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {}
        }).sheet(0).headRowNumber(1).doRead();
    }

    @Data
    public static class ExcelPo{
        @ExcelProperty(index = 0)
        private String property;
        @ExcelProperty(index = 2)
        private String service_code;
        @ExcelProperty(index = 3)
        private String mobile_type_code;
        @ExcelProperty(index = 5)
        private String max_quantity;
        @ExcelProperty(index = 6)
        private String fcs_service_code;
        @ExcelProperty(index = 7)
        private String sequence;
    }
}
