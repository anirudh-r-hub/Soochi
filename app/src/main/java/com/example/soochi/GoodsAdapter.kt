package com.example.soochi

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView

class GoodsAdapter (
    private val context:Context,
    private val goodsList:MutableList<GoodItem>,
    private val onDelete:(GoodItem)-> Unit,
    private val onUpdate:(GoodItem)->Unit
    ): ArrayAdapter<GoodItem>(context,0,goodsList){
        override fun getView(position: Int, convertView: View?, parent: ViewGroup):View{
            val view=convertView?: LayoutInflater.from(context)
                .inflate(R.layout.items_goods,parent,false)
            val itemText=view.findViewById<TextView>(R.id.item_text)
            val purchasedBtn=view.findViewById<ImageButton>(R.id.mark_button)
            val deleteBtn=view.findViewById<ImageButton>(R.id.delete_button)
            val item=goodsList[position]
            itemText.text=item.name
            if(item.purchased){
                itemText.paintFlags=itemText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                itemText.setTextColor(context.getColor(android.R.color.holo_red_dark))
            }else{
                itemText.paintFlags=itemText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                itemText.setTextColor(context.getColor(android.R.color.white))
            }

            deleteBtn.setOnClickListener{
                onDelete(item)
            }
            purchasedBtn.setOnClickListener{
                item.purchased=!item.purchased
                if(item.purchased){
                    itemText.paintFlags=itemText.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    itemText.setTextColor(context.getColor(android.R.color.holo_red_dark))
                }else{
                    itemText.paintFlags=itemText.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    itemText.setTextColor(context.getColor(android.R.color.white))
                }
                onUpdate(item)
                notifyDataSetChanged()
            }
            return view
        }
    }

