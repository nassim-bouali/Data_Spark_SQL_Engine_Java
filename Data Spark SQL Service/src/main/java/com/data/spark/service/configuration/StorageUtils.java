package com.data.spark.service.configuration;

import com.azure.core.credential.AccessToken;
import com.azure.core.credential.TokenRequestContext;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.sas.BlobContainerSasPermission;
import com.azure.storage.blob.sas.BlobServiceSasSignatureValues;
import com.data.spark.Storage;
import com.data.spark.Target;
import com.data.spark.flow.BatchPlan;
import org.apache.hadoop.conf.Configuration;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class StorageUtils {
    public static void updateHadoopConfig(BatchPlan batchPlan, Configuration config){
//        initialize empty hadoop configuration
        Configuration storageConfig = new Configuration(false);

//        update hadoop configuration with inputs sas tokens
        batchPlan.getInputs().stream()
                .filter(storage -> storage.getStorageAccount() != null)
                .forEach(fileStorage -> {
                    if (fileStorage.getStorageAccount() != null){
                        storageConfig.set(String.format("fs.azure.sas.token.provider.type.%s.dfs.core.windows.net", fileStorage.getStorageAccount()), "com.data.spark.service.configuration.SasTokenProvider");
                        storageConfig.set(String.format("fs.azure.account.auth.type.%s.dfs.core.windows.net", fileStorage.getStorageAccount()), "SAS");
                        storageConfig.set(String.format("%s.%s", fileStorage.getStorageAccount(), fileStorage.getContainer()), getSasToken(fileStorage));
                    }
                });

//        update hadoop configuration with outputs sas tokens
        batchPlan.getTargets().stream()
                .map(Target::getOutput)
                .filter(storage -> storage.getStorageAccount() != null)
                .forEach(fileStorage -> {
                    if (fileStorage.getStorageAccount() != null){
                        storageConfig.set(String.format("fs.azure.sas.token.provider.type.%s.dfs.core.windows.net", fileStorage.getStorageAccount()), "com.data.spark.service.configuration.SasTokenProvider");
                        storageConfig.set(String.format("fs.azure.account.auth.type.%s.dfs.core.windows.net", fileStorage.getStorageAccount()), "SAS");
                        storageConfig.set(String.format("%s.%s", fileStorage.getStorageAccount(), fileStorage.getContainer()), getSasToken(fileStorage));
                    }
                });

//        enrich existing hadoop config with the new one
        merge(storageConfig, config);
    }

    public static void merge(Configuration source, Configuration target){
        source.forEach(config -> target.set(config.getKey(), config.getValue()));
    }

    public static String getSasToken(Storage fileStorage){
        return fileStorage.getSasToken() != null ? fileStorage.getSasToken() : getSasTokenWithUserDelegationKey(fileStorage);
    }

    public static String getSasTokenWithUserDelegationKey(Storage fileStorage){
        // Use the DefaultAzureCredentialBuilder to authenticate without an account key
        // This will automatically use available credentials like Managed Identity or Azure CLI
        DefaultAzureCredentialBuilder credentialBuilder = new DefaultAzureCredentialBuilder();

        // Build the BlobServiceClient with authentication
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint("https://" + fileStorage.getStorageAccount() + ".blob.core.windows.net")
                .credential(credentialBuilder.build())
                .buildClient();

        // Get a reference to the container
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(fileStorage.getContainer());

        // Define the permissions and expiration for the SAS token
        BlobContainerSasPermission containerSasPermission = new BlobContainerSasPermission()
                .setReadPermission(true)
                .setListPermission(true)
                .setWritePermission(true);

        OffsetDateTime expirationTime = OffsetDateTime.now().plusHours(1);

        // Generate the SAS token values
        BlobServiceSasSignatureValues sasValues = new BlobServiceSasSignatureValues(expirationTime, containerSasPermission)
                .setStartTime(OffsetDateTime.now());

        // Generate the SAS token for the container
        String sasToken = containerClient.generateSas(sasValues);

        return sasToken;
    }

    public static Properties getProperties(Map<String, String> options){
        Properties connectionProps = new Properties();
        connectionProps.put("loginTimeout", "300");
        if (options == null || !options.containsKey("password")){
            connectionProps.put("user", "");
            connectionProps.put("password", "");
            connectionProps.put("accessToken", getJdbcAccessToken());
        }
        else
            connectionProps.putAll(options);

        return connectionProps;
    }

    public static String getJdbcAccessToken(){
        DefaultAzureCredential credential = new DefaultAzureCredentialBuilder().build();

        // Specify the token request context
        TokenRequestContext requestContext = new TokenRequestContext().addScopes("https://database.azure.net/.default");

        // Obtain the access token asynchronously
        Mono<AccessToken> tokenMono = credential.getToken(requestContext);

        // Block and get the access token
        AccessToken token = tokenMono.block();
        String accessToken = token.getToken();
        System.out.println("Access Token: " + accessToken);

        return accessToken;
    }
}
