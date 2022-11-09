package dev.klepto.dex.exchange;

import dev.klepto.dex.exchange.pair.Pair;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;

import java.util.List;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface PairExchange extends Exchange {

    Address getRouterAddress();

    List<Pair> getPairs();

    Pair getPair(Address address0, Address address1);

    Pair createPair(Address address0, Address address1, Uint256 amount0, Uint256 amount1);

}
