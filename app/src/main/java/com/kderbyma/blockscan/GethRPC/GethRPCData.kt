package com.kderbyma.blockscan.GethRPC

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by kderbyma on 07/10/17.
 */

class GethRPCData {

    private lateinit var Obj: JSONObject
    private lateinit var Arr: JSONArray
    var type: String? = null

    public lateinit var AssetList: ArrayList<RPCAsset>

    // Constructor
    constructor(JSON: JSONObject) {
        try {
            this.Obj = JSON
            this.AssetList = ArrayList()
            val keys = Obj.keys()
            while (keys.hasNext()) {
                val key = keys.next() as String

                if (this.Obj.get(key) is JSONArray) {
                    val groupArray = this.Obj.getJSONArray(key)
                    for (i in 0 until groupArray.length()) {
                        val asset = groupArray.getJSONObject(i)

                        val assetObj = RPCAsset(asset)
                        this.AssetList.add(assetObj)
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    constructor(JSON: JSONArray, Type: String) {

        this.Arr = JSON
        this.AssetList = ArrayList()

        val assetObj = RPCAsset(this.Arr, Type)
        this.AssetList.add(assetObj)
    }

    // ASSET Class
    inner class RPCAsset {
        var id: String? = null;
        var jsonrpc: String? = null;
        var result: String? = null;


        // GENERATOR ASSET EXTRACTION
        internal constructor(Asset: JSONObject) {
            try {
                this.id = Asset.getString("id")
                this.jsonrpc = Asset.getString("jsonrpc")
                this.result = Asset.getString("result")

            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

        // Type Based
        internal constructor(ReportArr: JSONArray, Type: String) {
            try {
                for (i in 0 until ReportArr.length()) {
                    val Report = ReportArr.getJSONObject(i)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }

    }

}
