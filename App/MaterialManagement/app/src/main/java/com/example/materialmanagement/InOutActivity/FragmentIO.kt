package com.example.materialmanagement.InOutActivity

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.TypedArrayUtils.getText
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.materialmanagement.R
import com.example.materialmanagement.SearchActivity.SearchInOrder
import com.example.materialmanagement.SearchActivity.SearchItem
import com.example.materialmanagement.SearchActivity.SearchOutOrder
import com.example.materialmanagement.SearchActivity.SearchStorage
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.zxing.integration.android.IntentIntegrator
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentIO.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentIO : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var toggleButton : MaterialButtonToggleGroup
    private lateinit var btnIn : Button
    private lateinit var btnOut : Button
    private lateinit var putBtn : Button
    private lateinit var barCodeScanBtn : ImageButton
    private lateinit var searchOrder : SearchView
    private lateinit var searchCustomer : TextView
    private lateinit var searchStorage : SearchView
    private lateinit var searchBarCode : SearchView
    private lateinit var searchItemName : SearchView

    //private lateinit var searchResult : TextView

    private var buttonState : Boolean = true // 입고는 true, 출고는 false
    private lateinit var tableDate : TextView

    private lateinit var intent : Intent

    private lateinit var dialogView : View
    private lateinit var setDate : TextView
    private lateinit var deleteBtn : Button

    private var searchCategory : Int = 0 // 1 : 수주번호, 2 : 발주번호,  3 : 창고, 4 : 품목명

    //dialog
    private var itemNameString : String = "0"
    private var storNameString : String = "0"
    private lateinit var itemName : TextView
    private lateinit var emp_name : TextView
    private lateinit var storName : TextView
    private lateinit var itemSize : EditText

    private lateinit var refreshBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        //recycler view
        val view = inflater.inflate(R.layout.fragment_i_o, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.item_list)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = InoutRecyclerAdapter()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleButton = view.findViewById(R.id.toggleButton)
        btnIn = view.findViewById(R.id.btnIn)
        btnOut = view.findViewById(R.id.btnOut)
        putBtn = view.findViewById(R.id.putBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        barCodeScanBtn = view.findViewById(R.id.barCodeScanBtn)
        tableDate = view.findViewById(R.id.tableDate)
        refreshBtn = view.findViewById(R.id.refreshBtn)

        toggleButton.addOnButtonCheckedListener{ toggleButton, checkedId, isChecked ->
            if(isChecked) {
                when (checkedId) {
                    R.id.btnIn -> {
                        //Toast.makeText(activity,"입고", Toast.LENGTH_SHORT).show()
                        btnIn.getBackground().setTint(view.getResources().getColor(R.color.white));
                        btnOut.getBackground().setTint(view.getResources().getColor(R.color.darkGray));

                        searchOrder.setQueryHint("발주 번호")
                        putBtn.text = "입고"
                        tableDate.text = "입고일자"
                        buttonState = true
                    }
                    R.id.btnOut -> {
                        //Toast.makeText(activity,"출고", Toast.LENGTH_SHORT).show()
                        btnOut.getBackground().setTint(view.getResources().getColor(R.color.white));
                        btnIn.getBackground().setTint(view.getResources().getColor(R.color.darkGray));

                        searchOrder.setQueryHint("수주 번호")
                        putBtn.text = "출고"
                        tableDate.text = "출고일자"
                        buttonState = false
                    }
                }
            } else {
                if(toggleButton.checkedButtonId == View.NO_ID) {
                    Toast.makeText(activity,"선택 사항 없음", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //새로 고침 버튼
        refreshBtn.setOnClickListener{
            Toast.makeText(activity, "refresh", Toast.LENGTH_SHORT).show()
        }

        searchOrder = view.findViewById(R.id.searchOrder)
        searchCustomer = view.findViewById(R.id.searchCustomer)
        searchStorage = view.findViewById(R.id.searchStorage)
        searchBarCode = view.findViewById(R.id.searchBarCode)
        searchItemName = view.findViewById(R.id.searchItemName)

        //searchResult = view.findViewById(R.id.searchResult)

        searchOrder.isSubmitButtonEnabled = true
        searchStorage.isSubmitButtonEnabled = true
        searchBarCode.isSubmitButtonEnabled = true
        searchItemName.isSubmitButtonEnabled = true

        searchOrder.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(buttonState){
                    intent = Intent(getActivity(), SearchInOrder::class.java)
                    searchCategory = 1
                } else {
                    intent = Intent(getActivity(), SearchOutOrder::class.java)
                    searchCategory = 2
                }
                intent.putExtra("query", query) // 전달하는 인수 이름, 값
                //getActivity()?.startActivity(intent)
                startActivityForResult(intent, 100) //호출한 화면으로 값 돌려주기

                // 검색 버튼 누를 때 호출

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                // 검색창에서 글자가 변경이 일어날 때마다 호출

                return true
            }
        })


        searchStorage.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                intent = Intent(getActivity(), SearchStorage::class.java)
                intent.putExtra("query", query)
                //getActivity()?.startActivity(intent)
                searchCategory = 3 // 창고검색
                startActivityForResult(intent, 100);// 검색 버튼 누를 때 호출

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                // 검색창에서 글자가 변경이 일어날 때마다 호출

                return true
            }
        })

        searchItemName.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                intent = Intent(getActivity(), SearchItem::class.java)
                intent.putExtra("query", query)
                //getActivity()?.startActivity(intent)
                searchCategory = 4 // 품목명검색
                startActivityForResult(intent, 100);// 검색 버튼 누를 때 호출
                // 검색 버튼 누를 때 호출

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                // 검색창에서 글자가 변경이 일어날 때마다 호출

                return true
            }
        })

        searchBarCode.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                // 검색 버튼 누를 때 호출

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                // 검색창에서 글자가 변경이 일어날 때마다 호출

                return true
            }
        })

        //바코드 스캔
        barCodeScanBtn.setOnClickListener {
            val scanIntegrator = IntentIntegrator.forSupportFragment(this@FragmentIO)
            scanIntegrator.setPrompt("Scan")
            scanIntegrator.setBeepEnabled(true)
            scanIntegrator.setBarcodeImageEnabled(true)
            scanIntegrator.initiateScan()
        }

        val positiveInButtonClick = { dialogInterface: DialogInterface, i: Int ->
            if(itemSize.getText().toString().equals("") || itemSize.getText().toString() == null){
                Toast.makeText(activity, "수량을 입력해주세요", Toast.LENGTH_SHORT).show()
                dialogInterface.dismiss()
            } else {
                Toast.makeText(activity, "입고되었습니다", Toast.LENGTH_SHORT).show()
                itemNameString = "0"
                storNameString = "0"
            }
        }
        val positiveOutButtonClick = { dialogInterface: DialogInterface, i: Int ->
            Toast.makeText(activity, "출고되었습니다", Toast.LENGTH_SHORT).show()
        }
        val negativeButtonClick = { dialogInterface: DialogInterface, i: Int ->

        }

        //입고 dialog // 현재 시간
        putBtn.setOnClickListener {
            dialogView = View.inflate(view.context, R.layout.in_dialog, null)

            itemName = dialogView.findViewById(R.id.itemName)
            setDate = dialogView.findViewById(R.id.setDate)
            storName = dialogView.findViewById(R.id.storName)
            itemSize = dialogView.findViewById(R.id.itemSize)

            val now = System.currentTimeMillis()
            var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(now)

            if(itemNameString != "0" && storNameString != "0"){
                setDate.setText(simpleDateFormat)
                itemName.text = itemNameString
                storName.text = storNameString

                var dlg = AlertDialog.Builder(view.context)
                if (buttonState){
                    dlg.setTitle("입고 등록")
                    dlg.setView(dialogView)
                    dlg.setPositiveButton("입고", positiveInButtonClick)
                } else {
                    dlg.setTitle("출고 등록")
                    dlg.setView(dialogView)
                    dlg.setPositiveButton("출고", positiveOutButtonClick)
                }

                dlg.setNegativeButton("취소", negativeButtonClick)
                dlg.show()
            } else {
                Toast.makeText(activity, "검색 요소가 부족합니다", Toast.LENGTH_SHORT).show()
            }
        }

        deleteBtn.setOnClickListener {
            Toast.makeText(activity, "삭제되었습니다", Toast.LENGTH_SHORT).show()
        }
    }

    //이벤트 handler
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data) //결과 파싱
        if (scanningResult != null) { //정상적으로 전달
            if (scanningResult.contents != null) { //result 값
                Toast.makeText(activity,"Scanned : ${scanningResult.contents} format : ${scanningResult.formatName}",
                    Toast.LENGTH_SHORT).show()
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                100 -> {
                    Toast.makeText(activity, "검색결과반환", Toast.LENGTH_SHORT).show()
                    //tv_title.visibility = View.VISIBLE
                    //tv_contents.visibility = View.VISIBLE

                    when(searchCategory){
                        3 -> storNameString = data!!.getStringExtra("searchResult").toString()
                        4 -> itemNameString = data!!.getStringExtra("searchResult").toString()
                    }
                    //itemName.text = data!!.getStringExtra("searchResult").toString()
                }
            }
        } else {
            Toast.makeText(activity, "검색결과없음", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentIO.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: String, param2: String) =
                FragmentIO().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}