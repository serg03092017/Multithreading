package example;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Handler {


    private String city;
    private int value;

    public Handler() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Handler(int value, String city) {
        this.value = value;
        this.city = city;
    }

    private Lock lock = new ReentrantLock();

    int result;//пусть это будет общая переменная

    int countForIncrement;//пусть это будет общая для потоков
    AtomicInteger a;

    synchronized int generatePasswordInSms(Handler handler) {

        countForIncrement = 0;

        lock.lock();

        try {
            countForIncrement++;  //используем не Атомарную переменную, и видим, что она всегда увеличивается на 1!
            int localVariableForPassword = 0;
            String CurrentCity = handler.city;
            int CurrentValue = handler.value;

            SimpleValidatePassword(CurrentCity, CurrentValue);
            if (SimpleValidatePassword(CurrentCity, CurrentValue) == false) {
                throw new Exception("Validate error");
            }

            for (int i = 0; i < CurrentCity.length(); i++) {
                char c = CurrentCity.charAt(i);
                localVariableForPassword += (c * CurrentValue);
            }

            //generate only 4 numbers
            result = generate4numbers(localVariableForPassword) % 10000;
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        if (countForIncrement != 1) System.out.println("Synchronized error!");

        return result;

    }

    private boolean SimpleValidatePassword(String city, int value) {
        boolean result1 = true;
        if (city.length() == 0) result1 = false;
        if (value < 0) result1 = false;
        return result1;
    }

    int generate4numbers(int localVariableForPassword) {
        int number = (localVariableForPassword % 10) * 1000 + rnd(0, 9) * 100 + rnd(0, 9) * 10 + rnd(0, 9);
        return number;
    }

    public static int rnd(int min, int max) {
        max -= min;
        return (int) ((Math.random() * ++max) + min);
    }

}

class MyRunClass implements Runnable {

    Handler handler;


    public MyRunClass(Handler handler) {
        this.handler = handler;
    }


    @Override
    public void run() {
        //тут проверили, что все потоки из заданного диапазона начались
        //System.out.println(Thread.currentThread().getName() + " start ");

        synchronized (handler) {
            handler.generatePasswordInSms(handler);
            System.out.println(handler.generatePasswordInSms(handler));
        }
        //тут проверили, что все потоки из заданного диапазона завершились
        //System.out.println(Thread.currentThread().getName() + " finish ");
    }
}

class MyCallableClass implements Callable<String> {

    Handler handler;

    public MyCallableClass(Handler handler) {
        this.handler = handler;
    }

    @Override
    public String call() throws Exception {
        //тут проверили, что все потоки из заданного диапазона начались


        synchronized (handler) {
            handler.generatePasswordInSms(handler);
            //System.out.println(handler.generatePasswordInSms(handler));
        }
        //тут проверили, что все потоки из заданного диапазона завершились
        System.out.println(Thread.currentThread().getName() + " finish ");
        return (Thread.currentThread().getName() + " finish ");
    }
}
