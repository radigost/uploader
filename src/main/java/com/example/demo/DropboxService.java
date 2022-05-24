package com.example.demo;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.TokenAccessType;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.example.domain.DropBoxServiceStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DropboxService {

    @Value("${dropbox.appKey}")
    private String appKey;

    @Value("${dropbox.appSecret}")
    private String appSecret;

    @Value("${dropbox.folder}")
    private String folderName;

    @Value("${dropbox.dataFolder}")
    private String dataFolderName;

    @Getter
    private DropBoxServiceStatus status = DropBoxServiceStatus.NOT_AUTHORIZED;

    private DbxWebAuth dbxWebAuth = null;

    private DbxClientV2 dbxClient = null;

    @SneakyThrows
    public String generateAuthUrl() {
        // Run through Dropbox API authorization process
        DbxAppInfo appInfo = new DbxAppInfo(appKey, appSecret);
        DbxRequestConfig requestConfig = new DbxRequestConfig("examples-authorize");
        dbxWebAuth = new DbxWebAuth(requestConfig, appInfo);

        // TokenAccessType.OFFLINE means refresh_token + access_token. ONLINE means access_token only.
        DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder()
            .withNoRedirect()
            .withTokenAccessType(TokenAccessType.OFFLINE)
            .build();

        String authorizeUrl = dbxWebAuth.authorize(webAuthRequest);
        status = DropBoxServiceStatus.WAITING_AUTH_CODE;
        return authorizeUrl;
    }

    @SneakyThrows
    public boolean completeAuth(@NonNull String code) {
        code = code.trim();
        try {
            DbxAuthFinish dbxAuthFinish = dbxWebAuth.finishFromCode(code);
            DbxCredential credential = new DbxCredential(dbxAuthFinish.getAccessToken(), dbxAuthFinish
                .getExpiresAt(), dbxAuthFinish.getRefreshToken(), appKey, appSecret);
            DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/java-tutorial").build();
            dbxClient = new DbxClientV2(config, credential);

            System.out.println("Authorization complete.");
            System.out.println("- User ID: " + dbxAuthFinish.getUserId());
            System.out.println("- Account ID: " + dbxAuthFinish.getAccountId());
            System.out.println("- Scope: " + dbxAuthFinish.getScope());

            status = DropBoxServiceStatus.AUTHORIZED;
            return true;
        } catch (DbxException ex) {
            System.err.println("Error in DbxWebAuth.authorize: " + ex.getMessage());
            status = DropBoxServiceStatus.NOT_AUTHORIZED;
            return false;
        }
    }

    @SneakyThrows
    public String uploadFileToDataFolder(File file, String path) {
        var extension = getFileExtension(getFileName(path));
        var dateValue = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        var resultFilePath = String.format("%s/%s.%s", dataFolderName, dateValue, extension);
        FileMetadata metadata = dbxClient.files().uploadBuilder(resultFilePath)
            .uploadAndFinish(new FileInputStream(file));
        return metadata.getName();
    }

    @SneakyThrows
    public String createAndUploadMdFile(String content) {
        var date = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        File tempFile = File.createTempFile("123", "", null);
        FileOutputStream fos = new FileOutputStream(tempFile);
        fos.write(content.getBytes(StandardCharsets.UTF_8));
        fos.close();
        InputStream in = new FileInputStream(tempFile);
        FileMetadata metadata = dbxClient.files().uploadBuilder(String.format("%s/%s.md", folderName, date))
            .uploadAndFinish(in);
        tempFile.deleteOnExit();
        return metadata.getName();
    }

    private String getFileName(String path) {
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
