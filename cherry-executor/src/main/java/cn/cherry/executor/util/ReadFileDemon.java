package cn.cherry.executor.util;

import cn.hutool.core.io.resource.ClassPathResource;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author: sinbad.cheng
 * @time: 2023/9/11 16:43
 * @description：
 */
@Slf4j
public class ReadFileDemon {

    public static void main(String[] args) {
        // JSON 文件路径
        String jsonFilePath = "read_demon.json";
        Optional<String> optionalString = readJsonFileFromResources(jsonFilePath);

        System.err.println(optionalString.orElse("null"));
    }


    public static Optional<String> readJsonFileFromResources(String path) {
        ClassPathResource classPathResource = new ClassPathResource(path);
        try (InputStream inputStream = classPathResource.getStream()) {
            // 读取文件内容到StringBuilder
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, length, StandardCharsets.UTF_8));
            }
            // 将StringBuilder转换为String并返回
            return Optional.of(stringBuilder.toString());
        } catch (Exception e) {
            // 处理异常
            return Optional.empty();
        }
    }

}
