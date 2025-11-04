public class Transaction {
    private final String transactionId;
    private final String sourceAccountId;
    private final String destinationAccountId;
    private final double amount;
    private final boolean isSuccessful;

    public Transaction(String transactionId, String sourceAccountId, String destinationAccountId, double amount, boolean isSuccessful) {
        this.transactionId = transactionId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.isSuccessful = isSuccessful;
    }

    public String getTransactionId() {
        return transactionId;
    }
    public String getSourceAccountId() {
        return sourceAccountId;
    }
    public String getDestinationAccountId() {
        return destinationAccountId;
    }
    public double getAmount() {
        return amount;
    }
    public boolean isSuccessful() {
        return isSuccessful;
    }


}