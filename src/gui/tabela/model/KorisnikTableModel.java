/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.tabela.model;

import domen.Korisnik;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cvetan
 */
public class KorisnikTableModel extends AbstractTableModel{
    
    private List<Korisnik> listaKorisnika;

    public KorisnikTableModel() {
        listaKorisnika = new ArrayList<>();
    }

    public KorisnikTableModel(List<Korisnik> listaKorisnika) {
        this.listaKorisnika = listaKorisnika;
    }
    
    
    
    public List<Korisnik> getListaKorisnika() {
        return listaKorisnika;
    }

    public void setListaKorisnika(List<Korisnik> listaKorisnika) {
        this.listaKorisnika = listaKorisnika;
        fireTableDataChanged();
    }
    
    
    @Override
    public int getRowCount() {
        if(listaKorisnika == null) {
            return 0;
        }else {
            return listaKorisnika.size();
        }
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Korisnik k = listaKorisnika.get(rowIndex);
        switch(columnIndex) {
            case 0: return k.getIme();
            case 1: return k.getPrezime();
            case 2: return k.getKorisnickoIme();
            default: return "Greška!";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch(column) {
            case 0: return "Ime";
            case 1: return "Prezime";
            case 2: return "Korisničko ime";
            default: return "Greška!";
        }
    }
    
    public Korisnik vratiOznacenogKorisnika(int red) {
        return listaKorisnika.get(red);
    }
    
    public void obrisiKorisnika(int red) {
        listaKorisnika.remove(red);
        fireTableDataChanged();
    }
}
