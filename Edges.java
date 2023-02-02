public class Edges {
    public boolean res =true;
    public Node second;
    public int weight;
    public Edges(Node to,int b,boolean isaut){
        this.second = to;
        this.weight = b;
        this.res = isaut;
    }

}
