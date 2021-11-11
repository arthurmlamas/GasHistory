package br.edu.ifsp.scl.ads.pdm.gashistory.firebase

import br.edu.ifsp.scl.ads.pdm.gashistory.model.GasRecord
import br.edu.ifsp.scl.ads.pdm.gashistory.model.GasRecordDAO
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.util.*

class FirebaseGasRecordDAO: GasRecordDAO {
    companion object {
        private const val BD_GAS_HISTORY = "gas_history"
    }

    // Referência para o RtDb -> gas_history
    private val gasHistoryRtDb = Firebase.database.getReference(BD_GAS_HISTORY)

    // Lista de registros que simula uma consulta
    private val gasRecordsList: MutableList<GasRecord> = mutableListOf()
    init {
        gasHistoryRtDb.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newGasRecord: GasRecord? = snapshot.value as? GasRecord
                newGasRecord?.apply {
                    if (gasRecordsList.find { it.date.toString() == this.date.toString() } == null) {
                        gasRecordsList.add(this)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val updatedGasRecord: GasRecord? = snapshot.value as? GasRecord
                updatedGasRecord?.apply {
                    gasRecordsList[gasRecordsList.indexOfFirst { it.date.toString() == this.date.toString() }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val removedGasRecord: GasRecord? = snapshot.value as? GasRecord
                removedGasRecord?.apply {
                    gasRecordsList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                // Não se aplica
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
        gasHistoryRtDb.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                gasRecordsList.clear()
                snapshot.children.forEach {
                    val gasRecord: GasRecord = it.getValue<GasRecord>()?: GasRecord()
                    gasRecordsList.add(gasRecord)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Não se aplica
            }
        })
    }

    override fun createGasRecord(gasRecord: GasRecord): Long {
        createOrUpdateGasRecord(gasRecord)
        return 0L
    }

    override fun findGasRecord(date: Date): GasRecord = gasRecordsList.firstOrNull { it.date.toString() == date.toString() } ?: GasRecord()

    override fun findAllGasRecords(): MutableList<GasRecord> = gasRecordsList

    override fun updateGasRecord(gasRecord: GasRecord): Int {
        createOrUpdateGasRecord(gasRecord)
        return 1
    }

    override fun deleteGasRecord(date: Date): Int {
        gasHistoryRtDb.child(date.toString()).removeValue()
        return 1
    }

    private fun createOrUpdateGasRecord(gasRecord: GasRecord) {
        gasHistoryRtDb.child(gasRecord.date.toString()).setValue(gasRecord)
    }
}