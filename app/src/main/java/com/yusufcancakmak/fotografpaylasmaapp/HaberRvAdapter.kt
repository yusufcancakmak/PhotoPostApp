package com.yusufcancakmak.fotografpaylasmaapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.rv_row.view.*

class HaberRvAdapter(val postlist :ArrayList<Post>) :RecyclerView.Adapter<HaberRvAdapter.PostHolder>() {
    class PostHolder(itemView:View) :RecyclerView.ViewHolder(itemView){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val infilater =LayoutInflater.from(parent.context)
        val  view = infilater.inflate(R.layout.rv_row,parent,false)

        return PostHolder(view)
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemView.rv_row_kullanici_email.text =postlist[position].kullaniciemaili
        holder.itemView.rv_yorumtext.text=postlist[position].kullaniciyorum
        Picasso.get().load(postlist[position].gorselurl).into(holder.itemView.rv_row_imageview)
    }

    override fun getItemCount(): Int {
        return postlist.size
    }
}