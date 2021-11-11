package br.edu.ifsp.scl.ads.pdm.gashistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import br.edu.ifsp.scl.ads.pdm.gashistory.controller.GasRecordController
import br.edu.ifsp.scl.ads.pdm.gashistory.databinding.ActivityMainBinding
import br.edu.ifsp.scl.ads.pdm.gashistory.model.GasRecord
import br.edu.ifsp.scl.ads.pdm.gashistory.onClickListener.OnGasRecordClickListener

class MainActivity : AppCompatActivity() {
    companion object Extras {
        const val EXTRA_GAS_RECORD = "EXTRA_GAS_RECORD"
        const val EXTRA_GAS_RECORD_POSITION = "EXTRA_GAS_RECORD_POSITION"
    }

    private val activityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var addManageGasRecordActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var updateManageGasRecordActivityResultLauncher: ActivityResultLauncher<Intent>

    private val gasRecordsList: MutableList<GasRecord> by lazy {
        gasRecordController.findAllGasRecords()
    }
    private val gasRecordController: GasRecordController by lazy {
        GasRecordController(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)
    }

    override fun onGasRecordClick(position: Int) {
        val gasRecord = gasRecordsList[position]
    }
}