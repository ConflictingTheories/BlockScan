package com.kderbyma.blockscan

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*
import com.kderbyma.blockscan.GethRPC.GethRPCCore
import kotlinx.android.synthetic.main.activity_block_scan_main.*
import kotlinx.android.synthetic.main.app_bar_block_scan_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.concurrent.ExecutionException

import com.kderbyma.blockscan.Utils.CallAPI
import org.spongycastle.util.encoders.Hex
import java.math.BigInteger
import org.json.JSONArray
import android.R.array
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.ceylonlabs.imageviewpopup.ImagePopup
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.kderbyma.blockscan.R.id.spinner


class BlockScanMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener , AdapterView.OnItemSelectedListener {

    private var getStorageAt: Button? = null;
    private var getBalance: Button? = null;
    private var callContract: Button? = null;
    private var JoinContract: Button? = null;
    private var newAccount: Button? = null;
    private var accountPassword: EditText? = null;
    private var accountAuth: EditText? = null;
    private var gameIdText: EditText? = null;
    private var accountSelect: Spinner? = null;
    private var gameSelect: Spinner? = null;
    private var viewAddress: Button? = null;
    private var RND: Button? = null
    private var ROCK: Button? = null
    private var PAPER: Button? = null
    private var SCISSORS: Button? = null
    private var addressImg:ImageView? = null
    private var qrDrawable:BitmapDrawable? = null

    private var textBox: TextView? = null;

