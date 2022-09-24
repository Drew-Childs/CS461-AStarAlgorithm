import java.util.HashSet;
import java.util.Set;

public class TownData {
    public double lattitude;
    public double longitude;
    public Set<String> frontiers;

    public TownData() {
        this.frontiers = new HashSet<>();
    }
}
