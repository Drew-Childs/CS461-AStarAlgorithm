import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        AStarAlgorithm algorithm = new AStarAlgorithm();
        String start, destination;

        while (true) {
            System.out.println("Enter your starting point:");
            System.out.println("--------------------------");
            start = in.nextLine();

            if (algorithm.townsWithData.containsKey(start)) {
                break;
            }

            System.out.println("<<<Town is not a known, please try again>>>");
        }

        while (true) {
            System.out.println("Enter your destination:");
            System.out.println("-----------------------");
            destination = in.nextLine();

            if (algorithm.townsWithData.containsKey(destination)) {
                break;
            }

            System.out.println("<<<Town is not a known, please try again>>>");
        }

        try {
            ArrayList<String> path = algorithm.execute(start, destination);

            for (String item : path) {
                System.out.println(item);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
