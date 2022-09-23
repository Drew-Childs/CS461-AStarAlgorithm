import java.util.HashSet;
import java.util.Set;

public class TownData {
    public String townName;
    public double lattitude;
    public double longitude;
    public Set<String> frontiers;

    public TownData(String townName) {
        this.frontiers = new HashSet<>();
        this.townName = townName;
    }
}
