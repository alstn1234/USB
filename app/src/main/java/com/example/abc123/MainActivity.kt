package com.example.abc123

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Paint
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.abc123.databinding.ActivityMainBinding
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.security.MessageDigest


private var auth: FirebaseAuth? = null

class MainActivity : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    var callbackManager: CallbackManager? = null
    private lateinit var binding: ActivityMainBinding
    private val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.passwordreset.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        binding.MoveSignupbutton.paintFlags = Paint.UNDERLINE_TEXT_FLAG
        val email = findViewById<EditText>(R.id.editTextTextEmailAddress3)
        val password = findViewById<EditText>(R.id.editTextTextPassword3)

        callbackManager = CallbackManager.Factory.create()

        auth = FirebaseAuth.getInstance()

        getAppKeyHash()



        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("64403141523-gsc2p4drfuhh4ssbahee4bnjp1o4kg3c.apps.googleusercontent.com")
            .requestEmail()
            .build()

        var googleSignInClient = GoogleSignIn.getClient(this, gso)
/* 자동로그인
       if (auth.getCurrentUser() != null) {
            val intent = Intent(application, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
*/
        binding.loginbutton.setOnClickListener {

            if (email.text.toString().length == 0 || password.text.toString().length == 0) {
                Toast.makeText(this, "이메일 혹은 비밀번호를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(ContentValues.TAG, "signInWithEmail:success")
                            Toast.makeText(baseContext, "로그인 되었습니다.", Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser

                            val intent = Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            finish()

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(ContentValues.TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "이메일 혹은 비밀번호를 다시 확인해주세요.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }


        }

        binding.MoveSignupbutton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


        binding.passwordreset.setOnClickListener {
            val intent = Intent(this, PasswordResetActivity::class.java)
            startActivity(intent)
        }

        binding.GoogleSignUpButton.setOnClickListener {
            Log.e(ContentValues.TAG, "google")
            val signIntent = googleSignInClient.signInIntent
            startActivityForResult(signIntent, RC_SIGN_IN)
        }

        binding.FacebookSignUpButton.setOnClickListener {
            facebookLogin()
        }

        binding.movetomainbutton.setOnClickListener {

            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    val intent = Intent(this, SNSSignupActivity::class.java)
                    intent.putExtra("uid", auth.currentUser?.uid)
                    startActivity(intent)
                    finish()

                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    Log.d("xxxx", "signInWithCredential:failure", task.exception)
                }
            }
    }
    private fun getAppKeyHash() {
        try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("Hash key", something)
            }
        } catch (e: Exception) {

            Log.e("name not found", e.toString())
        }
    }
    private fun facebookLogin() {
        LoginManager.getInstance()
            .logInWithReadPermissions(this, listOf("public_profile", "email"))

        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {

                override fun onSuccess(result: LoginResult) {
                    handleFacebookAccessToken(result?.accessToken)
                }

                override fun onCancel() {}

                override fun onError(error: FacebookException) {
                }

            })
    }

    fun handleFacebookAccessToken(token: AccessToken?) {
        var credential = FacebookAuthProvider.getCredential(token?.token!!)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    val intent = Intent(this, SNSSignupActivity::class.java)
                    intent.putExtra("uid", auth.currentUser?.uid)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(ContentValues.TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)

            } catch (e: ApiException) {
                Log.w(ContentValues.TAG, "로그인에 실패 하였습니다.", e)
                Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()

            }
        }
    }
}