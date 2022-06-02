package com.cybergod.oyeetaxi.web_socket


import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.cybergod.oyeetaxi.databinding.ActivityWsBinding
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject



@AndroidEntryPoint
class WSActivity : AppCompatActivity() {

    private lateinit var binding:ActivityWsBinding

    val data: MutableLiveData<String> = MutableLiveData<String>()

    @Inject
    lateinit var socket : Socket


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupSocketConnection()

        with(binding){

            data.observe(this@WSActivity, Observer {
                binding.textView.text = binding.textView.text.toString()+ "\n" + it

            })
          button.setOnClickListener {

              sendMessage()
              data.postValue("test")


          }


        }


    }

    fun setupSocketConnection(){


        socket.on("chatevent", onNewMessage)
        //socket.on("on typing", onTyping)
        socket.on("connect", Emitter.Listener {
            Toast.makeText(this@WSActivity, "connect $it",Toast.LENGTH_SHORT).show()
        })

        socket.on("disconnect",Emitter.Listener {
            Toast.makeText(this@WSActivity,"disconnect $it",Toast.LENGTH_SHORT).show()
        });

        socket.connect()

    }


    fun sendMessage() {
        Log.i(TAG, "sendMessage: ")
        val message ="Android Mensaje"
        val user = "Android User"

        val jsonObject = JSONObject()
        try {
            jsonObject.put("userName", user)
            jsonObject.put("message", message)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        socket.emit("chatevent", jsonObject)


        Log.i(TAG, "chatevent")
    }

    val TAG = "WSActivity"

    var onNewMessage =
        Emitter.Listener { args ->
            runOnUiThread(Runnable {

                Log.i(TAG, "run: ")
                Log.i(TAG, "run: " + args.size)
                val data = args[0] as JSONObject
                val username: String
                val message: String
                val id: String
                try {
                    username = data.getString("userName")
                    message = data.getString("message")
                    id = data.getString("uniqueId")
                    Log.i(TAG, "run: $username$message$id")
                    val format = "$username:$message:$id"
                    Log.i(TAG, "run:4 ")
//                    messageAdapter.add(format)
                    Toast.makeText(this@WSActivity, "onNewMessage $format",Toast.LENGTH_SHORT).show()
                    Log.i(TAG, "run:5 ")
                } catch (e: Exception) {
                    return@Runnable
                }
            })
        }

    var onNewUser = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            val length = args.size
            if (length == 0) {
                return@Runnable
            }
            //Here i'm getting weird error..................///////run :1 and run: 0
            Log.i(TAG, "run: ")
            Log.i(TAG, "run: " + args.size)
            var username = args[0].toString()
            try {
                val `object` = JSONObject(username)
                username = `object`.getString("userName")
            } catch (e: JSONException) {
                e.printStackTrace()
            }

//            messageAdapter.add(format)
//            messageListView.smoothScrollToPosition(0)
//            messageListView.scrollTo(0, messageAdapter.getCount() - 1)
            Log.i(TAG, "run: $username")
        })
    }



}

