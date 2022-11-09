package dev.klepto.dex.exchange.trade;

import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import dev.klepto.kweb3.util.number.Decimal;
import lombok.Value;
import lombok.val;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static dev.klepto.kweb3.util.number.Numeric.decimal;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class MultiTrade implements Trade {

    LinkedList<DirectTrade> path;

    public List<Address> getPathAddresses() {
        val addresses = new ArrayList<Address>();
        addresses.add(getInputAddress());
        addresses.addAll(path.stream().map(Trade::getOutputAddress).toList());
        return addresses;
    }

    @Override
    public Address getInputAddress() {
        return path.getFirst().getInputAddress();
    }

    @Override
    public Address getOutputAddress() {
        return path.getLast().getOutputAddress();
    }

    @Override
    public Uint256 getInputAmount() {
        return path.getFirst().getInputAmount();
    }

    @Override
    public Uint256 getOutputAmount() {
        return path.getLast().getOutputAmount();
    }

    @Override
    public Decimal getImpact() {
        return path.stream().map(Trade::getImpact).reduce(Decimal::add).orElse(decimal(0));
    }

    @Override
    public Decimal getFee() {
        return path.getLast().getFee();
    }

}
