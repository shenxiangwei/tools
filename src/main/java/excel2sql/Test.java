package excel2sql;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenxiangwei
 * @date 2022/5/27 下午9:01
 */
public class Test {

    public static void main(String[] args) {
        String a = "{\n" +
                "    \"SLBO\": \"41204\",\n" +
                "    \"SLFT\": \"626344\",\n" +
                "    \"SLFM\": \"234922\",\n" +
                "    \"SLSH\": \"337403\",\n" +
                "    \"SLFZ\": \"085473\",\n" +
                "    \"THPH\": \"249680\",\n" +
                "    \"SLBT\": \"004809\",\n" +
                "    \"SLH\": \"070396\",\n" +
                "    \"SLJ\": \"688886\",\n" +
                "    \"SLSN\": \"825372\",\n" +
                "    \"RRR\": \"016292\",\n" +
                "    \"SLQH\": \"968458\",\n" +
                "    \"CNSHA004\": \"024998\",\n" +
                "    \"SLQ\": \"953466\",\n" +
                "    \"SLWZ\": \"701639\",\n" +
                "    \"SLCD\": \"304652\",\n" +
                "    \"SLKL\": \"348493\",\n" +
                "    \"SLYZ\": \"594939\",\n" +
                "    \"SLCB\": \"138880\",\n" +
                "    \"SLCC\": \"717750\",\n" +
                "    \"ESL\": \"60861\",\n" +
                "    \"SLZ\": \"507181\",\n" +
                "    \"HJTS\": \"25221\",\n" +
                "    \"ISL\": \"63425\",\n" +
                "    \"HJHK\": \"927161\",\n" +
                "    \"SLCM\": \"82543\",\n" +
                "    \"SLSZ\": \"422874\",\n" +
                "    \"SLNC\": \"865070\",\n" +
                "    \"SLXM\": \"488864\",\n" +
                "    \"CNNNG001\": \"018382\",\n" +
                "    \"SLHF\": \"173796\",\n" +
                "    \"SUR\": \"82462\",\n" +
                "    \"SLTR\": \"120726\",\n" +
                "    \"SLTO\": \"817594\",\n" +
                "    \"SLNJ\": \"593560\",\n" +
                "    \"HBKC\": \"868110\",\n" +
                "    \"SBHI\": \"779874\",\n" +
                "    \"SLPG\": \"259418\",\n" +
                "    \"SLTY\": \"50879\",\n" +
                "    \"SLHN\": \"671791\",\n" +
                "    \"HJOG\": \"43956\",\n" +
                "    \"SLHI\": \"740419\",\n" +
                "    \"SLJI\": \"721807\",\n" +
                "    \"SLTS\": \"865697\"\n" +
                "}";
        JSONObject aj = JSONObject.parseObject(a);
        List<ExcelPo> s = new ArrayList<>();
        readExcel("/Users/sxw/File/s360/FCS工单/FCS情况统计.xlsx",s);
        for (ExcelPo excelPo : s){
            excelPo.setJobId(aj.getString(excelPo.getProperty()));
            excelPo.setDate("2022-05-27");
            excelPo.setIsSuc("1");
        }

        EasyExcel.write("/Users/sxw/File/s360/FCS工单/test.xlsx",ExcelPo.class).sheet().doWrite(s);

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
        @ExcelProperty(value = "code")
        private String property;
        @ExcelProperty(value = "版本")
        private String version;
        @ExcelProperty(value = "工单号")
        private String jobId;
        @ExcelProperty(value = "工单日期")
        private String date;
        @ExcelProperty(value = "是否成功")
        private String isSuc;
    }
}
