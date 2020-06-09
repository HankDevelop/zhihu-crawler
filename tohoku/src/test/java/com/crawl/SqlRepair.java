package com.crawl;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class SqlRepair {

    public static final String pre_sql = "insert into trans_word_info(trans_word, translation, dict_type, source_url, real_path) values(";
    public static final String suffix_sql = ");";

    public static void main(String[] args) {
        repairSqlFile();
    }

    public static void repairSqlFile() {
        File path = new File("E:\\temp\\tohoku\\crawlersql");
        if (!path.exists()) {
            return;
        }
        File[] sqlFiles = path.listFiles();
        for (File sqlFile : sqlFiles) {
            RandomAccessFile raf = null;
            try {
                raf = new RandomAccessFile(sqlFile, "rw");
                String contentLine;
                long lastPoint = 0; //记住上一次的偏移量
                while ((contentLine = raf.readLine()) != null) {
                    final long ponit = raf.getFilePointer();
                    if (contentLine.trim().length() > 0) {
                        contentLine = new String(contentLine.getBytes("ISO-8859-1"),"GBK");
                        contentLine = contentLine.substring(contentLine.lastIndexOf("values(") + 7, contentLine.lastIndexOf(")"));
                        String[] sqlParams = StringUtils.splitPreserveAllTokens(contentLine, "'");
                        if (sqlParams.length != 11) {
                            sqlParams = contentLine.split("','");
                            sqlParams[0] = sqlParams[0].substring(1).replaceAll("\'", "\\\\'");
                            sqlParams[1] = sqlParams[1].replaceAll("\'", "\\\\'");
                            raf.seek(lastPoint);
                            contentLine = pre_sql + "'" + StringUtils.join(sqlParams, "','") + suffix_sql;
                            raf.writeBytes(new String(contentLine.getBytes("GBK"), "ISO-8859-1"));
                        }
                    }
                    lastPoint = ponit;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
