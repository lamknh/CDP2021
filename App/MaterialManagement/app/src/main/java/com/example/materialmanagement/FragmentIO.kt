package com.example.materialmanagement

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentTransaction
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
    private lateinit var dialogView : View
    private lateinit var setDate : TextView

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
        return inflater.inflate(R.layout.fragment_i_o, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toggleButton = view.findViewById(R.id.toggleButton)
        btnIn = view.findViewById(R.id.btnIn)
        btnOut = view.findViewById(R.id.btnOut)
        putBtn = view.findViewById(R.id.putBtn)
        barCodeScanBtn = view.findViewById(R.id.barCodeScanBtn)
        refreshBtn = view.findViewById(R.id.refreshBtn)

        toggleButton.addOnButtonCheckedListener{ toggleButton, checkedId, isChecked ->
            if(isChecked) {
                when (checkedId) {
                    R.id.btnIn -> {
                        //Toast.makeText(activity,"입고", Toast.LENGTH_SHORT).show()
                        btnIn.getBackground().setTint(view.getResources().getColor(R.color.white));
                        btnOut.getBackground().setTint(view.getResources().getColor(R.color.darkGray));

                        searchOrder.setQueryHint("발주 번호")
                    }
                    R.id.btnOut -> {
                        //Toast.makeText(activity,"출고", Toast.LENGTH_SHORT).show()
                        btnOut.getBackground().setTint(view.getResources().getColor(R.color.white));
                        btnIn.getBackground().setTint(view.getResources().getColor(R.color.darkGray));

                        searchOrder.setQueryHint("수주 번호")
                    }
                }
            } else {
                if(toggleButton.checkedButtonId == View.NO_ID) {
                    Toast.makeText(activity,"선택 사항 없음", Toast.LENGTH_SHORT).show()
                }
            }
        }

        refreshBtn.setOnClickListener{
            Toast.makeText(activity, "refresh", Toast.LENGTH_SHORT).show()
        }

        searchOrder = view.findViewById(R.id.searchOrder)
        searchCustomer = view.findViewById(R.id.searchCustomer)
        searchStorage = view.findViewById(R.id.searchStorage)
        searchBarCode = view.findViewById(R.id.searchBarCode)

        searchOrder.isSubmitButtonEnabled = true
        searchStorage.isSubmitButtonEnabled = true
        searchBarCode.isSubmitButtonEnabled = true

        searchOrder.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                var intent = Intent(getActivity(), SearchOrder::class.java)
                intent.putExtra("query", query)
                getActivity()?.startActivity(intent)

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

        barCodeScanBtn.setOnClickListener {
            val scanIntegrator = IntentIntegrator.forSupportFragment(this@FragmentIO)
            scanIntegrator.setPrompt("Scan")
            scanIntegrator.setBeepEnabled(true)
            scanIntegrator.setBarcodeImageEnabled(true)
            scanIntegrator.initiateScan()
        }

        putBtn.setOnClickListener {
            dialogView = View.inflate(view.context, R.layout.in_dialog, null)
            setDate = dialogView.findViewById(R.id.setDate)

            val now = System.currentTimeMillis()
            var simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.KOREAN).format(now)

            setDate.setText(simpleDateFormat)


            var dlg = AlertDialog.Builder(view.context)
            dlg.setTitle("입고 등록")
            dlg.setView(dialogView)
            dlg.setPositiveButton("입고", null)
            dlg.setNegativeButton("취소", null)
            dlg.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data) //결과 파
        if (scanningResult != null) { //정상적으로 전달
            if (scanningResult.contents != null) { //result 값
                Toast.makeText(activity,"Scanned : ${scanningResult.contents} format : ${scanningResult.formatName}",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity, "Nothing scanned", Toast.LENGTH_SHORT).show()
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