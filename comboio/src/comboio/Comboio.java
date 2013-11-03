/**
 *
 *
 */
package comboio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Este programa implementa todas as funcionalidades de uma máquina de venda de
 * bilhetes de comboio.
 *
 * @author Davide Amaral Barruncho 21104379 LEI D1
 * @author André Filipe Silva Grãos 21105163 LEI D1
 * @author Yasmine de Costa Chede 21104555 LEI D1
 */
public class Comboio {

    public static Scanner leitor = new Scanner(System.in, "iso-8859-1"); // Variável Scanner
    public static Scanner bilhete = new Scanner(System.in, "iso-8859-1"); // Variável Scanner
    public static int i, origem, destino, numero_serie = 123456, coluna, linha; // Variáveis do tipo int
    public static double troco, preco_total, introduzido, resto, total; // Variáveis do tipo double
    public static ArrayList<Integer> stock_inicial = new ArrayList<Integer>(11); // Array do tipo int do stock incial, com ordem decrescente
    public static ArrayList<Double> moedasnotas = new ArrayList<Double>(11); // Array do tipo double com todas as notas e moedas aceites, com ordem decrescente
    public static double[][] preco = new double[12][12]; // Matriz com os preços das viagens
    public static ArrayList<String> cidades = new ArrayList<String>();

    /**
     * Função Raiz.
     */
    public static void main(String[] args) { // Declaração do main

        LerFicheiro_Cidades();
        LerFicheiro_stock_inicial();
        LerFicheiro_moedasnotas();
        LerFicheiro_precos();

        for (;;) {  //ciclo geral

            ciclo_origem();

            ciclo_destino();

            total = 0; // Total dinheiro introduzido pelo o utilizador

            resto = 0;  //O valor da diferença, pelo dinheiro introduzido pelo o utilizador e pelo o preço da viagem

            preco_total = preco[origem - 1][destino - 1] * 100;

            introduzido = 0;

            int[] dinheiro = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; // Ordenado de menor para maior (1,2,5,10,20,50 cent, 1 2 5 10 20 euros)

            while (total < preco_total && introduzido != -1) { // Enquanto o dinheiro não é suficiente para pagar, o ciclo funciona sempre

                introduzido = 0;

                System.out.println("A viagem de " + cidades.get(origem) + " para " + cidades.get(destino) + " tem um custo de " + preco_total / 100 + "€");

                resto = preco_total - total;

                System.out.println("Quantia introduzida " + total / 100 + "€. Faltam " + resto / 100 + "€");

                System.out.print(">");

                receber_dinheiro();

                if (introduzido == -1) {
                    System.out.println("Compra de viagem cancelada. São devolvidos: " + total / 100 + "€"); // Se o valor introduzido for inferior a zero, a viagem é cancelada.

                    for (i = 0; i < stock_inicial.size(); i++) {
                        //stock_inicial[i] = stock_inicial[i] - dinheiro[i];
                        stock_inicial.set(i, stock_inicial.get(i) - dinheiro[i]);
                    }

                }

                for (i = 0; i < moedasnotas.size(); i++) { // ciclo para o array moedasnotas

                    if (introduzido == moedasnotas.get(i)) {

                        dinheiro[i] = dinheiro[i] + 1;
                        //stock_inicial[i]++;
                        stock_inicial.set(i, stock_inicial.get(i) +1);                        
                        total = total + moedasnotas.get(i) * 100;
                    }
                }
                if (introduzido != 20 && introduzido != 10 && introduzido != 5 && introduzido != 2 && introduzido != 1 && introduzido != 0.5 && introduzido != 0.2 && introduzido != 0.1 && introduzido != 0.05 && introduzido != 0.02 && introduzido != 0.01 && introduzido != -1) { //condição para caso o utilizador por notas ou moedas nao reconhecidas pela a maquina
                    System.out.println("Esta máquina só aceita notas de 20€, 10€ ou 5€ e moedas de 2€, 1€, 0,5€, 0,20€, 0,10€, 0,05€, 0,02€ ou 0,01€");
                }
            }
            if (total >= preco_total) {
                System.out.println("A viagem de " + cidades.get(origem) + " para " + cidades.get(destino) + " tem um custo de " + preco_total / 100 + "€");

                resto = 0;

                System.out.println("Quantia introduzida " + total / 100 + "€. Faltam " + resto / 100 + "€");
            }

            int[] stock_troco = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

            if (total > preco_total) {
                troco = total - preco_total;
                System.out.println("Troco de " + troco / 100 + "€");
                i = 0;

                while (i < moedasnotas.size()) {

                    if (moedasnotas.get(i) * 100 <= troco && stock_inicial.get(i) > 0/*
                             * troco < 0 || stock[i] < 0
                             */) {

                        troco = troco - moedasnotas.get(i) * 100;
                        stock_troco[i]++;
                        //stock_inicial[i]--;
                        stock_inicial.set(i, stock_inicial.get(i) -1);

                    } else {
                        i++;
                    }
                }
                if (troco == 0) {

                    for (i = 0; i < moedasnotas.size(); i++) {
                        while (stock_troco[i] > 0) {
                            System.out.println(moedasnotas.get(i) + "€");
                            stock_troco[i]--;
                        }
                    }
                }
                if (troco != 0) {
                    System.out.println("Lamentamos informar que não temos moedas suficientes para lhe dar troco. A venda do bilhete será cancelada. Por favor introduza quantia certa ou dirija-se a outra máquina.");
                    System.out.println("Prima qualquer tecla para continuar...");

                    for (i = 0; i < stock_inicial.size(); i++) {
                        //stock_inicial[i] = stock_inicial[i] + stock_troco[i] - dinheiro[i];
                        stock_inicial.set(i, stock_inicial.get(i) + stock_troco[i] - dinheiro[i]);
                    }
                    String tecla = leitor.nextLine();
                } else {
                    imprimirbilhete();
                }
            } else {
                imprimirbilhete();
            }
        }
    }
    public static String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";

