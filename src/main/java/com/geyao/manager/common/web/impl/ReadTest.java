package com.geyao.manager.common.web.impl;

import org.apache.commons.lang3.ArrayUtils;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.zip.*;

public class ReadTest {
    private static String[] nos = {"3955000000000021","3955000000001007","3955000000000007","3955000000000004","3955000000000005","3955000000000002","3955000000000003","3955000000000011","3955000000000012"};


    public static void main(String[] args) throws IOException {
        FileInputStream isinput=new FileInputStream("C:/Users/secoo/Desktop/ahdakjbsdkqdj.zip");
        //获取ZIP输入流(一定要指定字符集Charset.forName("GBK")否则会报java.lang.IllegalArgumentException: MALFORMED)
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(isinput), Charset.forName("GBK"));

        //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
        ZipEntry ze = null;



        //循环遍历
        while ((ze = zipInputStream.getNextEntry()) != null) {

            System.out.println("file");

            InputStreamReader i = new InputStreamReader(zipInputStream);
            //读取
            BufferedReader br = new BufferedReader(i);

            String line;

            //内容不为空，输出
            while ((line = br.readLine()) != null) {
                System.out.println("内容"+line);
                File tempFile = new File("/temp/temp.txt");
                BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
//                if(StringUtils.isBlank(line) || line.endsWith("&0.00&")){
//                    continue;
//                }
                writer.write(line + System.getProperty("line.separator"));
//                String[] datas = line.split("&");
//                if(datas.length < 4){
//                    continue;
//                }
//                if(ArrayUtils.contains(nos,datas[2])){
//                    continue;
//                }
//                BigDecimal ba = new BigDecimal(datas[3]);
//
//                if(ba.compareTo(BigDecimal.ZERO) > 0){
//                    System.out.println(datas[2] +":"+ ba);
//                    total = total.add(ba);
//                }
            }
            System.out.println("");
        }

        //一定记得关闭流
        zipInputStream.closeEntry();
        isinput.close();
        //writer.close();
        //tempFile.renameTo(inputFile);
    }
}
