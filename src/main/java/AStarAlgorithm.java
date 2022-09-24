import java.io.*;
import java.util.*;

public class AStarAlgorithm {
    public Map<String, TownData> townsWithData;


    public AStarAlgorithm() throws IOException {
        this.townsWithData = new HashMap<>();
        constructFullTowns();
    }


    // constructs adjacencies for towns and assigns their coordinates
    private void constructFullTowns() throws IOException {
        File adjacencies = new File(System.getProperty("user.dir") + "\\Adjacencies.txt");
        BufferedReader adjacenciesBR = new BufferedReader(new FileReader(adjacencies));

        File coordinates = new File(System.getProperty("user.dir") + "\\coordinates.txt");
        BufferedReader coordinateBR = new BufferedReader(new FileReader(coordinates));

        constructFrontiers(adjacenciesBR);
        constructCoordinates(coordinateBR);
    }


    // construct frontiers of all towns
    private void constructFrontiers(BufferedReader file) throws IOException {
        String line;
        while ((line = file.readLine()) != null) {
            String[] towns = line.split(" ");

            // read all items in
            for (int i = 0; i < towns.length; i++) {
                // accounting for potential formatting issues with file
                if (towns[i] == "") {
                    continue;
                }

                // adding town of interest if doesn't already exist
                if (!townsWithData.containsKey(towns[i])) {
                    townsWithData.put(towns[i], new TownData());
                }

                // adds the appropriate frontiers to the town of interest, and town of interest to frontiers
                if (i != 0) {
                    townsWithData.get(towns[0]).frontiers.add(towns[i]);
                    townsWithData.get(towns[i]).frontiers.add(towns[0]);
                }
            }
        }
    }


    // assign coordinates to all towns
    private void constructCoordinates(BufferedReader file) throws IOException {
        String line;
        while ((line = file.readLine()) != null) {
            String[] coords = line.split(" ");
            ArrayList<String> filteredCoords = new ArrayList<>();

            // accounting for potential formatting issues with file
            for (int i = 0; i < coords.length; i++) {
                if (!(coords[i] == "")) {
                    filteredCoords.add(coords[i]);
                }
            }

            // assigning lattitude and longitude
            townsWithData.get(filteredCoords.get(0)).lattitude = Double.parseDouble(filteredCoords.get(1));
            townsWithData.get(filteredCoords.get(0)).longitude = Double.parseDouble(filteredCoords.get(2));
        }
    }


    // calculates distance from lattitude and longitude
    private double calculateDistance(TownData current, TownData desired) {
        return Math.pow(Math.pow(desired.longitude - current.longitude, 2) + Math.pow(desired.lattitude - current.lattitude, 2), 0.5);
    }


    // overall algorithm for A*
    public ArrayList<String> execute(String start, String destination) throws Exception {
        // setting up initial data structures required for algorithm
        String current = start;

        Set<String> openSet = townsWithData.get(start).frontiers;

        Map<String, String> cameFrom = new HashMap<>();
        cameFrom.put(start, "END");

        Map<String, String> calculatedFrom = new HashMap<>();

        Map<String, Double> currentScore = new HashMap<>();
        currentScore.put(start, 0.0);

        Map<String, Double> predictedScore = new HashMap<>();
        predictedScore.put(start, calculateDistance(townsWithData.get(start), townsWithData.get(destination)));

        while (!(openSet.isEmpty())) {
            String lowest = "";
            Double total;

            for(String item : openSet) {
                // calculates distance from frontier city to goal and stores it
                if (!(predictedScore.containsKey(item))) {
                    predictedScore.put(item, calculateDistance(townsWithData.get(item), townsWithData.get(destination)));
                }

                // calculates the distance from the start to the frontier city and stores it
                if (!(currentScore.containsKey(item))) {
                    currentScore.put(item, calculateDistance(townsWithData.get(current), townsWithData.get(item)) + currentScore.get(current));
                    calculatedFrom.put(item, current);
                }

                // calculating heuristic
                total = currentScore.get(current) + predictedScore.get(item);

                // determines which of the current frontier cities is the best option
                if (lowest == "" || total < currentScore.get(lowest) + predictedScore.get(lowest)) {
                    lowest = item;
                }
            }

            // resetting variables for each iteration and updating map
            cameFrom.put(lowest, calculatedFrom.get(lowest));
            openSet.addAll(townsWithData.get(lowest).frontiers);
            current = lowest;

            // resetting frontier
            for (Map.Entry<String, String> entry : cameFrom.entrySet()) {
                openSet.remove(entry.getKey());
            }

            // checks if we're at the end state
            if (current.equals(destination)) {
                return reconstructPath(cameFrom, lowest);
            }
        }

        throw new Exception("Unable to find route!!");
    }


    // traces the route from the end back to the beginning and returns it
    public ArrayList<String> reconstructPath(Map<String, String> path, String current) {
        ArrayList<String> totalPath = new ArrayList<>();
        totalPath.add(0, current);

        while (path.containsKey(current)) {
            current = path.get(current);
            if (current == "END") {
                break;
            }
            totalPath.add(0, current);
        }

        return totalPath;
    }
}
