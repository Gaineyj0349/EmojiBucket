@file:JvmName("Utils")
package com.gainwise.emojibucket

import android.content.Context
import android.support.v7.preference.PreferenceManager
import android.util.Log
import android.widget.Toast
import spencerstudios.com.jetdblib.JetDB

data class Emoji(val group: String, val subGroup: String, val emojiString: String, val description: String){}

fun toastShort(context: Context, message: String){
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun getRecentsList(context: Context): ArrayList<String>{
    var list = JetDB.getStringList(context, "recents")
    printListDebug2(list)
    return list;
}
fun saveTempList(context: Context, listObject: ArrayList<String>){
    printListDebug(listObject)

    JetDB.putStringList(context, listObject, "recents");
}

fun needsToSaveRecent(context: Context, emoji: String): Boolean {
  if(getRecentsList(context).contains(emoji)){
      return false
  }else{
      return true
  }
}

fun quickCopyMode(context: Context): Boolean {
            val on = PreferenceManager.getDefaultSharedPreferences(context).getString(SettingsFragment.EMOJIQUICKCOPY, "ON");
            if (on.equals("ON")){
                return true;
            }else{
                return false;
            }
}

//fun setCopyMessage(context: Context, message: String) {
//    val edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE).edit()
//    edit.putString("copyMessage", message)
//    edit.commit()
//}
//fun getCopyMessage(context: Context) : String {
//    val edit = context.getSharedPreferences("DEFAULTS", Context.MODE_PRIVATE)
//    return edit.getString("copyMessage", "")
//}

fun printListDebug(list : ArrayList<String>) {
    for (i in 0 until list.size) {
        Log.d("EMOJI99", list.get(i))
    }
}
fun printListDebug2(list : ArrayList<String>) {
        for (i in 0 until list.size) {
            Log.d("EMOJI98", list.get(i))
        }
}


