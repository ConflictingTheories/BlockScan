package com.kderbyma.blockscan.GethRPC

import android.util.Log
import com.kderbyma.blockscan.Utils.CallAPI
import org.json.JSONException
import org.json.JSONObject
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.core.methods.response.Web3ClientVersion




/**
 * Created by kderbyma on 23/09/17.
 */

class GethRPCCore {

    var web3 = Web3jFactory.build( HttpService("https://geth.kderbyma.com"))

    // ==== API CALL METHODS ====
    // WEB3
    fun web3_clientVersion_API(): CallAPI{
        val web3ClientVersion = web3.web3ClientVersion().sendAsync().get()
        val clientVersion = web3ClientVersion.web3ClientVersion
        Log.i("WEB3 Result", clientVersion)
        return CallAPI("POST", BASEURL, "{\"jsonrpc\":\"2.0\",\"method\":\"web3_clientVersion\",\"params\":[],\"id\":1}")
    }
    // NET
    fun net_version_API() : CallAPI{
        return CallAPI("POST", BASEURL, "{\"jsonrpc\":\"2.0\",\"method\":\"net_version\",\"params\":[],\"id\":1}")
    }
    fun net_peerCount_API(): CallAPI{
        return CallAPI("POST", BASEURL, "{\"jsonrpc\":\"2.0\",\"method\":\"net_peerCount\",\"params\":[],\"id\":1}")
    }
    // ETH
    fun eth_coinbase_API() : CallAPI{
        return CallAPI("POST", BASEURL, "{\"jsonrpc\":\"2.0\",\"method\":\"eth_coinbase\",\"params\":[],\"id\":1}")
    }

    fun eth_account_API() : CallAPI{
        return CallAPI("POST", BASEURL, "{\"jsonrpc\":\"2.0\",\"method\":\"eth_accounts\",\"params\":[],\"id\":1}")
    }

    fun eth_getStorageAt_API(address: String) : CallAPI{
        return CallAPI("POST",BASEURL,"{\"jsonrpc\":\"2.0\", \"method\": \"eth_getStorageAt\", \"params\": [\""+address+"\", \"0x0\", \"latest\"], \"id\": 1}")
    }

    fun eth_getBalance(address: String) : CallAPI{
        return CallAPI("POST",BASEURL,"{\"jsonrpc\":\"2.0\", \"method\": \"eth_getBalance\", \"params\": [\""+address+"\", \"latest\"], \"id\": 1}")
    }


    fun eth_call(address: String) : CallAPI{
        return CallAPI("POST", BASEURL, "{\"jsonrpc\":\"2.0\"," +
                " \"method\":\"eth_call\",\"params\":[{" +
                "  \"from\": \"" + address +"\"," +
                "  \"to\": \"0xbad08dc8e9adbf0a081c02f214b19d80bc2a9d5a\"," +
                "\"gas\": \"0x76c0\","+
                "\"gasPrice\": \"0x9184e72a000\","+
                "  \"value\": \"0x9184E72A000\"," +
                "  \"data\": \"0x124504540000000000000000000000000000000000000000000000000000000000000000\"" +
                "}], \"id\":\"1\"}");
    }


    companion object {

        // ==== MAIN API ENDPOINT ====
        private val BASEURL = "https://geth.kderbyma.com/"

        // -- Parse Data
        @Throws(JSONException::class)
        fun parseData(JSON: JSONObject): GethRPCData {
            return GethRPCData(JSON)
        }
    }

}
