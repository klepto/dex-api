package dev.klepto.dex.contract;

import dev.klepto.kweb3.contract.*;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.sized.Uint256;
import lombok.val;

import java.time.Instant;
import java.util.List;

import static dev.klepto.kweb3.type.sized.Uint256.uint256;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public interface UniRouter extends Contract {

    @View("factory")
    Address getFactoryAddress();

    @View("WETH")
    Address getNativeTokenAddress();

    @View("getAmountsOut")
    @Type(Uint256[].class)
    Uint256[] getOuputAmounts(Uint256 inputAmount, List<Address> path);

    @Transaction("addLiquidity")
    ContractResponse addLiquidity(
            Address token0,
            Address token1,
            Uint256 amount0,
            Uint256 amount1,
            Uint256 minAmount0,
            Uint256 minAmount1,
            Address receiverAddress,
            Uint256 deadline
    );

    @Transaction("swapExactTokensForTokensSupportingFeeOnTransferTokens")
    ContractResponse swapWithFee(
            Uint256 inputAmount,
            Uint256 outputAmount,
            List<Address> path,
            Address receiverAddress,
            Uint256 deadline
    );

    /* Utility functions */
    default Uint256 getOuputAmount(Uint256 inputAmount, List<Address> path) {
        val amounts = getOuputAmounts(inputAmount, path);

        if (amounts.length == 0) {
            return uint256(0);
        }

        return amounts[amounts.length - 1];
    }

    default ContractResponse swapWithFee(Uint256 inputAmount,
                                         Uint256 outputAmount,
                                         List<Address> path) {
        return swapWithFee(inputAmount, outputAmount, path, getClient().getAddress(), createDeadline());
    }

    default ContractResponse addLiquidity(Address token0, Address token1, Uint256 amount0, Uint256 amount1) {
        return addLiquidity(
                token0, token1, amount0, amount1, uint256(0), uint256(0), getClient().getAddress(), createDeadline()
        );
    }

    static Uint256 createDeadline() {
        return uint256(Instant.now().getEpochSecond() + 600); // 10 minutes
    }

}