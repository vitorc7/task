package com.example;
import java.util.Scanner;
public class TASK1 {

   
    public static boolean ehPalindromo(String str) {
        str = str.toLowerCase();

       
        int esquerda = 0;
        int direita = str.length() - 1;

        while (esquerda < direita) {
           
            while (esquerda < direita && !Character.isLetterOrDigit(str.charAt(esquerda))) {
                esquerda++;
            }
            
            while (esquerda < direita && !Character.isLetterOrDigit(str.charAt(direita))) {
                direita--;
            }
           
            if (str.charAt(esquerda) != str.charAt(direita)) {
                return false;
            }
            
            esquerda++;
            direita--;
        }

        return true;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite uma string para verificar se é um palíndromo:");
        String entrada = scanner.nextLine();
        scanner.close();

        if (ehPalindromo(entrada)) {
            System.out.println("'" + entrada + "' é um palíndromo.");
        } else {
            System.out.println("'" + entrada + "' não é um palíndromo.");
        }
    }
}
