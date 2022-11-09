package dev.klepto.dex.contract;

import dev.klepto.kweb3.contract.Contract;
import dev.klepto.kweb3.contract.Type;
import dev.klepto.kweb3.contract.View;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.Bytes;
import dev.klepto.kweb3.type.sized.Uint256;

import java.util.List;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface Multicall extends Contract {

    @View
    @Type({Uint256.class, Uint256[].class, Bytes[].class})
    List<Object> execute(Uint256 gasLimit, Uint256 sizeLimit, List<Address> addresses, List<Bytes> calls);

}
