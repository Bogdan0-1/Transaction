public class Logger{
    public void logTransaction(Transaction transaction){
        String status = transaction.isSuccessful() ? "SUCCESS" : "FAILURE";
        System.out.println("LOG: Transaction " + status + " for amount " + transaction.getAmount());
    }
}