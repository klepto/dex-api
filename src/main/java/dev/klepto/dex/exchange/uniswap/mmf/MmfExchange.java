package dev.klepto.dex.exchange.uniswap.mmf;

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
public class MmfExchange extends UniExchange {

    @Inject
    public MmfExchange(@Cronos Web3Client client) {
        super(client, address("0x145677FC4d9b8F19B5D56d1820c48e0443049a30"), decimal(0.0017));
    }

}