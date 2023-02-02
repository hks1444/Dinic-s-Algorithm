import java.io.*;
import java.util.*;

public class project5 {
    public static Node source = new Node("Src");
    public static Node sink = new Node("KL");
    public static HashMap<String,Node> nodes=new HashMap<>();
    public static void main(String[] args) throws IOException {
        nodes.put("KL",sink);
        nodes.put("Src",source);
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        int no_of_nodes = Integer.parseInt(reader.readLine());
        String[] cap = reader.readLine().split(" ");
        for(int i=0;i<6;i++){
            String[] line = reader.readLine().split(" ");
            Node temp = new Node(line[0]);
            nodes.put(line[0],temp);
            source.connections.add(new Edges(temp,Integer.parseInt(cap[i]),true));
            for(int j=1;j<line.length;j+=2){
                    if (nodes.containsKey(line[j])) {
                        temp.connections.add(new Edges(nodes.get(line[j]), Integer.parseInt(line[j + 1]),true));
                    } else {
                        Node temp2 = new Node(line[j]);
                        nodes.put(line[j], temp2);
                        temp.connections.add(new Edges(temp2, Integer.parseInt(line[j + 1]),true));
                    }
            }
        }
        for(int i=0;i<no_of_nodes;i++){
            String[] line = reader.readLine().split(" ");
            if(nodes.containsKey(line[0])){
                for(int j=1;j<line.length;j+=2){
                    if(nodes.containsKey(line[j])){
                        nodes.get(line[0]).connections.add(new Edges(nodes.get(line[j]),Integer.parseInt(line[j+1]),true));
                    }else{
                        Node temp2 = new Node(line[j]);
                        nodes.put(line[j],temp2);
                        nodes.get(line[0]).connections.add(new Edges(temp2,Integer.parseInt(line[j+1]),true));

                    }

                }
            }else{
                Node temp = new Node(line[0]);
                for(int j=1;j<line.length;j+=2){
                    if(nodes.containsKey(line[j])){
                        temp.connections.add(new Edges(nodes.get(line[j]),Integer.parseInt(line[j+1]),true));
                    }else{
                        Node temp2 = new Node(line[j]);
                        nodes.put(line[j],temp2);
                        temp.connections.add(new Edges(temp2,Integer.parseInt(line[j+1]),true));

                    }

                }
                nodes.put(line[0],temp);
            }

        }
        dfs();
        int ans=0;
        for(Edges x:sink.connections){
            ans += x.weight;
        }
        File out = new File(args[1]);
        FileWriter writer = new FileWriter(out);
        writer.write(ans + "\n");
        for(String q:min_cut()){
            writer.write(q + "\n");
        }
        writer.close();
    }
    public static  void level(){
        //By using breath first search, nodes' levels are enumerated
        //source has zero level and at the every iteration level is increased by one
        //and sink has maximum value
        Queue<Node> vis = new LinkedList<>();
        vis.add(source);
        HashSet<Node> others= new HashSet<>();
        source.level=0;
        others.add(source);
        while(!vis.isEmpty()){
            Node temp = vis.poll();
            for(Edges i:temp.connections){
                if(!others.contains(i.second)&i.weight>0){
                    i.second.level=temp.level+1;
                    others.add(i.second);
                    vis.add(i.second);
                }
            }

        }
        sink.level=Integer.MAX_VALUE;
    }
    public static HashSet<String> min_cut(){
        //first I find the reachable nodes
        //then the edges from reachable nodes to the unreachable nodes are determined
        //those edges are the min-cut
        Stack<Node> traversal = new Stack<>();
        HashSet<Node> vis = new HashSet<>();
        traversal.add(source);
        HashSet<String> ans = new HashSet<>();
        while (!traversal.isEmpty()){
            Node temp = traversal.pop();
            temp.reachable = true;
            for(Edges i:temp.connections){
                if(!i.second.reachable&i.weight>0){
                    traversal.add(i.second);
                }
            }
        }
        traversal.add(source);
        while (!traversal.isEmpty()){
            Node temp = traversal.pop();
            for(Edges i:temp.connections){
                if(!vis.contains(i.second)&i.second.reachable){
                    traversal.add(i.second);
                    vis.add(i.second);
                } else if(!i.second.reachable){
                    if(temp.name.equals("Src")){
                        ans.add(i.second.name);
                    }else{
                        ans.add(temp.name+" "+i.second.name);
                        }
                }
                    vis.add(i.second);

            }

        }

        return ans;
        }

    public static void dfs(){
        //here I tried to use dinics algorithm
        //while doing depth first search only with higher level nodes can be selected
        //I used stack to store nodes in required order
        //then I reverse the flow when I reach the sink
        Stack<Node> traversal = new Stack<>();
        traversal.add(source);
        level();
        while(!traversal.isEmpty()){
            Node current = traversal.pop();
            if(current.equals(sink)){
                //find min and reverse the flow
                int min = current.min;
                while(!current.equals(source)){
                    Node temp = current.parent;
                    current.connections.add(new Edges(temp,min,false));
                    current.parent2.weight-=min;
                    current=temp;
                }
                level();
                traversal= new Stack<>();
                traversal.add(source);
                continue;

            }
            for(Edges i:current.connections){
                if(i.second.level> current.level&i.weight>0){
                    i.second.min = Math.min(current.min, i.weight);
                    i.second.parent = current;
                    i.second.parent2 = i;
                    traversal.add(i.second);
                }
            }
        }
    }

}