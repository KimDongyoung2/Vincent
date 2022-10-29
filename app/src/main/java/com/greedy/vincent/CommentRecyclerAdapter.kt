package com.greedy.vincent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.greedy.sqlite.SqliteHelper
import com.greedy.vincent.databinding.CommentRecyclerBinding
import java.text.SimpleDateFormat

class CommentRecyclerAdapter : RecyclerView.Adapter<CommentRecyclerAdapter.Holder> () {

    var listData = mutableListOf<Comment>()
    var helper: SqliteHelper? = null

    inner class Holder(val binding: CommentRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {

        /* 하나의 Recycler View Holder에는 하나의 Comment Data가 존재 */
        var mComment: Comment? = null

        /* 삭제 버튼 클릭 시 발생 이벤트 */
        init {
            binding.deleteButton.setOnClickListener {
                helper?.deleteMemo(mComment!!)
                listData.remove(mComment)
                notifyDataSetChanged()
            }
        }

        fun setComment(comment: Comment) {
            binding.textNo.text = "${comment.no}"
            binding.textContent.text = comment.content
            val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")
            binding.textDatetime.text = "${sdf.format(comment.datetime)}"

            mComment = comment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = CommentRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val comment = listData[position]
        holder.setComment(comment)
    }
}