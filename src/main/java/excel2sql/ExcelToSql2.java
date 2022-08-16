package excel2sql;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenxiangwei
 * @date 2022/1/13 11:15 上午
 */
public class ExcelToSql2 {



    public static void main(String[] args) throws IOException {


        String replaceInto = "replace into infra_report_cate (id,hotel_id,biz_id,max_retry_num,period,start_time,end_time,state," +
                "step1_report,step2_outlet,step3_dept_cate,step4_export,step5_return,outlet,biz_code,hotel_code,file_id,step5_export," +
                "switch_language,biz_type,frequency,haveDate,main_code,code,`type`)" +
                "values(%s,%s,%s,'%s','%s','%s','%s',%s,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')";


        List<String> results = new ArrayList<>();
        final List<ExcelPo> excelPos = new ArrayList<>();
        String path = "/Users/sxw/Library/Containers/com.tencent.WeWorkMac/Data/Documents/Profiles/A925D02A17342D42A398CD17E4AEB192/Caches/Files/2022-01/f82f6db816777ea0626f8a36db22db90/Infra_Cate.xlsx";

        readExcel(path,excelPos);

        StringBuilder stringBuilder = new StringBuilder();
        for(ExcelPo excelPo : excelPos){
            if(excelPo.getV1() == null){
                continue;
            }
            String sql = String.format(replaceInto,excelPo.getV1(),excelPo.getV2(),excelPo.getV3(),excelPo.getV4(),
                    excelPo.getV5(),excelPo.getV6(),excelPo.getV7(),excelPo.getV8(),excelPo.getV9(),excelPo.getV10(),
                    excelPo.getV11(),excelPo.getV12(),excelPo.getV13(),excelPo.getV14(),excelPo.getV15(),excelPo.getV16(),
                    excelPo.getV17(),excelPo.getV18(),excelPo.getV19(),excelPo.getV20(),excelPo.getV21(),excelPo.getV22(),
                    excelPo.getV23(),excelPo.getV24(),excelPo.getV25());
            sql = sql.replace("'null'","null");
            results.add(sql);
            stringBuilder.append(sql).append(";\n");
            System.out.println(sql);
        }

        IOUtils.write(stringBuilder.toString(),new FileOutputStream("infra_report_cate.sql"),"utf-8");


    }

    private static void readExcel(String path,final List<ExcelPo> excelPos){
        EasyExcel.read(path, ExcelPo.class, new AnalysisEventListener<ExcelPo>() {
            @Override
            public void invoke(ExcelPo excelPo, AnalysisContext analysisContext) {
                excelPos.add(excelPo);
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {}
        }).sheet().doRead();
    }

    @Data
    public static class ExcelPo{
        @ExcelProperty(value = "id")
        private String v1;
        @ExcelProperty(value = "hotel_id")
        private String v2;
        @ExcelProperty(value = "biz_id")
        private String v3;
        @ExcelProperty(value = "max_retry_num")
        private String v4;
        @ExcelProperty(value = "period")
        private String v5;
        @ExcelProperty(value = "start_time")
        private String v6;
        @ExcelProperty(value = "end_time")
        private String v7;
        @ExcelProperty(value = "state")
        private String v8;

        @ExcelProperty(value = "step1_report")
        private String v9;
        @ExcelProperty(value = "step2_outlet")
        private String v10;
        @ExcelProperty(value = "step3_dept_cate")
        private String v11;
        @ExcelProperty(value = "step4_export")
        private String v12;
        @ExcelProperty(value = "step5_return")
        private String v13;
        @ExcelProperty(value = "outlet")
        private String v14;
        @ExcelProperty(value = "biz_code")
        private String v15;
        @ExcelProperty(value = "hotel_code")
        private String v16;

        @ExcelProperty(value = "file_id")
        private String v17;
        @ExcelProperty(value = "step5_export")
        private String v18;
        @ExcelProperty(value = "switch_language")
        private String v19;
        @ExcelProperty(value = "biz_type")
        private String v20;
        @ExcelProperty(value = "frequency")
        private String v21;
        @ExcelProperty(value = "haveDate")
        private String v22;
        @ExcelProperty(value = "main_code")
        private String v23;
        @ExcelProperty(value = "code")
        private String v24;

        @ExcelProperty(value = "type")
        private String v25;
    }
}
