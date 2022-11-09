package dev.klepto.dex.exchange.pair;

import dev.klepto.dex.exchange.PairExchange;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.Value;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class DynamicPair implements Pair {

    transient PairExchange exchange;
    Address contractAddress;
    Address address0;
    Address address1;

    public Uint256 getBalance0() {
        return getToken0().balanceOf(getContractAddress());
    }

    public Uint256 getBalance1() {
        return getToken1().balanceOf(getContractAddress());
    }

    @Override
    public DynamicPair toDynamic() {
        return this;
    }

    @Override
    public StaticPair toStatic() {
        return new StaticPair(exchange, contractAddress, address0, address1, getBalance0(), getBalance1());
    }

}
