package dev.klepto.dex.contract;

import dev.klepto.kweb3.contract.Contract;
import dev.klepto.kweb3.contract.Type;
import dev.klepto.kweb3.contract.View;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface UniFactory extends Contract {

    @View("allPairs")
    Address getPairAddress(@Type(Uint256.class) int index);

    @View("allPairsLength")
    @Type(Uint256.class)
    int getPairCount();

}
