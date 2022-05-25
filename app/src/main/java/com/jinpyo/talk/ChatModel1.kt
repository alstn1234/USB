package com.jinpyo.talk

class ChatModel1(val users: HashMap<String, Boolean> = HashMap(),
                 val comments : HashMap<String, Comment> = HashMap()){
    class Comment(val uid: String? = null, val chatimg: String? = null, val time: String? = null)
}