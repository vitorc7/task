package com.example;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class TASK4 {

    public static void main(String[] args) {

        String endpoint = "https://3ospphrepc.execute-api.us-west-2.amazonaws.com/prod/RDSLambda";

        try {

            // Criar uma URL para o endpoint
            URL url = new URL(endpoint);

            // Abrir uma conexão HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configurar o método da requisição para GET
            connection.setRequestMethod("GET");

            // Obter o código de resposta
            int responseCode = connection.getResponseCode();

            // Verificar se a resposta é bem sucedida (código 200 OK)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Ler a resposta da API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                // Ler linha por linha e adicionar ao StringBuffer
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                reader.close();

                // Fechar a conexão
                connection.disconnect();

                // Processar a resposta para extrair a propriedade "gender"
                List<String> genderList = extractGender(response.toString());

                // Contar os gêneros
                int masculino = 0;
                int feminino = 0;
                for (String gender : genderList) {
                    if (gender.equals("M")) {
                        masculino++;
                    } else if (gender.equals("F")) {
                        feminino++;
                    }
                }

                System.out.println("feminino = " + feminino);
                System.out.println("masculino = " + masculino);

                // Salvar a contagem no Amazon S3
                saveCountToS3(masculino, feminino);
            } else {
                System.out.println("Falha na solicitação GET com código de resposta: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para extrair a propriedade "gender" da resposta usando regex
    private static List<String> extractGender(String response) {

        List<String> genderList = new ArrayList<>();
        Pattern pattern = Pattern.compile("\"gender\"\\s*:\\s*\"(M|F)\"");
        Matcher matcher = pattern.matcher(response);
        while (matcher.find()) {
            genderList.add(matcher.group(1));
        }
        return genderList;
    }

    // Método para salvar a contagem no Amazon S3
    private static void saveCountToS3(int masculino, int feminino) throws IOException {

        Properties props = new Properties();
        // Obtém o diretório de trabalho atual
        String currentDirectory = System.getProperty("user.dir");

        // Constrói o caminho relativo ao diretório de trabalho atual
        String relativePath = currentDirectory + "\\myinterview\\src\\resources\\config.properties";

        // Cria um FileInputStream com o caminho relativo
        FileInputStream file = new FileInputStream(relativePath);

        props.load(file);
        file.close();

        // Obter os valores das chaves
        String amazonS3Key = props.getProperty("amazonS3Key");
        String encryptedSecretKey = props.getProperty("secretKey");

        // Credenciais de acesso à AWS
        String accessKey = amazonS3Key;
        String secretKey = encryptedSecretKey;

        // Nome do bucket S3
        String bucketName = "interview-digiage";

        // Nome do arquivo no bucket
        String fileName = "interview-digiage.txt";

        // Configurar as credenciais
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider credentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

        // Criar um cliente Amazon S3
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(credentialsProvider)
                .withRegion(Regions.US_WEST_2) // Definir a região explicitamente
                .build();

        // Conteúdo a ser enviado para o Amazon S3
        String content = "Feminino: " + feminino + " Masculino: " + masculino;

        // Converter o conteúdo para um fluxo de entrada
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());

        // Criar metadados para o objeto
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(content.length());

        // Enviar o arquivo para o Amazon S3
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, inputStream, metadata));
        System.out.println("Contagem de gêneros salva com sucesso no Amazon S3 " + "\nBucket Name: " + bucketName);
    }
}
