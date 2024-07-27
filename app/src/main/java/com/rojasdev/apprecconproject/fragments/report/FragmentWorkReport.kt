package com.rojasdev.apprecconproject.fragments.report

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.rojasdev.apprecconproject.ActivityWork
import com.rojasdev.apprecconproject.R
import com.rojasdev.apprecconproject.adapters.acapterItemDateWork
import com.rojasdev.apprecconproject.adapters.adapterItemDate
import com.rojasdev.apprecconproject.controller.dateFormat
import com.rojasdev.apprecconproject.controller.price
import com.rojasdev.apprecconproject.customCalendar.adapter
import com.rojasdev.apprecconproject.customCalendar.dataModelDay
import com.rojasdev.apprecconproject.customCalendar.montAndYear
import com.rojasdev.apprecconproject.data.dataBase.AppDataBase
import com.rojasdev.apprecconproject.data.dataModel.allCollecionAndCollector
import com.rojasdev.apprecconproject.data.dataModel.allWorkAndCollector
import com.rojasdev.apprecconproject.data.dataModel.totalCollection
import com.rojasdev.apprecconproject.databinding.FragmentWorkReportBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentWorkReport : Fragment() {
    private lateinit var adapter: adapterItemDate
    private lateinit var adapterWork: acapterItemDateWork
    private lateinit var adapterDates: adapter
    private lateinit var dayCalendar: Pair<Int,Int>
    private var _binding: FragmentWorkReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWorkReportBinding.inflate(inflater,container,false)

        dayCalendar = Pair(yearActual(),monthActual()-1)

        initSpinner()
        initSpinnerYear()

        daySelectedView(
            Pair(
                dayMonthActual(),
                dayWeek(dayMonthActual().toInt(),monthActual(),yearActual())
            )
        )

        return binding.root
    }

    private fun daySelectedView(day: Pair<String,String>) {
        binding.tvMonthDay.text = day.first
        binding.tvWorkDay.text = day.second.substring(0,3)
    }

    private fun initCalendar(it:Pair<Int,Int>) {
        CoroutineScope(Dispatchers.IO).launch {
            val collection = AppDataBase.getInstance(requireContext()).RecollectionDao().getDateCollection()
            val work = AppDataBase.getInstance(requireContext()).WorkDao().getDateWork()
            launch(Dispatchers.Main) {
                val listModificationCollection = collection.map { it.dropLast(9) }
                val listModificationWork = work.map { it.dropLast(9) }
                val month = getDaysMonth(it.first,it.second)
                calendarView(dayMonthActual(),month,listModificationCollection,listModificationWork)
            }
        }
    }

    private fun calendarView(dayMonthActual: String, month: List<List<dataModelDay>>, collection: List<String>,work: List<String>) {
        adapterDates = adapter(dayMonthActual,month,collection, work){
           if (it.third == true){
               calendarView(it.second,month,collection,work)
           }
            showAllRecollection(it.first)
            daySelectedView(
                Pair(
                    it.second,
                    dayWeek(
                        it.second.toInt(),
                        it.first.substring(5,7).toInt(),
                        it.first.substring(0,4).toInt()
                        )
                )
            )
        }

        binding.rvCalendar.adapter = adapterDates
        binding.rvCalendar.layoutManager = LinearLayoutManager(requireContext())
    }

    fun getDaysMonth(year: Int, month: Int): List<List<dataModelDay>> {
        val diasDelMes = mutableListOf<List<dataModelDay>>()
        val calendar = Calendar.getInstance()
        calendar.set(year, month - 1, 1)
        val ultimoDiaDelMes = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        var week = mutableListOf<dataModelDay>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("es", "ES"))
        val formatDayWeek = SimpleDateFormat("EEEE", Locale("es", "ES"))

        for (dia in 1..ultimoDiaDelMes) {
            calendar.set(Calendar.DAY_OF_MONTH, dia)
            val fechaActual = calendar.time
            val diaDeLaSemana = formatDayWeek.format(fechaActual)

            val day = dataModelDay(dateFormat.format(fechaActual),dia.toString(),diaDeLaSemana)

            week.add(day)

            if (diaDeLaSemana == "domingo" || dia == ultimoDiaDelMes) {
                diasDelMes.add(week)
                week = mutableListOf()
            }
        }



        return diasDelMes
    }

    fun initSpinner(){
        val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, montAndYear.month)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerMonth.adapter = adaptador

        binding.spinnerMonth.setSelection(monthActual()-1)

        binding.spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val mesSeleccionadoNumero = position + 1// Los meses en el arreglo empiezan en 0
                val mesSeleccionadoLetras = montAndYear.month[position]


                dayCalendar = Pair(dayCalendar.first,mesSeleccionadoNumero)
                initCalendar(dayCalendar)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    fun initSpinnerYear(){
        val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, montAndYear.listYear())
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerYear.adapter = adaptador

        binding.spinnerYear.setSelection(montAndYear.listYear().size-1)

        binding.spinnerYear.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val yearSelected = montAndYear.listYear()

                dayCalendar = Pair(yearSelected[position],dayCalendar.second)
                initCalendar(dayCalendar)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    fun dayMonthActual(): String {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH).toString()
    }

    fun yearActual(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun monthActual(): Int {return Calendar.getInstance().get(Calendar.MONTH) + 1
    }

    fun dayWeek(diaDelMes: Int, mes: Int, año: Int): String {
        val calendario = Calendar.getInstance()
        calendario.set(año, mes - 1, diaDelMes) // Los meses en Calendar van de 0 a 11

        val diaSemana = when(calendario.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> "Domingo"
            Calendar.MONDAY -> "Lunes"
            Calendar.TUESDAY -> "Martes"
            Calendar.WEDNESDAY -> "Miércoles"
            Calendar.THURSDAY -> "Jueves"
            Calendar.FRIDAY -> "Viernes"
            Calendar.SATURDAY -> "Sábado"
            else -> "Error" // Esto no debería ocurrir
        }

        return diaSemana
    }

    @SuppressLint("SetTextI18n")
    private fun showAllRecollection(selectedDate: String) {
        CoroutineScope(Dispatchers.IO).launch{
            val getAllID = AppDataBase.getInstance(requireContext()).RecolectoresDao().getAll()
            val getTotalKg = AppDataBase.getInstance(requireContext()).RecollectionDao().getTotalKgDate("${selectedDate}%")
            val getTotalWork = AppDataBase.getInstance(requireContext()).WorkDao().getTotalWorkDate("${selectedDate}%")
            launch(Dispatchers.Main) {
                val collectionAll = mutableListOf<allCollecionAndCollector>()
                val workAll = mutableListOf<allWorkAndCollector>()
                for (item in getAllID){
                    val collection = AppDataBase.getInstance(requireContext()).RecolectoresDao().getAllCollectorAndCollectionId("${selectedDate}%",item.toInt())
                    val work = AppDataBase.getInstance(requireContext()).RecolectoresDao().getAllCollectorAndWorkId("${selectedDate}%",item.toInt())

                    if(collection.isNotEmpty()){
                        if (collection[0].name_recolector != null) collectionAll.add(collection[0])
                    }
                    if(work.isNotEmpty()){
                        if (work[0].name_recolector != null) workAll.add(work[0])
                    }
                }

                if(collectionAll.isEmpty() && workAll.isEmpty()) {
                    showViewCollection(false)
                    showViewWork(false)
                    binding.tvNoDates.visibility = View.VISIBLE
                    binding.ivNoDates.visibility = View.VISIBLE
                } else{
                    binding.tvNoDates.visibility = View.GONE
                    binding.ivNoDates.visibility = View.GONE

                    if(collectionAll.isEmpty()) {
                        showViewCollection(false)
                    }else{
                        showTotalCollection(getTotalKg)
                        showViewCollection(true)
                        adapter = adapterItemDate(collectionAll)
                        binding.rvDates.adapter = adapter
                        binding.rvDates.layoutManager = LinearLayoutManager(requireContext())
                    }

                    if(workAll.isEmpty()) {
                        showViewWork(false)
                    }else{
                        showViewWork(true)
                        showTotalWork(getTotalWork)
                        adapterWork = acapterItemDateWork(workAll)
                        binding.rvDatesWork.adapter = adapterWork
                        binding.rvDatesWork.layoutManager = LinearLayoutManager(requireContext())
                    }
                }
            }
        }
    }

    private fun showTotalCollection(totalKg: totalCollection) {
        binding.tvKgTotal.text = "Total recolectado \n ${totalKg.Cantidad}Kg"
        price.priceSplit(totalKg.result.toInt()){
            binding.tvPriceTotal.text = "Total pagado \n ${it}"
        }
    }

    private fun showTotalWork(totalKg: totalCollection) {
        binding.tvWorkTotal.text = "Jornales trabajados \n ${totalKg.Cantidad.toInt()}"
        price.priceSplit(totalKg.result.toInt()){
            binding.tvPriceWorkTotal.text = "Total pagado \n ${it}"
        }
    }

    private fun showViewCollection(estate : Boolean){
        val visibility : Any
        if (estate == true){
            visibility = View.VISIBLE
        }else{
            visibility = View.GONE
        }

        binding.rvDates.visibility = visibility
        binding.tvCollection.visibility = visibility
        binding.ivCollection.visibility = visibility
        binding.lyTotalCollection.visibility = visibility
    }

    private fun showViewWork(estate : Boolean){
        val visibility : Any
            if (estate == true){
                visibility = View.VISIBLE
            }else{
                visibility = View.GONE
            }


        binding.rvDatesWork.visibility = visibility
        binding.tvWork.visibility = visibility
        binding.ivWork.visibility = visibility
        binding.lyTotalWork.visibility = visibility
    }
}