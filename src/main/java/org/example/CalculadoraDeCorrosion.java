package org.example;
/*
@Version: 1.0.0
@Autores: Martinez Dominguez Nery Alexander, Daniel López Ruiz, Aldair Sánchez Salinas
@Date: 21/11/2026
Descripción: Calculadora y balanceador de celdas de corrosión galvánica con ecuación de Nernst
 */
import java.util.Scanner;
public class CalculadoraDeCorrosion {

    public static void main(String[] args) {

        Scanner lector = new Scanner(System.in);

        String rojoError = "\u001B[31m";
        String resetColor = "\u001B[0m";

        // Datos por metal alineados por el mismo índice
        String[] nombresMetales = {
                "Oro (Au)", "Platino (Pt)", "Plata (Ag)", "Mercurio (Hg)",
                "Cobre (Cu)", "Hidrógeno (H2)", "Plomo (Pb)", "Estaño (Sn)",
                "Níquel (Ni)", "Cobalto (Co)", "Cadmio (Cd)", "Hierro (Fe)",
                "Cromo (Cr)", "Zinc (Zn)", "Aluminio (Al)", "Magnesio (Mg)", "Sodio (Na)"
        };

        String[] especiesReducidas = {
                "Au", "Pt", "Ag", "Hg",
                "Cu", "H2", "Pb", "Sn",
                "Ni", "Co", "Cd", "Fe",
                "Cr", "Zn", "Al", "Mg", "Na"
        };

        int[] coeficientesReducidos = {
                1, 1, 1, 2,
                1, 1, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1, 1
        };

        String[] especiesOxidadas = {
                "Au3+", "Pt2+", "Ag+", "Hg2(2+)",
                "Cu2+", "H+", "Pb2+", "Sn2+",
                "Ni2+", "Co2+", "Cd2+", "Fe2+",
                "Cr3+", "Zn2+", "Al3+", "Mg2+", "Na+"
        };

        int[] coeficientesOxidados = {
                1, 1, 1, 1,
                1, 2, 1, 1,
                1, 1, 1, 1,
                1, 1, 1, 1, 1
        };

        // Electrones (n) por semirreacción
        int[] electronesTabla = {
                3, 2, 1, 2,
                2, 2, 2, 2,
                2, 2, 2, 2,
                3, 2, 3, 2, 1
        };

        // Potenciales estándar E° en V
        double[] potencialesEstandar = {
                1.498, 1.200, 0.799, 0.788,
                0.337, 0.000, -0.126, -0.136,
                -0.250, -0.277, -0.403, -0.440,
                -0.744, -0.763, -1.662, -2.363, -2.714
        };

        boolean continuar = true;

        do {
            System.out.println();
            System.out.println("Calculadora de corrosión galvánica");
            System.out.println("1. Calcular par galvánico y balancear ecuación");
            System.out.println("2. Ver tabla de potenciales estándar (E°)");
            System.out.println("3. Salir");

            int opcionMenu;

            // Validar opción del menú (1 a 3)
            while (true) {
                try {
                    System.out.println("Elige una opción (1-3): ");
                    opcionMenu = lector.nextInt();
                    lector.nextLine(); // Limpiar salto de línea pendiente
                    if (opcionMenu >= 1 && opcionMenu <= 3) {
                        break;
                    } else {
                        System.out.println(rojoError + "ERROR: INGRESE UNA OPCIÓN VÁLIDA ENTRE 1 Y 3" + resetColor);
                    }
                } catch (Exception e) {
                    System.out.println(rojoError + "ERROR: INGRESE VALORES NUMÉRICOS ENTEROS" + resetColor);
                    lector.nextLine(); // Limpiar entrada inválida
                }
            }

            switch (opcionMenu) {

                // Opción 1: calcular celda, Nernst y balanceo
                case 1:
                    System.out.println();
                    System.out.println("Lista de metales y electrodos disponibles:");
                    for (int i = 0; i < nombresMetales.length; i++) {
                        System.out.println((i + 1) + ". " + nombresMetales[i] + " | E° = " + potencialesEstandar[i] + " V | n = " + electronesTabla[i] + "e-");
                    }

                    // Primer electrodo: acepta número de lista, nombre o símbolo
                    int indiceMetal1 = -1;
                    while (true) {
                        System.out.println("Elige el primer metal por número (1-" + nombresMetales.length + ") o símbolo/nombre (ej. Zn, Ni): ");
                        String entrada1 = lector.nextLine().trim();

                        if (entrada1.isEmpty()) {
                            System.out.println(rojoError + "ERROR: LA ENTRADA NO PUEDE ESTAR VACÍA. INTENTA DE NUEVO." + resetColor);
                            continue;
                        }

                        int encontrado = -1;
                        try {
                            int num1 = Integer.parseInt(entrada1);
                            if (num1 >= 1 && num1 <= nombresMetales.length) {
                                encontrado = num1 - 1;
                            } else {
                                System.out.println(rojoError + "ERROR: EL NÚMERO DEBE ESTAR ENTRE 1 Y " + nombresMetales.length + resetColor);
                                continue;
                            }
                        } catch (NumberFormatException e) {
                            String textoBuscado = entrada1.toLowerCase();
                            for (int i = 0; i < nombresMetales.length; i++) {
                                String nombreMinuscula = nombresMetales[i].toLowerCase();
                                String simboloMinuscula = especiesReducidas[i].toLowerCase();
                                if (nombreMinuscula.contains(textoBuscado) || simboloMinuscula.equals(textoBuscado)) {
                                    encontrado = i;
                                    break;
                                }
                            }
                        }

                        if (encontrado != -1) {
                            indiceMetal1 = encontrado;
                            break;
                        } else {
                            System.out.println(rojoError + "ERROR: EL ELEMENTO '" + entrada1 + "' NO SE ENCUENTRA EN LA TABLA. INTENTA DE NUEVO." + resetColor);
                        }
                    }

                    // Segundo electrodo (debe ser distinto al primero)
                    int indiceMetal2 = -1;
                    while (true) {
                        System.out.println("Elige el segundo metal por número (1-" + nombresMetales.length + ") o símbolo/nombre (ej. Zn, Ni): ");
                        String entrada2 = lector.nextLine().trim();

                        if (entrada2.isEmpty()) {
                            System.out.println(rojoError + "ERROR: LA ENTRADA NO PUEDE ESTAR VACÍA. INTENTA DE NUEVO." + resetColor);
                            continue;
                        }

                        int encontrado = -1;
                        try {
                            int num2 = Integer.parseInt(entrada2);
                            if (num2 >= 1 && num2 <= nombresMetales.length) {
                                encontrado = num2 - 1;
                            } else {
                                System.out.println(rojoError + "ERROR: EL NÚMERO DEBE ESTAR ENTRE 1 Y " + nombresMetales.length + resetColor);
                                continue;
                            }
                        } catch (NumberFormatException e) {
                            String textoBuscado = entrada2.toLowerCase();
                            for (int i = 0; i < nombresMetales.length; i++) {
                                String nombreMinuscula = nombresMetales[i].toLowerCase();
                                String simboloMinuscula = especiesReducidas[i].toLowerCase();
                                if (nombreMinuscula.contains(textoBuscado) || simboloMinuscula.equals(textoBuscado)) {
                                    encontrado = i;
                                    break;
                                }
                            }
                        }

                        if (encontrado == -1) {
                            System.out.println(rojoError + "ERROR: EL ELEMENTO '" + entrada2 + "' NO SE ENCUENTRA EN LA TABLA. INTENTA DE NUEVO." + resetColor);
                        } else if (encontrado == indiceMetal1) {
                            System.out.println(rojoError + "ERROR: NO PUEDES ELEGIR EL MISMO METAL DOS VECES" + resetColor);
                        } else {
                            indiceMetal2 = encontrado;
                            break;
                        }
                    }

                    // Condiciones estándar (1 M) o concentraciones manuales
                    System.out.println();
                    System.out.println("Condiciones de concentración:");
                    System.out.println("1. Condiciones estándar (1 M para ambos)");
                    System.out.println("2. Condiciones no estándar (ingresar concentraciones)");

                    int tipoCondicion;
                    while (true) {
                        try {
                            System.out.println("Elige el tipo de condición (1-2): ");
                            tipoCondicion = lector.nextInt();
                            lector.nextLine();
                            if (tipoCondicion == 1 || tipoCondicion == 2) {
                                break;
                            } else {
                                System.out.println(rojoError + "ERROR: ELIGE 1 O 2" + resetColor);
                            }
                        } catch (Exception e) {
                            System.out.println(rojoError + "ERROR: INGRESE VALORES NUMÉRICOS ENTEROS" + resetColor);
                            lector.nextLine();
                        }
                    }

                    double concentracion1 = 1.0;
                    double concentracion2 = 1.0;

                    if (tipoCondicion == 2) {
                        while (true) {
                            try {
                                System.out.println("Ingresa la concentración molar (M) para " + nombresMetales[indiceMetal1] + " (mayor a 0): ");
                                concentracion1 = lector.nextDouble();
                                lector.nextLine();
                                if (concentracion1 > 0) {
                                    break;
                                } else {
                                    System.out.println(rojoError + "ERROR: LA CONCENTRACIÓN DEBE SER MAYOR QUE 0" + resetColor);
                                }
                            } catch (Exception e) {
                                System.out.println(rojoError + "ERROR: INGRESE UN VALOR NUMÉRICO VÁLIDO" + resetColor);
                                lector.nextLine();
                            }
                        }

                        while (true) {
                            try {
                                System.out.println("Ingresa la concentración molar (M) para " + nombresMetales[indiceMetal2] + " (mayor a 0): ");
                                concentracion2 = lector.nextDouble();
                                lector.nextLine();
                                if (concentracion2 > 0) {
                                    break;
                                } else {
                                    System.out.println(rojoError + "ERROR: LA CONCENTRACIÓN DEBE SER MAYOR QUE 0" + resetColor);
                                }
                            } catch (Exception e) {
                                System.out.println(rojoError + "ERROR: INGRESE UN VALOR NUMÉRICO VÁLIDO" + resetColor);
                                lector.nextLine();
                            }
                        }
                    }

                    // Nernst: E = E° + (0.0592 / n) * log10(C)
                    double potencialEstandar1 = potencialesEstandar[indiceMetal1];
                    double potencialEstandar2 = potencialesEstandar[indiceMetal2];

                    double potencialReal1 = potencialEstandar1 + (0.0592 / electronesTabla[indiceMetal1]) * Math.log10(concentracion1);
                    double potencialReal2 = potencialEstandar2 + (0.0592 / electronesTabla[indiceMetal2]) * Math.log10(concentracion2);

                    double potencialReal1Redondeado = Math.round(potencialReal1 * 10000.0) / 10000.0;
                    double potencialReal2Redondeado = Math.round(potencialReal2 * 10000.0) / 10000.0;

                    double femEstandar;
                    if (potencialEstandar1 < potencialEstandar2) {
                        femEstandar = potencialEstandar2 - potencialEstandar1;
                    } else {
                        femEstandar = potencialEstandar1 - potencialEstandar2;
                    }
                    double femEstandarRedondeada = Math.round(femEstandar * 10000.0) / 10000.0;

                    System.out.println();
                    System.out.println("Resultados del par galvánico:");
                    System.out.println(nombresMetales[indiceMetal1] + " -> E° = " + potencialEstandar1 + " V | C = " + concentracion1 + " M | E = " + potencialReal1Redondeado + " V");
                    System.out.println(nombresMetales[indiceMetal2] + " -> E° = " + potencialEstandar2 + " V | C = " + concentracion2 + " M | E = " + potencialReal2Redondeado + " V");

                    if (potencialReal1Redondeado == potencialReal2Redondeado) {
                        System.out.println();
                        System.out.println("Estado: Equilibrio electroquímico (E celda = 0.0 V)");
                        System.out.println("Ambos electrodos tienen el mismo potencial bajo estas condiciones; no ocurre corrosión galvánica neta.");
                        System.out.println("Potencial estándar de celda (E° celda): " + femEstandarRedondeada + " V");
                    } else {
                        // Menor potencial = ánodo (se oxida); mayor potencial = cátodo (se reduce)
                        int indiceAnodo;
                        int indiceCatodo;
                        double potencialAnodo;
                        double potencialCatodo;

                        if (potencialReal1 < potencialReal2) {
                            indiceAnodo = indiceMetal1;
                            indiceCatodo = indiceMetal2;
                            potencialAnodo = potencialReal1;
                            potencialCatodo = potencialReal2;
                        } else {
                            indiceAnodo = indiceMetal2;
                            indiceCatodo = indiceMetal1;
                            potencialAnodo = potencialReal2;
                            potencialCatodo = potencialReal1;
                        }

                        double femReal = potencialCatodo - potencialAnodo;
                        double femRealRedondeada = Math.round(femReal * 10000.0) / 10000.0;

                        int electronesAnodo = electronesTabla[indiceAnodo];
                        int electronesCatodo = electronesTabla[indiceCatodo];

                        // MCM de electrones para igualar semirreacciones
                        int mayor;
                        if (electronesAnodo > electronesCatodo) {
                            mayor = electronesAnodo;
                        } else {
                            mayor = electronesCatodo;
                        }

                        int electronesTotales = mayor;
                        while (electronesTotales % electronesAnodo != 0 || electronesTotales % electronesCatodo != 0) {
                            electronesTotales = electronesTotales + mayor;
                        }

                        int factorAnodo = electronesTotales / electronesAnodo;
                        int factorCatodo = electronesTotales / electronesCatodo;

                        // Armar términos balanceados (ocultando el coeficiente 1)
                        int coefAnodoReactivo = factorAnodo * coeficientesReducidos[indiceAnodo];
                        String anodoReactivo;
                        if (coefAnodoReactivo == 1) {
                            anodoReactivo = especiesReducidas[indiceAnodo];
                        } else {
                            anodoReactivo = coefAnodoReactivo + especiesReducidas[indiceAnodo];
                        }

                        int coefAnodoProducto = factorAnodo * coeficientesOxidados[indiceAnodo];
                        String anodoProducto;
                        if (coefAnodoProducto == 1) {
                            anodoProducto = especiesOxidadas[indiceAnodo];
                        } else {
                            anodoProducto = coefAnodoProducto + especiesOxidadas[indiceAnodo];
                        }

                        int coefCatodoReactivo = factorCatodo * coeficientesOxidados[indiceCatodo];
                        String catodoReactivo;
                        if (coefCatodoReactivo == 1) {
                            catodoReactivo = especiesOxidadas[indiceCatodo];
                        } else {
                            catodoReactivo = coefCatodoReactivo + especiesOxidadas[indiceCatodo];
                        }

                        int coefCatodoProducto = factorCatodo * coeficientesReducidos[indiceCatodo];
                        String catodoProducto;
                        if (coefCatodoProducto == 1) {
                            catodoProducto = especiesReducidas[indiceCatodo];
                        } else {
                            catodoProducto = coefCatodoProducto + especiesReducidas[indiceCatodo];
                        }

                        System.out.println();
                        System.out.println("Ánodo (se oxida): " + nombresMetales[indiceAnodo]);
                        System.out.println("Cátodo (se reduce): " + nombresMetales[indiceCatodo]);

                        if (potencialesEstandar[indiceAnodo] > potencialesEstandar[indiceCatodo]) {
                            System.out.println("Nota: Las concentraciones no estándar invirtieron la polaridad respecto a condiciones estándar.");
                        }

                        System.out.println();
                        System.out.println("Potenciales de la celda:");
                        System.out.println("Potencial estándar (E° celda): " + femEstandarRedondeada + " V");
                        System.out.println("Potencial de celda (E celda): " + femRealRedondeada + " V");

                        System.out.println();
                        System.out.println("Electrones involucrados:");
                        System.out.println("Electrones liberados por unidad de ánodo: " + electronesAnodo + "e-");
                        System.out.println("Electrones ganados por unidad de cátodo: " + electronesCatodo + "e-");
                        System.out.println("Electrones compartidos / transferidos en la celda: " + electronesTotales + "e-");

                        System.out.println();
                        System.out.println("Ecuaciones balanceadas:");
                        System.out.println("Oxidación (Ánodo): " + anodoReactivo + " -> " + anodoProducto + " + " + electronesTotales + "e-");
                        System.out.println("Reducción (Cátodo): " + catodoReactivo + " + " + electronesTotales + "e- -> " + catodoProducto);
                        System.out.println("Ecuación global: " + anodoReactivo + " + " + catodoReactivo + " -> " + anodoProducto + " + " + catodoProducto);
                    }
                    break;

                // Opción 2: mostrar tabla de potenciales y semirreacciones
                case 2:
                    System.out.println();
                    System.out.println("Tabla de potenciales estándar de oxidación (E°):");
                    for (int i = 0; i < nombresMetales.length; i++) {
                        String reactivo;
                        if (coeficientesReducidos[i] == 1) {
                            reactivo = especiesReducidas[i];
                        } else {
                            reactivo = coeficientesReducidos[i] + especiesReducidas[i];
                        }

                        String producto;
                        if (coeficientesOxidados[i] == 1) {
                            producto = especiesOxidadas[i];
                        } else {
                            producto = coeficientesOxidados[i] + especiesOxidadas[i];
                        }

                        System.out.println((i + 1) + ". " + nombresMetales[i] + " | " + reactivo + " -> " + producto + " + " + electronesTabla[i] + "e- | E° = " + potencialesEstandar[i] + " V");
                    }
                    break;

                // Salir
                case 3:
                    System.out.println();
                    System.out.println("Saliendo del programa...");
                    continuar = false;
                    break;
            }

        } while (continuar);
        lector.close();
    }
}