package dev.klepto.dex;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.chain.Chain;
import dev.klepto.kweb3.chain.Chains;

import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class DexModule extends AbstractModule {

    @Qualifier
    @Retention(RUNTIME)
    public @interface Cronos {
    }

    @Provides
    @Cronos
    static Web3Client getCronosClient() {
        return Web3Client.createClient(Chains.CRONOS);
    }

    static Class<? extends Annotation> getChainAnnotation(Chain chain) {
        if (chain.getChainId() == Chains.CRONOS.getChainId()) {
            return Cronos.class;
        }
        return null;
    }

}
