package service;
import model.Transaction;
import repository.TransactionRepository;

public class TransactionService {

    TransactionRepository repo = new TransactionRepository();

    public void addTransaction(Transaction t) {

        // Validation 1
        if (t.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        // Validation 2
        if (t.getCategory() == null || t.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Category is required");
        }

        repo.save(t);
    }
    public java.util.List<Transaction> getAllTransactions() {
        return repo.loadTransactions();
    }
}