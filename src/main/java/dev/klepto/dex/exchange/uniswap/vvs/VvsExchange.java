package dev.klepto.dex.exchange.uniswap.vvs;

import dev.klepto.dex.DexModule.Cronos;
import dev.klepto.dex.exchange.uniswap.UniExchange;
import dev.klepto.kweb3.Web3Client;

import javax.inject.Inject;
import javax.inject.Singleton;

import static dev.klepto.kweb3.type.Address.address;
import static dev.klepto.kweb3.util.number.Numeric.decimal;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Singleton
public class VvsExchange extends UniExchange {

    @Inject
    public VvsExchange(@Cronos Web3Client client) {
        super(client, address("0x145863eb42cf62847a6ca784e6416c1682b1b2ae"), decimal(0.002));
    }

}