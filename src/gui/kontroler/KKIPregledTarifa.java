/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.kontroler;

import domen.Mesto;
import domen.Tarifa;
import domen.Usluga;
import gui.tabela.model.TarifaTableModel;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;

/**
 *
 * @author cvetan
 */
public class KKIPregledTarifa {

    public static void srediTabelu(JTable jtblTarife, TableModel model) {
        jtblTarife.setModel(model);
    }

    public static void srediComboBox(JComboBox jcbUsluga) {
        try {
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setOperacija(Konstante.VRATI_USLUGE);
            toZahtev.setParametar(new Usluga());
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            if (toOdgovor.isUspesan()) {
                List<Usluga> lista = (List<Usluga>) toOdgovor.getOdgovor();
                for (Usluga u : lista) {
                    jcbUsluga.addItem(u);
                }
            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void prikaziTarife(JTable jtblTarife, JComboBox jcbUsluga) {
        try {
            Usluga u = (Usluga) jcbUsluga.getSelectedItem();
            Tarifa t = new Tarifa(u);
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setOperacija(Konstante.VRATI_TARIFE_PO_USLUZI);
            toZahtev.setParametar(t);
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            if (toOdgovor.isUspesan()) {
                List<Tarifa> lista = (List<Tarifa>) toOdgovor.getOdgovor();
                if (!lista.isEmpty()) {
                    TarifaTableModel model = (TarifaTableModel) jtblTarife.getModel();
                    model.setLista(lista);
                } else {
                    JOptionPane.showMessageDialog(null, "Nema definisanih tarifa za tu uslugu.", "Obaveštenje", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }

    }
}
