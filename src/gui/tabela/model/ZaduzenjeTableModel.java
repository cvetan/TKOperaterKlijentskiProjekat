/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.tabela.model;

import domen.Zaduzenje;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cvetan
 */
public class ZaduzenjeTableModel extends AbstractTableModel {

    private List<Zaduzenje> lista;

    public ZaduzenjeTableModel(List<Zaduzenje> lista) {
        this.lista = lista;
    }

    public ZaduzenjeTableModel() {
        lista = new ArrayList<>();
    }

    public List<Zaduzenje> getLista() {
        return lista;
    }

    public void setLista(List<Zaduzenje> lista) {
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
        Zaduzenje zaduzenje = lista.get(rowIndex);
        switch (columnIndex) {
            case 0:
                DateFormat godinaFormat = new SimpleDateFormat("yyyy");
                DateFormat mesecFormat = new SimpleDateFormat("MM");
                return mesecFormat.format(zaduzenje.getMesec()) + "/" + godinaFormat.format(zaduzenje.getGodina());
            case 1:
                return zaduzenje.getUgovor().getTarifa().getUsluga().getNazivUsluge();
            case 2:
                return zaduzenje.getIznos();
            default:
                return "Greška!";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Period";
            case 1:
                return "Usluga";
            case 2:
                return "Iznos";
            default:
                return "Greška!";
        }
    }
    
    public void dodajZaduzenje(Zaduzenje z) {
        lista.add(z);
        fireTableDataChanged();
    }
    
    public void izbaciZaduzenje(int red) {
        lista.remove(red);
        fireTableDataChanged();
    }

}
