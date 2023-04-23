
package com.seeds.common.web.oss.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.seeds.common.web.oss.FileProperties;
import com.seeds.common.web.oss.FileTemplate;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * aws-s3 通用存储操作 支持所有兼容s3协议的云存储: {阿里云OSS，腾讯云COS，七牛云，京东云，minio 等}
 *
 * @author yk
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
@Primary
@Slf4j
public class OssTemplate implements InitializingBean, FileTemplate {

	private final FileProperties properties;

	private AmazonS3 amazonS3;
	private AmazonS3 gameoss1;
	private AmazonS3 gameoss2;
	private AmazonS3 gameoss3;
	private AmazonS3 medataOSS;

	/**
	 * 创建bucket
	 *
	 * @param bucketName bucket名称
	 */
	@Override
	@SneakyThrows
	public void createBucket(String bucketName) {
		if (!amazonS3.doesBucketExistV2(bucketName)) {
			amazonS3.createBucket((bucketName));
		}
	}

	/**
	 * 获取全部bucket
	 * <p>
	 *
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
	 * API Documentation</a>
	 */
	@Override
	@SneakyThrows
	public List<Bucket> getAllBuckets() {
		return amazonS3.listBuckets();
	}

	/**
	 * @param bucketName bucket名称
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListBuckets">AWS
	 * API Documentation</a>
	 */
	@SneakyThrows
	public Optional<Bucket> getBucket(String bucketName) {
		return amazonS3.listBuckets().stream().filter(b -> b.getName().equals(bucketName)).findFirst();
	}

	/**
	 * @param bucketName bucket名称
	 * @see <a href=
	 * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteBucket">AWS API
	 * Documentation</a>
	 */
	@Override
	@SneakyThrows
	public void removeBucket(String bucketName) {
		amazonS3.deleteBucket(bucketName);
	}

