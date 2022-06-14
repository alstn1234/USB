package com.example.abc123

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

<<<<<<< HEAD
=======
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)


>>>>>>> 7d16a551fe09ae78110c88ca11c363485ecabea6

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
<<<<<<< HEAD
        },2000)
=======
        },6000)
>>>>>>> 7d16a551fe09ae78110c88ca11c363485ecabea6

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    }

