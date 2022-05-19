package com.example.authorizationapp.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.authorizationapp.R
import com.example.authorizationapp.RecyclerView.CustomRecyclerAdapter
import com.example.authorizationapp.SubFolder.CurrencyType
import com.example.authorizationapp.SubFolder.Subscription
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    private lateinit var addButton : ImageButton;
    private lateinit var myDatabase: DatabaseReference
    private var sub_key: String = "Subscription"
    private lateinit var subs: ArrayList<Subscription>
    private lateinit var txtRub: TextView
    private lateinit var txtDol: TextView
    private lateinit var txtCyn: TextView

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        setupActionBar()
        getDataFromDB()
        addButton.setOnClickListener(addButtonListener)

    }

    private fun init(){
        auth = Firebase.auth
        addButton = findViewById(R.id.addButton)
        subs = ArrayList()
        myDatabase= FirebaseDatabase.getInstance().getReference(sub_key);
        txtRub = findViewById(R.id.txtRub)
        txtDol = findViewById(R.id.txtDol)
        txtCyn = findViewById(R.id.txtCyn)
    }

    @SuppressLint("SetTextI18n")
    private fun analytics(){
        var rubCount :Int = 0
        var dolCount: Int = 0
        var cynCount: Int = 0
        for(sub in subs){
            when(sub.currency){
                CurrencyType.RUB->{
                    rubCount+=sub.cost
                }
                CurrencyType.USD->{
                    dolCount+=sub.cost
                }
                CurrencyType.CNY->{
                    cynCount+=sub.cost
                }
            }
        }
        txtRub.text = "$rubCount ${getString(R.string.txtRubCount)}"
        txtCyn.text = "$cynCount ${getString(R.string.txtCynCount)}"
        txtDol.text = "$dolCount ${getString(R.string.txtDolCount)}"
    }

    private fun getDataFromDB(){
        var valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (subs.size >0)
                    subs.clear()
                for(ds:DataSnapshot  in dataSnapshot.children){
                    var sub: Subscription? = ds.getValue(Subscription::class.java)
                    assert( sub != null)
                    sub?.let { subs.add(it) }
                }
                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                val linearLayoutManager = LinearLayoutManager(applicationContext)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView.layoutManager = linearLayoutManager

                recyclerView.adapter = CustomRecyclerAdapter(subs!!, applicationContext)
                analytics()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        myDatabase.addValueEventListener(valueEventListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.sign_out){
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupActionBar(){
        val actionBar = supportActionBar
        Thread{
            val userBitMap = Picasso.get().load(auth.currentUser?.photoUrl).get()
            val dIcon = BitmapDrawable(resources, userBitMap)
            runOnUiThread {
                actionBar?.setDisplayHomeAsUpEnabled(true)
                actionBar?.setHomeAsUpIndicator(dIcon)
                actionBar?.title = auth.currentUser?.displayName
            }
        }.start()

    }

    fun addSub() {
        var intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    private var addButtonListener: View.OnClickListener = View.OnClickListener {
        addSub();
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
//preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            true
        } else super.onKeyDown(keyCode, event)
    }
}



