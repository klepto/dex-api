package dev.klepto.dex.exchange.pair;

import com.google.common.base.Stopwatch;
import dev.klepto.kweb3.type.Address;
import lombok.val;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
public class PairCache {

    private final Duration updateRate;
    private final PairUpdater updater;
    private final Stopwatch updateTimer = Stopwatch.createUnstarted();
    private final Map<String, Pair> pairs = new HashMap<>();

    public PairCache(Duration updateRate, PairUpdater updater) {
        this.updateRate = updateRate;
        this.updater = updater;
    }

    public void update() {
        pairs.clear();
        updater.fetchPairs().forEach(pair -> {
            pairs.put(getPairKey(pair.getAddress0(), pair.getAddress1()), pair);
        });
    }

    public void checkUpdate() {
        if (updateTimer.isRunning() && updateTimer.elapsed(TimeUnit.MILLISECONDS) < updateRate.toMillis()) {
            return;
        }

        update();
        updateTimer.reset();
        updateTimer.start();
    }

    public List<Pair> getPairs() {
        checkUpdate();
        return new ArrayList<>(pairs.values());
    }

    public Pair getPair(Address address0, Address address1) {
        checkUpdate();
        return pairs.get(getPairKey(address0, address1));
    }

    public static String getPairKey(Address address0, Address address1) {
        val smallAddress = address0.lessThan(address1) ? address0 : address1;
        val bigAddress = address0.lessThan(address1) ? address1 : address0;
        return smallAddress.toString() + bigAddress.toString();
    }

}
