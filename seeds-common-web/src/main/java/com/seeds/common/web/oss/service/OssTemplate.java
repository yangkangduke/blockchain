
package com.seeds.common.web.oss.service;

import com.amazonaws.ClientConfiguration;
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
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	/**
	 * 创建bucket
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

	/**
	 * 获取文件信息
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
	public void afterPropertiesSet() {
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setMaxConnections(properties.getOss().getMaxConnections());

		AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
				properties.getOss().getEndpoint(), properties.getOss().getRegion());
		AWSCredentials awsCredentials = new BasicAWSCredentials(properties.getOss().getAccessKey(),
				properties.getOss().getSecretKey());
		AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
		this.amazonS3 = AmazonS3Client.builder().withEndpointConfiguration(endpointConfiguration)
				.withClientConfiguration(clientConfiguration).withCredentials(awsCredentialsProvider)
				.disableChunkedEncoding().withPathStyleAccessEnabled(properties.getOss().getPathStyleAccess()).build();
	}

	/**
	 * 分段上传文件至S3
	 *
	 * @param file
	 * @param bucketName bucket名称
	 * @param objectName 文件名称
	 */
	@Override
	public void uploadMultipartFileByPart(MultipartFile file, String bucketName, String objectName) {

		amazonS3.createBucket(bucketName);
		// 设置公共读权限
		amazonS3.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
		//声明线程池
		ExecutorService exec = Executors.newFixedThreadPool(8);
		long size = file.getSize();
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
		// 第一步，初始化，声明下面将有一个 Multipart Upload
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, objectName);
		InitiateMultipartUploadResult initResponse = amazonS3.initiateMultipartUpload(initRequest);
		log.info("开始上传");
		long begin = System.currentTimeMillis();
		try {
			// MultipartFile 转 File
			File toFile = multipartFileToFile(file);
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
					partETags.add(amazonS3.uploadPart(uploadRequest).getPartETag());
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
			amazonS3.completeMultipartUpload(compRequest);
			//删除本地缓存文件
			toFile.delete();
		} catch (Exception e) {
			amazonS3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, objectName, initResponse.getUploadId()));
			log.error("Failed to upload, " + e.getMessage());
		}
		long end = System.currentTimeMillis();
		log.info("总上传耗时：{}", (end - begin) + "ms");
	}

	/**
	 * MultipartFile 转 File
	 */
	public static File multipartFileToFile(MultipartFile file) throws Exception {
		File toFile = null;
		if (file.equals("") || file.getSize() <= 0) {
			file = null;
		} else {
			InputStream ins = null;
			ins = file.getInputStream();
			toFile = new File(file.getOriginalFilename());
			//获取流文件
			OutputStream os = new FileOutputStream(toFile);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		}
		return toFile;
	}
}
