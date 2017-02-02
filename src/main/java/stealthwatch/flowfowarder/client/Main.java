package stealthwatch.flowfowarder.client;

public class Main {

    public static void main(String... args) {
        try {
            FlowWriter.start("10.0.37.208");

            Thread.sleep(5000);
        } catch (Exception e) {
            System.err.println("URISyntaxException exception: " + e.getMessage());
        }
    }
}

