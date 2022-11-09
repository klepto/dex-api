package dev.klepto.dex.exchange.trade;

import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import dev.klepto.kweb3.util.number.Decimal;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Trade {

    Address getInputAddress();

    Address getOutputAddress();

    Uint256 getInputAmount();

    Uint256 getOutputAmount();

    Decimal getImpact();

    Decimal getFee();

}