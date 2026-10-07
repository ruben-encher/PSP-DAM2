package main.java.es.dam.psp.ut1.practicas;

import java.util.Scanner;

/**&#xA; * E6 · Un "ps" casero con ProcessHandle: información de los procesos del sistema.&#xA; *&#xA; * Apuntes UT-1, apartado 6.4. Base de la Práctica 2.c (practicas/BuscarProcesos.java).&#xA; * Cómo ejecutarlo: triángulo ▶ junto a main, sin argumentos.&#xA; * Muestra el PID propio y el del padre, y una tabla PID, PPID, USUARIO y COMANDO con los&#xA; * 15 primeros procesos visibles. Solo verás los datos que el sistema operativo permite leer&#xA; * a tu usuario: cada dato de info() es un Optional que puede estar vacío.&#xA; */
public class BuscarProcesos {


    // RUBÉN ENCABO HERNÁNDEZ


    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: BuscarProcesos <cadena>");
            return;
        }
        String buscada = args[0];

        ProcessHandle yo = ProcessHandle.current();
        System.out.println("Soy el PID " + yo.pid() + ", mi padre es "
                + yo.parent().map(ProcessHandle::pid).orElse(-1L));
        System.out.printf("%-8s %-8s %-12s %-26s %-12s %s%n",
                "PID", "PPID", "USUARIO", "INICIO", "CPU", "COMANDO");

        ProcessHandle.allProcesses()
                .filter(p -> p.info().command().isPresent())
                .filter(p -> p.info().command().get().contains(buscada))
                .forEach(p -> {
                    ProcessHandle.Info info = p.info();
                    System.out.printf("%-8d %-8s %-12s %-26s %-12s %s%n",
                            p.pid(),
                            p.parent().map(padre -> String.valueOf(padre.pid())).orElse("-"),
                            ultimos(info.user().orElse("?"), 12),
                            info.startInstant().map(String::valueOf).orElse("?"),
                            info.totalCpuDuration().map(String::valueOf).orElse("?"),
                            ultimos(info.command().get(), 60));
                });
    }

    static String ultimos(String s, int n) {
        return s.length() <= n ? s : s.substring(s.length() - n);
    }
}