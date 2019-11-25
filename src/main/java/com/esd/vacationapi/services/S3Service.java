package com.esd.vacationapi.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.esd.vacationapi.services.exceptions.FileException;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	// retorna endereço web que foi criado
	public URI uploadFile(MultipartFile multipartFile) {
		/* é um tipo multiparfile que o endpoint recebe */
		try {
			String fileName = multipartFile.getOriginalFilename();  // extrai o nome do arquivo enviado
			InputStream is = multipartFile.getInputStream();  // objeto de leitura, encapsula o processo de leitura a partir da origem, nesse caso o filename
			String contentType = multipartFile.getContentType();  // informação do tipo do arquivo enviado, pode ser texto, imagem...

			return uploadFile(is, fileName, contentType);
		
		} catch (IOException e) {
			throw new FileException("Erro de IO: " + e.getMessage());
		}
	}

	public URI uploadFile(InputStream is, String fileName, String contentType) {

		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("Iniciando upload");
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("Upload finalizado");

			return s3client.getUrl(bucketName, fileName).toURI();
			//ele gera um URL e precisa ser convertido para URI por isso o toURI
		} 
		catch (URISyntaxException e) {
			throw new FileException("Erro ao converter URL para URI");
		}
	}
}