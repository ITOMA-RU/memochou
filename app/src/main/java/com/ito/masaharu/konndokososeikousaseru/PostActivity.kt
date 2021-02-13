package com.ito.masaharu.konndokososeikousaseru

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_post.*
import java.util.*

class PostActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        val CHANNEL_ID = "channel_id"
        val channel_name = "channel_name"
        val channel_description = "channel_description "

        ///APIレベルに応じてチャネルを作成
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = channel_name
            val descriptionText = channel_description
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            /// チャネルを登録
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        /// 通知の中身
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)    /// 表示されるアイコン
            .setContentTitle("メモ帳")                  /// 通知タイトル
            .setContentText("忘れないための通知を残しました")           /// 通知コンテンツ
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)   /// 通知の優先度


        var notificationId = 0   /// notificationID
        bellButton.setOnClickListener {
            /// ボタンを押して通知を表示
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
                notificationId += 1
            }
        }
        saveButton.setOnClickListener {
            // 起動する対象をクラスオブジェクトで指定する
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            create(titleText.text.toString())
        }
    }


    fun create(title: String) {
        realm.executeTransaction {
            val memo = it.createObject(Memo::class.java, UUID.randomUUID().toString())
            memo.title = title
        }
    }

}