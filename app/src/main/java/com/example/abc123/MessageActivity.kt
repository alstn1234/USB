package com.example.abc123

//import kotlinx.android.synthetic.main.item_image.*


import android.annotation.SuppressLint
import android.content.DialogInterface.BUTTON1
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.abc123.databinding.ActivityMessageBinding
import com.example.test28.DataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.sql.Date

class MessageActivity : AppCompatActivity() {
    private val fireDatabase = FirebaseDatabase.getInstance().reference
    private var chatRoomUid: String? = null
    private var destinationUid: String? = null
    private var uid: String? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var auth: FirebaseAuth
    lateinit var database: DatabaseReference
    var img_val = false
    var img_url: String = ""
    lateinit var curTime: String
    lateinit var imageView: ImageView
    private lateinit var binding: ActivityMessageBinding

    private var realUri: Uri? = null

    //이미지 등록
    private var imageUri: Uri? = null //이미지 등록
    private val getContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                imageUri = result.data?.data //이미지 뷰를 바꿈


                img_add(imageUri)
            } else {
                Log.d("이미지", "실패")
            }
        }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMessageBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        imageView = findViewById<ImageView>(R.id.messageActivity_ImageView)
        val editText = findViewById<TextView>(R.id.messageActivity_editText)
        val btnCamera = findViewById<ImageButton>(R.id.cammera_on_Btn)
        val btnGallery = findViewById<ImageView>(R.id.loadImage_Btn)
        val location = findViewById<ImageView>(R.id.imageButton3)
        //내갤러리 접근
        btnGallery.setOnClickListener {
            val intentImage = Intent(Intent.ACTION_PICK)

            intentImage.type = MediaStore.Images.Media.CONTENT_TYPE
            getContent.launch(intentImage)

        }

        location.setOnClickListener {
            val intent3=Intent(Intent.ACTION_VIEW,Uri.parse("https://search.naver.com/search.naver?where=nexearch&sm=tab_jum&query=%EB%B9%A0%EB%A5%B8%EA%B8%B8%EC%B0%BE%EA%B8%B0"))
        startActivity(intent3)
        }


        @SuppressLint("SimpleDateFormat")
        //메세지를 보낸 시간
        val time = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat("MM월dd일 hh:mm")
        curTime = dateFormat.format(Date(time)).toString()
        destinationUid = intent.getStringExtra("destinationUid")
        uid = Firebase.auth.currentUser?.uid.toString()
        recyclerView = findViewById(R.id.messageActivity_recyclerview)




        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (editText.length() > 0) {
                    btnGallery.visibility = View.GONE
                    location.visibility = View.GONE
                    btnCamera.visibility = View.GONE
                } else {
                    btnGallery.visibility = View.VISIBLE
                    location.visibility = View.VISIBLE
                    btnCamera.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })



        imageView.setOnClickListener {
            Log.d("클릭 시 dest", "$destinationUid")
            val chatModel = ChatModel()
            chatModel.users.put(uid.toString(), true)
            chatModel.users.put(destinationUid!!, true)


            //이미지 전송 코딩


            val comment = ChatModel.Comment(uid, editText.text.toString(), curTime)
            if (chatRoomUid == null) {
                imageView.isEnabled = false
                fireDatabase.child("chatrooms").push().setValue(chatModel)
                    .addOnSuccessListener {
                        //채팅방 생성
                        checkChatRoom()
                        //메세지 보내기

                        Handler().postDelayed({
                            println(chatRoomUid)
                            fireDatabase.child("chatrooms")
                                .child(chatRoomUid.toString())
                                .child("comments").push().setValue(comment)
                            binding.messageActivityEditText.setText("")
                        }, 1000L)
                        Log.d("chatUidNull dest", "$destinationUid")
                    }
            } else {
                fireDatabase.child("chatrooms").child(chatRoomUid.toString())
                    .child("comments")
                    .push().setValue(comment)
                binding.messageActivityEditText.setText("")
                Log.d("chatUidNotNull dest", "$destinationUid")
            }
        }
        checkChatRoom()

        btnCamera.setOnClickListener {
            val takePictureIntent =
                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePictureIntent, BUTTON1)


        }


    }

    //카메라 접근


    private fun checkChatRoom() {
        fireDatabase.child("chatrooms").orderByChild("users/$uid").equalTo(true)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (item in snapshot.children) {
                        println(item)
                        val chatModel = item.getValue(ChatModel::class.java)
                        if (chatModel?.users!!.containsKey(destinationUid)) {
                            chatRoomUid = item.key
                            binding.messageActivityImageView.isEnabled = true
                            recyclerView?.layoutManager =
                                LinearLayoutManager(this@MessageActivity)
                            recyclerView?.adapter = RecyclerViewAdapter()
                        }
                    }
                }
            })
    }

    inner class RecyclerViewAdapter
        :
        RecyclerView.Adapter<RecyclerViewAdapter.MessageViewHolder>() {
        private val comments = ArrayList<ChatModel.Comment>()

        private var friend: DataModel? = null

        init {
            fireDatabase.child("User").child(destinationUid.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {

                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                        friend = snapshot.getValue(DataModel::class.java)
                        binding.messageActivityTextViewTopName.text = friend?.nickname
                        getMessageList()
                    }
                })
        }

        fun getMessageList() {
            fireDatabase.child("chatrooms").child(chatRoomUid.toString())
                .child("comments")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        comments.clear()
                        for (data in snapshot.children) {
                            val item = data.getValue(ChatModel.Comment::class.java)
                            comments.add(item!!)
                            println(comments)
                        }
                        notifyDataSetChanged()
                        //메세지를 보낼 시 화면을 맨 밑으로 내림
                        recyclerView?.scrollToPosition(comments.size - 1)
                    }
                })
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MessageViewHolder {
            val view: View =
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message, parent, false)
            return MessageViewHolder(view)
        }


        //t사진전송좀 하자
        @SuppressLint("RtlHardcoded")
        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {


            holder.textView_time.text = comments[position].time
            if (comments[position].uid.equals(uid)) { // 본인 채팅
                if (comments[position].message!!.length <= 50) {
                    holder.textView_message.setBackgroundResource(R.drawable.rightbubble)
                }
                holder.textView_name.visibility = View.INVISIBLE
                holder.layout_destination.visibility = View.INVISIBLE
                holder.layout_main.gravity = Gravity.RIGHT
            } else { // 상대방 채팅
                Glide.with(holder.itemView.context)
                    .load(friend?.profileImageUrl)
                    .apply(RequestOptions().circleCrop())
                    .into(holder.imageView_profile)
                holder.textView_name.text = friend?.name
                holder.layout_destination.visibility = View.VISIBLE
                holder.textView_name.visibility = View.VISIBLE
                if (comments[position].message!!.length <= 50) {
                    holder.textView_message.setBackgroundResource(R.drawable.leftbubble)
                }
                holder.layout_main.gravity = Gravity.LEFT
            }

            if (comments[position].message!!.length > 50) {


                Glide.with(holder.itemView.context).load(comments[position].message)
                    .placeholder(R.drawable.img_loading).into(holder.imgg)
                holder.imgg.getLayoutParams().width = 350
                holder.imgg.getLayoutParams().height = 350
                holder.textView_message.getLayoutParams().width = 0
                holder.textView_message.getLayoutParams().height = 0

//채팅방 이미지 클릭시 풀화면으로 이미지를 보여줌
                holder.imgg.setOnClickListener {
                    val intent22 = Intent(holder.itemView.context, ImageActivity::class.java)
                    intent22.putExtra("imageUri", comments[position].message)
                    intent22.putExtra("img_num", "0")
                    ContextCompat.startActivity(holder.itemView.context, intent22, null)
                }


            } else {
                holder.textView_message.textSize = 20F
                holder.textView_message.text = comments[position].message

            }


        }

        override fun getItemViewType(position: Int): Int {
            return position
        }


        inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imgg: ImageView = view.findViewById(R.id.iimmaaggee)
            val textView_message: TextView =
                view.findViewById(R.id.messageItem_textView_message)
            val textView_name: TextView =
                view.findViewById(R.id.messageItem_textview_name)
            val imageView_profile: ImageView =
                view.findViewById(R.id.messageItem_imageview_profile)
            val layout_destination: LinearLayout =
                view.findViewById(R.id.messageItem_layout_destination)
            val layout_main: LinearLayout =
                view.findViewById(R.id.messageItem_linearlayout_main)
            val textView_time: TextView =
                view.findViewById(R.id.messageItem_textView_time)

        }

        override fun getItemCount(): Int {
            return comments.size
        }
    }


    //갤러리에서 사진 가져오기
    fun img_add(uri: Uri?) {
        val user = Firebase.auth.currentUser
        val userId = user?.uid
        val userIdSt = userId.toString()
        FirebaseStorage.getInstance()
            .reference.child("userchatimage").child("$userIdSt/photo").putFile(uri!!)
            .addOnSuccessListener {
                FirebaseStorage.getInstance().reference.child("userchatimage")
                    .child("$userIdSt/photo").downloadUrl
                    .addOnSuccessListener {
                        Log.d("클릭", "적용")
                        val photoUri: Uri = it
                        img_url = photoUri.toString()
                        img_val = true
                        println("$photoUri")
                        var comment = ChatModel.Comment("", "", "")
                        comment = ChatModel.Comment(uid, photoUri.toString(), curTime)
                        if (chatRoomUid == null) {
                            imageView.isEnabled = false
                            fireDatabase.child("chatrooms").push().setValue(comment)
                                .addOnSuccessListener {
                                    //채팅방 생성
                                    checkChatRoom()
                                    //메세지 보내기
                                    Handler().postDelayed({
                                        fireDatabase.child("chatrooms")
                                            .child(chatRoomUid.toString())
                                            .child("comments").push().setValue(comment)
                                        binding.messageActivityEditText.text = null
                                    }, 1000L)
                                }
                        } else {
                            fireDatabase.child("chatrooms")
                                .child(chatRoomUid.toString())
                                .child("comments")
                                .push().setValue(comment)
                            binding.messageActivityEditText.text = null
                            Log.d("chatUidNotNull dest", "$destinationUid")
                        }
                    }


            }
    }


}
