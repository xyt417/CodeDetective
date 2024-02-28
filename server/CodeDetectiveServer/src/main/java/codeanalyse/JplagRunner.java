package codeanalyse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JplagRunner {
    public void runJPlag(String submissionDirectory, String outputDirectory, String language, String args) {
        String jplagPath = Objects.requireNonNull(getClass().getClassLoader().getResource("jplag.jar")).getPath();

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-jar");
        command.add(jplagPath);
        if("".equals(submissionDirectory)) {
            throw new IllegalArgumentException("Submission directory cannot be empty");
        }
        command.add(submissionDirectory);
        if(!"".equals(language)) {
            command.add("-l");
            command.add(language);
        }
        if(!"".equals(outputDirectory)) {
            command.add("-r");
            command.add(outputDirectory);
        }
        if(!"".equals(args)) {
            String[] argArray = args.split(" ");
            Collections.addAll(command, argArray);
        }

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(true); // 将子进程的标准错误流合并到标准输出流

        try {
            System.out.println("Running JPlag with command: " + String.join(" ", command));
            Process process = processBuilder.start(); // 启动子进程, 子进程的输出会保存在缓冲区中

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream())); // 注：获取子进程的 标准输出流

            String line;
            while((line = reader.readLine()) != null) {
                System.out.println("JPlag: " + line);
            }

            int exitCode = process.waitFor();
            System.out.println("Jplag has exited with code " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
