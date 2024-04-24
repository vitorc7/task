package com.example;

import java.util.*;

public class TASK3 {

  public static void main(String[] args) {
    // Criando a lista
    List<String> lista = new ArrayList<>();

    // Adicionando strings aleatórias à lista
    lista.add("Maçã");
    lista.add("Banana");
    lista.add("Laranja");
    lista.add("Maçã");
    lista.add("Abacaxi");
    lista.add("Pêra");
    lista.add("Melão");
    lista.add("Banana");

    // Exibindo a lista
    System.out.println("\nLista de strings:");
    // Utilizando Iterator para percorrer a lista
    Iterator<String> iterator = lista.iterator();
    while (iterator.hasNext()) {
      String str = iterator.next();
      System.out.println(str);
    }

    // Criando um conjunto para armazenar os itens únicos
    Set<String> itensUnicos = new HashSet<>(lista);

    // Exibindo o número de itens distintos
    System.out.println("\nNúmero de itens distintos na lista: " + itensUnicos.size());
  }
}
