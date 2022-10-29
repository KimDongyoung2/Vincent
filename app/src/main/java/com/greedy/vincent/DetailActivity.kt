package com.greedy.vincent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.greedy.sqlite.SqliteHelper
import com.greedy.vincent.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    val helper = SqliteHelper(this, "comment", 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val adapter = CommentRecyclerAdapter()
        adapter.helper = helper
        /* selectComment를 통해 DB에 있는 커멘트를 모두 조회해서 List 반환 받고
        * 해당 데이터를 Adapter의 listData에 담는다. */
        adapter.listData.addAll(helper.selectComment())

        /* detal activity의 recyclerView에 생성한 어댑터 연결하고 레이아웃 설정 */
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        //binding.editComment.clearFocus();
        //binding.editComment.requestFocus();
        setListenerToEditText()

        /* 메모 저장 버튼 이벤트 */
        binding.saveButton.setOnClickListener {
            /* 메모 내용이 입력 된 경우만 동작 */
            if (binding.editComment.text.toString().isNotEmpty()) {
                val comment = Comment(null, binding.editComment.text.toString(), System.currentTimeMillis())
                helper.insertComment(comment)

                /* DB가 변동 되었을 때 화면도 변동될 수 있도록 adapter의 data를 수정하고
                *  데이터가 변화 했음을 알린다. */
                adapter.listData.clear()
                adapter.listData.addAll(helper.selectComment())
                adapter.notifyDataSetChanged()

                /* 입력란 비우기 */
                binding.editComment.setText("")
            }
            else { Log.d("오류","오류")}
        }




    }

    private fun setListenerToEditText() {
        binding.editComment.setOnKeyListener { view, keyCode, event ->
            // Enter Key Action
            if (event.action == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_ENTER
            ) {
                // 키패드 내리기
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.editComment.windowToken, 0)
                true
            }

            false
        }
    }
}

