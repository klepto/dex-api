package dev.klepto.dex.exchange.trade;

import dev.klepto.dex.exchange.Exchange;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import dev.klepto.kweb3.util.number.Decimal;
import lombok.Value;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Value
public class DirectTrade implements Trade {

    Exchange exchange;
    Address inputAddress;
    Address outputAddress;
    Uint256 inputAmount;
    Uint256 outputAmount;
    Decimal impact;
    Decimal fee;

}
