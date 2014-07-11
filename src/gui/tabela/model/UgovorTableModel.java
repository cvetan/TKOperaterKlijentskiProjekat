/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.tabela.model;

import domen.Ugovor;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cvetan
 */
public class UgovorTableModel extends AbstractTableModel {

    private List<Ugovor> lista;

    public List<Ugovor> getLista() {
        return lista;
    }

    public UgovorTableModel() {
        lista = new ArrayList<>();
    }

    public UgovorTableModel(List<Ugovor> lista) {
        this.lista = lista;
    }

    public void setLista(List<Ugovor> lista) {
        this.lista = lista;
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
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Ugovor u = lista.get(rowIndex);
        switch(columnIndex) {
            case 0: return u.getBrojUgovora();
            case 1: return u.getGodina();
            case 2: return u.getDatumPotpisivanja();
            case 3: return u.getUgovornaObaveza() + " meseca";
            case 4: return u.getKorisnik().getIme() + " " + u.getKorisnik().getPrezime();
            default: return "GREŠKA!";
         }
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0: return "Broj ugovora";
            case 1: return "Godina";
            case 2: return "Datum potpisivanja";
            case 3: return "Ugovorna obaveza";
            case 4: return "Korisnik";
            default: return "GREŠKA!";
        }
    }
    

}
