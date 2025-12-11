package org.iesvdm.ventas;

import org.iesvdm.ventas.modelo.Cliente;
import org.iesvdm.ventas.modelo.Comercial;
import org.iesvdm.ventas.modelo.Pedido;
import org.iesvdm.ventas.repositorio.ClienteRepository;
import org.iesvdm.ventas.repositorio.ComercialRepository;
import org.iesvdm.ventas.repositorio.PedidoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class VentasApplicationTests {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ComercialRepository comercialRepository;


    @Test
    void contextLoads() {

    }

    @Test
    void test() {
        fail("Not yet implemented");
    }


    @Test
    void testSkeletonCliente() {

            List<Cliente> list = clienteRepository.findAll();
            list.forEach(System.out::println);
            //TODO STREAMS

    }


    @Test
    void testSkeletonComercial() {

            List<Comercial> list = comercialRepository.findAll();
            list.forEach(System.out::println);
            //TODO STREAMS

    }

    @Test
    void testSkeletonPedido() {

            List<Pedido> list = pedidoRepository.findAll();
            list.forEach(System.out::println);
            //TODO STREAMS


    }
    /**
     * 1. Devuelve un listado de todos los pedidos que se realizaron durante el año 2017,
     * cuya cantidad total sea superior a 500€.
     * @throws ParseException
     */
    @Test
    void test1() throws ParseException {

        List<Pedido> list = pedidoRepository.findAll();

        Calendar cal = Calendar.getInstance();

        List<Pedido> pedidos2017 = list.stream()
                .filter(p -> {
                    cal.setTime(p.getFecha());
                    return cal.get(Calendar.YEAR) == 2017 && p.getTotal() > 500;
                })
                .collect(Collectors.toList());

        int count = (int) list.stream()
                .filter(p -> {
                    cal.setTime(p.getFecha());
                    return cal.get(Calendar.YEAR) == 2017 && p.getTotal() > 500;
                })
                .count();

        Assertions.assertEquals(3, count,
                "Deben existir exactamente 3 pedidos en 2017 con total > 500€");

        List<Integer> ids = list.stream()
                .filter(p -> {
                    cal.setTime(p.getFecha());
                    return cal.get(Calendar.YEAR) == 2017 && p.getTotal() > 500;
                })
                .map(Pedido::getId)
                .sorted()
                .collect(Collectors.toList());

        Assertions.assertIterableEquals(
                java.util.Arrays.asList(5, 8, 12),
                ids,
                "Los IDs esperados son 5, 8 y 12"
        );

    }


    /**
     * 2. Devuelve un listado con los identificadores de los clientes que NO han realizado algún pedido.
     *
     */
    @Test
    void test2() {

        List<Cliente> list = clienteRepository.findAll();

        List<Integer> clientesSinPedidos = list.stream()
                .filter(c -> c.getPedidos() == null || c.getPedidos().isEmpty())
                .map(Cliente::getId)
                .sorted()
                .collect(Collectors.toList());

        System.out.println("Clientes sin pedidos: " + clientesSinPedidos);

    }

    /**
     * 3. Devuelve el valor de la comisión de mayor valor que existe en la tabla comercial
     */
    @Test
    void test3() {

        List<Comercial> list = comercialRepository.findAll();

        Optional<Float> maxComision = list.stream()
                .map(Comercial::getComision)
                .max(Float::compare);

        maxComision.ifPresent(comision ->
            System.out.println("Comisión máxima: " + comision)
        );

    }

    /**
     * 4. Devuelve el identificador, nombre y primer apellido de aquellos clientes cuyo segundo apellido no es NULL.
     * El listado deberá estar ordenado alfabéticamente por apellidos y nombre.
     */
    @Test
    void test4() {

        List<Cliente> list = clienteRepository.findAll();

        list.stream()
                .filter(c -> c.getApellido2() != null)
                .sorted(Comparator.comparing(Cliente::getApellido1)
                        .thenComparing(Cliente::getApellido2)
                        .thenComparing(Cliente::getNombre))
                .forEach(c -> System.out.println(
                    "ID: " + c.getId() +
                    ", Nombre: " + c.getNombre() +
                    ", Apellido1: " + c.getApellido1()
                ));

    }

    /**
     * 5. Devuelve un listado con los nombres de los comerciales que terminan por "el" o "o".
     *  Tenga en cuenta que se deberán eliminar los nombres repetidos.
     */
    @Test
    void test5() {

        List<Comercial> list = comercialRepository.findAll();

        list.stream()
                .map(Comercial::getNombre)
                .filter(nombre -> nombre.endsWith("el") || nombre.endsWith("o"))
                .distinct()
                .forEach(System.out::println);

    }


    /**
     * 6. Devuelve un listado de todos los clientes que realizaron un pedido durante el año 2017, cuya cantidad esté entre 300 € y 1000 €.
     */
    @Test
    void test6() {

        List<Pedido> list = pedidoRepository.findAll();

        Calendar cal = Calendar.getInstance();

        list.stream()
                .filter(p -> {
                    cal.setTime(p.getFecha());
                    return cal.get(Calendar.YEAR) == 2017
                            && p.getTotal() >= 300
                            && p.getTotal() <= 1000;
                })
                .map(Pedido::getCliente)
                .distinct()
                .forEach(System.out::println);

    }


    /**
     * 7. Calcula la media del campo total de todos los pedidos realizados por el comercial Daniel Sáez
     */
    @Test
    void test7() {

        List<Comercial> list = comercialRepository.findAll();

        OptionalDouble media = list.stream()
                .filter(c -> c.getNombre().equals("Daniel")
                        && c.getApellido1().equals("Sáez"))
                .flatMap(c -> c.getPedidos().stream())
                .mapToDouble(Pedido::getTotal)
                .average();

        media.ifPresent(m ->
            System.out.println("Media de pedidos de Daniel Sáez: " + m)
        );

    }


    /**
     * 8. Devuelve un listado con todos los pedidos que se han realizado.
     *  Los pedidos deben estar ordenados por la fecha de realización
     * , mostrando en primer lugar los pedidos más recientes
     */
    @Test
    void test8() {

        List<Pedido> list = pedidoRepository.findAll();

        list.stream()
                .sorted(Comparator.comparing(Pedido::getFecha).reversed())
                .forEach(System.out::println);

    }

    /**
     * 9. Devuelve todos los datos de los dos pedidos de mayor valor.
     */
    @Test
    void test9() {

        List<Pedido> list = pedidoRepository.findAll();

        list.stream()
                .sorted(Comparator.comparing(Pedido::getTotal).reversed())
                .limit(2)
                .forEach(System.out::println);

    }

    /**
     * 10. Devuelve un listado con los identificadores de los clientes que han realizado algún pedido.
     * Tenga en cuenta que no debe mostrar identificadores que estén repetidos.
     */
    @Test
    void test10() {

        List<Pedido> list = pedidoRepository.findAll();

        list.stream()
                .map(p -> p.getCliente().getId())
                .distinct()
                .sorted()
                .forEach(System.out::println);

    }

    /**
     * 11. Devuelve un listado con el nombre y los apellidos de los comerciales que tienen una comisión entre 0.05 y 0.11.
     *
     */
    @Test
    void test11() {

        List<Comercial> list = comercialRepository.findAll();

        list.stream()
                .filter(c -> c.getComision() >= 0.05 && c.getComision() <= 0.11)
                .forEach(c -> System.out.println(
                    c.getNombre() + " " + c.getApellido1() + " " + c.getApellido2()
                ));

    }


    /**
     * 12. Devuelve el valor de la comisión de menor valor que existe para los comerciales.
     *
     */
    @Test
    void test12() {

        List<Comercial> list = comercialRepository.findAll();

        Optional<Float> minComision = list.stream()
                .map(Comercial::getComision)
                .min(Float::compare);

        minComision.ifPresent(comision ->
            System.out.println("Comisión mínima: " + comision)
        );

    }

    /**
     * 13. Devuelve un listado de los nombres de los clientes que
     * empiezan por A y terminan por n y también los nombres que empiezan por P.
     * El listado deberá estar ordenado alfabéticamente.
     *
     */
    @Test
    void test13() {

        List<Comercial> list = comercialRepository.findAll();

        list.stream()
                .map(Comercial::getNombre)
                .filter(nombre -> (nombre.startsWith("A") && nombre.endsWith("n"))
                        || nombre.startsWith("P"))
                .distinct()
                .sorted()
                .forEach(System.out::println);

    }

    /**
     * 14. Devuelve un listado de los nombres de los clientes
     * que empiezan por A y terminan por n y también los nombres que empiezan por P.
     * El listado deberá estar ordenado alfabéticamente.
     */
    @Test
    void test14() {

        List<Cliente> list = clienteRepository.findAll();

        list.stream()
                .map(Cliente::getNombre)
                .filter(nombre -> (nombre.startsWith("A") && nombre.endsWith("n"))
                        || nombre.startsWith("P"))
                .distinct()
                .sorted()
                .forEach(System.out::println);

    }

    /**
     * 15. Devuelve un listado de los clientes cuyo nombre no empieza por A.
     * El listado deberá estar ordenado alfabéticamente por nombre y apellidos.
     */
    @Test
    void test15() {

        List<Cliente> list = clienteRepository.findAll();

        list.stream()
                .filter(c -> !c.getNombre().startsWith("A"))
                .sorted(Comparator.comparing(Cliente::getNombre)
                        .thenComparing(Cliente::getApellido1)
                        .thenComparing(Comparator.nullsLast(Comparator.comparing(Cliente::getApellido2, Comparator.nullsLast(Comparator.naturalOrder())))))
                .forEach(System.out::println);

    }


    /**
     * 16. Devuelve un listado con el identificador, nombre y los apellidos de todos
     * los clientes que han realizado algún pedido.
     * El listado debe estar ordenado alfabéticamente por apellidos y nombre y se deben eliminar los elementos repetidos.
     */
    @Test
    void test16() {

        List<Pedido> list = pedidoRepository.findAll();

        list.stream()
                .map(Pedido::getCliente)
                .distinct()
                .sorted(Comparator.comparing(Cliente::getApellido1)
                        .thenComparing(Comparator.nullsLast(Comparator.comparing(Cliente::getApellido2, Comparator.nullsLast(Comparator.naturalOrder()))))
                        .thenComparing(Cliente::getNombre))
                .forEach(c -> System.out.println(
                    "ID: " + c.getId() +
                    ", Nombre: " + c.getNombre() +
                    ", Apellido1: " + c.getApellido1() +
                    ", Apellido2: " + c.getApellido2()
                ));

    }

    /**
     * 17. Devuelve un listado que muestre todos los pedidos que ha realizado cada cliente.
     * El resultado debe mostrar todos los datos del cliente primero junto con un sublistado de sus pedidos.
     * El listado debe mostrar los datos de los clientes ordenados alfabéticamente por nombre y apellidos.
     *
     Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100]
     Pedido [id=2, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=270.65, fecha=2016-09-10]
     Pedido [id=16, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=2389.23, fecha=2019-03-11]
     Pedido [id=15, cliente=Cliente [id=1, nombre=Aar�n, apellido1=Rivero, apellido2=G�mez, ciudad=Almer�a, categor�a=100], comercial=Comercial [id=5, nombre=Antonio, apellido1=Carretero, apellido2=Ortega, comisi�n=0.12], total=370.85, fecha=2019-03-11]
     Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200]
     Pedido [id=12, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=3045.6, fecha=2017-04-25]
     Pedido [id=7, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=5760.0, fecha=2015-09-10]
     Pedido [id=3, cliente=Cliente [id=2, nombre=Adela, apellido1=Salas, apellido2=D�az, ciudad=Granada, categor�a=200], comercial=Comercial [id=1, nombre=Daniel, apellido1=S�ez, apellido2=Vega, comisi�n=0.15], total=65.26, fecha=2017-10-05]
     ...
     */
    @Test
    void test17() {

        List<Cliente> list = clienteRepository.findAll();

        list.stream()
                .sorted(Comparator.comparing(Cliente::getNombre)
                        .thenComparing(Cliente::getApellido1)
                        .thenComparing(Comparator.nullsLast(Comparator.comparing(Cliente::getApellido2, Comparator.nullsLast(Comparator.naturalOrder())))))
                .forEach(cliente -> {
                    System.out.println(cliente);
                    if (cliente.getPedidos() != null) {
                        cliente.getPedidos().forEach(pedido ->
                            System.out.println("  " + pedido)
                        );
                    }
                });

    }

    /**
     * 18. Devuelve un listado que muestre todos los pedidos en los que ha participado un comercial.
     * El resultado debe mostrar todos los datos de los comerciales y el sublistado de pedidos.
     * El listado debe mostrar los datos de los comerciales ordenados alfabéticamente por apellidos.
     */
    @Test
    void test18() {

        List<Comercial> list = comercialRepository.findAll();

        list.stream()
                .sorted(Comparator.comparing(Comercial::getApellido1)
                        .thenComparing(Comparator.nullsLast(Comparator.comparing(Comercial::getApellido2, Comparator.nullsLast(Comparator.naturalOrder()))))
                        .thenComparing(Comercial::getNombre))
                .forEach(comercial -> {
                    System.out.println(comercial);
                    if (comercial.getPedidos() != null) {
                        comercial.getPedidos().forEach(pedido ->
                            System.out.println("  " + pedido)
                        );
                    }
                });

    }

    /**
     * 19. Devuelve el nombre y los apellidos de todos los comerciales que ha participado
     * en algún pedido realizado por María Santana Moreno.
     */
    @Test
    void test19() {

        List<Pedido> list = pedidoRepository.findAll();

        list.stream()
                .filter(p -> p.getCliente().getNombre().equals("María")
                        && p.getCliente().getApellido1().equals("Santana")
                        && p.getCliente().getApellido2().equals("Moreno"))
                .map(Pedido::getComercial)
                .distinct()
                .forEach(c -> System.out.println(
                    c.getNombre() + " " + c.getApellido1() + " " + c.getApellido2()
                ));

    }


    /**
     * 20. Devuelve un listado que solamente muestre los comerciales que no han realizado ningún pedido.
     */
    @Test
    void test20() {

        List<Comercial> list = comercialRepository.findAll();

        list.stream()
                .filter(c -> c.getPedidos() == null || c.getPedidos().isEmpty())
                .forEach(System.out::println);

    }

    /**
     * 21. Calcula el número total de comerciales distintos que aparecen en la tabla pedido
     */
    @Test
    void test21() {

        List<Pedido> list = pedidoRepository.findAll();

        long totalComerciales = list.stream()
                .map(Pedido::getComercial)
                .distinct()
                .count();

        System.out.println("Total de comerciales distintos: " + totalComerciales);

    }

    /**
     * 22. Calcula el máximo y el mínimo de total de pedido en un solo stream, transforma el pedido a un array de 2 double total, utiliza reduce junto con el array de double para calcular ambos valores.
     */
    @Test
    void test22() {

        List<Pedido> list = pedidoRepository.findAll();

        Optional<double[]> result = list.stream()
                .map(p -> new double[]{p.getTotal(), p.getTotal()})
                .reduce((a, b) -> new double[]{
                    Math.max(a[0], b[0]),  // máximo
                    Math.min(a[1], b[1])   // mínimo
                });

        result.ifPresent(r -> System.out.println(
            "Máximo: " + r[0] + ", Mínimo: " + r[1]
        ));

    }


    /**
     * 23. Calcula cuál es el valor máximo de categoría para cada una de las ciudades que aparece en cliente
     */
    @Test
    void test23() {

        List<Cliente> list = clienteRepository.findAll();

    }


    /**
     * 24. Calcula cuál es el máximo valor de los pedidos realizados
     * durante el mismo día para cada uno de los clientes. Es decir, el mismo cliente puede haber
     * realizado varios pedidos de diferentes cantidades el mismo día. Se pide que se calcule cuál es
     * el pedido de máximo valor para cada uno de los días en los que un cliente ha realizado un pedido.
     * Muestra el identificador del cliente, nombre, apellidos, la fecha y el valor de la cantidad.
     * Pista: utiliza collect, groupingBy, maxBy y comparingDouble métodos estáticos de la clase Collectors
     */
    @Test
    void test24() {

        List<Pedido> list = pedidoRepository.findAll();

    }

    /**
     *  25. Calcula cuál es el máximo valor de los pedidos realizados durante el mismo día para cada uno de los clientes,
     *  teniendo en cuenta que sólo queremos mostrar aquellos pedidos que superen la cantidad de 2000 €.
     *  Pista: utiliza collect, groupingBy, filtering, maxBy y comparingDouble métodos estáticos de la clase Collectors
     */
    @Test
    void test25() {

        List<Pedido> list = pedidoRepository.findAll();

    }

    /**
     *  26. Devuelve un listado con el identificador de cliente, nombre y apellidos
     *  y el número total de pedidos que ha realizado cada uno de clientes durante el año 2017.
     * @throws ParseException
     */
    @Test
    void test26() throws ParseException {

        List<Cliente> list = clienteRepository.findAll();

    }


    /**
     * 27. Devuelve cuál ha sido el pedido de máximo valor que se ha realizado cada año. El listado debe mostrarse ordenado por año.
     */
    @Test
    void test27() {

        List<Pedido> list = pedidoRepository.findAll();

    }


    /**
     *  28. Devuelve el número total de pedidos que se han realizado cada año.
     */
    @Test
    void test28() {

        List<Pedido> list = pedidoRepository.findAll();

    }

    /**
     *  29. Devuelve los datos del cliente que realizó el pedido
     *
     *   más caro en el año 2019.
     * @throws ParseException
     */
    @Test
    void test29() throws ParseException {


        List<Pedido> list = pedidoRepository.findAll();

    }


    /**
     *  30. Calcula la estadísticas de total de todos los pedidos.
     *  Pista: utiliza collect con summarizingDouble
     */
    @Test
    void test30() throws ParseException {

        List<Pedido> list = pedidoRepository.findAll();

        DoubleSummaryStatistics stats = list.stream()
                .collect(Collectors.summarizingDouble(Pedido::getTotal));

        System.out.println("Estadísticas de pedidos:");
        System.out.println("Count: " + stats.getCount());
        System.out.println("Sum: " + stats.getSum());
        System.out.println("Min: " + stats.getMin());
        System.out.println("Average: " + stats.getAverage());
        System.out.println("Max: " + stats.getMax());

    }


}
