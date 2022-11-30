package excel2sql;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shenxiangwei
 * @date 2022/11/15 下午2:02
 */
public class Location {

    public static void main(String[] args) {
        List<ExcelPo> result = new ArrayList<>();
        File file = new File("/Users/sxw/File/s360/酒店初始化文件/导出的csv");
        if(file.isDirectory()){
            for (int i = 0; i < file.listFiles().length; i++) {
                readExcel(file.listFiles()[i],result);
            }
        }

        EasyExcel.write("/Users/sxw/File/s360/酒店初始化文件/result.xlsx", ExcelPo.class).sheet("sheet").doWrite(result);
    }


    private static void readExcel(File file,final List<ExcelPo> excelPos){
        EasyExcel.read(file, ExcelPo.class, new AnalysisEventListener<ExcelPo>() {
            @Override
            public void invoke(ExcelPo excelPo, AnalysisContext analysisContext) {
                excelPos.add(excelPo);
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {}
        }).sheet().doRead();
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    public static class ExcelPo {
        @ExcelProperty("location_id")
        private String location_id;

        @ExcelProperty("node_type")
        private String node_type;

        @ExcelProperty("node_code")
        private String string;

        @ExcelProperty("parent_id")
        private String parent_id;

        @ExcelProperty("property_code")
        private String property_code;

        @ExcelProperty("node_name")
        private String node_name;

        @ExcelProperty("ext_code")
        private String ext_code;
    }
}
