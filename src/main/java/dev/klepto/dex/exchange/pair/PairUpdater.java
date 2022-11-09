package dev.klepto.dex.exchange.pair;

import java.util.List;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface PairUpdater {

    List<Pair> fetchPairs();

}