	/**
	 * 根据文件前置查询文件
	 * @param bucketName bucket名称
	 * @param prefix 前缀
	 * @param recursive 是否递归查询
	 * @return S3ObjectSummary 列表
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/ListObjects">AWS
	 * API Documentation</a>
	 */
	@Override
	@SneakyThrows
	public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
		ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
		return new ArrayList<>(objectListing.getObjectSummaries());
	}

	/**
	 * 获取文件外链
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param expires 过期时间 <=7
	 * @return url
	 * @see AmazonS3#generatePresignedUrl(String bucketName, String key, Date expiration)
	 */
	@SneakyThrows
	public String getObjectURL(String bucketName, String objectName, Integer expires) {
		Date date = new Date();
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, expires);
		URL url = amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime());
		return url.toString();
	}

	/**
	 * 获取文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @return 二进制流
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
	 * API Documentation</a>
	 */
	@Override
	@SneakyThrows
	public S3Object getObject(String bucketName, String objectName) {
		return amazonS3.getObject(bucketName, objectName);
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @throws Exception
	 */
	@Override
	public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
		putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param contextType 文件类型
	 * @throws Exception
	 */
	@Override
	public void putObject(String bucketName, String objectName, InputStream stream, String contextType)
			throws Exception {
		putObject(bucketName, objectName, stream, stream.available(), contextType);
	}

	@Override
	public void putMetadataObject(String bucketName, String objectName, InputStream stream,
								  String contextType) throws Exception {
		putMetadataObject(bucketName, objectName, stream, stream.available(), contextType);
	}

	/**
	 * 上传文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @param stream 文件流
	 * @param size 大小
	 * @param contextType 类型
	 * @throws Exception
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/PutObject">AWS
	 * API Documentation</a>
	 */
	public PutObjectResult putObject(String bucketName, String objectName, InputStream stream, long size,
									 String contextType) throws Exception {
		// String fileName = getFileName(objectName);
		byte[] bytes = IOUtils.toByteArray(stream);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(size);
		objectMetadata.setContentType(contextType);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		// 上传
		return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
	}

	public PutObjectResult putMetadataObject(String bucketName, String objectName, InputStream stream, long size,
											 String contextType) throws Exception {
		// String fileName = getFileName(objectName);
		byte[] bytes = IOUtils.toByteArray(stream);
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(size);
		objectMetadata.setContentType(contextType);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		// 上传
		return medataOSS.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);

	}

	/**
	 * 获取文件信息
	 *
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @see <a href="http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/GetObject">AWS
	 * API Documentation</a>
	 */
	public S3Object getObjectInfo(String bucketName, String objectName) throws Exception {
		@Cleanup
		S3Object object = amazonS3.getObject(bucketName, objectName);
		return object;
	}

	/**
	 * 删除文件
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 * @throws Exception
	 * @see <a href=
	 * "http://docs.aws.amazon.com/goto/WebAPI/s3-2006-03-01/DeleteObject">AWS API
	 * Documentation</a>
	 */
	@Override
	public void removeObject(String bucketName, String objectName) throws Exception {
		amazonS3.deleteObject(bucketName, objectName);
	}

	@Override
	public void removeObject(String objectName) throws Exception {
		gameoss1.deleteObject(properties.getGame().getOss1().getBucketName(), objectName);
		gameoss2.deleteObject(properties.getGame().getOss2().getBucketName(), objectName);
		gameoss3.deleteObject(properties.getGame().getOss3().getBucketName(), objectName);

	}

	@Override
	public void afterPropertiesSet() {
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setMaxConnections(properties.getOss().getMaxConnections());

		AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
				properties.getOss().getEndpoint(), properties.getOss().getRegion());

		AwsClientBuilder.EndpointConfiguration metadataEndpoint = new AwsClientBuilder.EndpointConfiguration(
				properties.getMetadata().getEndpoint(), properties.getMetadata().getRegion());

		AWSCredentials awsCredentials = new BasicAWSCredentials(properties.getOss().getAccessKey(),
				properties.getOss().getSecretKey());
		AWSCredentials gameCredentails = new BasicAWSCredentials(properties.getGame().getAccessKey(),
				properties.getGame().getSecretKey());
		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
		AWSCredentialsProvider awsGameCredentialsProvider = new AWSStaticCredentialsProvider(gameCredentails);

		this.amazonS3 = AmazonS3Client.builder().withEndpointConfiguration(endpointConfiguration)
				.withClientConfiguration(clientConfiguration).withCredentials(awsCredentialsProvider)
				.disableChunkedEncoding().withPathStyleAccessEnabled(properties.getOss().getPathStyleAccess()).build();

		this.medataOSS = AmazonS3Client.builder().withEndpointConfiguration(metadataEndpoint)
				.withClientConfiguration(clientConfiguration).withCredentials(awsCredentialsProvider)
				.disableChunkedEncoding().withPathStyleAccessEnabled(properties.getOss().getPathStyleAccess()).build();


		this.gameoss1 = AmazonS3Client.builder().withClientConfiguration(clientConfiguration)
				.withCredentials(awsGameCredentialsProvider).withRegion(properties.getGame().getOss1().getRegion())
				.enablePathStyleAccess()
				.build();
		this.gameoss2 = AmazonS3Client.builder().withClientConfiguration(clientConfiguration)
				.withCredentials(awsGameCredentialsProvider).withRegion(properties.getGame().getOss2().getRegion())
				.enablePathStyleAccess()
				.build();
		this.gameoss3 = AmazonS3Client.builder().withClientConfiguration(clientConfiguration)
				.withCredentials(awsGameCredentialsProvider).withRegion(properties.getGame().getOss3().getRegion())
				.enablePathStyleAccess()
				.build();
	}

	/**
	 * 分段上传文件至S3
	 */
	@Override
	@Async
	public void uploadMultipartFileByPart(InputStream in, String contentType, String originalFilename, String bucketName, String objectName, long size) {

		//声明线程池
		ExecutorService exec = Executors.newFixedThreadPool(8);
		File toFile = multipartFileToFile(in, originalFilename);
		int minPartSize = 25 * 1024 * 1024;
		// 得到总共的段数，和 分段后，每个段的开始上传的字节位置
		List<Long> positions = Collections.synchronizedList(new ArrayList<>());
		long filePosition = 0;
		while (filePosition < size) {
			positions.add(filePosition);
			filePosition += Math.min(minPartSize, (size - filePosition));
		}
		log.info("总大小：{}，分为{}段", size, positions.size());
		// 创建一个列表保存所有分传的 PartETag, 在分段完成后会用到
		List<PartETag> partETags = Collections.synchronizedList(new ArrayList<>());
		//第一步，初始化，声明下面将有一个 Multipart Upload
		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(contentType);
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, objectName, objectMetadata);
		//设置公共读取权限
		initRequest.withCannedACL(CannedAccessControlList.PublicRead);
		InitiateMultipartUploadResult initResponse = gameoss1.initiateMultipartUpload(initRequest);
		log.info("开始上传");
		long begin = System.currentTimeMillis();
		try {
			for (int i = 0; i < positions.size(); i++) {
				int finalI = i;
				exec.execute(() -> {
					long time1 = System.currentTimeMillis();
					UploadPartRequest uploadRequest = new UploadPartRequest()
							.withBucketName(bucketName)
							.withKey(objectName)
							.withUploadId(initResponse.getUploadId())
							.withPartNumber(finalI + 1)
							.withFileOffset(positions.get(finalI))
							.withFile(toFile)
							.withPartSize(Math.min(minPartSize, (size - positions.get(finalI))));
					// 第二步，上传分段，并把当前段的 PartETag 放到列表中
					partETags.add(gameoss1.uploadPart(uploadRequest).getPartETag());
					long time2 = System.currentTimeMillis();
					log.info("第{}段上传耗时：{}", finalI + 1, (time2 - time1) + "ms");
				});
			}
			//任务结束关闭线程池
			exec.shutdown();
			//判断线程池是否结束，不加会直接结束方法
			while (true) {
				if (exec.isTerminated()) {
					break;
				}
			}

			// 第三步，完成上传，合并分段
			CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, objectName,
					initResponse.getUploadId(), partETags);
			gameoss1.completeMultipartUpload(compRequest);
			//删除本地缓存文件
			toFile.delete();
		} catch (Exception e) {
			gameoss1.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, objectName, initResponse.getUploadId()));
			log.error("Failed to upload {}, " + e.getMessage());
		}
		long end = System.currentTimeMillis();
		log.info("总上传耗时：{}", (end - begin) + "ms");
	}

	@Override
	public List<S3ObjectSummary> getAllObjects() {


		ObjectListing japanList = gameoss1.listObjects(properties.getGame().getOss1().getBucketName());
		//ObjectListing euList = gameoss2.listObjects(properties.getGame().getOss2().getBucketName());
		//ObjectListing usList = gameoss3.listObjects(properties.getGame().getOss3().getBucketName());
		return new ArrayList<>(japanList.getObjectSummaries());
	}

	@Override
	public String getPresignedUrl(String fileName, String bucketName) {
		// token设置1小时后过期
		DateTime expiration = DateUtil.offsetHour(new Date(), 1);
		URL url = gameoss1.generatePresignedUrl(new GeneratePresignedUrlRequest(bucketName, fileName).withExpiration(expiration).withMethod(HttpMethod.PUT));
		return url.toString();
	}

	@Override
	public String getPresignedUrl(String fileName, String bucketName, String uploadId, Integer partNumber) {
		DateTime expiration = DateUtil.offsetHour(new Date(), 1);

		HashMap<String, Object> params = new HashMap<>();
		params.put("partNumber", partNumber);
		params.put("uploadId", uploadId);
		GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName)
				.withExpiration(expiration)
				.withMethod(HttpMethod.PUT);
		generatePresignedUrlRequest.addRequestParameter("partNumber", String.valueOf(partNumber));
		generatePresignedUrlRequest.addRequestParameter("uploadId", uploadId);
		URL url = gameoss1.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}

	@Override
	public InitiateMultipartUploadResult initiateMultipartUpload(String bucketName, String objectName, String contentType) {

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentType(contentType);
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, objectName, objectMetadata);
		InitiateMultipartUploadResult initResponse = gameoss1.initiateMultipartUpload(initRequest);
		return initResponse;
	}

	@Override
	public void completeMultipartUpload(String gameBucketName, String key, String uploadId) {

		// 获取已完成的 List<PartETag>
		ListPartsRequest listPartsRequest = new ListPartsRequest(gameBucketName, key, uploadId);
		PartListing partListing = gameoss1.listParts(listPartsRequest);
		List<PartETag> partETags = partListing.getParts().stream().map(p -> {
			PartETag partETag = new PartETag(p.getPartNumber(), p.getETag());
			return partETag;
		}).collect(Collectors.toList());


		// 完成上传，合并分段
		CompleteMultipartUploadRequest requset = new CompleteMultipartUploadRequest();
		requset.setBucketName(gameBucketName);
		requset.setKey(key);
		requset.setUploadId(uploadId);
		requset.setPartETags(partETags);
		gameoss1.completeMultipartUpload(requset);
	}

	@Override
	public void abortUpload(String gameBucketName, String key, String uploadId) {
		AbortMultipartUploadRequest request = new AbortMultipartUploadRequest(gameBucketName, key, uploadId);
		gameoss1.abortMultipartUpload(request);
	}


	@Override
	@SneakyThrows
	public List<S3ObjectSummary> getAllObjectsByPrefix(String bucketName, String prefix) {

		ListObjectsV2Request request = new ListObjectsV2Request().withBucketName(bucketName).withPrefix(prefix);
		ListObjectsV2Result listObjectsV2Result = gameoss1.listObjectsV2(request);
		return new ArrayList<>(listObjectsV2Result.getObjectSummaries());
	}

	/**
	 * MultipartFile 转 File
	 */
	public static File multipartFileToFile(InputStream in, String name) {
		File toFile = null;
		if (in != null) {
			try {
				String substring = name.substring(name.lastIndexOf("/") + 1);
				toFile = new File(substring);
				//获取流文件
				OutputStream os = new FileOutputStream(toFile);
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
					os.write(buffer, 0, bytesRead);
				}
				os.close();
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return toFile;
	}
}
