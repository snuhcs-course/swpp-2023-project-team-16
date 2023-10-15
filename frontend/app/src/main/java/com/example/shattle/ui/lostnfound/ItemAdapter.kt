import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shattle.R
import com.example.shattle.ui.lostnfound.LostnfoundFragment

class ItemAdapter(private val items: List<LostnfoundFragment.LostItem>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemImageView: ImageView = itemView.findViewById(R.id.itemImage)
        val itemDescription: TextView = itemView.findViewById(R.id.itemDescription)
        // Add other views as needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_lost_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.itemName.text = item.name
        holder.itemDescription.text = item.description

        // Load the image into the itemImage view using Glide
        Glide.with(holder.itemView)
            .load(item.imageUrl)
            .placeholder(R.drawable.img_loading) // Placeholder image while loading
            .error(R.drawable.img_error) // Image to display if loading fails
            .into(holder.itemImageView)
    }

    override fun getItemCount() = items.size
}
