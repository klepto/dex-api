package dev.klepto.dex.price;

import dev.klepto.dex.exchange.Exchange;
import dev.klepto.kweb3.contract.impl.Erc20;
import dev.klepto.kweb3.util.number.Decimal;
import dev.klepto.kweb3.util.number.Numeric;
import lombok.val;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.klepto.kweb3.Web3Error.require;
import static dev.klepto.kweb3.type.sized.Uint256.uint256;
import static dev.klepto.kweb3.util.number.Numeric.decimal;
import static dev.klepto.kweb3.util.number.Numeric.tokens;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class ExchangePriceService implements PriceService {

    private final Erc20 usdToken;
    private final List<Exchange> exchanges = new ArrayList<>();

    public ExchangePriceService(Erc20 usdToken, Exchange... exchanges) {
        this.usdToken = usdToken;
        this.exchanges.addAll(Arrays.stream(exchanges).toList());
    }

    @Override
    public Decimal getPrice(Erc20 token) {
        val serviceChain = usdToken.getClient().getChain();
        val tokenChain = token.getClient().getChain();
        require(tokenChain.getChainId() == tokenChain.getChainId(), "Chain mismatch {} - {}.", serviceChain, tokenChain);

        if (token.equals(usdToken)) {
            return decimal(1);
        }

        val decimals =  token.decimals();
        // use 1/100th of the token to minimize price impact when estimating the price
        val scale = decimals.moreThan(4) ? 100 : 1;
        val amount = tokens(1, decimals).div(scale);
        val prices = exchanges.stream()
                .map(exchange -> exchange.getSwapAmount(
                        token.getAddress(),
                        usdToken.getAddress(),
                        amount
                ))
                .filter(price -> price.moreThan(0))
                .toList();

        val total = prices.stream().reduce(Numeric::add).orElse(uint256(0));
        val average = total.div(prices.size());
        return average.toDecimal(usdToken.decimals()).mul(scale);
    }

}
