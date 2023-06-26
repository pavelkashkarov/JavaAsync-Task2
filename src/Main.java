import java.util.*;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    final static int THREAD_NUMBER = 1000;
    final static int ROUTE_LENGTH = 100;
    final static String LETTERS = "RLRFR";


    public static void main(String[] args) throws InterruptedException {

        long startTs = System.currentTimeMillis(); // start time

        long endTs = System.currentTimeMillis(); // end time

        for (int i = 0; i < THREAD_NUMBER; i++) {
            getNewThread().start();
        }

        Map.Entry<Integer, Integer> max = sizeToFreq
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();

        System.out.println("Самое частое количество повторений " + max.getKey()
                + " (встретилось " + max.getValue() + " раз)");

        System.out.println("Другие размеры:");
        sizeToFreq
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(i -> System.out.println("- " + i.getKey() + " (" + i.getValue() + " раз)"));

        System.out.println("Time: " + (endTs - startTs) + "ms");
    }

    private static void startThreads(List<Thread> threads) {
        threads.forEach(Thread::start);
    }

    private static String generateText() {

        String str;
        str = generateRoute(LETTERS, ROUTE_LENGTH);

        return str;
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

    public static Thread getNewThread() {

        return new Thread(() -> {
            int freqCount = 0;
            String text = generateText();

            for (int i = 0; i < text.length(); i++) {
                if (text.charAt(i) == 'R') {
                    freqCount++;
                }
            }

            synchronized (sizeToFreq) {
                if (sizeToFreq.containsKey(freqCount)) {
                    sizeToFreq.put(freqCount, sizeToFreq.get(freqCount) + 1);
                } else {
                    sizeToFreq.put(freqCount, 1);
                }
            }

        });
    }
}