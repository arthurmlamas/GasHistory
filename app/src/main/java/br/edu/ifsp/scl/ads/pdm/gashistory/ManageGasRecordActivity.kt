package br.edu.ifsp.scl.ads.pdm.gashistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.edu.ifsp.scl.ads.pdm.gashistory.databinding.ActivityManageGasRecordBinding
import br.edu.ifsp.scl.ads.pdm.gashistory.model.GasRecord
import java.text.SimpleDateFormat
import java.util.*

class ManageGasRecordActivity : AppCompatActivity() {
    private val manageGasRecordBinding: ActivityManageGasRecordBinding by lazy {
        ActivityManageGasRecordBinding.inflate(layoutInflater)
    }

    private var gasRecordPosition: Int = -1
    private lateinit var gasRecord: GasRecord

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(manageGasRecordBinding.root)

        manageGasRecordBinding.saveBt.setOnClickListener {
            with(manageGasRecordBinding) {

                gasRecord = GasRecord(
                    getDateFromDatepicker(),
                    this.gasRecordPriceEt.text.toString().toDouble()
                )
            }

            val resultIntent = Intent().putExtra(MainActivity.EXTRA_GAS_RECORD, gasRecord)
            if (gasRecordPosition != -1) {
                resultIntent.putExtra(MainActivity.EXTRA_GAS_RECORD_POSITION, gasRecordPosition)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        val gasRecord = intent.getParcelableExtra<GasRecord>(MainActivity.EXTRA_GAS_RECORD)
        gasRecordPosition = intent.getIntExtra(MainActivity.EXTRA_GAS_RECORD_POSITION, -1)

        if (gasRecord != null) {
            var date = gasRecord?.date
            var calendar = Calendar.getInstance()
            calendar.time = date

            manageGasRecordBinding.gasRecordDateDp.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            manageGasRecordBinding.gasRecordPriceEt.setText(gasRecord.price.toString())
        }
    }

    private fun getDateFromDatepicker(): Date {
        var day = manageGasRecordBinding.gasRecordDateDp.dayOfMonth
        var month = manageGasRecordBinding.gasRecordDateDp.month
        var year = manageGasRecordBinding.gasRecordDateDp.year
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(year, month, day)

        val sdf = SimpleDateFormat("dd-MM-yyyy")
        val formatedDate = sdf.format(calendar.time)
        val date = sdf.parse(formatedDate)
        return date
    }

}