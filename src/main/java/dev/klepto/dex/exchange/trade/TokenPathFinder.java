package dev.klepto.dex.exchange.trade;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import dev.klepto.kweb3.type.Address;
import lombok.val;

import java.util.*;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class TokenPathFinder {

    private MutableGraph<Address> tokenGraph = GraphBuilder
            .undirected()
            .allowsSelfLoops(true)
            .build();

    public int size() {
        return tokenGraph.nodes().size();
    }

    public void addPath(Address address0, Address address1) {
        tokenGraph.putEdge(address0, address1);
    }

    public List<List<Address>> findPaths(Address source, Address destination, int depth) {
        if (!tokenGraph.nodes().contains(source) || !tokenGraph.nodes().contains(destination)) {
            return Collections.emptyList();
        }

        val result = new LinkedList<List<Address>>();
        val paths = new LinkedList<LinkedList<Address>>();
        paths.add(new LinkedList<>(Arrays.asList(source)));

        for (var i = 0; i < depth; i++) {
            val newPaths = new LinkedList<LinkedList<Address>>();
            PathLoop:
            for (val path : paths) {
                for (val child : tokenGraph.adjacentNodes(path.getLast())) {
                    if (path.size() == depth + 1 && !child.equals(destination)) {
                        continue PathLoop;
                    }

                    val newPath = new LinkedList<>(path);
                    newPath.add(child);

                    if (!child.equals(destination)) {
                        newPaths.add(newPath);
                    } else {
                        result.add(newPath);
                    }
                }
            }
            paths.clear();
            paths.addAll(newPaths);
        }

        return result;
    }

}