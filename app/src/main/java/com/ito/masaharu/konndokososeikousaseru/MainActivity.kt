package com.ito.masaharu.konndokososeikousaseru

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val realm: Realm by lazy {
        Realm.getDefaultInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setOnClickListener {
            Toast.makeText(applicationContext, "追加！", Toast.LENGTH_SHORT).show()
        }

        val memoList = readAll()

        // タスクリストが空だったときにダミーデータを生成する
        if (memoList.isEmpty()) {
            createDummyData()
        }

        val adapter =
            MemoAdapter(this, memoList, object : MemoAdapter.OnItemClickListener {
                override fun onItemClick(item: Memo) {
                    // クリック時の処理
                    Toast.makeText(applicationContext, item.title + "を削除しました", Toast.LENGTH_SHORT).show()
                    delete(item.id)
                }
            }, true)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    fun createDummyData() {
        for (i in 0..10) {
            create("やること $i")
        }
    }

    fun create(title: String) {
        realm.executeTransaction {
            val memo = it.createObject(Memo::class.java, UUID.randomUUID().toString())
            memo.title = title
        }
    }

    fun readAll(): RealmResults<Memo> {
        return realm.where(Memo::class.java).findAll().sort("createdAt", Sort.ASCENDING)
    }

    fun update(id: String, title: String, content: String) {
        realm.executeTransaction {
            val memo = realm.where(Memo::class.java).equalTo("id", id).findFirst()
                ?: return@executeTransaction
            memo.title = title
            memo.content = content
        }
    }

    fun update(memo: Memo, title: String, content: String) {
        realm.executeTransaction {
            memo.title = title
            memo.content = content
        }
    }

    fun delete(id: String) {
        realm.executeTransaction {
            val memo = realm.where(Memo::class.java).equalTo("id", id).findFirst()
                ?: return@executeTransaction
            memo.deleteFromRealm()
        }
    }

    fun delete(memo: Memo) {
        realm.executeTransaction {
            memo.deleteFromRealm()
        }
    }

    fun deleteAll() {
        realm.executeTransaction {
            realm.deleteAll()
        }
    }

}