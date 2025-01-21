import java.util.*;

public class Robot extends Thread {
    private static String LETTERS = "RLRFR";
    private static int LENGHT = 100;
    private static int TGHREADS = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void StartRobot() throws InterruptedException {

        Runnable runnable = () -> {
            for (int i = 0; i < TGHREADS; i++) {
                String route = generateRoute(LETTERS, LENGHT);
                System.out.println("Route = " + route);
                int numb = (int) route.chars().filter(ch -> ch == 'R').count();

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(numb)) {
                        sizeToFreq.put(numb, sizeToFreq.get(numb) + 1);
                    } else {
                        sizeToFreq.put(numb, 1);
                    }
                    sizeToFreq.notify();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();

        //сартуем еще один поток
        Thread thread1 = new Thread(() -> {
            while (!Thread.interrupted()) {
                synchronized (sizeToFreq) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        return;
                    }
                    Map.Entry<Integer, Integer> max = sizeToFreq.entrySet()
                            .stream()
                            .max(Map.Entry.comparingByValue())
                            .get();
                    System.out.println("Текущий лидер среди частот " + max.getKey() + " (встретилось " + max.getValue() + " раз)");
                }
            }
        });

        thread1.start();

        thread.join();
        thread1.interrupt();


        Map.Entry<Integer, Integer> max = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();
        System.out.println("Самое частое количество повторений " + max.getKey() + " (встретилось " + max.getValue() + " раз)");

        System.out.println("Другий размеры: ");
        sizeToFreq.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(e -> System.out.println(" - " + e.getKey() + " встретилось " + e.getValue() + " раз)"));
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
