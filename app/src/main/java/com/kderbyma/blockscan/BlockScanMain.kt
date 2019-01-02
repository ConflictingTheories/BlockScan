package com.kderbyma.blockscan

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.kderbyma.blockscan.GethRPC.GethRPCCore
import kotlinx.android.synthetic.main.activity_block_scan_main.*
import kotlinx.android.synthetic.main.app_bar_block_scan_main.*
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList
import java.util.concurrent.ExecutionException

import com.kderbyma.blockscan.Utils.CallAPI

class BlockScanMain : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var getAccounts: Button? = null;
    private var getStorageAt: Button? = null;
    private var getBalance: Button? = null;
    private var textBox: TextView? = null;
//    private var gRPC: GethRPCCore = GethRPCCore();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_block_scan_main)
        setSupportActionBar(toolbar)

        textBox = findViewById(R.id.textViewOut);
        textBox?.text = "Get Accounts"

        // GET Accounts
        getAccounts = findViewById(R.id.button);
        getAccounts?.setOnClickListener{ view ->
            val gRPC = GethRPCCore()
            val RPCCall = gRPC.eth_account_API()
            makeRPCCall(RPCCall);
        }

        // GET Storage
        getStorageAt = findViewById(R.id.button2);
        getStorageAt?.setOnClickListener{ view ->
            val account = "0x53AD1D1443eB95cfE295EC1E160858864F81bf6F"
            val gRPC = GethRPCCore()
            val RPCCall = gRPC.eth_getStorageAt_API(account);
            makeRPCCall(RPCCall);
        }

        // GET Balance
        getBalance = findViewById(R.id.button8);
        getBalance?.setOnClickListener{ view ->
            val account = "0x53AD1D1443eB95cfE295EC1E160858864F81bf6F"
            val gRPC = GethRPCCore()
            val RPCCall = gRPC.eth_getBalance(account);
            makeRPCCall(RPCCall);
        }


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.block_scan_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                // Handle the camera action
            }
            R.id.nav_wallet -> {

            }
            R.id.nav_settings -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    // Historical Data (Single Selection)
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

                this.textBox?.text = jsonData.toString();
            }else{
    //
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }
}
