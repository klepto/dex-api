package dev.klepto.dex.exchange.trade;

import dev.klepto.dex.exchange.pair.Pair;
import dev.klepto.dex.exchange.PairExchange;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.val;

import java.util.*;

import static dev.klepto.kweb3.util.number.Numeric.decimal;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class Trading {

    private final List<PairExchange> exchanges;
    private final TokenPathFinder pathFinder = new TokenPathFinder();

    public Trading(PairExchange... exchanges) {
        this.exchanges = Arrays.stream(exchanges).toList();
    }

    private void update() {
        exchanges.forEach(exchange -> {
            exchange.getPairs().forEach(pair -> pathFinder.addPath(pair.getAddress0(), pair.getAddress1()));
        });
    }

    public DirectTrade getDirectTrade(Address inputAddress, Address outputAddress, Uint256 inputAmount) {
        val trades = getDirectTrades(inputAddress, outputAddress, inputAmount);
        return trades.isEmpty() ? null : trades.get(0);
    }

    public List<DirectTrade> getDirectTrades(Address inputAddress, Address outputAddress, Uint256 inputAmount) {
        return exchanges.stream().map(exchange -> exchange.getPair(inputAddress, outputAddress))
                .filter(Objects::nonNull)
                .map(pair -> getDirectTrade(pair, inputAddress, inputAmount))
                .sorted(Comparator.comparing(Trade::getOutputAmount).reversed())
                .toList();
    }

    public DirectTrade getDirectTrade(Pair pair, Address inputAddress, Uint256 inputAmount) {
        val outputAddress = pair.getAddress0().equals(inputAddress) ? pair.getAddress1() : pair.getAddress0();
        val inputBalance = pair.getBalance(inputAddress);
        val outputBalance = pair.getBalance(outputAddress);

        val fee = pair.getExchange().getSwapFee();
        val inputWithFee = inputAmount.mul(decimal(1).sub(fee));
        val impact = inputWithFee.toDecimal().div(inputBalance.add(inputWithFee));

        val product = inputBalance.mul(outputBalance);
        val remainingReserve = product.div(inputBalance.add(inputWithFee));
        val outputAmount = outputBalance.sub(remainingReserve);

        return new DirectTrade(pair.getExchange(), inputAddress, outputAddress, inputAmount, outputAmount, impact, fee);
    }

    public MultiTrade getMultiTrade(Address inputAddress, Address outputAddress, Uint256 inputAmount, int depth) {
        val trades = getMultiTrades(inputAddress, outputAddress, inputAmount, depth);
        return trades.isEmpty() ? null : trades.get(0);
    }

    public List<MultiTrade> getMultiTrades(Address inputAddress, Address outputAddress, Uint256 inputAmount, int depth) {
        update();
        val paths = pathFinder.findPaths(inputAddress, outputAddress, depth);
        return paths.stream()
                .map(path -> getMultiTrade(path, inputAmount))
                .sorted(Comparator.comparing(Trade::getOutputAmount).reversed())
                .toList();
    }

    public MultiTrade getMultiTrade(List<Address> path, Uint256 inputAmount) {
        val trades = new LinkedList<DirectTrade>();
        for (var i = 1; i < path.size(); i++) {
            val output = path.get(i);
            val input = trades.isEmpty() ? path.get(0) : trades.getLast().getOutputAddress();
            val amount = trades.isEmpty() ? inputAmount : trades.getLast().getOutputAmount();
            trades.add(getDirectTrade(input, output, amount));
        }
        return new MultiTrade(trades);
    }

}
