/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.kontroler;

import domen.Korisnik;
import gui.FrmNoviKorisnik;
import gui.tabela.model.KorisnikTableModel;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import komunikacija.Komunikacija;
import transfer.TransferObjekatOdgovor;
import transfer.TransferObjekatZahtev;
import util.Konstante;

/**
 *
 * @author cvetan
 */
public class KKIGlavnaForma {

    public static void srediTabelu(JTable jtblKorisnici, TableModel model) {
        jtblKorisnici.setModel(model);
    }

    public static void pretraziKorisnike(JTextField jtfPretraga, JTable jtblKorisnici) {
        String pretraga = jtfPretraga.getText().trim();
        if (pretraga.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Niste uneli tekst za pretragu!", "Pretraga korisnika", JOptionPane.WARNING_MESSAGE);
        } else {
            try {
                Korisnik k = new Korisnik(pretraga);
                TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
                TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();

                toZahtev.setOperacija(Konstante.PRETRAZI_KORISNIKE);
                toZahtev.setParametar(k);
                Komunikacija.vratiInstancu().posalji(toZahtev);
                toOdgovor = Komunikacija.vratiInstancu().procitaj();

                if (toOdgovor.isUspesan()) {
                    List<Korisnik> lista = (List<Korisnik>) toOdgovor.getOdgovor();
                    if (lista.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Nema korisnika sa tim podacima.", "Pretraga korisnika", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        KorisnikTableModel model = new KorisnikTableModel(lista);
                        jtblKorisnici.setModel(model);
                    }

                } else {
                    throw new RuntimeException(toOdgovor.getRezultat());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void izmeniKorisnika(JTable tabela) {
        if (tabela.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Niste označili korisnika!", "Izmena korisnika", JOptionPane.WARNING_MESSAGE);
        } else {
            KorisnikTableModel model = (KorisnikTableModel) tabela.getModel();
            Korisnik korisnik = model.getListaKorisnika().get(tabela.getSelectedRow());
            FrmNoviKorisnik ik = new FrmNoviKorisnik(korisnik, null, "Izmena korisnika", true);
            ik.setLocationRelativeTo(null);
            ik.setVisible(true);
        }
    }

    public static void obrisiKorisnika(JTable tabela) {
        if (tabela.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Niste odabrali korisnika za brisanje!", "Brisanje korisnika", JOptionPane.WARNING_MESSAGE);
        } else {
            KorisnikTableModel model = (KorisnikTableModel) tabela.getModel();
            Korisnik k = model.getListaKorisnika().get(tabela.getSelectedRow());
            int odgovor = JOptionPane.showConfirmDialog(null, "Jeste li sigurni?", "Brisanje korisnika", JOptionPane.YES_NO_OPTION);
            if (odgovor == 1) {
                return;
            } else {
                try {
                    TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
                    TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
                    toZahtev.setOperacija(Konstante.OBRISI_KORISNIKA);
                    toZahtev.setParametar(k);
                    Komunikacija.vratiInstancu().posalji(toZahtev);
                    toOdgovor = Komunikacija.vratiInstancu().procitaj();

                    if (toOdgovor.isUspesan()) {
                        JOptionPane.showMessageDialog(null, toOdgovor.getRezultat(), "Brisanje korisnika", JOptionPane.INFORMATION_MESSAGE);
                        model.obrisiKorisnika(tabela.getSelectedRow());
                    } else {
                        throw new RuntimeException(toOdgovor.getRezultat());
                    }
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static void prikaziZaduzenjeKorisnika(JTable jtblKorisnici) {
        if (jtblKorisnici.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Niste odabrali korisnika za pregled zaduženja!", "Pregled zaduženja", JOptionPane.WARNING_MESSAGE);
        } else {
            KorisnikTableModel model = (KorisnikTableModel) jtblKorisnici.getModel();
            Korisnik k = model.getListaKorisnika().get(jtblKorisnici.getSelectedRow());
            if (k.getZaduzenje() > 0) {
                JOptionPane.showMessageDialog(null, "Korisnik ima zaduženje! \n Trenutno zaduženje korisnika je: " + k.getZaduzenje() + " dinara.", "Pregled zaduženja", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Korisnik nema neizmirenih obaveza.", "Pregled zaduženja", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

}
