package com.example.soochi

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var goodsList:MutableList<GoodItem>
    private lateinit var adapter: GoodsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            val goods=findViewById<EditText>(R.id.goods)
            val enter=findViewById<Button>(R.id.enter)
            val listView=findViewById<ListView>(R.id.goodsList)
            enter.isEnabled=false
            goods.requestFocus()
            goods.addTextChangedListener(object: TextWatcher{
                override fun afterTextChanged(s: Editable?) {
                    enter.isEnabled=!s.isNullOrBlank()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
            goodsList= mutableListOf()
            loadList()
            adapter = GoodsAdapter(
                this,
                goodsList,
                onDelete = { itemToDelete ->
                    goodsList.remove(itemToDelete)
                    adapter.notifyDataSetChanged()
                    saveList()
                },
                onUpdate = {updatedItem->
                    saveList()
                }
            )
            listView.adapter=adapter
            enter.setOnClickListener {
                val item=goods.text.toString().trim()
                val newItem=GoodItem(item,false)
                goodsList.add(newItem)
                adapter.notifyDataSetChanged()
                goods.text.clear()
                saveList()
                Toast.makeText(this,"$item सूची मे जोड़ दी गई है",Toast.LENGTH_SHORT).show()

            }
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun saveList(){
        val prefs=getSharedPreferences("my_prefs", MODE_PRIVATE)
        val editor =prefs.edit()
        val serializedList=goodsList.map { "${it.name}|${it.purchased}" }.toSet()
        editor.putStringSet("goods_list",serializedList)
        editor.apply()
    }
    private fun loadList(){
        val prefs=getSharedPreferences("my_prefs", MODE_PRIVATE)
        val savedList=prefs.getStringSet("goods_list",setOf())?:setOf()
        goodsList.clear()
        for(entry in savedList){
            val parts=entry.split("|")
            if(parts.size==2){
                val name=parts[0]
                val purchased=parts[1].toBoolean()
                goodsList.add(GoodItem(name,purchased))
            }
        }
    }
}