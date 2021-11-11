package br.edu.ifsp.scl.ads.pdm.gashistory.controller

import br.edu.ifsp.scl.ads.pdm.gashistory.MainActivity
import br.edu.ifsp.scl.ads.pdm.gashistory.firebase.FirebaseGasRecordDAO
import br.edu.ifsp.scl.ads.pdm.gashistory.model.GasRecord
import br.edu.ifsp.scl.ads.pdm.gashistory.model.GasRecordDAO
import java.util.*

class GasRecordController(mainActivity: MainActivity) {
    private val gasRecordDAO: GasRecordDAO = FirebaseGasRecordDAO()

    fun insertGasRecord(gasRecord: GasRecord) = gasRecordDAO.createGasRecord(gasRecord)
    fun findGasRecord(date: Date) = gasRecordDAO.findGasRecord(date)
    fun findAllGasRecords() = gasRecordDAO.findAllGasRecords()
    fun editGasRecord(gasRecord: GasRecord) = gasRecordDAO.updateGasRecord(gasRecord)
    fun removeGasRecord(date: Date) = gasRecordDAO.deleteGasRecord(date)
}