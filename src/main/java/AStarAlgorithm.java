import java.io.*;
import java.util.*;

public class AStarAlgorithm {
    public Map<String, TownData> townsWithData;

    public AStarAlgorithm() throws IOException {
        this.townsWithData = new HashMap<>();
        constructFullTowns();
    }

    public ArrayList<String> execute(String start, String destination) throws Exception {
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
                if (!(predictedScore.containsKey(item))) {
                    predictedScore.put(item, calculateDistance(townsWithData.get(item), townsWithData.get(destination)));
                }

                if (!(currentScore.containsKey(item))) {
                    currentScore.put(item, calculateDistance(townsWithData.get(current), townsWithData.get(item)) + currentScore.get(current));
                    calculatedFrom.put(item, current);
                }

                total = currentScore.get(current) + predictedScore.get(item);

                if (lowest == "" || total < currentScore.get(lowest) + predictedScore.get(lowest)) {
                    lowest = item;
                }
            }

            cameFrom.put(lowest, calculatedFrom.get(lowest));
            openSet.addAll(townsWithData.get(lowest).frontiers);
            current = lowest;

            for (Map.Entry<String, String> entry : cameFrom.entrySet()) {
                openSet.remove(entry.getKey());
            }

            if (current.equals(destination)) {
                return reconstructPath(cameFrom, lowest);
            }
        }

        throw new Exception("Unable to find route!!");
    }

    // pythagorean theorem
    private double calculateDistance(TownData current, TownData desired) {
        return Math.pow(Math.pow(desired.longitude - current.longitude, 2) + Math.pow(desired.lattitude - current.lattitude, 2), 0.5);
    }

    // construct initial adjacency tree from file input
    private void constructFullTowns() throws IOException {
        File adjacencies = new File(System.getProperty("user.dir") + "\\Adjacencies.txt");
        BufferedReader adjacenciesBR = new BufferedReader(new FileReader(adjacencies));

        String line;
        while ((line = adjacenciesBR.readLine()) != null) {
            String[] towns = line.split(" ");

            // read all items in
            for (int i = 0; i < towns.length; i++) {
                if (towns[i] == "") {
                    continue;
                }

                if (!townsWithData.containsKey(towns[i])) {
                    townsWithData.put(towns[i], new TownData(towns[i]));
                }

                if (i != 0) {
                    townsWithData.get(towns[0]).frontiers.add(towns[i]);
                    townsWithData.get(towns[i]).frontiers.add(towns[0]);
                }
            }
        }

        File coordinates = new File(System.getProperty("user.dir") + "\\coordinates.txt");
        BufferedReader coordinateBR = new BufferedReader(new FileReader(coordinates));

        while ((line = coordinateBR.readLine()) != null) {
            String[] coords = line.split(" ");
            ArrayList<String> filteredCoords = new ArrayList<>();

            for (int i = 0; i < coords.length; i++) {
                if (!(coords[i] == "")) {
                    filteredCoords.add(coords[i]);
                }
            }

            townsWithData.get(filteredCoords.get(0)).lattitude = Double.parseDouble(filteredCoords.get(1));
            townsWithData.get(filteredCoords.get(0)).longitude = Double.parseDouble(filteredCoords.get(2));
        }
    }

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
