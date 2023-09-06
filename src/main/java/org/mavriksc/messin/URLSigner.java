package org.mavriksc.messin;

import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities;
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest;
import software.amazon.awssdk.services.cloudfront.url.SignedUrl;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.nio.file.Paths;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class URLSigner {
    public static final long EXPIRES = 5;
    public static final String PRIVATE_KEY_FILE_PATH = "D:\\code\\jus-messin\\private_key.der";
    public static final String KEY_PAIR_ID = "K32AXKNIDJCC6X";
    public static final String URL_BASE = "https://d16ybfbofo66j5.cloudfront.net/";
    public static final String BUCKET_NAME = "dev-cs-smcdickbutt";
    public static final CloudFrontUtilities CFU = CloudFrontUtilities.create();
    public static final S3Client S_3 = S3Client.builder()
            .region(Region.US_EAST_1)
            .credentialsProvider(EnvironmentVariableCredentialsProvider.create())
            .build();

    public static void main(String[] args) {
        List<S3Object> s3Objects = listBucketObjects();
        s3Objects.stream().filter(it -> it.key().contains("1.jpg")).forEach(it -> signedURLForObjetKey(it.key()));
    }

    private static void signedURLForObjetKey(String key) {
        try {
            CannedSignerRequest csr = CannedSignerRequest.builder()
                    .resourceUrl(URL_BASE + key)
                    .privateKey(Paths.get(PRIVATE_KEY_FILE_PATH))
                    .keyPairId(KEY_PAIR_ID)
                    .expirationDate(Instant.now().plus(EXPIRES, ChronoUnit.DAYS)).build();
            Date exp = new Date(csr.expirationDate().toEpochMilli());
            System.out.println("Expiration date: for " + key + " " + exp.toString());

            SignedUrl url = CFU.getSignedUrlWithCannedPolicy(csr);
            System.out.println(url);
        } catch (Exception e) {
            System.out.println("couldn't load key");
        }
    }

    public static List<S3Object> listBucketObjects() {
        ListObjectsRequest listObjects = ListObjectsRequest
                .builder()
                .bucket(BUCKET_NAME)
                .build();
        ListObjectsResponse res = S_3.listObjects(listObjects);
        return res.contents();
    }

    public static List<S3Object> listBucketObjectsForItemNumber(long itemNumber) {
        String prefix = "ATT/WAREHOUSE/" + itemNumber + "/";

        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(BUCKET_NAME)
                    .prefix(prefix)
                    .build();

            ListObjectsResponse res = S_3.listObjects(listObjects);
            return res.contents();

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
        }
        return Collections.emptyList();
    }

}
