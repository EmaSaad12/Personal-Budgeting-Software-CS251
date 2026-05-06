
import java.io.FileWriter;

public class TransactionRepository {

    public void save(Transaction t) {
        try {
            FileWriter fw = new FileWriter("transactions.txt", true);

            fw.write(
                t.getId() + "," +
                t.getType() + "," +
                t.getAmount() + "," +
                t.getCategory() + "," +
                t.getUserId() + ","+
                t.getDate() + "\n"
            );

            fw.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}