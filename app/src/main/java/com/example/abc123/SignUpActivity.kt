package com.example.abc123

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.abc123.databinding.ActivitySignUpBinding
import com.example.test28.DataModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var database: DatabaseReference

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private val TAG: String = "CreateAccount"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding  = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        database= Firebase.database.reference

        val email = binding.signupemail
        val password = binding.signuppassword
        val passwordcheck = binding.signuppasswordcheck


            binding.Signupbutton.setOnClickListener {
                if (email.text.toString().length == 0 || password.text.toString().length == 0) {
                    Toast.makeText(this, "이메일 혹은 비밀번호를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
                } else {
                    if (password.text.toString() == passwordcheck.text.toString()) {
                        auth.createUserWithEmailAndPassword(
                            email.text.toString(),
                            password.text.toString()
                        )
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {

                                    Log.d(TAG, "회원등록을 성공 하였습니다")
                                    val user = auth.currentUser


                                    var uid: String = auth.currentUser?.uid.toString()
                                    val dataInput = DataModel(
                                        binding.signupemail.text.toString(),
                                        binding.signuppassword.text.toString(),
                                        binding.signupname.text.toString(),
                                        binding.signupbirth.text.toString(),

                                        binding.signupnickname.text.toString(),
                                        uid
                                    )
                                    database.child("User").child(uid).setValue(dataInput)
                                    database.child("User").child(uid).child("profileImageUrl").setValue("https://firebasestorage.googleapis.com/v0/b/abc123-34300.appspot.com/o/profile%2Fbasicprofile.png?alt=media&token=bdd5a488-aa8d-4bbd-b17f-49e51af4af12")
                                    database.child("User").child(uid).child("major").setValue("")
                                    database.child("User").child(uid).child("school").setValue("")
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)

                                    finish()

                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                    Toast.makeText(
                                        baseContext, "회원등록을 실패 하였습니다",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    email?.setText("")
                                    password?.setText("")
                                    passwordcheck?.setText("")
                                    email.requestFocus()
                                }
                            }

                    } else {
                        Toast.makeText(
                            baseContext, "비밀번호가 일치 하지 않습니다",
                            Toast.LENGTH_SHORT
                        ).show()

                        password?.setText("")
                        passwordcheck?.setText("")

                        email.requestFocus()
                    }
                }




            binding.cancelbutton.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}

