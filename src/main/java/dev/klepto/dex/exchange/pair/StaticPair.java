package dev.klepto.dex.exchange.pair;

import dev.klepto.dex.exchange.PairExchange;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.Value;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class StaticPair implements Pair {

    transient PairExchange exchange;
    Address contractAddress;
    Address address0;
    Address address1;
    Uint256 balance0;
    Uint256 balance1;

    @Override
    public DynamicPair toDynamic() {
        return new DynamicPair(exchange, contractAddress, address0, address1);
    }

    @Override
    public StaticPair toStatic() {
        return this;
    }

}
