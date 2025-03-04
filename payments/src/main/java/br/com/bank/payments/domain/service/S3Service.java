package br.com.bank.payments.domain.service;

import br.com.bank.payments.api.dto.events.TransacaoS3DTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3Service {

    private S3Client s3Client;
    private ObjectMapper objectMapper;

    public S3Service() {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public void salvarTransacaoNoS3(String chave, TransacaoS3DTO data) {
        try {
            String jsonString = objectMapper.writeValueAsString(data);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("ms-payments-bucket")
                    .key(chave)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromString(jsonString));

            System.out.println("Deu certo!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
