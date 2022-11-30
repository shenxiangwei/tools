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
 * location 映射关系
 * @author shenxiangwei
 * @date 2022/11/17 下午2:59
 */
public class UpdateLocationMapping {
    public static void main(String[] args) throws IOException {


        String template = "update location set ext_code = '%s' WHERE property_code = '%s' and node_code = '%s'";


        List<String> results = new ArrayList<>();
        final List<ExcelPo> excelPos = new ArrayList<>();
        String path = "/Users/sxw/File/s360/酒店初始化文件/25家location mapping-开发版-11.17.xlsx";

        readExcel(path,excelPos);

        StringBuilder stringBuilder = new StringBuilder();
        for(ExcelPo excelPo : excelPos){
            if(excelPo.getProperty() == null){
                continue;
            }
//            if(excelPo.getExtCode().equals(excelPo.getCode())){
//                continue;
//            }
            String sql = String.format(template,excelPo.getExtCode(),excelPo.getProperty(),excelPo.getCode());
            results.add(sql);
            stringBuilder.append(sql).append(";\n");
            System.out.println(sql + ";");
        }
        System.out.println(results.size());

        IOUtils.write(stringBuilder.toString(),new FileOutputStream("update_location.sql"),"utf-8");


    }

    private static void readExcel(String path,final List<ExcelPo> excelPos){
        EasyExcel.read(path, ExcelPo.class, new AnalysisEventListener<ExcelPo>() {
            @Override
            public void invoke(ExcelPo excelPo, AnalysisContext analysisContext) {
                excelPos.add(excelPo);
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {}
        }).sheet("mapping").doRead();
    }

    @Data
    public static class ExcelPo{
        @ExcelProperty(value = "property_code")
        private String property;
        @ExcelProperty(value = "node_code")
        private String code;
        @ExcelProperty(value = "FCS_code")
        private String extCode;
    }
}
