package br.edu.ifsp.scl.ads.pdm.gashistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.ads.pdm.gashistory.adapter.GasRecordRvAdapter
import br.edu.ifsp.scl.ads.pdm.gashistory.controller.GasRecordController
import br.edu.ifsp.scl.ads.pdm.gashistory.databinding.ActivityMainBinding
import br.edu.ifsp.scl.ads.pdm.gashistory.model.GasRecord
import br.edu.ifsp.scl.ads.pdm.gashistory.onClickListener.OnGasRecordClickListener
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(), OnGasRecordClickListener {
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
    private val gasRecordsRvAdapter: GasRecordRvAdapter by lazy {
        GasRecordRvAdapter(this, gasRecordsList)
    }
    private val gasRecordsLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        activityMainBinding.gasRecordsRv.adapter = gasRecordsRvAdapter
        activityMainBinding.gasRecordsRv.layoutManager = gasRecordsLayoutManager

        addManageGasRecordActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getParcelableExtra<GasRecord>(EXTRA_GAS_RECORD)?.apply {
                    gasRecordController.insertGasRecord(this)
                    gasRecordsList.add(this)
                    gasRecordsRvAdapter.notifyDataSetChanged()
                }
            }
        }

        updateManageGasRecordActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val position = result.data?.getIntExtra(EXTRA_GAS_RECORD_POSITION, -1)
                result.data?.getParcelableExtra<GasRecord>(EXTRA_GAS_RECORD)?.apply {
                    if (position != null && position != -1) {
                        gasRecordController.editGasRecord(this)
                        gasRecordsList[position] = this
                        gasRecordsRvAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.addGasRecordFab.setOnClickListener {
            addManageGasRecordActivityResultLauncher.launch(Intent(this, ManageGasRecordActivity::class.java))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val position = gasRecordsRvAdapter.gasRecordPosition
        val gasRecord = gasRecordsList[position]
        return when (item.itemId) {
            R.id.editGasRecordMi -> {
                val updateGasRecordIntent = Intent(this, ManageGasRecordActivity::class.java)
                updateGasRecordIntent.putExtra(EXTRA_GAS_RECORD, gasRecord)
                updateGasRecordIntent.putExtra(EXTRA_GAS_RECORD_POSITION, position)
                updateManageGasRecordActivityResultLauncher.launch(updateGasRecordIntent)
                true
            }
            R.id.removeGasRecordMi -> {
                with(AlertDialog.Builder(this)) {
                    setMessage("Confirma remoção?")
                    setPositiveButton("Sim") { _, _ ->
                        gasRecordController.removeGasRecord(gasRecord.date!!)
                        gasRecordsList.removeAt(position)
                        gasRecordsRvAdapter.notifyDataSetChanged()
                        Snackbar.make(activityMainBinding.root, "Registro removído!", Snackbar.LENGTH_SHORT).show()
                    }
                    setNegativeButton("Não") { _, _ ->
                        Snackbar.make(activityMainBinding.root, "Remoção cancelada!", Snackbar.LENGTH_SHORT).show()
                    }
                    create()
                }.show()
                true
            }
            else -> { false }
        }
    }

    override fun onGasRecordClick(position: Int) {
        val gasRecord = gasRecordsList[position]
        val displayGasRecordIntent = Intent(this, ManageGasRecordActivity::class.java)
        displayGasRecordIntent.putExtra(EXTRA_GAS_RECORD, gasRecord)
        addManageGasRecordActivityResultLauncher.launch(displayGasRecordIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.refreshMi -> {
            gasRecordsRvAdapter.notifyDataSetChanged()
            true
        }
        else -> { false }
    }
}