    /**
     * Ciclo do para a origem da viagem.
     */
    public static void ciclo_origem() {
        do {
            origem = -1;

            System.out.println("Qual é a origem da viagem?");

            for (i = 1; i < cidades.size(); i++) {
                System.out.println(i + " - " + cidades.get(i));
            }

            System.out.print(">");
            if (leitor.hasNextInt()) {
                if (leitor.nextInt() == 0000) {
                    modo_depuracao();
                } else {
                    origem = leitor.nextInt(); // Lê o numero dado pelo o utilizador
                }
            } else {
                easter_egg();
            }

            desligar_maquina();


            if (origem < 1 || origem > 12) {
                System.out.println("\nOrigem Inválido! Introduza nova Origem!\n");
            }


        } while (origem < 1 || origem > 12);

    }

    /**
     * Ciclo do para o destino da viagem.
     */
    public static void ciclo_destino() { 
        /*
         * ciclo do para o destino da viagem
         */
        do {
            destino = -1;
            System.out.println("Qual é o destino?");
            for (i = 1; i < origem; i++) { // Lista de destinos sem a origem escolhida
                System.out.println(i + " - " + cidades.get(i));
            }
            for (i = origem + 1; i < cidades.size(); i++) {
                System.out.println(i + " - " + cidades.get(i));
            }

            System.out.print(">");
            if (leitor.hasNextInt()) {
                destino = leitor.nextInt();  // Le o numero dado pelo o utilizador
            } else {
                leitor.next();
            }
            if (destino < 1 || destino > 12) {
                System.out.println("\nDestino Inválido! Insira novo Destino!\n");
            } else if (destino == origem) {
                System.out.println("\nDestino igual à origem!\n");
            }
        } while (destino < 1 || destino > 12 || destino == origem);
    }

    /**
     * Easter Egg.
     */
    public static void easter_egg() {
        if ("reinobuedalonge".equals(leitor.next())) {
            ciclo_origem();
            ciclo_destino();
            imprimirbilhete();
            origem = -1;
        }

    }

    /**
     * Modo Depuracao.
     */
    public static void modo_depuracao() {
        for (i = 0; i < stock_inicial.size(); i++) {
            System.out.println("Stock = " + stock_inicial.get(i));
        }
    }