    private var selection = "0"
    var account = "0x53AD1D1443eB95cfE295EC1E160858864F81bf6F"
    val contractAddress = "0xa9Ef479aef4bfd581c28513C1FDd1365B2634783"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_scan_main)
        setSupportActionBar(toolbar)

        textBox = findViewById(R.id.textViewOut)
        textBox?.text = "Get Accounts"

        viewAddress = findViewById(R.id.viewAddress)
        addressImg = findViewById(R.id.qrCode)

        val list = ArrayList<String>()
        val gameslist = ArrayList<String>()

        accountSelect = findViewById(R.id.spinner)
        gameSelect = findViewById(R.id.spinner2)
        try {
            val gRPC = GethRPCCore()
            val AccountsRPCCall = JSONObject(gRPC.eth_account_API().execute().get());
            val filterRPCCall = JSONObject(gRPC.eth_newFilter("LogGameCreated(uint256,address,uint256)",contractAddress).execute().get())
            Log.i("Games Filter Created:", filterRPCCall.toString())
            val GamesRPCCall = JSONObject(gRPC.eth_getFilterChanges(filterRPCCall.getString("result")).execute().get())
            Log.i("GAMES --> ", GamesRPCCall.toString())
            // Arrays
            val jsonAccountsArray = AccountsRPCCall.getJSONArray("result")
            val jsonGamesArray = GamesRPCCall.getJSONArray("result")
            // Accounts
            for (i in 0 until jsonAccountsArray.length()) {
                try {
                    list.add("" + jsonAccountsArray.get(i))
                } catch (e: JSONException) {
                    // TODO Auto-generated catch block
                    e.printStackTrace()
                }
            }
            // Games
            for (i in 0 until jsonGamesArray.length()) {
                    try {
                        var obj = jsonGamesArray.getJSONObject(i)
                        val data = obj.getString("data")
                        Log.i("DATA::",data.toString());
                        val game = data.slice(2..65)
                        Log.i("DATA::",game.toString());

                        val organizer = data.slice(66..129)

                        Log.i("DATA::",organizer.toString());
                        val amount = data.slice(130..193)
                        Log.i("DATA::",amount.toString());

                        gameslist.add("Game:" + BigInteger(game,16).toInt() + " Amount: " + BigInteger(amount,16).divide(BigInteger("1000000000")).toFloat() + " Gwei")
                    } catch (e: JSONException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }
            }
        } catch (e: JSONException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

        val gamesAdapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, gameslist
        );
        gameSelect?.adapter = gamesAdapter

        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item, list
        );
        accountSelect?.setOnItemSelectedListener(this)
        accountSelect?.adapter = adapter

        accountPassword = findViewById(R.id.editText)
        accountAuth = findViewById(R.id.editText2)
        gameIdText = findViewById(R.id.editText3)

        // New Account
        newAccount = findViewById(R.id.button4)
        newAccount?.setOnClickListener{ view ->
            val gRPC = GethRPCCore()
            val password = accountPassword.toString()
            val RPCCall = gRPC.personal_newAccount(password)
            makeRPCCall(RPCCall);
        }

        // GET Storage
        getStorageAt = findViewById(R.id.button2);
        getStorageAt?.setOnClickListener{ view ->
            val gRPC = GethRPCCore()
            account = accountSelect?.getSelectedItem().toString();
            val RPCCall = gRPC.eth_getStorageAt_API(account)//account);
            makeRPCCall(RPCCall);
        }

        // GET Balance
        getBalance = findViewById(R.id.button8);
        getBalance?.setOnClickListener{ view ->
            val gRPC = GethRPCCore()
            account = accountSelect?.getSelectedItem().toString();
            val RPCCall = gRPC.eth_getBalance(account);
            makeRPCCall(RPCCall);
        }

        // ROCK
        RND = findViewById(R.id.ROCK2);
        RND?.setOnClickListener{ view ->
            selection = "0"
            RND?.setBackgroundColor(getResources().getColor(R.color.colorPrimary))
            ROCK?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
            PAPER?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
            SCISSORS?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
        }
        ROCK = findViewById(R.id.ROCK);
        ROCK?.setOnClickListener{ view ->
            selection = "1"
            RND?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
            ROCK?.setBackgroundColor(getResources().getColor(R.color.colorPrimary))
            PAPER?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
            SCISSORS?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
        }
        // PAPER
        PAPER = findViewById(R.id.PAPER);
        PAPER?.setOnClickListener{ view ->
            selection = "2"
            RND?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
            ROCK?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
            PAPER?.setBackgroundColor(getResources().getColor(R.color.colorPrimary))
            SCISSORS?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
        }
        // PAPER
        SCISSORS = findViewById(R.id.SCISSORS);
        SCISSORS?.setOnClickListener{ view ->
            selection = "3"
            RND?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
            ROCK?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
            PAPER?.setBackgroundColor(getResources().getColor(R.color.button_material_light))
            SCISSORS?.setBackgroundColor(getResources().getColor(R.color.colorPrimary))
        }

        // GET Balance
        callContract = findViewById(R.id.button3);
        callContract?.setOnClickListener{ view ->
            val gRPC = GethRPCCore()
            val password:String = accountAuth?.text.toString();
            account = accountSelect?.getSelectedItem().toString();
            val UnlockAccount = gRPC.personal_unlockAccount(account,password)
            val RPCCall = gRPC.eth_call(contractAddress,account,"createRandomGame(uint8)",arrayOf("0"),"100000");
            makeRPCCall(UnlockAccount);
            makeRPCCall(RPCCall);
        }
        JoinContract = findViewById(R.id.button6);
        JoinContract?.setOnClickListener{ view ->
            val gRPC = GethRPCCore()
            account = accountSelect?.getSelectedItem().toString();
            val password:String = accountAuth?.text.toString();
            val UnlockAccount = gRPC.personal_unlockAccount(account,password)

            val gameId = gameIdText?.text.toString()

            val RPCCall = gRPC.eth_call(contractAddress,account,"JoinGame(uint256,uint8)",arrayOf(gameId,selection),"100000" );
            makeRPCCall(UnlockAccount);
            makeRPCCall(RPCCall);
        }

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        val imageView = findViewById<View>(R.id.qrCode) as ImageView
        imageView.setOnClickListener {
            loadPhoto(imageView,400,400)
        }
        viewAddress?.setOnClickListener {
            loadPhoto(imageView,400,400)
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        // use position to know the selected item
        account = accountSelect?.selectedItem.toString();

        var barcodeEncoder = BarcodeEncoder();
        var bitmap = barcodeEncoder.encodeBitmap (account, BarcodeFormat.QR_CODE, 1024, 1024);
        qrDrawable = BitmapDrawable(resources,bitmap)
        addressImg?.setImageDrawable(qrDrawable)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
// Handle navigation view item clicks here.
        when (item.itemId) {
            // Home Page
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_wallet -> {
                // Open Account Page

            }
            R.id.nav_settings -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun loadPhoto(imageView: ImageView, width:Int, height:Int) {

        var tempImageView = imageView;
        var imageDialog = AlertDialog.Builder(this);

        var builder = AlertDialog.Builder(this);
        var inflater = layoutInflater;

//        var dialogView = inflater.inflate(R.layout.custom_full_image_dialog, null);
        var dialogView = inflater.inflate(R.layout.custom_full_image_dialog, findViewById(R.id.layout_root));
//
        builder.setView(dialogView).create().show();

//        var inflator = layoutInflater

        var image:ImageView = dialogView.findViewById(R.id.fullimage);
        image.setImageDrawable(tempImageView.drawable);
//
//        imageDialog.create();
//        imageDialog.show();
    }

    fun makeRPCCall(accountCall: CallAPI?) {
        var call: String?
        // Fetch Data
        try {
            call = accountCall!!.execute().get()
        } catch (e: InterruptedException) {
            e.printStackTrace()
            call = "InteruptedError;"
        } catch (e: ExecutionException) {
            e.printStackTrace()
            call = "Execution Error;"
        }

        // Parse Data and Chart
        try {
            // Data Object
            if(call != null) {
                val jsonData = JSONObject(call)
                Log.i("JSON",jsonData.toString())

                textBox?.text = jsonData.toString();
            }else{
                //
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

}
