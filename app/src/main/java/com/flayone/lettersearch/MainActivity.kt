package com.flayone.lettersearch

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.flayone.lettersearch.LetterView.Companion.setOnLettersListViewListener
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk25.coroutines.textChangedListener
import java.util.*


class MainActivity : AppCompatActivity(), LetterView.OnLettersListViewListener {
    private val strName = arrayOf("阿尔法-罗密欧 A-阿尔法-罗密欧", "北京汽车 W-威旺", "长安福特马自达 F-福特", "宾利汽车 B-宾利", "奥迪汽车 A-奥迪",
            "大众汽车 D-大众", "大众汽车 S-斯柯达", "广汽吉奥 J-吉奥", "捷豹汽车 J-捷豹", "斯巴鲁汽车 S-斯巴鲁", "新凯汽车 X-新凯", "日产汽车 R-日产",
            "日产汽车 Y-英菲尼迪", "昌河汽车 C-昌河", "曙光汽车 H-黄海", "江淮安驰 A-安驰", "沈阳黑豹 H-黑豹", "迈凯伦汽车 M-迈凯伦",
            "通用汽车 G-GMC", "郑州日产 D-东风风度", "铃木汽车 L-铃木", "苏州金龙 H-海格", "科尼赛克 K-科尼赛克", "沃尔沃汽车 W-沃尔沃", "其他厂家 Q-其他品牌", "潍柴汽车 Y-英致"
            , "三菱汽车 S-三菱", "一汽马自达 M-马自达", "东南汽车 D-东南", "保斐利 B-保斐利", "众泰汽车 Z-众泰", "克莱斯勒 K-克莱斯勒",
            "凯翼汽车 K-凯翼", "力帆汽车 L-力帆", "北京吉普(现北京奔驰) S-三菱", "卡尔森汽车 K-卡尔森", "哈飞汽车 H-哈飞")

    private lateinit var adapter: CarListAdapter
    private lateinit var mList: List<LettersModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        letters.setmTextView(toast_text)
        mList = parsingData()
        setOnLettersListViewListener(letters, this)
        //对字母进行排序A-Z #
        Collections.sort(mList, LettersSorting())
        //加载适配器
        adapter = CarListAdapter(this, mList)
        //设置数据
        mListView.adapter = adapter

        search_edit.textChangedListener {
            onTextChanged { charSequence, _, _, _ ->
                if (charSequence!!.isNotEmpty()) {
                    searchResult(charSequence)
                } else {
                    adapter.updateData(mList)
                }
            }
        }
    }

    private fun searchResult(c: CharSequence) {
        //先将输入的字符转为拼音或英文，然后用现有的全拼音
        val resultList: MutableList<LettersModel> = mutableListOf()
        val pinyinFull = convertAll(c.toString())
        (0 until mList.size)
                .filter { mList[it].pinyin.contains(pinyinFull) }
                .mapTo(resultList) { mList[it] }
        adapter.updateData(resultList)
    }

    /**
     * 数组转换实体数据
     */
    private fun parsingData(): List<LettersModel> {
        val listModel: MutableList<LettersModel> = mutableListOf()
        for (i in 0 until strName.size) {
            val model = LettersModel()
            //转换拼音截取首字母并且大写
            val pinyin = convert(strName[i].substring(0, 1))
            val pinyinFull = convertAll(strName[i])
            Log.d("MainActivity", pinyinFull)
            val letter = pinyin!!.substring(0, 1).toUpperCase()
            model.letter = letter
            model.pinyin = pinyinFull
            model.name = strName[i]
            listModel.add(model)
        }
        return listModel
    }

    override fun onLettersListener(s: String) {
        //对应的位置
        val position = getFirstPosition(mList, s[0])
        //移动
        mListView.setSelection(position)
    }
}
