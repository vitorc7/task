package com.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;

public class TASK5 {

  public static void main(String[] args) {
    String apiUrl = "https://fakestoreapi.com/products";

    try {
      String allProductsResponse = getAllProducts(apiUrl);
      System.out.println("Todos os produtos:\n" + allProductsResponse);

      // ID do produto desejado
      String productByIdResponse = getProductById(apiUrl, 15);
      System.out.println("Produto por ID 15:\n" + productByIdResponse);

      // Exemplo de criação de novo produto
      Product newProduct = new Product(
          "Iphone 12",
          3.134,
          "Novo modelo de smartphone Iphone 12, com 128gb 8gb de RAM e camera full hd",
          "https://www.amazon.com.br/Apple-iPhone-12-128-GB-Branco/dp/B09B7YYGSN/ref=asc_df_B09B7YYGSN/?hvadid=379773236150&hvpos=&hvnetw=g&hvrand=4112333893554923129&hvpone=&hvptwo=&hvqmt=&hvdev=c&hvdvcmdl=&hvlocint=&hvlocphy=9100925&hvtargid=pla-1474163872536&psc=1&mcid=a84fc302a1693d8bbef1acbd78c7d1d3",
          "Smartphone");

      // Enviar o novo produto para o servidor
      postProduct(apiUrl, newProduct);

      // Exemplo de atualização de um produto existente
      Product updatedProduct = new Product(
          "Produto de teste",
          13.5,
          "lorem ipsum set",
          "https://i.pravatar.cc",
          "eletrônico");

      // Atualizar o produto com ID 4
      updateProduct(apiUrl, 4, updatedProduct);

      deleteProduct(apiUrl, 4);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String getAllProducts(String apiUrl) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
    connection.setRequestMethod("GET");
    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        return response.toString();
      }
    } else {
      throw new IOException("Erro ao realizar a solicitação. Código de resposta: " + responseCode);
    }
  }

  private static String getProductById(String apiUrl, int id) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl + "/" + id).openConnection();
    connection.setRequestMethod("GET");
    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
          response.append(inputLine);
        }
        return response.toString();
      }
    } else {
      throw new IOException("Erro ao realizar a solicitação. Código de resposta: " + responseCode);
    }
  }

  private static void deleteProduct(String apiUrl, int id) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl + "/" + id).openConnection();
    connection.setRequestMethod("DELETE");

    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      System.out.println("Produto excluído com sucesso.");
    } else {
      throw new IOException("Erro ao excluir o produto. Código de resposta: " + responseCode);
    }
  }

  private static void postProduct(String apiUrl, Product product) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);

    Gson gson = new Gson();
    String jsonInputString = gson.toJson(product);

    try (OutputStream outputStream = connection.getOutputStream()) {
      byte[] input = jsonInputString.getBytes("utf-8");
      outputStream.write(input, 0, input.length);
    }

    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      System.out.println("Falha na solicitação GET com código de resposta:");
    } else {
      throw new IOException("Erro ao criar o produto. Código de resposta: " + responseCode);
    }
  }

  private static void updateProduct(String apiUrl, int id, Product product) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl + "/" + id).openConnection();
    connection.setRequestMethod("PUT");
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);

    Gson gson = new Gson();
    String jsonInputString = gson.toJson(product);

    try (OutputStream outputStream = connection.getOutputStream()) {
      byte[] input = jsonInputString.getBytes("utf-8");
      outputStream.write(input, 0, input.length);
    }

    int responseCode = connection.getResponseCode();
    if (responseCode == HttpURLConnection.HTTP_OK) {
      System.out.println("Produto atualizado com sucesso.");
    } else {
      throw new IOException("Erro ao atualizar o produto. Código de resposta: " + responseCode);
    }
  }
}

class Product {
  private String title;
  private double price;
  private String description;
  private String image;
  private String category;

  public Product(String title, double price, String description, String image, String category) {
    this.title = title;
    this.price = price;
    this.description = description;
    this.image = image;
    this.category = category;
  }

}
