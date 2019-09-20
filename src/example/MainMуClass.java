package example;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class MainMуClass {
    public static void main(String args[]) throws Exception {

        Handler a1 = new Handler(31, "Moscow");
        Handler b1 = new Handler(1, "Moscow");
        Handler c1 = new Handler(1, "Saratov");

        //тут p можно менять и проверять на exception последний поток НОМЕР (p-1)
        // смотрим без ИСПОЛЬЗОВАНИЯ sleep(), join(), yield(),... главное, что все потоки завершены без ошибок,
        // и синхронизированы!!!
        //для этого берем один класс и создаём объекты на нём!
        for (int p = 0; p < 5000; p++) {
            Thread r1 = new Thread(new MyRunClass(a1), "p1-" + p);
            Thread r2 = new Thread(new MyRunClass(b1), "p2-" + p);
            Thread r3 = new Thread(new MyRunClass(c1), "p3-" + p);
            r1.start();
            r2.start();
            r3.start();
        }



        //через Callable
        //
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future> list = new ArrayList<Future>();
        for (int i = 0; i < 10000; i++) {

            MyCallableClass callable;
            Future future;
            String result;

            callable = new MyCallableClass(a1);
            future = executor.submit(callable);
            list.add(future);
           /*
            result = (String) future.get();
            System.out.println("**********");
            System.out.println("result=" + result);
            System.out.println("**********");
            */

            callable = new MyCallableClass(b1);
            future = executor.submit(callable);
            list.add(future);

            callable = new MyCallableClass(c1);
            future = executor.submit(callable);
            list.add(future);

        }

        executor.shutdown();


        //"Synchronized error!" в консоле не найден,
        // потоки все завершаются
    }
}


