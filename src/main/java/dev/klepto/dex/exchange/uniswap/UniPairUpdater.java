package dev.klepto.dex.exchange.uniswap;

import dev.klepto.dex.contract.UniPair;
import dev.klepto.dex.exchange.PairExchange;
import dev.klepto.dex.exchange.pair.Pair;
import dev.klepto.dex.exchange.pair.PairUpdater;
import dev.klepto.dex.exchange.pair.StaticPair;
import dev.klepto.dex.util.Multicaller;
import dev.klepto.kweb3.contract.impl.Erc20;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

import static dev.klepto.kweb3.type.sized.Uint256.uint256;
import static dev.klepto.kweb3.util.Collections.array;
import static dev.klepto.kweb3.util.Collections.until;
import static dev.klepto.kweb3.util.Logging.log;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class UniPairUpdater implements PairUpdater {

    private final UniExchange exchange;
    private final Multicaller multicall;

    public UniPairUpdater(UniExchange exchange) {
        this.exchange = exchange;
        this.multicall = new Multicaller(exchange.getClient());
    }

    public List<Pair> fetchPairs() {
        val router = exchange.getRouter();
        val pairAddresses = fetchPairAddresses(router.getFactoryAddress());
        val tokenAddresses = fetchTokenAddresses(pairAddresses);
        val tokenBalances = fetchTokenBalances(pairAddresses, tokenAddresses);

        val result = new ArrayList<Pair>();
        for (var i = 0; i < pairAddresses.size(); i++) {
            val balances = tokenBalances.get(i);
            if (balances[0].lessThanOrEquals(0) || balances[1].lessThanOrEquals(0)) {
                continue;
            }

            val contractAddress = pairAddresses.get(i);
            val tokenAddress0 = tokenAddresses.get(i)[0];
            val tokenAddress1 = tokenAddresses.get(i)[1];
            result.add(new StaticPair(exchange, contractAddress, tokenAddress0, tokenAddress1, balances[0], balances[1]));
        }
        return result;
    }

    public List<Address> fetchPairAddresses(Address factoryAddress) {
        log().debug("Fetching pair addresses for {}.", factoryAddress);
        val pairCount = exchange.getFactory().getPairCount();
        val pairAddresses = new ArrayList<Address>();
        until(pairCount).forEach(index ->
                multicall.queue(
                        () -> exchange.getFactory().getPairAddress(index),
                        result -> pairAddresses.add((Address) result.get(0))
                )
        );
        multicall.execute();
        return pairAddresses;
    }

    public List<Address[]> fetchTokenAddresses(List<Address> pairs) {
        log().debug("Fetching token addresses for {} pairs.", pairs.size());
        val token0Addresses = new ArrayList<Address>();
        val token1Addresses = new ArrayList<Address>();

        until(pairs.size()).forEach(index -> {
            val pair = exchange.getClient().getContract(UniPair.class, pairs.get(index));
            multicall.queue(pair::getAddress0, result -> token0Addresses.add((Address) result.get(0)));
            multicall.queue(pair::getAddress1, result -> token1Addresses.add((Address) result.get(0)));
        });
        multicall.execute();

        return until(pairs.size())
                .mapToObj(index -> array(token0Addresses.get(index), token1Addresses.get(index)))
                .toList();
    }

    public List<Uint256[]> fetchTokenBalances(List<Address> pairs, List<Address[]> tokens) {
        log().debug("Fetching token balances for {} pairs.", pairs.size());
        val balances0 = new ArrayList<Uint256>();
        val balances1 = new ArrayList<Uint256>();

        until(pairs.size()).forEach(index -> {
            val pairAddress = pairs.get(index);
            val token0 = exchange.getClient().getContract(Erc20.class, tokens.get(index)[0]);
            val token1 = exchange.getClient().getContract(Erc20.class, tokens.get(index)[1]);
            multicall.queue(() -> token0.balanceOf(pairAddress), result -> {
                balances0.add(result.isEmpty() ? uint256(0) : (Uint256) result.get(0));
            });
            multicall.queue(() -> token1.balanceOf(pairAddress), result -> {
                balances1.add(result.isEmpty() ? uint256(0) : (Uint256) result.get(0));
            });
        });
        multicall.execute();
        return until(pairs.size())
                .mapToObj(index -> array(balances0.get(index), balances1.get(index)))
                .toList();
    }

}
