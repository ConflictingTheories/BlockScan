package com.kderbyma.blockscan.GethRPC

import android.telecom.Call
import android.util.Log
import com.kderbyma.blockscan.Utils.CallAPI
import org.json.JSONException
import org.json.JSONObject
import org.spongycastle.util.encoders.Hex
import org.web3j.protocol.Web3j
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import org.web3j.protocol.core.methods.response.Web3ClientVersion
import java.math.BigInteger


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

    fun personal_newAccount(password: String) : CallAPI{
        return CallAPI("POST",BASEURL,"{\"jsonrpc\":\"2.0\", \"method\": \"personal_newAccount\", \"params\": [\""+password+"\"], \"id\": 1}")
    }

    fun personal_unlockAccount(address:String, password: String) : CallAPI{
        return CallAPI("POST",BASEURL,"{\"jsonrpc\":\"2.0\", \"method\": \"personal_unlockAccount\", \"params\": [\""+address+"\",\""+password+"\", 300], \"id\": 1}")
    }

    fun eth_getStorageAt_API(address: String) : CallAPI{
        return CallAPI("POST",BASEURL,"{\"jsonrpc\":\"2.0\", \"method\": \"eth_getStorageAt\", \"params\": [\""+address+"\", \"0x0\", \"latest\"], \"id\": 1}")
    }

    fun eth_getBalance(address: String) : CallAPI{
        return CallAPI("POST",BASEURL,"{\"jsonrpc\":\"2.0\", \"method\": \"eth_getBalance\", \"params\": [\""+address+"\", \"latest\"], \"id\": 1}")
    }

    fun web3_sha3(signature: String) : CallAPI{
        Log.i("TEXT", signature)
        val hexString = Hex.toHexString(signature.toByteArray())
        return CallAPI("POST", BASEURL,"{\"jsonrpc\":\"2.0\",\"method\":\"web3_sha3\",\"params\":[\"0x"+hexString+"\"],\"id\":64}")
    }

    // NEEDS EDITS
    fun eth_newFilter(filter:String, contractAddress: String): CallAPI{
        val topic=JSONObject(web3_sha3(filter).execute().get()).getString("result")

        return CallAPI("POST", BASEURL, "{\"jsonrpc\":\"2.0\",\"method\":\"eth_newFilter\",\"params\":[{\n" +
                "  \"fromBlock\": \"0x"+BigInteger("7022573",10).toString(16)+"\",\n" +
                "  \"toBlock\": \"latest\",\n" +
                "  \"address\": \""+contractAddress+"\",\n" +
                "  \"topics\": [\""+topic+"\"]" +
//                "  \"topics\": []" +
                "}],\"id\":73}")
    }

    fun eth_getFilterChanges(filterId: String): CallAPI{
        return CallAPI("POST", BASEURL, "{\"jsonrpc\":\"2.0\",\"method\":\"eth_getFilterLogs\",\"params\":[\""+filterId+"\"],\"id\":73}")
    }

    // Calls Contract Method with Params
    fun eth_call(contractAddress: String, address: String, signature: String, params: Array<String>, amount:String) : CallAPI{
        val data = JSONObject(web3_sha3(signature).execute().get()).getString("result")
        val formattedData = data.slice(2..9)
        var dataStr = formattedData;
        var cnt = -1
        var len = params.size
        while(cnt++ < len-1){
            val myInt = BigInteger(dataStr,16).shiftLeft(256 )
            val combinedInt = myInt or BigInteger(params[cnt])
            dataStr = combinedInt.toString(16)

            Log.i("INT::", formattedData+"::"+myInt.toString(16))
            Log.i("FORMAT::",data+"::"+formattedData)
            Log.i("COMB::",combinedInt.toString(16))

        }
        var amountCalc = BigInteger(amount).multiply(BigInteger("1000000000")).toString(16)

        // 0.0001 ether
        return CallAPI("POST", BASEURL,
            "{ \"jsonrpc\": \"2.0\", \"id\" : 1, \"method\": \"eth_sendTransaction\", \"params\" : [ { \"to\" : \""+contractAddress+"\", \"from\" : \""+ address +"\", \"data\" : \"0x" + dataStr + "\","+
                    "\"gas\": \"0x376c0\"," +
                    "\"value\": \"0x"+amountCalc+"\" } ]}")
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
