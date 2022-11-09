package dev.klepto.dex.exchange.uniswap.croswap;

import dev.klepto.dex.exchange.uniswap.UniPairUpdater;
import dev.klepto.kweb3.type.Address;

import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class CroswapPairUpdater extends UniPairUpdater {

    private static final String[] PAIR_ADDRESSES = {
            "0x95a80c0a90e5d5b208ea3588cf3a92a87206cdd9",
            "0xB0234DB57a9798cE960509387c966D55ED35206b",
            "0x557207972848b56626942CC49C04Be0189CBCAD0",
            "0x80E4e3af19745375087E63569759833f7d0D2213",
            "0x9Ae69645Fbac6fb19A080212B8E74938ab17FbA3",
            "0x38baaA63bdA095f0F5a7243A24A2e914a6952eAB",
            "0x3e21326ACbb7d538C7A5A30D300743337b9B6643",
            "0x41D9171Ca989C16dB8243D340c0A486408Ab28B1",
            "0x3b9cc416C041491db90DEECb3A29BF409D10937B",
            "0x3306c8A9F312d02b65E1e5dAF8e63A28B4554409",
            "0x2A69bb09A413824B3779AA433De333e4B7E96394",
            "0xD3619A0DC331B545cAc23FE648c251827054B55e",
            "0x2E2D5f81aA8c76C1AB0E6502F3ffB656263d0d04",
            "0x3c75e3Dc5d9364B7F137E82B388CeF3D111957c7",
            "0xc69Ece7Ed92F0Efe0e9B5CC479855615BFA12f31",
            "0x0fF8f259C46a1120469269DF466e566Fc09Db25B",
            "0x1b0fFA225C1F9de9d7847ae12db231d3FBf10c63",
            "0x663C804b8803De9B387309edf4f58C712791ffb9",
            "0x8F23a1B8CB2304023D1f24616001A7503c33dAEb",
            "0xBCEC59b0DF69a5A9dc5B0FC21236B3Ed3b53D1D8",
            "0xE2b8ceb533D2e1e39A0dAF73D74B58E21b1E5069",
            "0xa1A06F1f2712cdA81Feef4D1103690832f8c57aD"
    };

    public CroswapPairUpdater(CroswapExchange exchange) {
        super(exchange);
    }

    @Override
    public List<Address> fetchPairAddresses(Address factoryAddress) {
        return Arrays.stream(PAIR_ADDRESSES).map(Address::new).toList();
    }

}
