package cn.cherry.executor.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author: sinbad.cheng
 * @time: 2023/9/11 16:43
 * @description：
 */
public class RefreshReadData {

    public static void main(String[] args) {
        // 创建 ObjectMapper 对象，用于读取 JSON
        ObjectMapper objectMapper = new ObjectMapper();

        // JSON 文件路径
        String jsonFilePath = "src/main/java/cn/cherry/executor/util/0911Waybill.json";

        try {
            // 读取 JSON 文件
            File jsonFile = new File(jsonFilePath);

            List<String> waybills=objectMapper.readValue(jsonFile, new TypeReference<List<String>>() {});

            System.out.println(waybills);
            System.out.println(waybills.size());

        } catch (IOException e) {
            e.printStackTrace();
        }
        // test
    }

}
