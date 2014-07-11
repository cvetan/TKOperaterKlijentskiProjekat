/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.kontroler;

import domen.Korisnik;
import domen.Racun;
import domen.StavkaRacuna;
import domen.Ugovor;
import domen.Zaduzenje;
import gui.tabela.model.RacunTableModel;
import gui.tabela.model.ZaduzenjeTableModel;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class KKINoviRacun {

    public static void srediTabelu(JTable jtblZaduzenje, TableModel model) {
        jtblZaduzenje.setModel(model);
    }

    public static void dodajStavkuRacuna(JTable jtblRacun, JTable jtblZaduzenja) {
        if (jtblZaduzenja.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Niste odabrali zaduženje koje unosite u račun!", "Unos stavke računa", JOptionPane.WARNING_MESSAGE);
        } else {
            RacunTableModel modelRacuna = (RacunTableModel) jtblRacun.getModel();
            ZaduzenjeTableModel modelZaduzenja = (ZaduzenjeTableModel) jtblZaduzenja.getModel();
            Zaduzenje zaduzenje = modelZaduzenja.getLista().get(jtblZaduzenja.getSelectedRow());
            StavkaRacuna stavka = new StavkaRacuna(modelRacuna.getRacun().getListaStavki().size() + 1, zaduzenje);
            modelRacuna.dodajStavku(stavka);
            modelZaduzenja.izbaciZaduzenje(jtblZaduzenja.getSelectedRow());
        }
    }

    public static void izbaciStavkuRacuna(JTable jtblRacun, JTable jtblZaduzenja) {
        if (jtblRacun.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(null, "Niste odabrali stavku koju želite da izbacite!", "Brisanje stavke računa", JOptionPane.WARNING_MESSAGE);
        } else {
            RacunTableModel modelRacuna = (RacunTableModel) jtblRacun.getModel();
            ZaduzenjeTableModel modelZaduzenja = (ZaduzenjeTableModel) jtblZaduzenja.getModel();
            StavkaRacuna sr = modelRacuna.vratiRed(jtblRacun.getSelectedRow());
            Zaduzenje zad = sr.getZaduzenje();
            modelZaduzenja.dodajZaduzenje(zad);
            modelRacuna.izbaciStavku(jtblRacun.getSelectedRow());
        }
    }

    public static void srediComboBox(JComboBox jcbKorisnici) {
        try {
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setOperacija(Konstante.VRATI_KORISNIKE);
            toZahtev.setParametar(new Korisnik());
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            if (toOdgovor.isUspesan()) {
                List<Korisnik> lista = (List<Korisnik>) toOdgovor.getOdgovor();
                for (Korisnik k : lista) {
                    jcbKorisnici.addItem(k);
                }
            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void vratiZaduzenja(JComboBox jcbKorisnici, JTable jtblZaduzenje, JButton jbtnPrikaziZaduzenja) {
        Korisnik k = (Korisnik) jcbKorisnici.getSelectedItem();
        Ugovor ugovor = new Ugovor(k);
        Zaduzenje zaduzenje = new Zaduzenje(ugovor);
        try {
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setParametar(zaduzenje);
            toZahtev.setOperacija(Konstante.VRATI_ZADUZENJA);
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            if (toOdgovor.isUspesan()) {
                ZaduzenjeTableModel model = (ZaduzenjeTableModel) jtblZaduzenje.getModel();
                List<Zaduzenje> lista = (List<Zaduzenje>) toOdgovor.getOdgovor();
                if (lista.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Korisnik nema neizmirenih obaveza.", "Prikaz zaduženja", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    model.setLista(lista);
                    disableDugme(jbtnPrikaziZaduzenja);
                }

            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void postaviBrojRacuna(JTextField jtfBrojRacuna) {
        try {
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setParametar(new Racun(new Date()));
            toZahtev.setOperacija(Konstante.VRATI_POSLEDNJI_RACUN);
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            if (toOdgovor.isUspesan()) {
                Racun r = (Racun) toOdgovor.getOdgovor();
                int brojUgovora = r.getBrojRacuna();
                brojUgovora++;
                jtfBrojRacuna.setText(String.valueOf(brojUgovora));
            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void postaviGodinu(JTextField jtfGodina) {
        DateFormat godinaFormat = new SimpleDateFormat("yyyy");
        Date godina = new Date();
        jtfGodina.setText(godinaFormat.format(godina));
    }

    public static void postaviDatum(JTextField jtfDatum) {
        DateFormat datumFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date datum = new Date();
        jtfDatum.setText(datumFormat.format(datum));
    }

    public static void izracunajUkupanIznos(JTextField jtfUkupanIznos, JTable jtblRacun) {
        RacunTableModel model = (RacunTableModel) jtblRacun.getModel();
        double suma = 0;
        for (StavkaRacuna st : model.getRacun().getListaStavki()) {
            suma += st.getZaduzenje().getIznos();
        }
        jtfUkupanIznos.setText(String.valueOf(suma));
    }

    public static void disableDugme(JButton jbtnPrikaziZaduzenja) {
        jbtnPrikaziZaduzenja.setEnabled(false);
    }

    public static void noviRacun(JTextField jtfGodina, JTextField jtfDatum, JTextField jtfBroj, JButton jbtnPrikazi, JTable jtblRacun, JTable jtblZaduzenja, JComboBox jcbKorisnici, JTextField jtfUkupanIznos) {
        postaviBrojRacuna(jtfBroj);
        postaviDatum(jtfDatum);
        postaviGodinu(jtfGodina);
        jbtnPrikazi.setEnabled(true);
        srediTabelu(jtblZaduzenja, new ZaduzenjeTableModel());
        srediTabelu(jtblRacun, new RacunTableModel());
        srediComboBox(jcbKorisnici);
        jtfUkupanIznos.setText("0.0");
    }

    public static Racun VratiRacun(JTable jtblRacun, JComboBox jcbKorisnici, JTextField jtfBrojRacuna, JTextField jtfGodina, JTextField jtfDatumPlacanja, JTextField jtfUkupanIznos) throws ParseException {
        RacunTableModel model = (RacunTableModel) jtblRacun.getModel();
        Racun racun = model.getRacun();
        if (model.getRacun().getListaStavki().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Račun nema nijednu stavku!", "Novi račun", JOptionPane.WARNING_MESSAGE);
        } else {
            int brojRacuna = Integer.valueOf(jtfBrojRacuna.getText().trim());
            DateFormat godinaFormat = new SimpleDateFormat("yyyy");
            DateFormat datumFormat = new SimpleDateFormat("dd.MM.yyyy");
            Date godina = godinaFormat.parse(jtfGodina.getText().trim());
            Date datumPlacanja = datumFormat.parse(jtfDatumPlacanja.getText().trim());
            Double ukupanIznos = Double.valueOf(jtfUkupanIznos.getText().trim());
            Korisnik k = (Korisnik) jcbKorisnici.getSelectedItem();
            racun.setBrojRacuna(brojRacuna);
            racun.setGodina(godina);
            racun.setDatumPlacanja(datumPlacanja);
            racun.setKorisnik(k);
            racun.setUkupanIznos(ukupanIznos);
            for (StavkaRacuna sr : racun.getListaStavki()) {
                sr.setRacun(racun);
            }
        }
        return racun;
    }

    public static void sacuvajRacun(Racun r) {
        try {
            TransferObjekatZahtev toZahtev = new TransferObjekatZahtev();
            TransferObjekatOdgovor toOdgovor = new TransferObjekatOdgovor();
            toZahtev.setOperacija(Konstante.SACUVAJ_RACUN);
            toZahtev.setParametar(r);
            Komunikacija.vratiInstancu().posalji(toZahtev);
            toOdgovor = Komunikacija.vratiInstancu().procitaj();
            if (toOdgovor.isUspesan()) {
                JOptionPane.showMessageDialog(null, toOdgovor.getRezultat(), "Novi račun", JOptionPane.INFORMATION_MESSAGE);
            } else {
                throw new RuntimeException(toOdgovor.getRezultat());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Greška!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void azurirajZaduzenjeKorisnika(Racun r) {
        Korisnik k = r.getKorisnik();
        System.out.println(k.getZaduzenje());
        double zaduzenje = k.getZaduzenje();
        zaduzenje = zaduzenje - r.getUkupanIznos();
        k.setZaduzenje(zaduzenje);
        System.out.println(k.getZaduzenje());
    }

    public static void azurirajListuZaduzenja(Racun r) {
        List<StavkaRacuna> lista = r.getListaStavki();
        for (StavkaRacuna st : lista) {
            st.getZaduzenje().setPlaceno(true);
        }
    }
}
