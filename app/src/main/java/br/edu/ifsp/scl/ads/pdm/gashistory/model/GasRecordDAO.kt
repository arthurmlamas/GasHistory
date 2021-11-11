package br.edu.ifsp.scl.ads.pdm.gashistory.model

import java.util.*

interface GasRecordDAO {
    fun createGasRecord(gasRecord: GasRecord): Long
    fun findGasRecord(date: Date): GasRecord
    fun findAllGasRecords(): MutableList<GasRecord>
    fun updateGasRecord(gasRecord: GasRecord): Int
    fun deleteGasRecord(date: Date): Int
}