    /**
     * Desligar a Máquina.
     */
    public static void desligar_maquina() {
        if (origem == 9999) { // Desligar a máquina!

            System.out.println("Máquina Desligada!");
            Escrever_stock_inicial();
            System.exit(0);
        }
    }

    public static String now() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
        return sdf.format(cal.getTime());

    }

    /**
     * Recebe o dinheiro do utilizador.
     */
    public static void receber_dinheiro() {
        introduzido = 0;
        if (leitor.hasNextDouble()) {
            introduzido = leitor.nextDouble(); // Lê o valor introduzido pelo o utilizador
        } else {//erro
            System.out.println("Quantia Introduzida Inválida!");
            leitor.next();
        }
    }

    /**
     * Imprimir o bilhete.
     */
    public static void imprimirbilhete() {
        // Print Bilhete
        System.out.println("+----------------------------------------------------------------------+");
        System.out.println("|                                                                      |");
        System.out.println("| B I L H E T E                                                        |");
        System.out.println("|                                                                      |");

        System.out.printf("| %-68s |\n", "  válido de " + cidades.get(origem).toUpperCase() + " para " + cidades.get(destino).toUpperCase());

        System.out.println("|                                                                      |");

        String data = now();
        System.out.println("| emitido em " + data + "                                       |");

        System.out.println("|                                                                      |");

        //num_serie++;
        System.out.printf("| %-68s |\n", "Bilhete numero " + numero_serie);
        numero_serie++; //incrementação num_serie para dar + 1 no bilhete numero

        System.out.println("|                                                                      |");

        System.out.printf("| %-68s |\n", "Preço: " + preco_total / 100 + "€ (impostos incluídos à  taxa legal em vigor)");

        System.out.println("|                                                                      |");

        System.out.println("+----------------------------------------------------------------------+");

        System.out.println("Prima qualquer tecla para continuar...");
        //Scanner bilhete = new Scanner(System.in, "iso-8859-1");
        String texto = bilhete.nextLine();
        //System.out.println(texto);

    }

    /**
     * Ler o ficheiro de todas as estações.
     */
    public static void LerFicheiro_Cidades() {

        Scanner teclado;
        File fich = new File("cidades.txt");

        try {
            teclado = new Scanner(fich);
            while (teclado.hasNextLine()) {
                String nome = teclado.nextLine();
                cidades.add(nome);
            }
            teclado.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro nao existe");

        }
    }

    /**
     * Ler o ficheiro do stock inicial.
     */
    public static void LerFicheiro_stock_inicial() {
        
        Scanner teclado;
        File fich = new File("stock.txt");

        try {
            teclado = new Scanner(fich);
            while (teclado.hasNextLine()) {
                Integer num = teclado.nextInt();
                stock_inicial.add(num);
            }
            teclado.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro nao existe");

        }
    }

    /**
     * Ler o ficheiro com todas as moedas e notas disponiveis.
     */
    public static void LerFicheiro_moedasnotas() {

        Scanner teclado;
        File fich = new File("moedasnotas.txt");

        try {
            teclado = new Scanner(fich);
            while (teclado.hasNextLine()) {
                Double numero = teclado.nextDouble();
                moedasnotas.add(numero);
            }
            teclado.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro nao existe");

        }
    }

    /**
     * Ler o ficheiro com a tabela dos preços.
     */
    public static void LerFicheiro_precos() {
        File fich = new File("preco.txt");

        try {
            Scanner teclado = new Scanner(fich);
            for (linha = 0; linha < 12; linha++) {
                for (coluna = 0; coluna < 12; coluna++) {
                    if (teclado.hasNextDouble()) {
                        preco[linha][coluna] = teclado.nextDouble();
                    }
                }
            }
            teclado.close();

        } catch (FileNotFoundException ex) {
            System.out.println("Ficheiro nao existe");
        }
    }

    /**
     * Escrever o ficheiro com o stock disponivel.
     */
    public static void Escrever_stock_inicial() {
        try {

            FileWriter fstream = new FileWriter("stock.txt");
            BufferedWriter out = new BufferedWriter(fstream);
            for (coluna = 0; coluna < stock_inicial.size(); coluna++) {
                out.write("" + stock_inicial.get(coluna) + '\r' + '\n');
            }

            out.close();

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        }
    }
}