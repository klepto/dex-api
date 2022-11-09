package dev.klepto.dex;

import com.google.inject.Guice;
import com.google.inject.Injector;
import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.chain.Chain;
import dev.klepto.kweb3.contract.impl.Erc20;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.util.number.Decimal;

import static dev.klepto.kweb3.type.Address.address;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface DexApi {

    Injector injector = Guice.createInjector(new DexModule());

    void setClient(Web3Client client);

    Web3Client getClient();

    Chain getChain();

    Decimal getPrice(Erc20 token);

    default Decimal getPrice(Address tokenAddress) {
        return getPrice(getClient().getContract(Erc20.class, tokenAddress));
    }

    default Decimal getPrice(String tokenAddress) {
        return getPrice(address(tokenAddress));
    }

    static CronosApi getCronos() {
        return injector.getInstance(CronosApi.class);
    }

}
