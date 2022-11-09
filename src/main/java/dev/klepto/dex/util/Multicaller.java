package dev.klepto.dex.util;

import com.google.common.collect.Lists;
import dev.klepto.dex.contract.Multicall;
import dev.klepto.kweb3.Web3Client;
import dev.klepto.kweb3.Web3Request;
import dev.klepto.kweb3.type.Address;
import dev.klepto.kweb3.type.Bytes;
import dev.klepto.kweb3.type.sized.Uint256;
import dev.klepto.kweb3.util.number.Numeric;
import lombok.Getter;
import lombok.Value;
import lombok.val;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static dev.klepto.kweb3.Web3Error.require;
import static dev.klepto.kweb3.type.sized.Uint256.uint256;

/**
 * @author <a href="http://github.com/klepto">Augustinas R.</a>
 */
@Getter
public class Multicaller {

    private static final int DEFAULT_BATCH_SIZE = 2048;
    private static final long DEFAULT_GAS_LIMIT = 1_000_000;
    private static final long DEFAULT_SIZE_LIMIT = 1_000_000;

    private final Web3Client client;
    private final Multicall contract;
    private final List<BatchCall> batchCalls = new ArrayList<>();

    public Multicaller(Web3Client client) {
        this.client = client;
        this.contract = client.getContract(Multicall.class, "0xF33d2E47001ddbD7b71301363f68F57e318Bd4c8");
    }

    public void queue(Runnable runnable, Consumer<List<Object>> consumer) {
        val callCount = contract.getClient().getLogs(runnable).size();
        require(callCount == 1, "Multicall must contain no more or no less than 1 call.");
        batchCalls.add(new BatchCall(runnable, consumer));
    }

    public void execute() {
        execute(DEFAULT_BATCH_SIZE);
    }

    public void execute(int batchSize) {
        execute(batchSize, DEFAULT_GAS_LIMIT,  DEFAULT_SIZE_LIMIT);
    }

    public void execute(int batchSize, long gasLimit, long sizeLimit) {
        val partitions = Lists.partition(batchCalls, batchSize);
        partitions.forEach(calls -> execute(calls, gasLimit,  sizeLimit));
        batchCalls.clear();
    }

    private void execute(List<BatchCall> batchCalls, long gasLimit, long sizeLimit) {
        val requests = new ArrayList<Web3Request>();
        val addresses = new ArrayList<Address>();
        val calls = new ArrayList<Bytes>();

        for (val batchCall : batchCalls) {
            val request = client.getLogs(batchCall.getRunnable()).get(0);
            val abi = client.abiEncode(request);
            val address = request.getAddress();
            val call = Numeric.toBytes(abi);

            requests.add(request);
            addresses.add(address);
            calls.add(call);
        }

        val response = contract.execute(uint256(gasLimit), uint256(sizeLimit), addresses, calls);
        val statuses = (Uint256[]) response.get(1);
        val results = (Bytes[]) response.get(2);

        for (var i = 0; i < statuses.length; i++) {
            val status = statuses[i];
            val consumer = batchCalls.get(i).getConsumer();
            if (!status.equalTo(1)) {
                consumer.accept(Collections.emptyList());
                continue;
            }

            val request = requests.get(i);
            val abi = Numeric.toHex(results[i]);
            val result = contract.getClient().abiDecode(abi, request.getFunction().getReturnTypes());
            consumer.accept(result);
        }
    }

    @Value
    public class BatchCall {
        Runnable runnable;
        Consumer<List<Object>> consumer;
    }

}
