package com.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

                // Exibir a resposta recebida
                System.out.println("Response Body:");
                System.out.println(response.toString());

                // Processar a resposta para extrair a propriedade "gender"
                List<String> genderList = extractGender(response.toString());

                // Exibir a lista de gêneros
                int masculino = 0;
                int feminino = 0;
                //System.out.println("Gender List:");
                for (String gender : genderList) {
                    if(gender.equals("M")) {                     
                        masculino = masculino+1;
                    } 
                    else if(gender.equals("F")) {                     
                        feminino = feminino+1;
                    } 
                }

                System.out.println("feminino = " + feminino);
                System.out.println("masculino = " + masculino);


            } else {
                System.out.println("GET request failed with response code: " + responseCode);
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
    
}
