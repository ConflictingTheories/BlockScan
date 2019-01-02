package com.kderbyma.blockscan.GethRPC

import com.kderbyma.blockscan.Utils.CallAPI
import org.json.JSONException
import org.json.JSONObject

/**
 * Created by kderbyma on 23/09/17.
 */

class GethRPCCore {

    // ==== API CALL METHODS ====
    // WEB3
    fun web3_clientVersion_API(): CallAPI{
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
