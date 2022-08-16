package excel2sql;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenxiangwei
 * @date 2022/1/13 11:15 上午
 */
public class ExcelToSql {



    public static void main(String[] args) throws IOException {
        String update = "update infra_biz " +
                "set hotel_id = '%s',biz_code = '%s',biz_name = '%s', infra_username = '%s',infra_password = '%s',hotel_code='%s',`type`='%s'" +
                " where id = '%s'";

        String replaceInto = "replace into infra_biz" +
                "(id,hotel_id,biz_code,biz_name, infra_username,infra_password,hotel_code,`type`)" +
                "values(%s,%s,'%s','%s','%s','%s','%s','%s')";


        List<String> results = new ArrayList<>();
        final List<ExcelPo> excelPos = new ArrayList<>();
        String path = "/Users/sxw/Library/Containers/com.tencent.WeWorkMac/Data/Documents/Profiles/A925D02A17342D42A398CD17E4AEB192/Caches/Files/2022-02/8872a65be2bb57c1239eb1931970aa60/infrAccounts.xlsx";

        readExcel(path,excelPos);

        StringBuilder stringBuilder = new StringBuilder();
        for(ExcelPo excelPo : excelPos){
            String sql = String.format(replaceInto,excelPo.getId(),excelPo.getHotelId(),excelPo.getBizCode()
                    ,excelPo.getBizName(),excelPo.getInfraUserName(),excelPo.getInfraPass(),
                    excelPo.getHotelCode(),excelPo.getType());
            results.add(sql);
            stringBuilder.append(sql).append(";\n");
            System.out.println(sql);
        }

        IOUtils.write(stringBuilder.toString(),new FileOutputStream("infra_biz.sql"),"utf-8");


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
    public static class ExcelPo {
        @ExcelProperty(value = "id")
        private String id;
        @ExcelProperty(value = "hotel_id")
        private String hotelId;
        @ExcelProperty(value = "biz_code")
        private String bizCode;
        @ExcelProperty(value = "biz_name")
        private String bizName;
        @ExcelProperty(value = "infra_username")
        private String infraUserName;
        @ExcelProperty(value = "infra_password")
        private String infraPass;
        @ExcelProperty(value = "hotel_code")
        private String hotelCode;
        @ExcelProperty(value = "type")
        private String type;

    }
}
