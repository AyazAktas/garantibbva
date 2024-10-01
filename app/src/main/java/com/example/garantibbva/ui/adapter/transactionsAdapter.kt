import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.garantibbva.R
import com.example.garantibbva.data.entity.Customer
import com.example.garantibbva.data.entity.Transaction
import com.example.garantibbva.databinding.CardViewTransactionsBinding

class TransactionAdapter(
    private val transactions: List<Transaction>,
    private val customer: Customer
) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    inner class TransactionViewHolder(private val binding: CardViewTransactionsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(transaction: Transaction) {
            // Transaction date - day, month, and year
            val dateParts = transaction.date!!.split(" ")
            binding.textViewTransactionDateDay.text = dateParts[0] // Gün
            binding.textViewTransactionDateMandY.text = "${dateParts[1]} ${dateParts[2]}" // Ay ve Yıl
            binding.textViewTransactionHour.text = transaction.transactionHour // Saat

            binding.textViewTransactionType.text = transaction.transactionType
            binding.textViewTransactionReceiverName.text = transaction.receiverName
            binding.textViewTransactionId.text = transaction.transactionId

            // Transaction amount (positive or negative)
           if (transaction.receiverIban == customer.ibanNumber) {
                binding.imageViewSenderOrReceiver.setImageResource(R.drawable.baseline_circle_24)
            } else {
                binding.imageViewSenderOrReceiver.setImageResource(R.drawable.baseline_circle_grey_24)
            }

            binding.textViewTransactionAmount.text = transaction.amount.toString()

            val balanceAfterTransaction= customer.customersBalance!!-transaction.amount!!

            // Balance after transaction
            binding.textViewBalanceAfterTransaction.text =
                "İşlem Sonu Bakiye: ${balanceAfterTransaction} TL"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = CardViewTransactionsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.bind(transaction)
    }

    override fun getItemCount(): Int = transactions.size
}
