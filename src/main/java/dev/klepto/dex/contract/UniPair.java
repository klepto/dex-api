package dev.klepto.dex.contract;

import dev.klepto.kweb3.contract.Contract;
import dev.klepto.kweb3.contract.View;
import dev.klepto.kweb3.type.Address;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface UniPair extends Contract {

    @View("token0")
    Address getAddress0();

    @View("token1")
    Address getAddress1();
    
}
