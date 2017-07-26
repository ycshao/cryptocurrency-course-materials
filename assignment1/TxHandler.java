import java.util.ArrayList;
import java.util.HashSet;

public class TxHandler {

    private UTXOPool uPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        uPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        double totalInput = 0;
        HashSet<UTXO> utxosSeen = new HashSet<UTXO>();
        for (int i = 0; i < tx.getInputs().size(); i++) {
            Transaction.Input in = tx.getInput(i);
            UTXO ut = new UTXO(in.prevTxHash, in.outputIndex);
            // (3) no UTXO is claimed multiple times by {@code tx}
            if (!utxosSeen.add(ut))
                return false;
            Transaction.Output op = uPool.getTxOutput(ut);
            // (1) all outputs claimed by {@code tx} are in the current UTXO pool
            if (op == null)
                return false;
            // (2) the signatures on each input of {@code tx} are valid
            if (!Crypto.verifySignature(op.address, tx.getRawDataToSign(i), in.signature))
                return false;
            totalInput += op.value;
        }
        double totalOutput = 0;
        ArrayList<Transaction.Output> txOutputs = tx.getOutputs();
        for (Transaction.Output op : txOutputs) {
            // 4) all of {@code tx}s output values are non-negative
            if (op.value < 0)
                return false;
            totalOutput += op.value;
        }
        // (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output value
        return (totalInput >= totalOutput);
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        ArrayList<Transaction> successTxs = new ArrayList<>();



        Transaction[] result = new Transaction[successTxs.size()];
        for (int i = 0; i < successTxs.size(); i++)
            result[i] = successTxs.get(i);
        return result;
    }


}
