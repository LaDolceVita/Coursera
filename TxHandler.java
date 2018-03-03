import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class TxHandler {

    private UTXOPool mUtxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. This should make a copy of utxoPool by using the UTXOPool(UTXOPool uPool)
     * constructor.
     */
    public TxHandler(UTXOPool utxoPool) {
        if (utxoPool == null) {
            throw new IllegalArgumentException("UTXOPool argument cannot be null!");
        }
        mUtxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool,
     * (2) the signatures on each input of {@code tx} are valid,
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     * values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // IMPLEMENT THIS
        return (tx != null && tx.getInputs().size() != 0) &&
                areAllOutputsInPoolNoDoubleSpend(tx) && isBalanceOK(tx);
    }

    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
        ArrayList<UTXO> claimedOutputs = new ArrayList<>();
        ArrayList<UTXO> tmpClaimedOutputs = new ArrayList<>();
        HashMap<UTXO, Transaction.Output> utxoPoolIncrement = new HashMap<>();
        HashSet<Transaction> handledTransactions = new HashSet<>();
        //check if every tx is valid and no double spend attempt is detected
        outerLoop:
        for (Transaction tx : possibleTxs) {
            tmpClaimedOutputs.clear();
            if (isValidTx(tx)) {
                for (Transaction.Input input : tx.getInputs()) {
                    UTXO tmpUTXO = new UTXO(input.prevTxHash, input.outputIndex);
                    //check for cross-tx double spend
                    if (claimedOutputs.contains(tmpUTXO)) continue outerLoop;
                    tmpClaimedOutputs.add(tmpUTXO);
                }
                claimedOutputs.addAll(tmpClaimedOutputs);
                for (int i = 0; i < tx.numOutputs(); i++) {
                    UTXO utxo = new UTXO(tx.getHash(), i);
                    utxoPoolIncrement.put(utxo, tx.getOutput(i));
                }
                handledTransactions.add(tx);
            }
        }
        //remove all consumed UTXO's
        for (UTXO utxo : claimedOutputs) {
            mUtxoPool.removeUTXO(utxo);
        }
        //add all new UTXO's
        for (UTXO utxo : utxoPoolIncrement.keySet()) {
            mUtxoPool.addUTXO(utxo, utxoPoolIncrement.get(utxo));
        }
        Transaction[] resultArray = new Transaction[handledTransactions.size()];
        handledTransactions.toArray(resultArray);
        return resultArray;
    }

    private boolean areAllOutputsInPoolNoDoubleSpend(Transaction tx) {
        //(1) all inputs claimed by {@code tx} are in the current UTXO pool
        //(2) the signatures on each input of {@code tx} are valid
        //(3) no UTXO is claimed multiple times by {@code tx}
        ArrayList<Transaction.Input> inputs = tx.getInputs();
        if (inputs.size() > mUtxoPool.getAllUTXO().size()) return false;
        ArrayList<UTXO> claimedInputs = new ArrayList<>();
        for (int i = 0; i < inputs.size(); i++) {
            Transaction.Input input = inputs.get(i);
            byte[] signature = input.signature;
            UTXO tmpUTXO = new UTXO(input.prevTxHash, input.outputIndex);
            Transaction.Output output = mUtxoPool.getTxOutput(tmpUTXO);
            if (output == null) return false;
            PublicKey publicKey = output.address;
            if (!mUtxoPool.getAllUTXO().contains(tmpUTXO) || claimedInputs.contains(tmpUTXO) ||
                    !Crypto.verifySignature(publicKey, tx.getRawDataToSign(i), signature)) {
                return false;
            }
            claimedInputs.add(tmpUTXO);
        }
        return true;
    }

    private boolean isBalanceOK(Transaction tx) {
        //4) all of {@code tx}s output values are non-negative
        //(5) the sum of {@code tx}s input values is greater than or equal to the sum of its output values
        double totalOutputSum = 0;
        double totalInputSum = 0;

        for (Transaction.Output output : tx.getOutputs()) {
            totalOutputSum += output.value;
            if (output.value < 0) return false;
        }

        for (Transaction.Input input : tx.getInputs()) {
            UTXO tmpUTXO = new UTXO(input.prevTxHash, input.outputIndex);
            Transaction.Output output = mUtxoPool.getTxOutput(tmpUTXO);
            if (output == null) return false;
            totalInputSum += output.value;
        }

        return Double.compare(totalInputSum, totalOutputSum) >= 0;
    }
}
