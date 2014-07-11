/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.tabela.model;

import domen.Racun;
import domen.StavkaRacuna;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cvetan
 */
public class RacunTableModel extends AbstractTableModel {

    private Racun racun;

    public RacunTableModel() {
        racun = new Racun();
    }

    public RacunTableModel(Racun racun) {
        this.racun = racun;
    }

    public Racun getRacun() {
        return racun;
    }

    public void setRacun(Racun racun) {
        this.racun = racun;
    }

    @Override
    public int getRowCount() {
        if (racun.getListaStavki() == null) {
            return 0;
        } else {
            return racun.getListaStavki().size();
        }
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        StavkaRacuna stavka = racun.getListaStavki().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return stavka.getRedniBroj();
            case 1:
                DateFormat formatMesec = new SimpleDateFormat("MM");
                DateFormat formatGodina = new SimpleDateFormat("yyyy");
                return formatMesec.format(stavka.getZaduzenje().getMesec()) + "/" + formatGodina.format(stavka.getZaduzenje().getGodina());
            case 2:
                return stavka.getZaduzenje().getUgovor().getTarifa().getUsluga().getNazivUsluge();
            case 3:
                return stavka.getZaduzenje().getIznos();
            default:
                return "Greška!";
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "RB";
            case 1:
                return "Period";
            case 2:
                return "Usluga";
            case 3:
                return "Iznos";
            default:
                return "Greška!";
        }
    }

    public void dodajStavku(StavkaRacuna stavka) {
        racun.getListaStavki().add(stavka);
        sortirajTebelu();
        fireTableDataChanged();
    }

    public void izbaciStavku(int red) {
        racun.getListaStavki().remove(red);
        sortirajTebelu();
        fireTableDataChanged();
    }

    public void sortirajTebelu() {
        List<StavkaRacuna> lista = racun.getListaStavki();
        for (int i = 0; i < lista.size(); i++) {
            StavkaRacuna sr = lista.get(i);
            int redniBroj = i;
            redniBroj++;
            sr.setRedniBroj(redniBroj);
        }
    }
    public StavkaRacuna vratiRed(int red) {
        return racun.getListaStavki().get(red);
    } 

}
