package dev.klepto.dex.price;

import dev.klepto.kweb3.contract.impl.Erc20;
import dev.klepto.kweb3.util.number.Decimal;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface PriceService {

    Decimal getPrice(Erc20 token);

}
