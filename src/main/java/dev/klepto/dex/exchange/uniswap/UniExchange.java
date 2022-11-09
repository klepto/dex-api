package dev.klepto.dex.exchange.uniswap;


import dev.klepto.dex.contract.UniFactory;
import dev.klepto.dex.contract.UniRouter;
import dev.klepto.dex.exchange.pair.Pair;
import dev.klepto.dex.exchange.PairExchange;
import dev.klepto.dex.exchange.pair.PairCache;
import dev.klepto.dex.exchange.trade.Trade;
import dev.klepto.dex.exchange.trade.Trading;
import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.contract.impl.Erc20;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import dev.klepto.kweb3.util.number.Decimal;
import dev.klepto.kweb3.util.number.Numeric;
import lombok.Getter;
import lombok.val;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;

import static dev.klepto.kweb3.type.sized.Uint256.uint256;
import static dev.klepto.kweb3.util.number.Numeric.decimal;
import static java.lang.Math.min;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class UniExchange implements PairExchange {

    private final Web3Client client;
    private final Address routerAddress;
    private final Decimal swapFee;

    private final UniRouter router;
    private final UniFactory factory;
    private final PairCache pairs;
    private final Trading trading;

    public UniExchange(Web3Client client, Address routerAddress, Decimal swapFee) {
        this.client = client;
        this.routerAddress = routerAddress;
        this.swapFee = swapFee;

        this.router = client.getContract(UniRouter.class, routerAddress);
        this.factory = client.getContract(UniFactory.class, router.getFactoryAddress());
        this.pairs = new PairCache(Duration.ofMinutes(5), new UniPairUpdater(this));
        this.trading = new Trading(this);
    }

    @Override
    public Uint256 swap(Address inputAddress, Address outputAddress, Uint256 inputAmount, Decimal slippage) {
        val trade = trading.getMultiTrade(inputAddress, outputAddress, inputAmount, 2);
        if (trade == null) {
            return uint256(0);
        }

        val minimumOutputAmount = trade.getOutputAmount().mul(decimal(1).sub(slippage));
        val response = router.swapWithFee(inputAmount, minimumOutputAmount, trade.getPathAddresses());

        return response.getEvents(Erc20.TransferEvent.class)
                .filter(event -> event.getAddress().equals(outputAddress) && event.getReceiver().equals(client.getAddress()))
                .map(Erc20.TransferEvent::getValue)
                .findFirst().orElse(uint256(0));
    }

    @Override
    public Uint256 getSwapAmount(Address inputAddress, Address outputAddress, Uint256 inputAmount) {
        val trades = trading.getMultiTrades(inputAddress, outputAddress, inputAmount, 2);
        if (trades.isEmpty()) {
            return uint256(0);
        }

        // Pick the lowest impact trade to report most stable price.
        return trades.stream()
                .sorted(Comparator.comparing(Trade::getImpact))
                .map(Trade::getOutputAmount)
                .findFirst().orElse(uint256(0));
    }

    @Override
    public List<Pair> getPairs() {
        return pairs.getPairs();
    }

    @Override
    public Pair getPair(Address address0, Address address1) {
        return pairs.getPair(address0, address1);
    }

    @Override
    public Pair createPair(Address address0, Address address1, Uint256 amount0, Uint256 amount1) {
        router.addLiquidity(address0, address1, amount0, amount1);
        pairs.update();
        return pairs.getPair(address0, address1);
    }

}
