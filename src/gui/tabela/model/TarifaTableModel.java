/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.tabela.model;

import domen.Tarifa;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cvetan
 */
public class TarifaTableModel extends AbstractTableModel {

    private List<Tarifa> lista;

    public TarifaTableModel() {
        lista = new ArrayList<>();
    }

    public TarifaTableModel(List<Tarifa> lista) {
        this.lista = lista;
    }

    public List<Tarifa> getLista() {
        return lista;
    }

    public void setLista(List<Tarifa> lista) {
        this.lista = lista;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        if (lista == null) {
            return 0;
        } else {
            return lista.size();
        }
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Tarifa t = lista.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return t.getUsluga().getNazivUsluge();
            case 1:
                return t.getNazivTarife();
            case 2:
                return t.getPretplata();
            default:
                return "GREŠKA!";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Vrsta usluge";
            case 1:
                return "Naziv tarife";
            case 2:
                return "Mesečna pretplata";
            default:
                return "GREŠKA!";
        }
    }

}
