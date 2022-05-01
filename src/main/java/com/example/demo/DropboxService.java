package com.example.demo;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DropboxService {
    private final DbxClientV2 client;

    @Value("${dropbox.folder}")
    private String folderName;

    @Value("${dropbox.dataFolder}")
    private String dataFolderName;

    public String getFileName(String path) {
        return path.substring(path.lastIndexOf("/")+1);
    }

    public String uploadFile(File file, String path) throws Exception {
        var resultFilePath = String.format("%s/%s",dataFolderName,path);
        try {
            FileMetadata metadata = client.files().uploadBuilder(resultFilePath)
                .uploadAndFinish(new FileInputStream(file));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFilePath;
    }

    public String uploadTextFile(String content) {
        var dateValue = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
        try {
            File tempFile = File.createTempFile("123", "", null);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(content.getBytes(StandardCharsets.UTF_8));
            fos.close();
            InputStream in = new FileInputStream(tempFile);
            FileMetadata metadata = client.files().uploadBuilder(String.format("%s/%s.md",folderName,dateValue) )
                .uploadAndFinish(in);
            tempFile.deleteOnExit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.format("%s.md",dateValue);
    }

    public File appendLinkToEndOfFile(String fileName, String link) {
        try {
            File tempFile = File.createTempFile("123", "", null);
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(link.getBytes(StandardCharsets.UTF_8));
            fos.close();
            InputStream in = new FileInputStream(tempFile);
            FileMetadata metadata = client.files().uploadBuilder(String.format("%s/%s",folderName,fileName) )
                .uploadAndFinish(in);
            tempFile.deleteOnExit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(fileName);
    }


    DropboxService(@Value("${dropbox.token}") String dropboxToken) {
        DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
        client = new DbxClientV2(config, dropboxToken);
        try {
            FullAccount account = client.users().getCurrentAccount();
            System.out.println(account.getName().getDisplayName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
