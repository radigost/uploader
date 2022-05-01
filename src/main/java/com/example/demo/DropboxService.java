package com.example.demo;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.users.FullAccount;
import com.example.domain.HasAttachments;
import com.example.domain.TelegramMessage;
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


    public String uploadAttachment(TelegramMessage telegramMessage) throws Exception {
        return null;
    }
    public String uploadAttachment(HasAttachments telegramMessage) throws Exception {

        if (telegramMessage.getAttachment() != null){
            var resultFilePath = String.format("%s/%s",dataFolderName,telegramMessage.getAttachment().getName());
            try {
                FileMetadata metadata = client.files().uploadBuilder(resultFilePath)
                    .uploadAndFinish(new FileInputStream(telegramMessage.getAttachment()));

            } catch (Exception e) {
                e.printStackTrace();
            }
            return resultFilePath;
        }
        else{
            return null;
        }

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

    public File appendLinkToEndOfFile(TelegramMessage fileName, String link) {
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
