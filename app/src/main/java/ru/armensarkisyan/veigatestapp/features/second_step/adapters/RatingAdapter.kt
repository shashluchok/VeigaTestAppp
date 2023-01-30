package ru.armensarkisyan.veigatestapp.features.second_step.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.armensarkisyan.veigatestapp.common.data.net.responses.Raiting
import ru.armensarkisyan.veigatestapp.databinding.ItemRatingBinding

class RatingAdapter :
    RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    class RatingViewHolder(val binding: ItemRatingBinding) : RecyclerView.ViewHolder(binding.root)

    private val ratings: MutableList<Raiting> = mutableListOf()
    private var mContext: Context? = null


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mContext = recyclerView.context
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RatingAdapter.RatingViewHolder {
        val binding = ItemRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.binding.apply {
            mContext?.let {
                Glide.with(it).load(ratings[position].image).into(cardRatingImageIv)
            }
            cardRatingTextTv.text = ratings[position].title
        }

    }

    override fun getItemCount(): Int = ratings.size

    fun setData(data: List<Raiting>) {
        ratings.clear()
        ratings.addAll(data)
        notifyDataSetChanged()
    }

}