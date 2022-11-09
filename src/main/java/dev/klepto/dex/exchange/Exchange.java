package dev.klepto.dex.exchange;

import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import dev.klepto.kweb3.util.number.Decimal;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Exchange {

    Web3Client getClient();

    Uint256 swap(Address inputAddress, Address outputAddress, Uint256 inputAmount, Decimal slippage);

    Uint256 getSwapAmount(Address inputAddress, Address outputAddress, Uint256 inputAmount);

    Decimal getSwapFee();

}
