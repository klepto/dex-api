package dev.klepto.dex;

import dev.klepto.dex.DexModule.Cronos;
import com.google.inject.Injector;
import dev.klepto.dex.exchange.uniswap.croswap.CroswapExchange;
import dev.klepto.dex.exchange.uniswap.vvs.VvsExchange;
import dev.klepto.dex.price.ExchangePriceService;
import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.contract.impl.Erc20;
import dev.klepto.kweb3.util.number.Decimal;
import lombok.val;

import javax.inject.Inject;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class CronosApi extends AbstractDexApi {

    private final ExchangePriceService priceService;

    @Inject
    public CronosApi(Injector injector, @Cronos Web3Client client, CroswapExchange croswap, VvsExchange vvs) {
        super(injector, client);

        val usdc = client.getContract(Erc20.class, "0xc21223249CA28397B4B6541dfFaEcC539BfF0c59");
        this.priceService = new ExchangePriceService(usdc, croswap, vvs);
    }

    public Decimal getPrice(Erc20 token) {
        return priceService.getPrice(token);
    }

}
