package com.example.authorizationapp.Activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.authorizationapp.R
import com.example.authorizationapp.SubFolder.CurrencyType
import com.example.authorizationapp.SubFolder.Subscription
import com.example.authorizationapp.SubFolder.SubscriptionType
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class AddActivity: AppCompatActivity() {
    lateinit var txtTitle: EditText
    lateinit var cmbType: Spinner
    lateinit var cmbCurrency: Spinner
    lateinit var txtCost:EditText
    lateinit var txtFirstDateView: TextView
    lateinit var txtLastDateView: TextView
    lateinit var btnAdd : ImageButton
    lateinit var firstDateButton: Button
    lateinit var lastDateButton: Button
    private lateinit var myDatabase: DatabaseReference
    private var sub_key: String = "Subscription"

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        init()
        btnAdd.setOnClickListener(addButtonListener)

        firstDateButton.setOnClickListener(firstButtonListener)
        lastDateButton.setOnClickListener(lastButtonListener)
    }

    fun init(){
        txtCost = findViewById(R.id.txtCost)
        txtTitle = findViewById(R.id.txtTitle)
        txtFirstDateView = findViewById(R.id.txtFirstDateView)
        txtLastDateView = findViewById(R.id.txtLastDateView)
        cmbCurrency = findViewById(R.id.cmbCurrency)
        cmbType = findViewById(R.id.cmbType)
        btnAdd = findViewById(R.id.btnAdd)
        firstDateButton = findViewById(R.id.firstDateButton)
        lastDateButton = findViewById(R.id.lastDateButton)
        myDatabase= FirebaseDatabase.getInstance().getReference(sub_key);
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private var addButtonListener: View.OnClickListener = View.OnClickListener {
        addSub();
    }

    private var firstButtonListener: View.OnClickListener = View.OnClickListener {
        txtFirstDateView.text = getString(R.string.date_first)
        selectFirstDate(it);
    }

    private var lastButtonListener: View.OnClickListener = View.OnClickListener {
        txtLastDateView.text = getString(R.string.date_last)
        selectLastDate(it);
    }

    @SuppressLint("SetTextI18n")
    private fun selectFirstDate(view: View){
        val inflater = layoutInflater
        val builder = AlertDialog.Builder(view.context)
        val dialoglayout: View = inflater.inflate(R.layout.dialog_datepicker, null)
        builder.setView(dialoglayout)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        val dialogButton = dialoglayout.findViewById<Button>(R.id.selectDateBtn)
        val dialogDatePicker = dialoglayout.findViewById<DatePicker>(R.id.datePicker)

        dialogButton.setOnClickListener {
            var day : String = when(dialogDatePicker.dayOfMonth){
                1, 2, 3 , 4, 5, 6, 7, 8, 9 ->{
                    "0${dialogDatePicker.dayOfMonth}"
                }
                else->{
                    "${dialogDatePicker.dayOfMonth}"
                }
            }
            var month: String = "01"
            month = when(dialogDatePicker.month){
                0, 1, 2, 3, 4, 5, 6, 7, 8 -> {
                    "0${dialogDatePicker.month+1}"
                }
                else ->{
                    "${dialogDatePicker.month+1}"
                }
            }
            txtFirstDateView.text =  "${day}.${month}.${dialogDatePicker.year}"
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun selectLastDate(view: View){
        val inflater = layoutInflater
        val builder = AlertDialog.Builder(view.context)
        val dialoglayout: View = inflater.inflate(R.layout.dialog_datepicker, null)
        builder.setView(dialoglayout)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_background)

        val dialogButton = dialoglayout.findViewById<Button>(R.id.selectDateBtn)
        val dialogDatePicker = dialoglayout.findViewById<DatePicker>(R.id.datePicker)

        dialogButton.setOnClickListener {
            var day : String = when(dialogDatePicker.dayOfMonth){
                1, 2, 3 , 4, 5, 6, 7, 8, 9 ->{
                    "0${dialogDatePicker.dayOfMonth}"
                }
                else->{
                    "${dialogDatePicker.dayOfMonth}"
                }
            }
            var month : String = when(dialogDatePicker.month){
                0, 1, 2, 3, 4, 5, 6, 7, 8 -> {
                    "0${dialogDatePicker.month+1}"
                }
                else ->{
                    "${dialogDatePicker.month+1}"
                }
            }
            txtLastDateView.text =  "${day}.${month}.${dialogDatePicker.year}"
            dialog.dismiss()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addSub() {
        var id: String? = myDatabase.key
        var sub: Subscription = Subscription()
        when(cmbCurrency?.selectedItem.toString()){
            "USD" -> sub.currency = CurrencyType.USD
            "RUB" -> sub.currency = CurrencyType.RUB
            "CNY" -> sub.currency = CurrencyType.CNY
        }
        when(cmbType?.selectedItemPosition){
            0 -> sub.type = SubscriptionType.TEMPORARY
            1 -> sub.type = SubscriptionType.PERMANENT
        }


        if(txtTitle?.text?.length == 0){
            Toast.makeText(this, getString(R.string.titleIsNull), Toast.LENGTH_SHORT).show();
            return
        }
        sub.title = txtTitle?.text.toString()

        if(txtCost?.text?.length==0 ||  txtCost?.text?.toString()?.toInt()!! < 1){
            Toast.makeText(this, getString(R.string.costIsNull), Toast.LENGTH_SHORT).show();
            return
        }
        var cost = txtCost.text.toString().toInt()
        sub.cost = cost
        var formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

        if(txtFirstDateView.text == getString(R.string.date_first)){
            Toast.makeText(this, getString(R.string.firstDateIsNull), Toast.LENGTH_SHORT).show();
            return
        }
       sub.firstDate = txtFirstDateView.text as String
        if(txtLastDateView.text == getString(R.string.date_last)){
            Toast.makeText(this, getString(R.string.lastDateIsNull), Toast.LENGTH_SHORT).show();
            return
        }
        if(LocalDate.parse(txtLastDateView.text as String, formatter) <= LocalDate.parse(txtFirstDateView.text as String, formatter)){
            Toast.makeText(this, getString(R.string.firstDateMoreThanLastDate), Toast.LENGTH_SHORT).show();
            return
        }

        print(LocalDate.parse(txtFirstDateView.text as String, formatter))
        print(LocalDate.parse(txtLastDateView.text as String, formatter))

        sub.lastDate = txtLastDateView.text as String
        sub.id = id.toString()

        txtLastDateView.text = getString(R.string.date_last)
        txtFirstDateView.text = getString(R.string.date_first)
        txtTitle.setText("")
        txtCost.setText("")
        Toast.makeText(this, getString(R.string.addSuccess), Toast.LENGTH_SHORT).show();

        myDatabase.push().setValue(sub)
        Log.e("DataBaseAdd", "Запись добавлена")
    }
}