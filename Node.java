import java.util.LinkedList;

public class Node {
    public boolean reachable = false;
    public int min = Integer.MAX_VALUE;
    public Node parent;
    public Edges parent2;
    public LinkedList<Edges> connections = new LinkedList<>();
    public String name;
    public int level = Integer.MAX_VALUE;
    public Node(String name){
        this.name = name;
    }
}
