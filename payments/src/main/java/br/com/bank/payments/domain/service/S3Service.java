package br.com.bank.payments.domain.service;

import br.com.bank.payments.api.dto.events.TransacaoS3DTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;

    public S3Service(
            @Value("${aws.access-key-id}") String accessKeyId,
            @Value("${aws.secret-key}") String secretKey,
            @Value("${aws.region}") String region
    ) {
        this.s3Client = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretKey)
                ))
                .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public void salvarTransacaoNoS3(String chave, TransacaoS3DTO novaTransacao) {
        chave += ".json"; // forcando para ser json

        try {
            List<TransacaoS3DTO> transacoes = recuperarListaTransacoesDoS3(chave);

            if (transacoes == null) {
                transacoes = new ArrayList<>();
            }
            transacoes.add(novaTransacao);

            String jsonString = objectMapper.writeValueAsString(transacoes);

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket("ms-payments-bucket")
                    .key(chave)
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromString(jsonString));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private List<TransacaoS3DTO> recuperarListaTransacoesDoS3(String chave) {
        try {
            HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
                    .bucket("ms-payments-bucket")
                    .key(chave)
                    .build();

            s3Client.headObject(headObjectRequest); // lanca excecao se nao existir

            // se existir recupera
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket("ms-payments-bucket")
                    .key(chave)
                    .build();

            String jsonString = s3Client.getObjectAsBytes(getObjectRequest).asUtf8String();

            // converte json para lista
            return objectMapper.readValue(jsonString, new TypeReference<List<TransacaoS3DTO>>() {});

        } catch (NoSuchKeyException e) {
            // arquivo nao existe
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Erro!");
            return null;
        }
    }
}

