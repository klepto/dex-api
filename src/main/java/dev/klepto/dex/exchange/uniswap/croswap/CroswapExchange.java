package dev.klepto.dex.exchange.uniswap.croswap;

import dev.klepto.dex.DexModule.Cronos;
import dev.klepto.dex.exchange.pair.Pair;
import dev.klepto.dex.exchange.pair.PairCache;
import dev.klepto.dex.exchange.uniswap.UniExchange;
import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.type.Address;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.time.Duration;
import java.util.List;

import static dev.klepto.kweb3.type.Address.address;
import static dev.klepto.kweb3.util.number.Numeric.decimal;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Singleton
public class CroswapExchange extends UniExchange {

    private final PairCache pairs;

    @Inject
    public CroswapExchange(@Cronos Web3Client client) {
        super(client, address("0x6c3A0E2E78848274B7E3346b8Ef8a4cBB2fEE2a9"), decimal(0.002));
        this.pairs = new PairCache(Duration.ofMinutes(5), new CroswapPairUpdater(this));
    }

    @Override
    public List<Pair> getPairs() {
        return pairs.getPairs();
    }

    @Override
    public Pair getPair(Address address0, Address address1) {
        return pairs.getPair(address0, address1);
    }

}
