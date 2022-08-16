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
 * @date 2022/2/24 3:20 下午
 */
public class ExcelToSql3 {


    public static void main(String[] args) throws IOException {


        String template = "update location set node_name = '%s' WHERE property_code = '%s' and node_code = '%s'";


        List<String> results = new ArrayList<>();
        final List<ExcelPo> excelPos = new ArrayList<>();
        String SLDBPath = "/Users/sxw/Library/Containers/com.tencent.WeWorkMac/Data/Documents/Profiles/A925D02A17342D42A398CD17E4AEB192/Caches/Files/2022-02/7483d3c53910d063f3354c6dabf14251/SLDB Location-0224.xlsx";
        String ISLPath = "/Users/sxw/Library/Containers/com.tencent.WeWorkMac/Data/Documents/Profiles/A925D02A17342D42A398CD17E4AEB192/Caches/Files/2022-02/938f7d3586cde13821d5a24c6f7aec1c/ISL Location.xlsx";

        readExcel(ISLPath,excelPos);

        StringBuilder stringBuilder = new StringBuilder();
        for(ExcelPo excelPo : excelPos){
            String sql = String.format(template,excelPo.getName(),excelPo.getProperty(),excelPo.getCode());
            results.add(sql);
            stringBuilder.append(sql).append(";\n");
            System.out.println(sql + ";");
        }
        System.out.println(results.size());

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
        }).sheet().doRead();
    }

    @Data
    public static class ExcelPo{
        @ExcelProperty(value = "Property")
        private String property;
        @ExcelProperty(value = "Code")
        private String code;
        @ExcelProperty(value = "Name")
        private String name;
    }
}
