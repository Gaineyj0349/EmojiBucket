package com.gainwise.emojibucket

import android.content.*
import android.graphics.Color
import android.os.Bundle
import android.support.text.emoji.EmojiCompat
import android.support.text.emoji.bundled.BundledEmojiCompatConfig
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.preference.PreferenceManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import br.com.joinersa.oooalertdialog.Animation
import br.com.joinersa.oooalertdialog.OnClickListener
import br.com.joinersa.oooalertdialog.OoOAlertDialog
import com.gainwise.seed.Vitals.FirstRunHandler
import com.gainwise.seed.Vitals.FirstRunner
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.quick_copy_block.*
import osmandroid.project_basics.Task
import spencerstudios.com.bungeelib.Bungee
import spencerstudios.com.fab_toast.FabToast
import java.io.IOException


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener{

    private val filteredList = mutableListOf<Emoji>()
    private var subGroupList = ArrayList<String>()

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {


        filteredList.clear()
        listOfEmojiBlocks.clear()

        if(p0!!.length>0) {

            for (i in 0 until masterList.size)
                if (masterList.get(i).description.toLowerCase().contains(p0!!.toLowerCase())) {
                    filteredList.add(masterList.get(i))
                }
            Log.d("EMOJI221", "" + filteredList.size)


            for (i in 0 until subGroupList.size) {


                val groupedList =
                    filteredList.asSequence().filter { it.subGroup.equals(subGroupList.get(i)) }.toMutableList() as ArrayList<Emoji>
                if (groupedList.size > 0) {
                    listOfEmojiBlocks.add(EmojiBlock(this, groupedList, false))
                }

            }

            adapter = MainAdapter(listOfEmojiBlocks, this)
            mainRV.adapter = adapter
        }else{
            appendEmojiBlocks()
        }

        return true
    }


    var masterList = ArrayList<Emoji>()
    var groupedRecentList = ArrayList<Emoji>()
    var listOfEmojiBlocks = ArrayList<EmojiBlock>()
    lateinit var adapter: MainAdapter
     var recentBlock: EmojiBlock? = null
    var recentList = ArrayList<String>()
    private var mOptionsMenu: Menu? = null
    lateinit var prefs: SharedPreferences
    lateinit var  prefsManager: SharedPreferences.OnSharedPreferenceChangeListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)

        setContentView(R.layout.activity_main)



        val obj = object : FirstRunner{
            override fun execute() {
                         OoOAlertDialog.Builder(this@MainActivity)
                        .setTitle("Welcome!")
                        .setMessage("Scroll up & down to view all of the categories. Scroll left and right to view all Emojis!" +
                                " There are important details in the information page (in the options) on why some Emoji's may not be visible in all devices, would you like to go to there now? ")
                        .setImage(R.mipmap.icon)
                        .setAnimation(Animation.POP)
                             .setPositiveButton("YES",  object : OnClickListener{
                                 override fun onClick() {
                                     startActivity(Intent(this@MainActivity,Information::class.java))
                                 }

                             })
                        .setNegativeButton("NO",  object : OnClickListener{
                            override fun onClick() {
                                FabToast.makeText(this@MainActivity, "Awesome!!!!", FabToast.LENGTH_LONG,
                                    FabToast.SUCCESS, FabToast.POSITION_DEFAULT).show()
                            }

                        })
                        .build();
            }
        }

        val fHandler = FirstRunHandler(this, obj)

            val s = SpannableString(title)

        s.setSpan(ForegroundColorSpan(Color.BLACK), 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar!!.setTitle(s)

        if(quickCopyMode(this)){
            et_block.visibility = View.GONE
        }else{
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            et_block.visibility = View.VISIBLE
        }


        establishLists()
        appendEmojiBlocks()
        initSPListener()
        assignRecentList()



    }



    private fun initSPListener() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefsManager = SharedPreferences.OnSharedPreferenceChangeListener() { prefs, key ->
            assignRecentList()
        if(quickCopyMode(this)){

        }else{

        }
            Log.d("EMOJI91", "size of listofemojiblocks ${listOfEmojiBlocks.size}" )

        }
    }

    fun copyFab(v: View){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("emoji", emojiET.text.toString().trim())
        clipboard.primaryClip = clip
        toastShort(this, emojiET.text.toString().trim())
    }
    fun clearFab(v: View){
        emojiET.setText("")
    }

    override fun onResume() {
        super.onResume()
        prefs.registerOnSharedPreferenceChangeListener(prefsManager);

    }

    override fun onDestroy() {
        super.onDestroy()
    }


    private fun appendEmojiBlocks() {


         subGroupList= getSubGroupList()
        Log.d("EMOJI22", "" + subGroupList.size)

        for (i in 0 until subGroupList.size) {
            Log.d("EMOJI22", "" + subGroupList.get(i))

            val groupedList =
                masterList.asSequence().filter { it.subGroup.equals(subGroupList.get(i)) }.toMutableList() as ArrayList<Emoji>
            listOfEmojiBlocks.add(EmojiBlock(this, groupedList, false))
        }

        Log.d("EMOJI22", "" + listOfEmojiBlocks.size)
        val layoutManager = LinearLayoutManager(this);
        val itemDecorator = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider)!!)
        mainRV.layoutManager = layoutManager
        mainRV.addItemDecoration(itemDecorator)
         adapter = MainAdapter(listOfEmojiBlocks, this)
        mainRV.adapter = adapter


    }

    private fun assignRecentList() {
        recentList = getRecentsList(this)
        groupedRecentList.clear()

        val tempList =
            masterList.asSequence().filter { it.emojiString in recentList }.toMutableList() as ArrayList<Emoji>

        //sort recents here
        for (i in 0 until recentList.size){
            for (j in 0 until tempList.size){
               if(tempList.get(j).emojiString.equals(recentList.get(i))){
                   val tempEmoji = Emoji(
                       tempList.get(j).group,
                       tempList.get(j).subGroup,
                       tempList.get(j).emojiString,
                       tempList.get(j).description
                   )
                   groupedRecentList.add(tempEmoji)
                   break;
               }
            }

        }

        if(recentBlock == null){
            recentBlock = EmojiBlock(this, groupedRecentList, true)
            view_for_recents.addView(recentBlock)

        }else{
            recentBlock?.notifyChange()
        }

    }

    private fun getSubGroupList(): ArrayList<String> {
        var text = ""
        try {
            val inputStream = assets.open("sub_groups.txt")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            text = String(buffer)
        } catch (e: IOException) {
        }

        val gson = Gson()
        val type = object : TypeToken<List<String>>() {
        }.type
        return gson.fromJson(text, type)

    }

    private fun establishLists() {
        val emojiJson = grabEmojiJson()
        val gson = Gson()
        val type = object : TypeToken<List<Emoji>>() {
        }.type
        masterList = gson.fromJson(emojiJson, type)
    }

    private fun grabEmojiJson(): String {
        var text = ""
        try {
            val inputStream = assets.open("json.txt")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            text = String(buffer)
        } catch (e: IOException) {
        }
        return text;
    }

    override fun onPause() {
        super.onPause()
        Bungee.windmill(this)
        prefs.unregisterOnSharedPreferenceChangeListener(prefsManager)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)

        val searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(getResources().getColor(R.color.black));
        searchEditText.setHintTextColor(getResources().getColor(R.color.black));


        val searchMagIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button) as ImageView
        searchMagIcon.setImageResource(R.drawable.ic_search_black_24dp)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.bookmark_menu -> {
                startActivity(Intent(this,SettingsActivity::class.java))
                return true
            }
            R.id.information -> {
                startActivity(Intent(this,Information::class.java))
                return true
            }
            R.id.funGif -> {
                Task.MoreApps(this, "GainWise")
                return true
            }
            R.id.rate -> {
                Task.RateApp(this,"com.gainwise.emojibucket" )
                return true
            }
            R.id.share -> {
                Task.ShareApp(this,"com.gainwise.emojibucket", "Emoji Bucket", "Quick copy and paste Emojis" )
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun appendET(string: String){
        emojiET.append(string)
    }
}




