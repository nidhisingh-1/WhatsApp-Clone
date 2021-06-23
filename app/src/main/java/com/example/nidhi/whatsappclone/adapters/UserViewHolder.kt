package com.example.nidhi.whatsappclone.adapters

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.nidhi.whatsappclone.R
import com.example.nidhi.whatsappclone.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item.view.*

class UserViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    fun bind(user: User, param: (String, String, String) -> Unit) = with(itemView) {
        countTv.isVisible = false
        timeTv.isVisible = false

        titleTv.text = user.name;
        subTitleTv.text = user.status;

        Picasso.get()
            .load(user.thumbImage)
            .placeholder(R.drawable.default_avatar)
            .error(R.drawable.default_avatar)
            .into(userImgView)

    }
}