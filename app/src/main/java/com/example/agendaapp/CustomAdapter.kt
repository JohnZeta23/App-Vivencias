import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.agendaapp.ItemsViewModel
import com.example.agendaapp.VivenciaActivity
import com.example.agendaapp.databinding.CardViewDesignBinding

class CustomAdapter(private val mList: List<ItemsViewModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = CardViewDesignBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        holder.textView.text = ItemsViewModel.text
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: CardViewDesignBinding) : RecyclerView.ViewHolder(ItemView.root) {
        val textView: TextView = ItemView.textView
        val intent = Intent(itemView.context, VivenciaActivity::class.java)

        init {
            itemView.setOnClickListener{v : View ->
                val position : Int = absoluteAdapterPosition
                intent.putExtra("VIVENCIA",position)
                itemView.context.startActivity(intent)
            }
        }
    }
}