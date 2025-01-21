import java.util.*;

public class Robot extends Thread {
    private static String LETTERS = "RLRFR";
    private static int LENGHT = 100;
    private static int TGHREADS = 1000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void StartRobot() {
        for (int i = 0; i < TGHREADS; i++) {
            new Thread(() -> {
                String route = generateRoute(LETTERS, LENGHT);
                System.out.println("Route = " + route);
                int numb = (int) route.chars().filter(ch -> ch == 'R').count();

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(numb)) {
                        sizeToFreq.put(numb, sizeToFreq.get(numb) + 1);
                    } else {
                        sizeToFreq.put(numb, 1);
                    }
                }
            }).start();
        }
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
