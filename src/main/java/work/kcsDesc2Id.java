package work;

import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shenxiangwei
 * @date 2022/8/4 上午10:22
 */
public class kcsDesc2Id {

    static String sql = "update job_mobile_config set kcs_category = %s , kcs_description = %s where property_code = 'SLV' " +
            "and kcs_category = '%s' and kcs_description = '%s'";

    public static void main(String[] args) {
        List<ExcelPo> slsPos = new ArrayList<>();
        List<ExcelPo> slvPos = new ArrayList<>();
        readExcel("/Users/sxw/Downloads/KCS.xlsx",slsPos,0);
        readExcel("/Users/sxw/Downloads/KCS.xlsx",slvPos,1);
        List<ExcelPo> pos;

        pos = slvPos;

        String s = FileUtil.readString("/Users/sxw/File/s360/knowcross/slv.json", StandardCharsets.UTF_8);
        JSONArray array = JSON.parseArray(s);

        JSONArray categories = array.getJSONObject(0).getJSONArray("CallCategories");
        JSONArray descriptions = array.getJSONObject(0).getJSONArray("CallDescriptions");

        Map<String,Integer> categoryMap = new HashMap<>();
        Map<String,Integer> descriptionMap = new HashMap<>();

        for (int i = 0; i < categories.size(); i++) {
            categoryMap.put(categories.getJSONObject(i).getString("Description").toLowerCase(),
                    categories.getJSONObject(i).getInteger("Id"));
        }

        for (int i = 0; i < descriptions.size(); i++) {
            descriptionMap.put(descriptions.getJSONObject(i).getString("Description").toLowerCase(),
                    descriptions.getJSONObject(i).getInteger("Id"));
        }

        for (ExcelPo excelPo : pos) {
            excelPo.setCateId(categoryMap.get(excelPo.getCategory().toLowerCase()));
            excelPo.setDesId(descriptionMap.get(excelPo.getDescription().toLowerCase()));
//            if(categoryMap.get(excelPo.getCategory().toLowerCase()) != null && descriptionMap.get(excelPo.getDescription().toLowerCase()) != null){
//                System.out.println(String.format(sql,excelPo.getCateId(),excelPo.getDesId(),excelPo.getCategory(),excelPo.getDescription()) + ";");
//            }

            if(categoryMap.get(excelPo.getCategory().toLowerCase()) == null || descriptionMap.get(excelPo.getDescription().toLowerCase()) == null){
                System.out.println(excelPo.getCategory() + " ____ " + excelPo.getDescription());
            }
        }


//        System.out.println(JSON.toJSONString(slsPos));

    }

    private static void readExcel(String path,final List<ExcelPo> excelPos,int sheet){
        EasyExcel.read(path, ExcelPo.class, new AnalysisEventListener<ExcelPo>() {
            @Override
            public void invoke(ExcelPo excelPo, AnalysisContext analysisContext) {
                excelPos.add(excelPo);
            }
            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {}
        }).sheet(sheet).doRead();
    }

    @Data
    public static class ExcelPo{
        @ExcelProperty(value = "Service Item Code/Category")
        private String category;
        @ExcelProperty(value = "Knowcross Description")
        private String description;
        @ExcelProperty(value = "Hotel code")
        private String code;
        @ExcelIgnore
        private Integer cateId;
        @ExcelIgnore
        private Integer desId;
    }
}
