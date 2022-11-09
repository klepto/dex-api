package dev.klepto.dex.exchange.pair;

import dev.klepto.dex.exchange.PairExchange;
import dev.klepto.kweb3.contract.impl.Erc20;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;

import static dev.klepto.kweb3.Web3Error.require;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Pair {

    PairExchange getExchange();

    Address getContractAddress();

    Address getAddress0();

    Address getAddress1();

    Uint256 getBalance0();

    Uint256 getBalance1();

    DynamicPair toDynamic();

    StaticPair toStatic();

    default Erc20 getToken(Address address) {
        require(address.equals(getAddress0()) || address.equals(getAddress1()), "Not a pair token address.");
        return getExchange().getClient().getContract(Erc20.class, address);
    }

    default Erc20 getToken0() {
        return getToken(getAddress0());
    }

    default Erc20 getToken1() {
        return getToken(getAddress1());
    }

    default Uint256 getBalance(Address address) {
        require(address.equals(getAddress0()) || address.equals(getAddress1()), "Not a pair token address.");
        return address.equals(getAddress0()) ? getBalance0() : getBalance1();
    }

}
