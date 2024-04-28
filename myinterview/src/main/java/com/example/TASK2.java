package com.example;

import java.util.ArrayList;
import java.util.List;

public class TASK2 {
  public static void main(String[] args) {
        // Criar uma lista
        List<Integer> list = new ArrayList<>();

        // Adicionar elementos à lista
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        // Imprimir a lista original
        System.out.println("Lista original:");
        System.out.println(list);

        // Remover o elemento do meio
        int middleIndex = list.size() / 2;
        list.remove(middleIndex);

        // Imprimir a lista após remover o elemento do meio
        System.out.println("Lista após remover o elemento do meio:");
        System.out.println(list);
    }
}

