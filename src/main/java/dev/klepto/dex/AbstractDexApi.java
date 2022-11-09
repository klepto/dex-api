package dev.klepto.dex;

import com.google.inject.Injector;
import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.chain.Chain;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import static dev.klepto.kweb3.Web3Error.require;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
@Setter
public abstract class AbstractDexApi implements DexApi {

    private Injector injector;
    private Web3Client client;

    public AbstractDexApi(Injector injector, Web3Client client) {
        this.injector = injector;
        this.client = client;
    }

    @Override
    public void setClient(Web3Client client) {
        val annotation = DexModule.getChainAnnotation(client.getChain());
        require(annotation != null, "Unsupported chain: {}", client.getChain());

        setInjector(injector.createChildInjector(module -> {
            module.bind(Web3Client.class)
                    .annotatedWith(annotation)
                    .toInstance(client);
        }));

        this.client = client;
    }

    @Override
    public Web3Client getClient() {
        return client;
    }

    public Chain getChain() {
        return client.getChain();
    }

}
