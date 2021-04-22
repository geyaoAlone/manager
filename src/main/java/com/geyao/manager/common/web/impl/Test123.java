package com.geyao.manager.common.web.impl;

import java.io.*;

public class Test123 {
    public static void main(String[] args) throws Exception{

            File sourceFile = new File("C:/Users/secoo/Desktop/YE");

            File[]  s = sourceFile.listFiles();
        for (File file:s) {
            String line;
            File newFile = new File(file.getAbsolutePath() + ".tmp");
            BufferedReader br = new BufferedReader(new FileReader(file));
            PrintWriter pw = new PrintWriter(new FileWriter(newFile));
            //内容不为空，输出
            while ((line = br.readLine()) != null) {
                if(line.endsWith("&0.00&")) {
                    continue;
                }
                pw.println(line);
                pw.flush();
            }
            pw.close();
            br.close();

            //删除原文件
            if (!file.delete()) {
                System.out.println("删除老文件失败");
            }

            //重命名
            if (!newFile.renameTo(file)) {
                System.out.println("重新命名失败");
            }


        }





    }
}
